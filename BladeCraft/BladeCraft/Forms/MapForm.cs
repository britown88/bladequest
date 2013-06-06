using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Imaging;

using System.Text;
using System.Windows.Forms;
using BladeCraft.Classes;



namespace BladeCraft.Forms
{
   public partial class MapForm : Form
   {
      private static int sideLeft = 0;      
      private static int sideTop = 1;
      private static int sideRight = 2;      
      private static int sideBottom = 3;

      private main parent;
      private BQMap map;
      private Bitmap tileset;
      private Bitmap collArrows, E;
      private Rectangle ERect;//hehe
      private Rectangle[] collArrowRects;
      private string selectedTileset;
      private float tsScale = 2.0f, mapScale = 1.0f;
      private int tileSize = 16;

      private Point selectedObject;
      private bool mouseDown, erase;
      private Point lastPointAdded;

      private Tile selectedTile;

      private Point zoneStart, zoneEnd;
      private bool drawingzone;
      private EncounterZone selectedZone;

      private GameObject copiedObject;

      private int currentLayer, swapToLayer; 


      public MapForm(BQMap map)
      {
         InitializeComponent();
         mouseDown = false;
         selectedTileset = "";
         selectedTile = new Tile(0, 0, 0, 0, 0);
         //this.SetStyle(ControlStyles.AllPaintingInWmPaint | ControlStyles.UserPaint | ControlStyles.DoubleBuffer, true);
         
         
         collArrows = new Bitmap("misc\\collarrows.png");
         collArrowRects = new Rectangle[4];
         collArrowRects[0] = new Rectangle(0, 0, 16, 16);
         collArrowRects[1] = new Rectangle(16, 0, 16, 16);
         collArrowRects[2] = new Rectangle(32, 0, 16, 16);
         collArrowRects[3] = new Rectangle(48, 0, 16, 16);

         this.map = map;

         E = new Bitmap("misc\\E.png");
         ERect = new Rectangle(0, 0, 16, 16);

         

      }

      

      private void MapForm_Load(object sender, EventArgs e)
      {
         parent = (main)MdiParent;
         Text = "MAP - " + map.getName() + ":\"" + map.getDisplayName() + "\"";
      }

      public BQMap getMap() { return map; }

      public void updateMap()
      {
         //map = parent.getMap();
         if (map != null)
         {
            if (map.getTileset() != null)
               tscbTilesetName.SelectedItem = map.getTileset();
            mapPanel.Width = (int)(map.width() * tileSize * mapScale);
            mapPanel.Height = (int)(map.height() * tileSize * mapScale);
            mapPanel.Invalidate();

         }         
      }

      private void addMaterial()
      {
         map.MatList.Add(new Point(selectedTile.bmpX, selectedTile.bmpY));
         matPanel.Height = (int)(tileSize * tsScale);
         matPanel.Width = (int)(tileSize * map.MatList.Count * tsScale);
         matPanel.Invalidate();
      }
      

      private void tscbTilesetName_SelectedIndexChanged(object sender, EventArgs e)
      {
         string filename = "assets\\drawable\\tilesets\\" + tscbTilesetName.SelectedItem.ToString().ToLower() + ".png";
         try
         {
            tileset = new Bitmap(filename);
            selectedTileset = tscbTilesetName.SelectedItem.ToString();
            if(map != null) map.setTileset(selectedTileset);
         }
         catch
         {
            MessageBox.Show("Tileset not found!");
            tileset = null;
            tscbTilesetName.SelectedItem = selectedTileset;
         }
         
         if (tileset != null)
         {
            //pbTileset.Image = (Image)tileset;
            tsPanel.Width = (int)(tileset.Width * tsScale);
            tsPanel.Height = (int)(tileset.Height * tsScale);
            

            tsPanel.Invalidate();
            mapPanel.Invalidate();
         }
      }

      private void tsPanel_Paint(object sender, PaintEventArgs e)
      {
         Graphics g = e.Graphics;
         Pen selPen = new Pen(Color.White);
         selPen.Width = 4;

         if (tileset != null)
         {
            g.DrawImage(tileset, 0, 0, tsPanel.Width, tsPanel.Height);
            if(!selectedTile.IsMaterial())
               g.DrawRectangle(selPen, selectedTile.bmpX * tileSize * tsScale,
                  selectedTile.bmpY * tileSize * tsScale, tileSize * tsScale, tileSize * tsScale);
         }

         matPanel.Invalidate();
            
      }

