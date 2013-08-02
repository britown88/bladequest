﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Imaging;

using System.Text;
using System.Windows.Forms;
using BladeCraft.Classes;
using BladeCraft.Classes.Tools;


namespace BladeCraft.Forms
{
   public partial class MapForm : Form
   {

      private main parent;
      private BQMap map;
      private Bitmap tileset;
      private Bitmap collArrows, E;
      private Rectangle ERect;//hehe
      private Rectangle[] collArrowRects;
      private float tsScale = 2.0f, mapScale = 1.0f;
      private int tileSize = 16;

      private Point selectedObject;
      private bool mouseDown, erase;
      private Point lastPointAdded;

      private Tile selectedTile;
      private string tilesetPath;

      private Point zoneStart, zoneEnd;
      private bool drawingzone;
      private EncounterZone selectedZone;

      private GameObject copiedObject;

      private int currentLayer, swapToLayer;

      private Tool activeTool;

      private class TileSelectionMapFormData : TileSelectionData
      {
         MapForm form;
         public TileSelectionMapFormData(MapForm form)
         {
            this.form = form;
         }

         public Tile selectedTile()
         {
            return form.selectedTile;
         }

         public bool eraseSelected()
         {
            return form.erase;
         }
      }
      TileSelectionMapFormData mapFormTileSelection;

      private class MapFormData : MapData
      {
         MapForm form;

         public MapFormData(MapForm form)
         {
            this.form = form;
         }
         public BQMap getMap()
         {
            return form.map;
         }

         public void invalidateDraw()
         {
            form.mapPanel.Invalidate();
         }

         public bool isAnimationFrame()
         {
            return form.tsbFrameTwo.Checked;
         }

         public int getCurrentLayer()
         {
            return form.currentLayer;
         }
         public float getTileSize()
         {
            return form.tileSize;
         }
         public float getMapScale()
         {
            return form.mapScale;
         }
      }
      private MapFormData mapFormData;

      private class CollisionMapFormData : CollisionMapData
      {
         MapData m_mapData;
         ToolStripMenuItem[] collisionItems;

         public CollisionMapFormData(MapData mapData_in,ToolStripMenuItem left, ToolStripMenuItem top, ToolStripMenuItem right, ToolStripMenuItem bottom)
         {
            m_mapData = mapData_in;
            collisionItems = new ToolStripMenuItem[4];
            collisionItems[0] = left;
            collisionItems[1] = top;
            collisionItems[2] = right;
            collisionItems[3] = bottom;
         }

         public bool getSide(int side)
         {
            return collisionItems[side].Checked;
         }
         public MapData getMapData()
         {
            return m_mapData;
         }
      }
      private CollisionMapFormData collisionMapFormData;
      

      private class SwapLayerMapFormData : SwapLayerMapData
      {
         MapForm form;
         public SwapLayerMapFormData(MapForm form)
         {
            this.form = form;
         }
         public int getSwapToLayer()
         {
            return form.swapToLayer;
         }

         public MapData getMapData()
         {
            return form.mapFormData;
         }
      }

      private SwapLayerMapFormData swapLayerMapFormData;
      string stripPath(string path)
      {

         string mapName = path.Remove(0, path.LastIndexOf('\\') + 1);
         mapName = mapName.Remove(mapName.LastIndexOf('.'));

         return mapName;
      }

