using System;
using System.Collections.Generic;
using System.Drawing;
using System.Text;

using BladeCraft.Forms;

namespace BladeCraft.Classes.Tools
{
   class PipeTool : Tool
   {
      MapData mapData;
      TileSelectionData selectionData;
      IList<Point> polyPts;
      Nullable<Point> mousePoint;

      Point getPoint(int x, int y)
      {
         if (polyPts.Count == 0)
         {
            return new Point(x, y);
         }

         Point prevPt = polyPts[polyPts.Count - 1];

         int offsetY = y - prevPt.Y;
         int offsetX = x - prevPt.X;

         //if (offsetX > 0) ++x;
         //  else if (offsetX < 0) --x;

         if (Math.Abs(offsetY) > Math.Abs(offsetX))
         {
            x = prevPt.X;
         }
         else
         {
            y = prevPt.Y;
         }
         return new Point(x, y);
      }
      void addTile(int x, int y, int bmpX, int bmpY)
      {
         var map = mapData.getMap();
         string tileset = selectionData.selectedTile().tileset;
         var layer = mapData.getCurrentLayer();

         foreach (var writeTile in Tile.getTilesetTiles(tileset, x, y, bmpX, bmpY, layer, Tile.Type.Pipe))
         {
            map.writeTile(writeTile, mapData.isAnimationFrame(), tileset, bmpX, bmpY);
         }
      }
      public PipeTool(MapData mapData, TileSelectionData selectionData)
      {
         this.mapData = mapData;
         this.selectionData = selectionData;
         polyPts = new List<Point>();
         mousePoint = null;
      }
      bool isVertical(Point p1, Point p2)
      {
         return p1.X == p2.X;
      }
      bool isHorizontal(Point p1, Point p2)
      {
         return !isVertical(p1, p2);
      }
      public void writeSection(Point p1, Point p2, Nullable<Point> p3, bool first)
      {
         if (isVertical(p1, p2))
         {
            //vertical
            int bmpX = 0;
            int bmpY = 0;
            int step = 1;
            if (p1.Y > p2.Y) 
            {
               step = -1;
            }
            if (p3 == null || isVertical(p2, p3.Value)) //last point (or just a continuation)... write it regularly.  we don't have "end caps."
            {
               p2.Y += step;
            }
            if (first) p1.Y -= step;
            int x = p1.X;
            for (int y = p1.Y + step; y != p2.Y; y += step)
            {
               addTile(x, y, bmpX, bmpY);
            }
            if (p3 != null && !isVertical(p2, p3.Value))
            {
               bmpX = 0;
               bmpY = 2;
               if (p2.X > p3.Value.X) bmpX = 1;
               if (p1.Y > p2.Y) bmpY = 1;
               addTile(p2.X, p2.Y, bmpX, bmpY);
            }
         }
         else
         {
            //horizontal
            int bmpX = 1;
            int bmpY = 0;
            int step = 1;
            if (p1.X > p2.X)
            {
               step = -1;
            }
            if (p3 == null || isHorizontal(p2, p3.Value)) //last point (or just a continuation)... write it regularly.  we don't have "end caps."
            {
               p2.X += step;
            }
            if (first) p1.X -= step;
            int y = p1.Y;
            for (int x = p1.X + step; x != p2.X; x += step)
            {
               addTile(x, y, bmpX, bmpY);
            }
            if (p3 != null && !isHorizontal(p2, p3.Value))
            {
               bmpX = 1;
               bmpY = 1;
               if (p1.X > p2.X) bmpX = 0;
               if (p2.Y > p3.Value.Y) bmpY = 2;
               addTile(p2.X, p2.Y, bmpX, bmpY);
            }
         }
      }

      public void onClick(int x, int y)
      {
         Point nextPt = getPoint(x, y);
         mousePoint = nextPt;
         if (polyPts.Count > 1 && polyPts[polyPts.Count - 1].Equals(nextPt))
         {

            for (int i = 0; i < polyPts.Count - 1; ++i)
            {
               if (i == polyPts.Count - 2)
               {
                  writeSection(polyPts[i], polyPts[i + 1], null, i==0);
               }
               else
               {
                  writeSection(polyPts[i], polyPts[i + 1], polyPts[i + 2], i == 0);
               }
            }
            polyPts = new List<Point>();
            mousePoint = null;
         }
         else
         {
            if (polyPts.Count == 0 || !polyPts[polyPts.Count - 1].Equals(nextPt))
            {
               polyPts.Add(nextPt);
            }
         }
         mapData.invalidateDraw();
      }

      public void mouseMove(int x, int y)
      {
         mousePoint = getPoint(x, y);
         mapData.invalidateDraw();
      }
      public bool handleRightClick(int x, int y)
      {
         return false;
      }
      public void mouseUp(int x, int y)
      {

      }
      Point scale(Point p)
      {
         float tileSize = mapData.getTileSize();
         float mapScale = mapData.getMapScale();

         return new Point((int)((p.X + 0.5f) * tileSize * mapScale), (int)((p.Y + 0.5f) * tileSize * mapScale));
      }
      public void onDraw(Graphics g)
      {
         Nullable<Point> prev = null;
         Pen pen = new Pen(Color.Blue, 2.0f);

         foreach (Point p in polyPts)
         {
            if (prev != null)
            {
               g.DrawLine(pen, scale(prev.Value), scale(p));
            }
            prev = p;
         }
         if (prev != null && mousePoint != null)
         {
            g.DrawLine(pen, scale(prev.Value), scale(mousePoint.Value));
         }
      }

      public bool equals(Tool rhs)
      {
         if (rhs == null) return false;
         var thisType = rhs as PipeTool;
         return thisType != null;
      }
   }
}