      private void tsPanel_MouseClick(object sender, MouseEventArgs e)
      {
         erase = false;
         tilesetPanel.Focus();
         selectedTile.isMaterial = false;
         selectedTile.bmpX = (int)(e.X / (tileSize * tsScale));
         selectedTile.bmpY = (int)(e.Y / (tileSize * tsScale));
         tsPanel.Invalidate();
      }

      private void mapPanel_Paint(object sender, PaintEventArgs e)
      {
         Graphics g = e.Graphics;
         Pen gridPen = new Pen(Color.Black);
         if (map != null)
         {
            Rectangle frame = new Rectangle(
            (int)(-mapFrame.AutoScrollPosition.X / (tileSize * mapScale)),
            (int)(-mapFrame.AutoScrollPosition.Y / (tileSize * mapScale)),
            (int)(mapFrame.Width / (tileSize * mapScale)),
            (int)(mapFrame.Height / (tileSize * mapScale))
               );

            if (!tsbObjectLayer.Checked)
            {
               int belowLayers = currentLayer;
               int aboveLayers = map.layerCount - currentLayer - 1;

               for (int i = 0; i < belowLayers; ++i)
                  drawTiles(g, frame, i, 1.0f);

               drawGrid(g, frame, gridPen);
               drawTiles(g, frame, currentLayer, 1.0f);

               for (int i = 1; i <= aboveLayers; ++i)
                  drawTiles(g, frame, currentLayer + i, (1.0f-((float)i/(float)aboveLayers))*0.75f);


               if (tsbCollision.Checked)
                  drawCollision(g, frame);
            }
            else
            {
               for (int i = 0; i < map.layerCount; ++i)
                  drawTiles(g, frame, i, 1.0f);

               drawGrid(g, frame, gridPen);
               drawObjects(g, frame);
               drawSelect(g);
               if (tsbEncounters.Checked)
                  drawEncounterZones(g);

            }

         }
         
      }

      private void drawGrid(Graphics g, Rectangle frame, Pen pen)
      {
         for (int x = frame.Left; x < Math.Min(map.width(), frame.Right); ++x)
            for (int y = frame.Top; y < Math.Min(map.height(), frame.Bottom); ++y)
            {
               g.DrawRectangle(pen,
                  x * tileSize * mapScale,
                  y * tileSize * mapScale,
                  tileSize * mapScale,
                  tileSize * mapScale);
            }
      }

      private void drawObjects(Graphics g, Rectangle Frame)
      {
         foreach (GameObject go in map.getObjects())
         {
           if (Frame.Contains(go.X, go.Y))
           {
             Rectangle destRect = new Rectangle((int)(go.X * tileSize * mapScale), (int)(go.Y * tileSize * mapScale), (int)(tileSize * mapScale), (int)(tileSize * mapScale));
             g.DrawImage(E, destRect, ERect, GraphicsUnit.Pixel);
           }
         }
      }

      private void drawTiles(Graphics g, Rectangle frame, int layer, float alpha)
      {
         ColorMatrix cm = new ColorMatrix();
         cm.Matrix33 = alpha;
         ImageAttributes ia = new ImageAttributes();
         ia.SetColorMatrix(cm);         

         for (int x = frame.Left; x < Math.Min(map.width(), frame.Right); ++x)
            for (int y = frame.Top; y < Math.Min(map.height(), frame.Bottom); ++y)
            {
               Tile t = map.getTile(x, y, layer);

               if (t != null)
               {
                  Rectangle destRect = new Rectangle((int)(t.x * tileSize * mapScale), (int)(t.y * tileSize * mapScale), (int)(tileSize * mapScale), (int)(tileSize * mapScale));
                  g.DrawImage(tileset,
                     destRect,
                     (tsbFrameTwo.Checked && t.animated) ? t.animBmpX * tileSize : t.bmpX * tileSize,
                     (tsbFrameTwo.Checked && t.animated) ? t.animBmpY * tileSize : t.bmpY * tileSize,
                     tileSize, tileSize, GraphicsUnit.Pixel, ia);
               }
                  


            }
      }

      private void drawSelect(Graphics g)
      {
         Pen pen = new Pen(Color.White, 2.0f);

         if (selectedObject != null)
         {
            g.DrawRectangle(pen,
                  selectedObject.X * tileSize * mapScale,
                  selectedObject.Y * tileSize * mapScale,
                  tileSize * mapScale,
                  tileSize * mapScale);
         }

         
      }

