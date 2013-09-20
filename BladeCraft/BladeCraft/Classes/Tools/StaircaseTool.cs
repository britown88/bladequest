using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace BladeCraft.Classes.Tools
{
   class StaircaseTool : Tool
   {
      MapData mapData;
      TileSelectionData selectionData;
      Nullable<Point> clickPoint;
      Point mousePoint;

      public StaircaseTool(MapData mapData, TileSelectionData selectionData)
      {
         this.mapData = mapData;
         this.selectionData = selectionData;
      }

      public bool handleRightClick(int x, int y)
      {
         return false; //unused!
      }

      public void onClick(int x, int y)
      {
         if (clickPoint.HasValue)
         {
            var startPt = clickPoint.Value;
            clickPoint = null;
            int dist = Math.Abs(x - startPt.X) + 1;
            if (x < startPt.X)
            {
               x = startPt.X;
               y = startPt.Y;
               for (int i = 0; i < dist; ++i)
               {
                  addTile(x, y - 1, 0, 2);
                  addTile(x, y, 0, 3);
                  --x;
                  --y;
               }
            }
            else
            {
               x = startPt.X;
               y = startPt.Y;
               for (int i = 0; i < dist; ++i)
               {
                  addTile(x, y - 1, 0, 0);
                  addTile(x, y, 0, 1);
                  ++x;
                  --y;
               }
            }
         }
         else
         {
            clickPoint = mousePoint = new Point(x, y);
         }
         mapData.invalidateDraw();
      }

      public void mouseMove(int x, int y)
      {
         mousePoint = new Point(x, y);
         mapData.invalidateDraw();
      }

      public void mouseUp(int x, int y)
      {
         
      }
      void addTile(int x, int y, int bmpX, int bmpY)
      {
         var map = mapData.getMap();
         string tileset = selectionData.selectedTile().tileset;
         var layer = mapData.getCurrentLayer();

         map.deleteTile(x, y, mapData.getCurrentLayer());
         foreach (var writeTile in Tile.getTilesetTiles(tileset, x, y, bmpX, bmpY, layer, Tile.Type.Staircase))
         {
            map.writeTile(writeTile, tileset, bmpX, bmpY);
         }
      }

      public Point scale(Point p)
      {
         float tileSize = mapData.getTileSize();
         float mapScale = mapData.getMapScale();

         return new Point((int)(p.X * tileSize * mapScale), (int)(p.Y * tileSize * mapScale));
      }
      public void onDraw(System.Drawing.Graphics g)
      {
         if (!clickPoint.HasValue) return;

         Pen pen = new Pen(Color.Blue, 2.0f);

         var startPt = clickPoint.Value;
         int dist = Math.Abs(mousePoint.X - startPt.X) + 1;
         var rhsPt = new Point(mousePoint.X, startPt.Y - dist);
         if (mousePoint.X < startPt.X)
         {
            startPt.X += 1;
            var p1 = scale(startPt);
            var p2 = scale(rhsPt);
            startPt.Y += 1;
            rhsPt.Y += 1;
            var p3 = scale(startPt);
            var p4 = scale(rhsPt);
            g.DrawLine(pen, p1, p2);
            g.DrawLine(pen, p3, p4);
         }
         else
         {
            rhsPt.X += 1;
            var p1 = scale(startPt);
            var p2 = scale(rhsPt);
            startPt.Y += 1;
            rhsPt.Y += 1;
            var p3 = scale(startPt);
            var p4 = scale(rhsPt);
            g.DrawLine(pen, p1, p2);
            g.DrawLine(pen, p3, p4);
         }
      }
      public bool equals(Tool rhs)
      {
         if (rhs == null) return false;
         var thisType = rhs as StaircaseTool;
         return thisType != null;
      }

   }
}