      void addDrawNode(string folderName)
      {
          addDrawNode(folderName, null);
      }
      void addDrawNode(string folderName, Action<string> onCall)
      {
         int nodeCnt = TileSetTreeView.Nodes.Count;
         TileSetTreeView.Nodes.Add(folderName);
         TileSetTreeView.Nodes[nodeCnt].Tag = onCall;
         int i = 0;
         foreach (var path in System.IO.Directory.GetFiles(Application.StartupPath + "\\assets\\drawable\\"  + folderName))
         {
            TileSetTreeView.Nodes[nodeCnt].Nodes.Add(stripPath(path));
            TileSetTreeView.Nodes[nodeCnt].Nodes[i++].Tag = path.Substring(Application.StartupPath.Length + 1);
         }
      }
      private void setMaterialTile(string path)
      {
          selectedTile.tileType = Tile.Type.Material;
          selectedTile.tileset = path;
      }
      private void setWallTile(string path)
      {
          selectedTile.tileType = Tile.Type.Wall;
          selectedTile.tileset = path;
      }
      private void setObjectTile(string path)
      {
          selectedTile.tileType = Tile.Type.Object;
          selectedTile.tileset = path;
      }
      public MapForm(BQMap map)
      {
         InitializeComponent();
         mouseDown = false;
         selectedTile = new Tile(0, 0, 0, 0, null, 0);
         //this.SetStyle(ControlStyles.AllPaintingInWmPaint | ControlStyles.UserPaint | ControlStyles.DoubleBuffer, true);
         
         
         collArrows = new Bitmap("misc\\collarrows.png");
         collArrowRects = new Rectangle[4];
         collArrowRects[0] = new Rectangle(0, 0, 16, 16);
         collArrowRects[1] = new Rectangle(16, 0, 16, 16);
         collArrowRects[2] = new Rectangle(32, 0, 16, 16);
         collArrowRects[3] = new Rectangle(48, 0, 16, 16);

         this.map = map;

         map.writeMemento();

         E = new Bitmap("misc\\E.png");
         ERect = new Rectangle(0, 0, 16, 16);

         mapFormData = new MapFormData(this);
         collisionMapFormData = new CollisionMapFormData(mapFormData,
                                                         leftToolStripMenuItem, topToolStripMenuItem,
                                                         rightToolStripMenuItem, bottomToolStripMenuItem);
         swapLayerMapFormData = new SwapLayerMapFormData(this);
         mapFormTileSelection = new TileSelectionMapFormData(this);

         addDrawNode("materials", path => setMaterialTile(path));
         addDrawNode("walls", path => setWallTile(path));
         addDrawNode("stairs");
         addDrawNode("objects", path => setObjectTile(path));
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
            mapPanel.Width = (int)(map.width() * tileSize * mapScale);
            mapPanel.Height = (int)(map.height() * tileSize * mapScale);
            mapPanel.Invalidate();

         }         
      }

//       private void addMaterial()
//       {
//          map.MatList.Add(new Point(selectedTile.bmpX, selectedTile.bmpY));
//          matPanel.Height = (int)(tileSize * tsScale);
//          matPanel.Width = (int)(tileSize * map.MatList.Count * tsScale);
//          matPanel.Invalidate();
//       }
//       

      private void tscbTilesetName_SelectedIndexChanged(object sender, EventArgs e)
      {
         return;
//          string filename = "assets\\drawable\\tilesets\\" + tscbTilesetName.SelectedItem.ToString().ToLower() + ".png";
//          try
//          {
//             tileset = new Bitmap(filename);
//             selectedTileset = tscbTilesetName.SelectedItem.ToString();
//             if(map != null) map.setTileset(selectedTileset);
//          }
//          catch
//          {
//             MessageBox.Show("Tileset not found!");
//             tileset = null;
//             tscbTilesetName.SelectedItem = selectedTileset;
//          }
//          
//          if (tileset != null)
//          {
//             //pbTileset.Image = (Image)tileset;
//             tsPanel.Width = (int)(tileset.Width * tsScale);
//             tsPanel.Height = (int)(tileset.Height * tsScale);
//             
// 
//             tsPanel.Invalidate();
//             mapPanel.Invalidate();
//          }
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

         //matPanel.Invalidate();
            
      }