      private void drawEncounterZones(Graphics g)
      {
         Pen pen = new Pen(Color.Blue, 2.0f);
         foreach (EncounterZone ez in map.zones)
            g.DrawRectangle(pen, 
               ez.zone.X * tileSize * mapScale,
               ez.zone.Y * tileSize * mapScale,
               ez.zone.Width * tileSize * mapScale,
               ez.zone.Height * tileSize * mapScale);

         if (drawingzone)
         {
            Rectangle current = new Rectangle(
               Math.Min((int)(zoneEnd.X * tileSize * mapScale), (int)(zoneStart.X * tileSize * mapScale)),
               Math.Min((int)(zoneEnd.Y * tileSize * mapScale), (int)(zoneStart.Y * tileSize * mapScale)),
               Math.Abs((int)(zoneEnd.X * tileSize * mapScale - zoneStart.X * tileSize * mapScale)),
               Math.Abs((int)(zoneEnd.Y * tileSize * mapScale - zoneStart.Y * tileSize * mapScale)));

            g.DrawRectangle(pen, current);
         }
      }

      private void drawCollision(Graphics g, Rectangle frame)
      {
         for (int x = frame.Left; x < Math.Min(map.width(), frame.Right); ++x)
            for (int y = frame.Top; y < Math.Min(map.height(), frame.Bottom); ++y)
            {
               Tile t = map.getTile(x, y, 0);
               if (t != null)
               {
                  Rectangle destRect = new Rectangle((int)(t.x * tileSize * mapScale), (int)(t.y * tileSize * mapScale), (int)(tileSize * mapScale), (int)(tileSize * mapScale));
                  if (t.collSides[sideTop]) g.DrawImage(collArrows, destRect, collArrowRects[sideTop], GraphicsUnit.Pixel);
                  if (t.collSides[sideRight]) g.DrawImage(collArrows, destRect, collArrowRects[sideRight], GraphicsUnit.Pixel);
                  if (t.collSides[sideBottom]) g.DrawImage(collArrows, destRect, collArrowRects[sideBottom], GraphicsUnit.Pixel);
                  if (t.collSides[sideLeft]) g.DrawImage(collArrows, destRect, collArrowRects[sideLeft], GraphicsUnit.Pixel);

               }
            }
      }

      private void mapFrame_Scroll(object sender, ScrollEventArgs e)
      {
         mapPanel.Invalidate();
      }

      private void mapFrame_Resize(object sender, EventArgs e)
      {
         mapPanel.Invalidate();
      }

      private void addMaterial(int x, int y)
      {
         if (erase)
            map.deleteTile(x, y, currentLayer);
         else
         {
            map.addMaterial(x, y, selectedTile.matX, selectedTile.matY, tsbFrameTwo.Checked, currentLayer);
            lastPointAdded = new Point(x, y);
            mapPanel.Invalidate();
         }
      }

      private void swapLayer(int x, int y)
      {
         //map.swapLayer(x, y, tsbForeground.Checked);
         lastPointAdded = new Point(x, y);
         mapPanel.Invalidate();
      }

      private void addtile(int x, int y)
      {
         if (map != null)
         {
            if (erase)
               map.deleteTile(x, y, currentLayer);
            else
            {
               if (tsbFrameTwo.Checked)
                  map.animateTile(x, y, selectedTile.bmpX, selectedTile.bmpY, currentLayer);
               else
                  map.addTile(new Tile(x, y, selectedTile.bmpX, selectedTile.bmpY, currentLayer));
            
            }

            lastPointAdded = new Point(x, y);
            mapPanel.Invalidate();
         }
      }

      private void setCollision(int x, int y)
      {
         if (map != null)
         {
            Tile t = map.getTile(x, y, 0);
            if (t != null)
            {
               t.setCollision(sideTop, topToolStripMenuItem.Checked);
               t.setCollision(sideLeft, leftToolStripMenuItem.Checked);
               t.setCollision(sideBottom, bottomToolStripMenuItem.Checked);
               t.setCollision(sideRight, rightToolStripMenuItem.Checked);
               
            }
            lastPointAdded = new Point(x, y);
            mapPanel.Invalidate();
         }
      }

