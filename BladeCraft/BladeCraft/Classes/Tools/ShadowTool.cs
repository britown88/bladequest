using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace BladeCraft.Classes.Tools
{
   //general shadow rules - corners can be connected to other corners, or directly across from itself in half tile increments (pure X or Y only.)
   //if at a midpoint - can be connected over to a corner or directly across from itself.  (no way to go to the center of a tile!!!)
   //a midpoint can also continue on to a corner point.

   //double precision!

   class ShadowTool : Tool, ToolSpecialFeatures
   {
      public MapData mapData;
      public TileSelectionData selectionData;
      public List<Point> polyPts;
      public Nullable<Point> mousePoint;

      class CornerTool : Tool 
      {
         ShadowTool parent;
         Point filterClick(int x, int y)
         {
            var lastPoint = parent.polyPts[parent.polyPts.Count-1];

            // is it more diagonal, more vertical, or horizontal?
            int xOffset = Math.Abs(lastPoint.X - x);
            int yOffset = Math.Abs(lastPoint.Y - y);
            int offset = (xOffset > yOffset) ? xOffset : yOffset;
            int diagonalOffset = offset - Math.Abs(xOffset - yOffset);

            if (diagonalOffset > offset/2)  //primarily diagonal.
            {
               if ((offset & 1) != 0) ++offset; //can't go half-steps diagonally!
               if (x > lastPoint.X) x = lastPoint.X + offset;
               else x = lastPoint.X - offset;

               if (y > lastPoint.Y) y = lastPoint.Y + offset;
               else y = lastPoint.Y - offset;
            }
            else if (xOffset > yOffset)
            {
               y = lastPoint.Y;
            }
            else
            {
               x = lastPoint.X;
            }
            return new Point(x,y);
         }
         public CornerTool(ShadowTool parent)
         {
            this.parent = parent;
         }
         public bool handleRightClick(int x, int y)
         {
            return false;
         }

         public void onClick(int x, int y)
         {
            var clickPt = filterClick(x, y);
            parent.polyPts.Add(clickPt);
            if ((clickPt.X % 2) != 0 || (clickPt.Y % 2) != 0)
            {
               parent.activeTool = new MidpointTool(parent);
            }
         }

         public void mouseMove(int x, int y)
         {
            parent.mousePoint = filterClick(x, y);
         }

         public void mouseUp(int x, int y)
         {
            
         }

         public void onDraw(System.Drawing.Graphics g)
         {
            
         }

         public bool equals(Tool rhs)
         {
            return false;
         }
      }
      class MidpointTool : Tool
      {
         ShadowTool parent;
         Point filterClick(int x, int y)
         {
            var lastPoint = parent.polyPts[parent.polyPts.Count-1];

            // is it more diagonal, more vertical, or horizontal?
            int xOffset = Math.Abs(lastPoint.X - x);
            int yOffset = Math.Abs(lastPoint.Y - y);
            int offset = (xOffset > yOffset) ? xOffset : yOffset;
            int diagonalOffset = offset - Math.Abs(xOffset - yOffset);

            if (diagonalOffset > offset/2)  //primarily diagonal.
            {
               //if ((offset & 1) == 0) ++offset; //can ONLY go half-steps diagonally!
               offset = 1;
               if (x > lastPoint.X) x = lastPoint.X + offset;
               else x = lastPoint.X - offset;

               if (y > lastPoint.Y) y = lastPoint.Y + offset;
               else y = lastPoint.Y - offset;
            }
            else if (xOffset > yOffset)
            {
               y = lastPoint.Y;
               if ((y % 2) != 0 && (offset % 2) != 0)  //would put things into a bad midpoint state.
               {
                  if (x > lastPoint.X) ++x;
                  else --x;
               }
            }
            else
            {
               x = lastPoint.X;
               if ((x % 2) != 0 && (offset % 2) != 0)  //would put things into a bad midpoint state.
               {
                  if (y > lastPoint.Y) ++y;
                  else --y;
               }
            }
            return new Point(x,y);
         }
         public MidpointTool(ShadowTool parent)
         {
            this.parent = parent;
         }
         public bool handleRightClick(int x, int y)
         {
            return false;
         }

         public void onClick(int x, int y)
         {
            var clickPt = filterClick(x, y);
            parent.polyPts.Add(clickPt);
            if ((clickPt.X % 2) == 0 && (clickPt.Y % 2) == 0)
            {
               parent.activeTool = new CornerTool(parent);
            }
         }

         public void mouseMove(int x, int y)
         {
            parent.mousePoint = filterClick(x, y);
         }

         public void mouseUp(int x, int y)
         {
            
         }

         public void onDraw(System.Drawing.Graphics g)
         {
            
         }

         public bool equals(Tool rhs)
         {
            return false;
         }
      }

      Tool activeTool;

      public ShadowTool(MapData mapData, TileSelectionData tileSelection)
      {
         this.mapData = mapData;
         this.selectionData = tileSelection;
         polyPts = new List<Point>();
         activeTool = null;
      }
      
      public bool handleRightClick(int x, int y)
      {
         return false;   
      }
      enum ShadowTileClass
      {
         Inside, Outside, Coincident
      }

      ShadowTileClass classifyAgainst(int x, int y, Point pt1, Point pt2)
      {
         Point p1, p2;
         if (pt1.Y > pt2.Y)
         {
            p1 = pt2;
            p2 = pt1;
         }
         else
         {
            p1 = pt1;
            p2 = pt2;
         }

         //required to be between 0/1 to classify against 0.  ignore horiz.

         if (y >= p2.Y / 2 || y < p1.Y / 2) return ShadowTileClass.Outside;
         if (x > p2.X / 2 && x > p1.X / 2) return ShadowTileClass.Outside;

         //a more interesting case!  Are we coincident?

         int v0x = (p2.X - p1.X);
         int v0y = (p2.Y - p1.Y);
         int v1x = (x * 2 + 1) - p1.X;
         int v1y = (y * 2 + 1) - p1.Y;

         int det = v0x * v1y - v0y * v1x;

         if (det == 0) //oh shit, we're coincident!
         {
            return ShadowTileClass.Coincident;
         }
         else if (det < 0) //wrong side
         {
            return ShadowTileClass.Outside;
         }
         return ShadowTileClass.Inside;
      }
      void addTile(int x, int y, int bmpX, int bmpY)
      {
         var map = mapData.getMap();
         string tileset = selectionData.selectedTile().tileset;
         var layer = mapData.getCurrentLayer();

         map.deleteTile(x, y, mapData.getCurrentLayer());
         foreach (var writeTile in Tile.getTilesetTiles(tileset, x, y, bmpX, bmpY, layer, Tile.Type.Shadow))
         {
            map.writeTile(writeTile, tileset, bmpX, bmpY);
         }
      } 
      void writeShadowTile(int x, int y)
      {
         Nullable<Point> prev = null;
         int refs = 0;
         foreach (Point p in polyPts)
         {

            if (prev != null)
            {
               switch (classifyAgainst(x, y, prev.Value, p))
               {
                  case ShadowTileClass.Outside:
                     break;
                  case ShadowTileClass.Inside:
                     ++refs;
                     break;
                  case ShadowTileClass.Coincident:
                     return;
               }
            }
            prev = p;
         }
         switch (classifyAgainst(x, y, prev.Value, polyPts[0]))
         {
            case ShadowTileClass.Outside:
               break;
            case ShadowTileClass.Inside:
               ++refs;
               break;
            case ShadowTileClass.Coincident:
               return;
         }
         if (refs % 2 != 0)  //fully internal!
         {
            addTile(x, y, 0,0);
         }
      }
      void orientPolyPoints()
      {
         int topPt = 0;
         int i = 0;
         foreach (var p in polyPts)
         {
            var top = polyPts[topPt];
            if (p.X < top.X ||
               (p.X == top.X && p.Y < top.Y))
            {
               topPt = i;
            }
            ++i;
         }
         int prev = (topPt - 1 + polyPts.Count) % polyPts.Count;
         int next = (topPt + 1) % polyPts.Count;

         //take the det of this to determine whether or not the segment is a hole. (cocks)

         int v0x, v0y, v1x, v1y;
         v0x = polyPts[next].X - polyPts[prev].X;
         v0y = polyPts[next].Y - polyPts[prev].Y;
         v1x = polyPts[topPt].X - polyPts[prev].X;
         v1y = polyPts[topPt].Y - polyPts[prev].Y;

         int det = v0x * v1y - v0y * v1x;

         if (det < 0) //BITCHES WE AINT GOT TIME FOR YOUR CLOCKWISE BULLSHIT!
         {
            polyPts.Reverse();
         }
      }
      void writeLine(Point p1, Point p2)
      {
         if (p1.X != p2.X)
         {
            if (p1.Y == p2.Y)
            {
               if (p1.Y %2 == 0) return;
               int y = p1.Y/2;
               if (p1.X < p2.X)  //left to right, so bottom half.
               {
                  int x1 = p1.X / 2;
                  int x2 = p2.X / 2;
                  
                  for (int x = x1; x < x2; ++x)
                  {
                     addTile(x, y, 0, 2);
                  }
               }
               else
               {
                  int x1 = p2.X / 2;
                  int x2 = p1.X / 2;

                  for (int x = x1; x < x2; ++x)
                  {
                     addTile(x, y, 1, 2);
                  }
               }
            }
            else
            {
               int x = p1.X / 2;
               int y = p1.Y / 2;
               int diff = Math.Abs(p2.X - p1.X);
               if (diff == 1)
               {
                  //if stepX < 0 then p1.X %2 == 1 (NOT TRUE, ignore steps!)
                  //use the X and Y
                  if (p1.X % 2 == 1) x = p1.X / 2;
                  else x = p2.X / 2;

                  if (p1.Y % 2 == 1) y = p1.Y / 2;
                  else y = p2.Y / 2;
               }
               int stepX = (p2.X - p1.X)/(diff);
               int stepY = (p2.Y - p1.Y)/(diff);
               int bmpX = 0;
               int bmpY = 0;

               if (diff > 1)
               {
                  if (stepX < 0) --x;
                  if (stepY < 0) --y;
               }

               //possibly reversed because math is HARD
               //1 - bottom left -> top right
               //2 -> top left -> bottom right
               //3 -> bottom right -> top left
               //4 -> top right -> bottom left 

               if (p1.X < p2.X)  //left -> right
               {
                  if (p1.Y > p2.Y) // bottom -> top
                  {
                     bmpX = 0;
                     bmpY = 0;
                  }
                  else // top - > bottom
                  {
                     bmpX = 1;
                     bmpY = 0;
                  }
               }
               else  // right -> left
               {
                  if (p1.Y > p2.Y) // bottom -> top
                  {
                     bmpX = 0;
                     bmpY = 1;
                  }
                  else // top - > bottom
                  {
                     bmpX = 1;
                     bmpY = 1;
                  }
               }
               if (p1.X % 2 != 0 || p1.Y % 2 != 0)
               {
                  bmpY += 5;
               }
               else
               {
                  bmpY += 3;
               }
               diff /= 2;
               if (diff == 0) diff = 1;
               for (int i = 0; i < diff; ++i)
               {
                  addTile(x, y, bmpX, bmpY);
                  x += stepX;
                  y += stepY;
               }
            }
         }
         else if (p1.Y != p2.Y)
         {
            if (p1.X % 2 == 0) return;
            int x = p1.X / 2;
            if (p1.Y < p2.Y)  //left to right, so bottom half.
            {
               int y1 = p1.Y / 2;
               int y2 = p2.Y / 2;

               for (int y = y1; y < y2; ++y)
               {
                  addTile(x, y, 1, 1);
               }
            }
            else
            {
               int y1 = p2.Y / 2;
               int y2 = p1.Y / 2;

               for (int y = y1; y < y2; ++y)
               {
                  addTile(x, y, 0, 1);
               }
            }
         }
      }
      void writeShadow()
      {
         orientPolyPoints();

         Point top = new Point(int.MaxValue, int.MaxValue);
         Point bot = new Point(int.MinValue, int.MinValue);

         foreach (var p in polyPts)
         {
            if (p.X < top.X) top.X = p.X;
            if (p.X > bot.X) bot.X = p.X;
            if (p.Y < top.Y) top.Y = p.Y;
            if (p.Y > bot.Y) bot.Y = p.Y;
         }

         for (int y = top.Y / 2; y < bot.Y / 2; ++y)
         {
            for (int x = top.X / 2; x < bot.X / 2; ++x)
            {
               writeShadowTile(x, y);
            }
         }

         Nullable<Point> prev = null;
         foreach (Point p in polyPts)
         {
            if (prev.HasValue)
            {
               writeLine(prev.Value, p);
            }
            prev = p;
         }
         writeLine(prev.Value, polyPts[0]);

         polyPts = new List<Point>();
         activeTool = null;
      }
      public void onClick(int x, int y)
      {

         if (activeTool == null) //first click!
         {
            if ((y % 2) != 0)  //vertical midpoint.
               //   |
               //   x
               //   |
            {
               if ((x % 2) != 0) x -= 1;  //can't start right in the middle.
               activeTool = new MidpointTool(this);
            }
            else if ((x % 2) != 0) //horizontal midpoint. -x-
            {
               activeTool = new MidpointTool(this);
            }
            else  //on a corner 
            {
               activeTool = new CornerTool(this);
            }
            mousePoint = new Point(x,y);
            polyPts.Add(mousePoint.Value);
            //do something here....
            return;
         }
         activeTool.onClick(x, y);
         if (polyPts.Count > 1 && polyPts[0].Equals(polyPts[polyPts.Count-1]))
         {
            //we've come full loop!
            polyPts.RemoveAt(polyPts.Count - 1);
            writeShadow();
         }
         mapData.invalidateDraw();
      }

      public void mouseMove(int x, int y)
      {
         if (activeTool == null) 
         {
            return;
         }
         activeTool.mouseMove(x, y);
         mapData.invalidateDraw();
      }

      public void mouseUp(int x, int y)
      {
         if (activeTool == null)
         {
            return;
         }
         activeTool.mouseUp(x, y);                  
      }
      Point scale(Point p)
      {
         float tileSize = mapData.getTileSize() * 0.5f;
         float mapScale = mapData.getMapScale();

         return new Point((int)(p.X * tileSize * mapScale), (int)(p.Y * tileSize * mapScale));
      }
      public void onDraw(System.Drawing.Graphics g)
      {
         if (activeTool != null)
         {
            activeTool.onDraw(g);
         }

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
            prev = mousePoint.Value;
         }
      }

      public bool equals(Tool rhs)
      {
         if (rhs == null) return false;
         var thisType = rhs as ShadowTool;
         return thisType != null;
      }

      public bool hasDoublePrecision()
      {
         return true;
      }
   }
}