      private void tsPanel_MouseClick(object sender, MouseEventArgs e)
      {
         erase = false;
         tilesetPanel.Focus();
         selectedTile.tileType= Tile.Type.Singular;
         selectedTile.bmpX = (int)(e.X / (tileSize * tsScale));
         selectedTile.bmpY = (int)(e.Y / (tileSize * tsScale));
         selectedTile.tileset = tilesetPath;
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

            if (activeTool != null) activeTool.onDraw(g);

         }
         
      }

      private void drawGrid(Graphics g, Rectangle frame, Pen pen)
      {
         if (tsbShowGrid.Checked)
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

               if (t != null && t.tileset != null)
               {
                  Bitmap bmp = null;
                  if (!Bitmaps.bitmaps.TryGetValue(t.tileset, out bmp)) continue;
                  Rectangle destRect = new Rectangle((int)(t.x * tileSize * mapScale), (int)(t.y * tileSize * mapScale), (int)(tileSize * mapScale), (int)(tileSize * mapScale));
                  g.DrawImage(bmp,
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
                  if (t.collSides[Tile.sideTop]) g.DrawImage(collArrows, destRect, collArrowRects[Tile.sideTop], GraphicsUnit.Pixel);
                  if (t.collSides[Tile.sideRight]) g.DrawImage(collArrows, destRect, collArrowRects[Tile.sideRight], GraphicsUnit.Pixel);
                  if (t.collSides[Tile.sideBottom]) g.DrawImage(collArrows, destRect, collArrowRects[Tile.sideBottom], GraphicsUnit.Pixel);
                  if (t.collSides[Tile.sideLeft]) g.DrawImage(collArrows, destRect, collArrowRects[Tile.sideLeft], GraphicsUnit.Pixel);
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



      private void swapLayer(int x, int y)
      {
         map.swapLayer(x, y, currentLayer, swapToLayer);
         lastPointAdded = new Point(x, y);
         mapPanel.Invalidate();
      }



      Point clickedPoint(MouseEventArgs e)
      {
          Point gridPoint = new Point();
          gridPoint.X = (int)(e.X / (tileSize * mapScale));
          gridPoint.Y = (int)(e.Y / (tileSize * mapScale));

          return gridPoint;
      }

      private void updateTool(MouseEventArgs e)
      {
         Tool currentTool = null;
         if (!tsbObjectLayer.Checked)
         {
             if (selectedTile.IsMaterial())
             {
                 currentTool = new MaterialTool(mapFormData, mapFormTileSelection);
             }
             else if (selectedTile.tileType == Tile.Type.Wall)
             {
                 currentTool = new WallTool(mapFormData, mapFormTileSelection);
             }
             else if (selectedTile.tileType == Tile.Type.Object)
             {
                 currentTool = new ObjectTool(mapFormData, mapFormTileSelection);
             }
            if (e.Button == System.Windows.Forms.MouseButtons.Left && btnDraw.Checked)
            {
               if (Form.ModifierKeys == Keys.Control)
               {
                  currentTool = new FillTool(mapFormData, mapFormTileSelection);
               }
               else if (tsbCollision.Checked)
               {
                  currentTool = new CollisionTool(collisionMapFormData);
               }
               else if (tsbSwapLayers.Checked)
               {
                  currentTool = new SwapLayerTool(swapLayerMapFormData);
               }
               else if (currentTool == null)
               {
                   currentTool = new AddTileTool(mapFormData, mapFormTileSelection);
               }
            }

         }
         else if (tsbEncounters.Checked)
         {
            currentTool = new EncounterZoneTool(mapFormData);
         }
         if (activeTool == null || !activeTool.equals(currentTool))
         {
            activeTool = currentTool;
         }
      }

      private void mapPanel_MouseDown(object sender, MouseEventArgs e)
      {
         mapFrame.Focus();

         if (e.Button == MouseButtons.Left)
         {
             Point gridPoint = clickedPoint(e);
             updateTool(e);
             if (activeTool != null) activeTool.onClick(gridPoint.X, gridPoint.Y);
         }
      }
      private void mapPanel_MouseMove(object sender, MouseEventArgs e)
      {

         Point gridPoint = clickedPoint(e);
         updateTool(e);
         if (activeTool != null) activeTool.mouseMove(gridPoint.X, gridPoint.Y);
      }

      private void mapPanel_MouseUp(object sender, MouseEventArgs e)
      {
         Point gridPoint = clickedPoint(e);
         updateTool(e);
         if (activeTool != null)
         {
            activeTool.mouseUp(gridPoint.X, gridPoint.Y);
            map.writeMemento();
         }
      }

      private void mapPanel_MouseDoubleClick(object sender, MouseEventArgs e)
      {
         if (tsbObjectLayer.Checked)
         {
            Point gridPoint = clickedPoint(e);

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
         Point gridPoint = clickedPoint(e);

         if (!tsbObjectLayer.Checked)
         {
            if (e.Button == System.Windows.Forms.MouseButtons.Right)
            {
               if (tsbCollision.Checked)
               {
                  Tile copyColTile = map.getTile(gridPoint.X, gridPoint.Y, 0);
                  if (copyColTile != null)
                  {
                     topToolStripMenuItem.Checked = copyColTile.collSides[Tile.sideTop];
                     leftToolStripMenuItem.Checked = copyColTile.collSides[Tile.sideLeft];
                     bottomToolStripMenuItem.Checked = copyColTile.collSides[Tile.sideBottom];
                     rightToolStripMenuItem.Checked = copyColTile.collSides[Tile.sideRight];
                  }

               }
               else
               {
                  Tile copyTile = map.getTile(gridPoint.X, gridPoint.Y, currentLayer);
                  if (copyTile != null)
                  {

                     erase = false;
                     selectedTile = new Tile(copyTile);

                  }
                  else
                  {
                     erase = true;
                     selectedTile = new Tile(0, 0, 0, 0, null, 0);
                  }
                  
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
         map.writeXML();
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
      private void tsbSwapLayers_CheckStateChanged(object sender, EventArgs e)
      {
         tsbLayerSwapTo.Enabled = tsbSwapLayers.Checked;
         if (tsbSwapLayers.Checked)
            tsbLayerSwapTo.Text = tsbLayer.Text;
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
         if (e.KeyChar >= '1' && e.KeyChar <= '7')
         {
            tsbLayer.Text = e.KeyChar.ToString();
         }

         if (e.KeyChar == '`')
         {
            tsbLayer.Text = "0";
         }

         if (e.KeyChar == 26) //CTRL-Z
         {
            map.undo();
            mapPanel.Invalidate();
         }
         if (e.KeyChar == 25  ) //ctrl-Y
         {
            map.redo();
            mapPanel.Invalidate();
         }
      }

      private void tsbLayerSwapTo_TextChanged(object sender, EventArgs e)
      {
         try
         {
            swapToLayer = Convert.ToInt32(tsbLayerSwapTo.Text);
         }
         catch (Exception e2)
         {
            tsbLayerSwapTo.Text = swapToLayer.ToString();
            return;
         }

         if (swapToLayer > 7)
            tsbLayerSwapTo.Text = "7";

         if (swapToLayer < 0)
            tsbLayerSwapTo.Text = "0";
      }

      private void tsbShowGrid_CheckStateChanged(object sender, EventArgs e)
      {
         mapPanel.Invalidate();
      }

      private void btnRemoveMat_Click(object sender, EventArgs e)
      {

      }

      private void treeView1_AfterSelect(object sender, TreeViewEventArgs e)
      {

      }

      private void TileSetTreeView_NodeMouseClick(object sender, TreeNodeMouseClickEventArgs e)
      {
         if (e.Node.Tag != null && e.Node.Tag is string)
         {
            tilesetPath = (string)e.Node.Tag;
            

            if (Bitmaps.bitmaps.TryGetValue(tilesetPath, out tileset))
            {
                
               //pbTileset.Image = (Image)tileset;
               tsPanel.Width = (int)(tileset.Width * tsScale);
               tsPanel.Height = (int)(tileset.Height * tsScale);
                         
             
               tsPanel.Invalidate();
               mapPanel.Invalidate();

               if (e.Node.Parent.Tag != null)
               {
                   (e.Node.Parent.Tag as Action<string>).Invoke(tilesetPath);
               }
            }
         }
      }
   }
}