      private void mapPanel_MouseDown(object sender, MouseEventArgs e)
      {
         mapFrame.Focus();
         Point gridPoint = new Point();
         gridPoint.X = (int)(e.X / (tileSize * mapScale));
         gridPoint.Y = (int)(e.Y / (tileSize * mapScale));

         if (!tsbObjectLayer.Checked && Form.ModifierKeys != Keys.Control)
         {
            if (e.Button == System.Windows.Forms.MouseButtons.Left && btnDraw.Checked)
            {
               if (tsbCollision.Checked)
                 setCollision(gridPoint.X, gridPoint.Y);
               else if (tsbSwapLayers.Checked)
                  swapLayer(gridPoint.X, gridPoint.Y);
               else if (selectedTile.isMaterial)
                  addMaterial(gridPoint.X, gridPoint.Y);                 
               else
                  addtile(gridPoint.X, gridPoint.Y);

               mouseDown = true;
            }
         }
         else if (tsbEncounters.Checked)
         {
            drawingzone = true;
            zoneStart = gridPoint;
            mapPanel.Invalidate();
         }
         
      }
      private void mapPanel_MouseMove(object sender, MouseEventArgs e)
      {
         Point gridPoint = new Point();
         gridPoint.X = (int)(e.X / (tileSize * mapScale));
         gridPoint.Y = (int)(e.Y / (tileSize * mapScale));

         if (!tsbObjectLayer.Checked && Form.ModifierKeys != Keys.Control)
         {
            if (e.Button == System.Windows.Forms.MouseButtons.Left && btnDraw.Checked)
            {
               if (mouseDown)
               {  
                  if (!lastPointAdded.Equals(gridPoint))
                  {
                     if (tsbCollision.Checked)
                        setCollision(gridPoint.X, gridPoint.Y);
                     else if (tsbSwapLayers.Checked)
                        swapLayer(gridPoint.X, gridPoint.Y);
                     else if (selectedTile.isMaterial)
                        addMaterial(gridPoint.X, gridPoint.Y);
                     else
                        addtile(gridPoint.X, gridPoint.Y);
                  }                     
               }
            }
         }
         else if( tsbEncounters.Checked)
         {
            zoneEnd = gridPoint;
            mapPanel.Invalidate();
         }
         
      }

      private void mapPanel_MouseUp(object sender, MouseEventArgs e)
      {
         Point gridPoint = new Point();
         gridPoint.X = (int)(e.X / (tileSize * mapScale));
         gridPoint.Y = (int)(e.Y / (tileSize * mapScale));

         if (!tsbObjectLayer.Checked)
         {
            if (e.Button == System.Windows.Forms.MouseButtons.Left)
            {
               mouseDown = false;

               if (Form.ModifierKeys == Keys.Control)
               {
                  map.fill(gridPoint.X, gridPoint.Y, selectedTile, tsbFrameTwo.Checked, currentLayer);
                  mapPanel.Invalidate();
               }
            }
         }
         else if (tsbEncounters.Checked)
         {
            drawingzone = false;
            if (!zoneStart.Equals(gridPoint))
            {
               EncounterZone ez = new EncounterZone();
               ez.zone = new Rectangle(
                  Math.Min(gridPoint.X, zoneStart.X),
                  Math.Min(gridPoint.Y, zoneStart.Y),
                  Math.Abs(gridPoint.X - zoneStart.X),
                  Math.Abs(gridPoint.Y - zoneStart.Y));

               

               ZoneForm zf = new ZoneForm(map, ez, true);
               zf.ShowDialog();
               mapPanel.Invalidate();
            }            
            
         }
      }

      private void mapPanel_MouseDoubleClick(object sender, MouseEventArgs e)
      {
         if (tsbObjectLayer.Checked)
         {
            Point gridPoint = new Point();
            gridPoint.X = (int)(e.X / (tileSize * mapScale));
            gridPoint.Y = (int)(e.Y / (tileSize * mapScale));

            if (tsbEncounters.Checked)
            {
               foreach(EncounterZone ez in map.zones)
                  if(ez.zone.Contains(gridPoint))
                  {
                     ZoneForm zf = new ZoneForm(map, ez, false);
                     zf.ShowDialog();
                     mapPanel.Invalidate();
                     break;
                  }
            }
            else
               newObject(gridPoint.X, gridPoint.Y);            
         }

      }

