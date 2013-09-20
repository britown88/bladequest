using System;
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
      private TileImage tileset;
      private Bitmap collArrows, E;
      private Rectangle ERect;//hehe
      private Rectangle[] collArrowRects;
      private float tsScale = 2.0f, mapScale = 1.0f;
      public static int tileSize = 16;

      private Point selectedObject;
      private bool erase;
      private Point lastPointAdded;

      private Tile selectedTile;
      private string tilesetPath;

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
         public int getCurrentLayer()
         {
            return form.currentLayer;
         }
         public float getTileSize()
         {
            return MapForm.tileSize;
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
      private void setRoofTile(string path)
      {
         selectedTile.tileType = Tile.Type.Roof;
         selectedTile.tileset = path;
      }
      private void setStaircaseTile(string path)
      {
         selectedTile.tileType = Tile.Type.Staircase;
         selectedTile.tileset = path;
      }
      private void setObjectTile(string path)
      {
          selectedTile.tileType = Tile.Type.Object;
          selectedTile.tileset = path;
      }
      private void setPipeTile(string path)
      {
         selectedTile.tileType = Tile.Type.Pipe;
         selectedTile.tileset = path;
      }
      private void setShadowTile(string path)
      {
         selectedTile.tileType = Tile.Type.Shadow;
         selectedTile.tileset = path;
      }
      void setSpecialHandler(TreeNode dirNode, string basePath)
      {
         Action<string> onCall = null;
         switch (basePath)
         {
            case "materials": onCall = setMaterialTile; break;
            case "objects": onCall = setObjectTile; break;
            case "pipes": onCall = setPipeTile; break;
            case "roofs": onCall = setRoofTile; break;
            case "shadows": onCall = setShadowTile; break;
            case "stairs": onCall = setStaircaseTile; break;
            case "walls": onCall = setWallTile; break;
         }
         dirNode.Tag = onCall;
      }


      class MapGFXLoader : ImageHierarchyStrategy
      {
          MapForm parent;
          public MapGFXLoader(MapForm parent)
          {
              this.parent = parent;
          }
          public void onNode(TreeNode node, string baseFolder, string relativePath)
          {
              node.Tag = relativePath;
          }

          public void onDirectory(TreeNode node, string baseFolder, string relativePath)
          {
              parent.setSpecialHandler(node, baseFolder);
          }

          public bool filter(string pathName)
          {
              return true;
          }

          public void onAnimatedDirectory(TreeNode node, string baseFolder, string relativePath)
          {
              node.Tag = relativePath;
          }
      }


      public MapForm(BQMap map)
      {
         InitializeComponent();
         selectedTile = new Tile(0, 0, 0, 0, null, 0, 0);
         //this.SetStyle(ControlStyles.AllPaintingInWmPaint | ControlStyles.UserPaint | ControlStyles.DoubleBuffer, true);


         collArrows = Bitmaps.loadRawBitmap("misc/collarrows.png");
         collArrowRects = new Rectangle[4];
         collArrowRects[0] = new Rectangle(0, 0, 16, 16);
         collArrowRects[1] = new Rectangle(16, 0, 16, 16);
         collArrowRects[2] = new Rectangle(32, 0, 16, 16);
         collArrowRects[3] = new Rectangle(48, 0, 16, 16);

         this.map = map;

         map.writeMemento();

         E = Bitmaps.loadRawBitmap("misc/E.png");
         ERect = new Rectangle(0, 0, 16, 16);

         mapFormData = new MapFormData(this);
         collisionMapFormData = new CollisionMapFormData(mapFormData,
                                                         leftToolStripMenuItem, topToolStripMenuItem,
                                                         rightToolStripMenuItem, bottomToolStripMenuItem);
         swapLayerMapFormData = new SwapLayerMapFormData(this);
         mapFormTileSelection = new TileSelectionMapFormData(this);


         ImageTreeViewBuilder.BuildImageTreeView(new MapGFXLoader(this), TileSetTreeView.Nodes);
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
          //             tileset = Bitmaps.loadRawBitmap(filename);
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

         int frameNum = (int)((DateTime.Now.Ticks / 100) % 4);

         if (tileset != null)
         {
            int xSize = tileset.xPixels / MapForm.tileSize;
            int ySize = tileset.yPixels / MapForm.tileSize;
            for (int layer = 0; layer < 8; ++layer)
            {
               for (int j = 0; j < ySize; ++j)
               {
                  for (int i = 0; i < xSize; ++i)
                  {
                      TileBitmap bestBmp = null;
                     var t = tileset.tiles[i + j * xSize];
                     Rectangle destRect = new Rectangle((int)(i * tileSize * tsScale), (int)(j * tileSize * tsScale), (int)(tileSize * tsScale), (int)(tileSize * tsScale));
                     foreach (var bmp in t.bitmaps)
                     {
                        if (bmp.layerOffset != layer) continue;
                        if (bmp.frame <= frameNum && (bestBmp == null || (bmp.frame > bestBmp.frame)))
                        {
                            bestBmp = bmp;
                        }
                     }
                     if (bestBmp != null)
                     {
                        g.DrawImage(bestBmp.bitmap,
                           destRect,
                           bestBmp.x * tileSize,
                           bestBmp.y * tileSize,
                           tileSize, tileSize, GraphicsUnit.Pixel);
                     }
                  }
               }
            }

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
      void drawTiles(Graphics g, Rectangle frame, int startLayer, int endLayer, int currentLayer, int frameNum)
      {
         float alphaStep = 0.0f;
         if (currentLayer < map.layerCount)
         {
            alphaStep = .75f / (map.layerCount - (currentLayer + 1));
         }

         for (int i = startLayer; i < endLayer; ++i)
         {
            float alpha = 1.0f;
            if (i > currentLayer)
            {
               alpha -= alphaStep * (i - currentLayer);
            }
            drawTiles(g, frame, i, alpha, frameNum);
         }
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


            int frameNum = (int)((DateTime.Now.Ticks / 100) % 4);

            int halfLayers = map.layerCount / 2;
            if (!tsbObjectLayer.Checked)
            {
               

               if (currentLayer < halfLayers)
               {
                  drawTiles(g, frame, 0, currentLayer, currentLayer, frameNum);
                  drawGrid(g, frame, gridPen);
                  drawTiles(g, frame, currentLayer, halfLayers, currentLayer, frameNum);
                  drawTiles(g, frame, 4, map.layerCount, currentLayer, frameNum);
               }
               else
               {
                  drawTiles(g, frame, 0, halfLayers, currentLayer, frameNum);
                  drawTiles(g, frame, halfLayers, currentLayer, currentLayer, frameNum);
                  drawGrid(g, frame, gridPen);
                  drawTiles(g, frame, currentLayer, map.layerCount, currentLayer, frameNum);
               }
               if (tsbCollision.Checked)
                  drawCollision(g, frame);
            }
            else
            {
               drawTiles(g, frame, 0, halfLayers, frameNum);
               drawTiles(g, frame, halfLayers, map.layerCount, frameNum);

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
      private void drawSprites(List<ISprite> sprites, Rectangle frame, float alpha)
      {
         ColorMatrix cm = new ColorMatrix();
         cm.Matrix33 = alpha;
         ImageAttributes ia = new ImageAttributes();
         ia.SetColorMatrix(cm);


      }
      private void drawTiles(Graphics g, Rectangle frame, int layer, float alpha, int frameNum)
      {
         ColorMatrix cm = new ColorMatrix();
         cm.Matrix33 = alpha;
         ImageAttributes ia = new ImageAttributes();
         ia.SetColorMatrix(cm);         

         for (int x = frame.Left; x < Math.Min(map.width(), frame.Right); ++x)
            for (int y = frame.Top; y < Math.Min(map.height(), frame.Bottom); ++y)
            {
               Tile t = null;

              int findframe = frameNum;
               while (t == null)
               {
                   t = map.getTile(x, y, layer, findframe--);
                   if (findframe < 0) break;
               }

               if (t != null)
               {
                  String tileset = t.tileset;
                  if (tileset == null) continue;

                  TileImage bmp = null;
                  if (!Bitmaps.bitmaps.TryGetValue(tileset, out bmp)) continue;

                  int bmpX = t.bmpX;
                  int bmpY = t.bmpY;

                  TileInfo info = bmp.tiles[bmpX + (bmpY * bmp.xPixels / MapForm.tileSize)];
                  
                  Rectangle destRect = new Rectangle((int)(t.x * tileSize * mapScale), (int)(t.y * tileSize * mapScale), (int)(tileSize * mapScale), (int)(tileSize * mapScale));
                  foreach(var b in info.bitmaps)
                  {
                     g.DrawImage(b.bitmap,
                        destRect,
                        b.x * tileSize,
                        b.y * tileSize,
                        tileSize, tileSize, GraphicsUnit.Pixel, ia);
                  }
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
               Tile t = map.getTile(x, y, 0, 0);
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

      Point doublePrecisionClickedPoint(MouseEventArgs e)
      {
         float scale = 0.5f * tileSize * mapScale;
         Point gridPoint = new Point();
         gridPoint.X = (int)((e.X + scale/2) / (scale));
         gridPoint.Y = (int)((e.Y + scale/2) / (scale));

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
             else if (selectedTile.tileType == Tile.Type.Roof)
             {
                currentTool = new RoofTool(mapFormData, mapFormTileSelection);
             }
             else if (selectedTile.tileType == Tile.Type.Shadow)
             {
                currentTool = new ShadowTool(mapFormData, mapFormTileSelection);
             }
             else if (selectedTile.tileType == Tile.Type.Staircase)
             {
                currentTool = new StaircaseTool(mapFormData, mapFormTileSelection);
             }
             else if (selectedTile.tileType == Tile.Type.Object)
             {
                 currentTool = new ObjectTool(mapFormData, mapFormTileSelection);
             }
             else if (selectedTile.tileType == Tile.Type.Pipe)
             {
                currentTool = new PipeTool(mapFormData, mapFormTileSelection);
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
             if (activeTool != null)
             {
                if (activeTool.getSpecialFeatures().hasDoublePrecision())
                {
                   gridPoint = doublePrecisionClickedPoint(e);
                }
                activeTool.onClick(gridPoint.X, gridPoint.Y);
             }
         }
      }
      private void mapPanel_MouseMove(object sender, MouseEventArgs e)
      {
         Point gridPoint = clickedPoint(e);
         if (gridPoint.X < 0 || gridPoint.Y < 0 ||
             gridPoint.X >= map.width() ||
             gridPoint.Y >= map.height()) return;
         updateTool(e);
         if (activeTool != null)
         {
            if (activeTool.getSpecialFeatures().hasDoublePrecision())
            {
               gridPoint = doublePrecisionClickedPoint(e);
            }
            activeTool.mouseMove(gridPoint.X, gridPoint.Y);
         }
      }

      private void mapPanel_MouseUp(object sender, MouseEventArgs e)
      {
         Point gridPoint = clickedPoint(e);
         updateTool(e);
         if (activeTool != null)
         {
            if (activeTool.getSpecialFeatures().hasDoublePrecision())
            {
               gridPoint = doublePrecisionClickedPoint(e);
            }
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
         updateTool(e);
         if (e.Button == System.Windows.Forms.MouseButtons.Right && activeTool != null)
         {
            if (activeTool.getSpecialFeatures().hasDoublePrecision())
            {
               Point p = doublePrecisionClickedPoint(e);
               if (activeTool.handleRightClick(p.X, p.Y)) return;
            }
            else if (activeTool.handleRightClick(gridPoint.X, gridPoint.Y))
            {
               return;
            }
         }

         if (!tsbObjectLayer.Checked)
         {
            if (e.Button == System.Windows.Forms.MouseButtons.Right)
            {
               if (tsbCollision.Checked)
               {
                  Tile copyColTile = map.getTile(gridPoint.X, gridPoint.Y, 0, 0);
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
                   Tile copyTile = map.getTile(gridPoint.X, gridPoint.Y, currentLayer, 0);
                  if (copyTile != null)
                  {

                     erase = false;
                     selectedTile = new Tile(copyTile);

                  }
                  else
                  {
                     erase = true;
                     selectedTile = new Tile(0, 0, 0, 0, null, 0, 0);
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
         catch (Exception)
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
         if (e.KeyChar == 25) //ctrl-Y
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
               erase = false;

               tsPanel.Width = (int)(tileset.xPixels* tsScale);
               tsPanel.Height = (int)(tileset.yPixels* tsScale);
                         
             
               tsPanel.Invalidate();
               mapPanel.Invalidate();

               if (e.Node.Parent.Tag != null)
               {
                   var parentNode = e.Node.Parent;
                   while (parentNode.Tag != null && parentNode.Tag is string) //animated directories...
                   {
                       parentNode = parentNode.Parent;
                   }
                   if (parentNode.Tag != null)
                   {
                       (parentNode.Tag as Action<string>).Invoke(tilesetPath);
                   }
               }
            }
         }
      }

      private void RedrawTimer_Tick(object sender, EventArgs e)
      {
          tsPanel.Invalidate();
          mapPanel.Invalidate();
      }
   }
}

