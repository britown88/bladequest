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
      
      private enum corners
      {
        TopRight =   1 << 0,
        Top =      1 << 1,
        TopLeft =   1 << 2,
        Left =      1 << 3,
        BottomLeft = 1 << 4,
        Bottom =    1 << 5,
        BottomRight =1 << 6,
        Right =     1 << 7
      };

      private main parent;
      private BQMap map;
      private Bitmap tileset;
      private Bitmap collArrows, E;
      private Rectangle ERect;//hehe
      private Rectangle[] collArrowRects;
      private string selectedTileset;
      private float tsScale = 2.0f, mapScale = 1.0f;
      private int tileSize = 16;

      private Point selectedPoint,selectedObject;
      private bool mouseDown, erase;
      private Point lastPointAdded;

      private Point zoneStart, zoneEnd;
      private bool drawingzone;
      private EncounterZone selectedZone;

      private GameObject copiedObject;

      private  Point[] materialPoints;


      public MapForm(BQMap map)
      {
         InitializeComponent();
         mouseDown = false;
         selectedTileset = "";
         selectedPoint = new Point(0, 0);
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

         buildMaterialLocations();
         
      }

      private void buildMaterialLocations()
      {
         materialPoints = new Point[1<<8];


         materialPoints[(char)corners.Bottom | (char)corners.Right | (char)corners.BottomRight] = new Point(0, 0);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.BottomRight | (char)corners.BottomLeft] = new Point(1, 0);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.BottomLeft] = new Point(2, 0);
         materialPoints[(char)corners.Bottom | (char)corners.Right] = new Point(3, 0);
         materialPoints[(char)corners.Left | (char)corners.Right] = new Point(4, 0);
         materialPoints[(char)corners.Bottom | (char)corners.Left] = new Point(5, 0);
         materialPoints[(char)corners.Right] = new Point(6, 0);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right] = new Point(7, 0);
         materialPoints[(char)corners.Bottom] = new Point(8, 0);

         materialPoints[(char)corners.Bottom | (char)corners.Right | (char)corners.Top | (char)corners.TopRight | (char)corners.BottomRight] = new Point(0, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.TopRight | (char)corners.BottomRight | (char)corners.BottomLeft] = new Point(1, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Top | (char)corners.TopLeft | (char)corners.BottomLeft] = new Point(2, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Top] = new Point(3, 1);
         materialPoints[0] = new Point(4, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Top] = new Point(5, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Right | (char)corners.Top] = new Point(6, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top] = new Point(7, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Top] = new Point(8, 1);

         materialPoints[(char)corners.Right | (char)corners.Top | (char)corners.TopRight] = new Point(0, 2);
         materialPoints[(char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.TopRight] = new Point(1, 2);
         materialPoints[(char)corners.Left | (char)corners.Top | (char)corners.TopLeft] = new Point(2, 2);
         materialPoints[(char)corners.Right | (char)corners.Top] = new Point(3, 2);
         materialPoints[(char)corners.Left | (char)corners.Right] = new Point(4, 2);
         materialPoints[(char)corners.Left | (char)corners.Top] = new Point(5, 2);
         materialPoints[(char)corners.Top] = new Point(6, 2);
         materialPoints[(char)corners.Left | (char)corners.Right | (char)corners.Top] = new Point(7, 2);
         materialPoints[(char)corners.Left] = new Point(8, 2);

         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopRight | (char)corners.BottomRight | (char)corners.BottomLeft] = new Point(0, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.BottomRight | (char)corners.BottomLeft] = new Point(1, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.BottomRight | (char)corners.BottomLeft] = new Point(2, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopRight | (char)corners.BottomLeft] = new Point(3, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.BottomRight] = new Point(4, 3);
         materialPoints[(char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopRight] = new Point(5, 3);
         materialPoints[(char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft] = new Point(6, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.BottomRight] = new Point(7, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.BottomLeft] = new Point(8, 3);

         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopRight | (char)corners.BottomRight] = new Point(0, 4);
         //materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top] = new Point(1, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.BottomLeft] = new Point(2, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.BottomRight] = new Point(3, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.BottomLeft] = new Point(4, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Top | (char)corners.BottomLeft] = new Point(5, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Right | (char)corners.Top | (char)corners.BottomRight] = new Point(6, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Top | (char)corners.TopLeft] = new Point(7, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Right | (char)corners.Top | (char)corners.TopRight] = new Point(8, 4);

         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.TopRight | (char)corners.BottomRight] = new Point(0, 5);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.TopRight] = new Point(1, 5);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.TopRight | (char)corners.BottomLeft] = new Point(2, 5);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopRight] = new Point(3, 5);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft] = new Point(4, 5);

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
      
      private void tsbBelow_Click(object sender, EventArgs e)
      {
         tsbForeground.Checked = !tsbBackground.Checked;
         mapPanel.Invalidate();
      }
      
      private void tsbAbove_Click(object sender, EventArgs e)
      {
         if (tsbForeground.Checked)
         {
            btnDraw.Checked = true;
            btnFill.Checked = false;
         }         
         tsbBackground.Checked = !tsbForeground.Checked;
         mapPanel.Invalidate();
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

         Point selectSize = tsbMaterial.Checked ? new Point(9, 6) : new Point(1, 1);

         if (tileset != null)
         {
            g.DrawImage(tileset, 0, 0, tsPanel.Width, tsPanel.Height);
            g.DrawRectangle(selPen, selectedPoint.X * tileSize * tsScale,
               selectedPoint.Y * tileSize * tsScale, tileSize * tsScale * selectSize.X, tileSize * tsScale * selectSize.Y);
         }
            
      }

      private void tsPanel_MouseClick(object sender, MouseEventArgs e)
      {
         erase = false;
         tilesetPanel.Focus();
         selectedPoint.X = (int)(e.X / (tileSize * tsScale));
         selectedPoint.Y = (int)(e.Y / (tileSize * tsScale));
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
               if (tsbBackground.Checked)
               {
                  drawGrid(g, frame, gridPen);
                  drawTiles(g, frame, false, false);
                  drawTiles(g, frame, true, true);
               }
               else
               {
                  drawTiles(g, frame, false, true);
                  drawGrid(g, frame, gridPen);
                  drawTiles(g, frame, true, false);
               }

               if (tsbCollision.Checked)
                  drawCollision(g, frame);
            }
            else
            {
               
               drawTiles(g, frame, false, false);
               drawTiles(g, frame, true, false);
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

      private void drawTiles(Graphics g, Rectangle frame, bool foreground, bool alpha)
      {
         ColorMatrix cm = new ColorMatrix();
         if(alpha)
            cm.Matrix33 = 0.75f;
         ImageAttributes ia = new ImageAttributes();
         ia.SetColorMatrix(cm);
         

         for (int x = frame.Left; x < Math.Min(map.width(), frame.Right); ++x)
            for (int y = frame.Top; y < Math.Min(map.height(), frame.Bottom); ++y)
            {
               Tile t = map.getTile(x, y, foreground);

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
               Tile t = map.getTile(x, y, false);
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
            
      private void addtile(int x, int y)
      {
         if (map != null)
         {
            if (erase)
               map.deleteTile(x, y, tsbForeground.Checked);
            else
            {
               if (tsbFrameTwo.Checked)
                  map.animateTile(x, y, selectedPoint.X, selectedPoint.Y, tsbForeground.Checked);
               else
                  map.addTile(new Tile(x, y, selectedPoint.X, selectedPoint.Y, ""), tsbForeground.Checked);
            
            }

            lastPointAdded = new Point(x, y);
            mapPanel.Invalidate();
         }
      }

      private void addMaterial(int x, int y)
      {
         Tile t = map.getTile(x, y, tsbForeground.Checked);
         if(t != null)
            t.addToMaterial(selectedPoint.X, selectedPoint.Y);

         updateMaterialTile(x - 1, y - 1);
         updateMaterialTile(x   , y - 1);
         updateMaterialTile(x + 1, y - 1);
         updateMaterialTile(x - 1, y   );
         updateMaterialTile(x   , y   );
         updateMaterialTile(x + 1, y   );
         updateMaterialTile(x - 1, y + 1);
         updateMaterialTile(x   , y + 1);
         updateMaterialTile(x + 1, y + 1);

         lastPointAdded = new Point(x, y);
         mapPanel.Invalidate();         
      }

      private bool hasSameMaterial(int x, int y)
      {
         Tile other = map.getTile(x, y, tsbForeground.Checked);
         if (other != null)
            return other.hasSameMaterial(selectedPoint.X, selectedPoint.Y);
         else
            return false;
      }

      private void updateMaterialTile(int x, int y)
      {
         int index = y * map.width() + x;

         Tile t = map.getTile(x, y, tsbForeground.Checked);

         if (x < 0 || y < 0 || x >= map.width() || y >= map.height() || t == null || !t.hasSameMaterial(selectedPoint.X, selectedPoint.Y))
           return;

         bool top = y > 0 ? hasSameMaterial(x, y - 1): false;
         bool left = x > 0 ? hasSameMaterial(x - 1, y): false;
         bool right = x < map.width() - 1 ? hasSameMaterial(x + 1, y ) : false;
         bool bottom = y < map.height() - 1 ? hasSameMaterial(x, y + 1) : false;

         bool topLeft = top && left && hasSameMaterial(x - 1, y - 1);
         bool bottomLeft = bottom && left && hasSameMaterial(x - 1, y + 1);
         bool bottomRight = bottom && right && hasSameMaterial(x + 1, y + 1);
         bool topRight = top && right && hasSameMaterial(x + 1, y - 1);

         char flags = (char)((top ? (char)corners.Top : (char)0) | (bottom ? (char)corners.Bottom : (char)0) |
                  (right ? (char)corners.Right : (char)0) | (left ? (char)corners.Left : (char)0) |
                  (topRight ? (char)corners.TopRight : (char)0) | (bottomRight ? (char)corners.BottomRight : (char)0) |
                  (topLeft ? (char)corners.TopLeft : (char)0) | (bottomLeft ? (char)corners.BottomLeft : (char)0));


         Point p = materialPoints[flags];

         p.X += selectedPoint.X;
         p.Y += selectedPoint.Y;



         if (tsbFrameTwo.Checked)
           map.animateTile(x, y, p.X, p.Y, tsbForeground.Checked);
         else
           map.addTile(new Tile(x, y, p.X, p.Y, ""), tsbForeground.Checked);

         map.getTile(x, y, tsbForeground.Checked).addToMaterial(selectedPoint.X, selectedPoint.Y);

         

      }

      private void setCollision(int x, int y)
      {
         if (map != null)
         {
            Tile t = map.getTile(x, y, false);
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

         if (!tsbObjectLayer.Checked)
         {
            if (e.Button == System.Windows.Forms.MouseButtons.Left && btnDraw.Checked)
            {
               if (tsbCollision.Checked)
                 setCollision(gridPoint.X, gridPoint.Y);
               else
                 if (tsbMaterial.Checked)
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

         if (!tsbObjectLayer.Checked)
         {
            if (e.Button == System.Windows.Forms.MouseButtons.Left && btnDraw.Checked)
            {
               if (mouseDown)
               {
                  

                  if (!lastPointAdded.Equals(gridPoint))
                  {
                     if (tsbCollision.Checked)
                        setCollision(gridPoint.X, gridPoint.Y);
                     else
                        if (tsbMaterial.Checked)
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
               if (btnFill.Checked)
               {
                  

                  map.fill(gridPoint.X, gridPoint.Y, selectedPoint.X, selectedPoint.Y);
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
            oForm = new ObjectForm(map, obj, selectedPoint.X, selectedPoint.Y);
            oForm.MdiParent = parent;
            oForm.Show();
         }
         else
         {
            oForm = new ObjectForm(map, x, y, selectedPoint.X, selectedPoint.Y);
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
               Tile copyTile = map.getTile(gridPoint.X, gridPoint.Y, tsbForeground.Checked);
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
                     selectedPoint = new Point(copyTile.bmpX, copyTile.bmpY);
                  }
               }
               else
                  erase = true;

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

      private void btnFill_CheckedChanged(object sender, EventArgs e)
      {
         if (tsbForeground.Checked && btnFill.Checked)
         {
            btnDraw.Checked = true;
            btnFill.Checked = false;
         }
         else
         {
            btnDraw.Checked = !btnFill.Checked;
         }
         
      }

      private void btnDraw_CheckedChanged(object sender, EventArgs e)
      {
         if (tsbForeground.Checked)
         {
            btnDraw.Checked = true;
            btnFill.Checked = false;
         }
         else
            btnFill.Checked = !btnDraw.Checked;
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


      
   }
}