      private void newObject(int x, int y)
      {
         ObjectForm oForm;

         var obj = map.getObjectAt(x, y);

         if (obj != null)
         {
            oForm = new ObjectForm(map, obj, selectedTile.bmpX, selectedTile.bmpY);
            oForm.MdiParent = parent;
            oForm.Show();
         }
         else
         {
            oForm = new ObjectForm(map, x, y, selectedTile.bmpX, selectedTile.bmpY);
            oForm.MdiParent = parent;
            oForm.Show();
         }
         
         mapPanel.Invalidate();

      }


      private void mapPanel_MouseClick(object sender, MouseEventArgs e)
      {
         Point gridPoint = new Point();
         gridPoint.X = (int)(e.X / (tileSize * mapScale));
         gridPoint.Y = (int)(e.Y / (tileSize * mapScale));

         if (!tsbObjectLayer.Checked)
         {
            if (e.Button == System.Windows.Forms.MouseButtons.Right)
            {
               Tile copyTile = map.getTile(gridPoint.X, gridPoint.Y, currentLayer);
               if (copyTile != null)
               {
                  if (tsbCollision.Checked)
                  {
                     topToolStripMenuItem.Checked = copyTile.collSides[sideTop];
                     leftToolStripMenuItem.Checked = copyTile.collSides[sideLeft];
                     bottomToolStripMenuItem.Checked = copyTile.collSides[sideBottom];
                     rightToolStripMenuItem.Checked = copyTile.collSides[sideRight];
                  }
                  else
                  {
                     erase = false;
                     selectedTile = new Tile(copyTile);
                  }
               }
               else
               {
                  erase = true;
                  selectedTile = new Tile(0, 0, 0, 0, 0);
               }
                  

               tsPanel.Invalidate();

            }
         }
         else
         {
            selectedObject = new Point(gridPoint.X, gridPoint.Y);

            if (e.Button == System.Windows.Forms.MouseButtons.Right)
            {  
               if (tsbEncounters.Checked)
               {
                  foreach (EncounterZone ez in map.zones)
                     if (ez.zone.Contains(gridPoint))
                     {
                        selectedZone = ez;
                        break;
                     }
               }

               deleteZoneToolStripMenuItem.Enabled = selectedZone != null;
               rightClickMenu.Show(mapPanel.PointToScreen(new Point(e.X, e.Y)));
            
            }

            mapPanel.Invalidate();
            
         }

         
      }

      private void allToolStripMenuItem_Click(object sender, EventArgs e)
      {
         leftToolStripMenuItem.Checked = true;
         topToolStripMenuItem.Checked = true;
         rightToolStripMenuItem.Checked = true;
         bottomToolStripMenuItem.Checked = true;
         tsbCollision.Checked = true;
      }

      private void noneToolStripMenuItem_Click(object sender, EventArgs e)
      {
         leftToolStripMenuItem.Checked = false;
         topToolStripMenuItem.Checked = false;
         rightToolStripMenuItem.Checked = false;
         bottomToolStripMenuItem.Checked = false;
      }

      private void bottomToolStripMenuItem_CheckedChanged(object sender, EventArgs e)
      {
         if (((ToolStripMenuItem)sender).Checked)
            tsbCollision.Checked = true;
      }

      private void tsbCollision_CheckedChanged(object sender, EventArgs e)
      {
         mapPanel.Invalidate();
      }

      private void tsbViewMap_CheckedChanged(object sender, EventArgs e)
      {
         mapPanel.Invalidate();
         if (!tsbObjectLayer.Checked)
            tsbEncounters.Checked = false;
      }

      private void numMapZoom_ValueChanged(object sender, EventArgs e)
      {
         if (map != null)
         {
            mapScale = (float)numMapZoom.Value / 100.0f;
            mapPanel.Width = (int)(map.width() * tileSize * mapScale);
            mapPanel.Height = (int)(map.height() * tileSize * mapScale);
            mapPanel.Invalidate();
         }
         

      }
      private void newObjectToolStripMenuItem_Click(object sender, EventArgs e)
      {
         newObject(selectedObject.X, selectedObject.Y);
      }

      private void deleteToolStripMenuItem_Click(object sender, EventArgs e)
      {
         if (MessageBox.Show("Delete Object?", "Confirm", MessageBoxButtons.YesNo) == System.Windows.Forms.DialogResult.Yes)
            map.deleteObject(selectedObject.X, selectedObject.Y);

         mapPanel.Invalidate();

      }

      private void tsbEncounters_CheckedChanged(object sender, EventArgs e)
      {
         if(tsbEncounters.Checked)
            tsbObjectLayer.Checked = true;

         mapPanel.Invalidate();
      }

      private void deleteZoneToolStripMenuItem_Click(object sender, EventArgs e)
      {
         if (selectedZone != null)
         {
            map.zones.Remove(selectedZone);
            selectedZone = null;
            mapPanel.Invalidate();
         }
         
      }

      private void copyToolStripMenuItem_Click(object sender, EventArgs e)
      {
         copiedObject = map.getObjectAt(selectedObject.X, selectedObject.Y);
      }

      private void pasteToolStripMenuItem_Click(object sender, EventArgs e)
      {
         if (copiedObject != null)
         {
            var obj = new GameObject(selectedObject.X, selectedObject.Y);
            obj.Script = copiedObject.Script;
            map.addObject(obj);
            mapPanel.Invalidate();
         }
      }

      private void tsbFrameTwo_CheckStateChanged(object sender, EventArgs e)
      {
         mapPanel.Invalidate();
      }

      private void tsbOptions_Click(object sender, EventArgs e)
      {
         MapInfoForm infoForm = new MapInfoForm(map);
         infoForm.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MapInfoForm_FormClosed);
         infoForm.ShowDialog();
      }

      public void MapInfoForm_FormClosed(object sender, FormClosingEventArgs e)
      {
         updateMap();

      }

      private void toolStripButton1_Click(object sender, EventArgs e)
      {
         map.write();
         parent.readMaps();
         MessageBox.Show("Map saved!");
      }

      private void tsbMaterial_Click(object sender, EventArgs e)
      {
         tsPanel.Invalidate();
      }

      private void toolStripButton2_Click(object sender, EventArgs e)
      {
         map.openHeaderForm(MdiParent);
      }

      private void matPanel_Paint(object sender, PaintEventArgs e)
      {
         Graphics g = e.Graphics;
         Pen selPen = new Pen(Color.Red);
         selPen.Width = 4;

         int x = 0;
         int selectedX = 0;
         foreach (Point p in map.MatList)
         {
            Rectangle destRect = new Rectangle((int)(x * tileSize * tsScale), 0, (int)(tileSize * tsScale), (int)(tileSize * tsScale));
            
            g.DrawImage( tileset,
               destRect,
               (p.X + 4) * tileSize,
               (p.Y + 1) * tileSize,
               tileSize, tileSize, GraphicsUnit.Pixel);

            if (selectedTile.IsMaterial())
               if (p.X == selectedTile.matX && p.Y == selectedTile.matY)
                  selectedX = x;

            x += 1;
         }

         if (selectedTile.IsMaterial())
            g.DrawRectangle(selPen, selectedX * tileSize * tsScale, 0, tileSize * tsScale, tileSize * tsScale);

      }

      private void btnAddMat_Click(object sender, EventArgs e)
      {
         addMaterial();
      }

      private void matPanel_MouseClick(object sender, MouseEventArgs e)
      {
         erase = false;
         matPanel.Focus();
         
         int index = (int)(e.X / (tileSize * tsScale));
         if (index < map.MatList.Count)
         {
            selectedTile.isMaterial = true;
            Point selectedMat = map.MatList[index];
            selectedTile.matX = selectedMat.X;
            selectedTile.matY = selectedMat.Y;

            tsPanel.Invalidate();
         }
         
      }

      private void tsbSwapLayers_CheckStateChanged(object sender, EventArgs e)
      {
         tsbLayerSwapTo.Enabled = tsbSwapLayers.Checked;
      }

      private void tsbLayer_TextChanged(object sender, EventArgs e)
      {
         try
         {
            currentLayer = Convert.ToInt32(tsbLayer.Text);
         }
         catch (Exception e2)
         {
            tsbLayer.Text = currentLayer.ToString();
            return;
         }

         if (currentLayer > 7)
            tsbLayer.Text = "7";

         if (currentLayer < 0)
            tsbLayer.Text = "0";

         mapPanel.Invalidate();
      }

      private void MapForm_KeyPress(object sender, KeyPressEventArgs e)
      {
         if (e.KeyChar >= '0' && e.KeyChar <= '7')
         {
            tsbLayer.Text = e.KeyChar.ToString();
         }
      }
      
   }
}
