using System;
using System.Drawing;
using System.Collections.Generic;
using System.Text;

using BladeCraft.Forms;

namespace BladeCraft.Classes.Tools
{
   class RoofTool : Tool
   {
      public MapData mapData;
      TileSelectionData tileSelection;

      Tool subTool; //FSM

      public int getHalfScale()
      {
         float tileSize = mapData.getTileSize();
         float mapScale = mapData.getMapScale();
         return (int)(tileSize/2 * mapScale);
      }

      public Point scale(Point p)
      {
         float tileSize = mapData.getTileSize();
         float mapScale = mapData.getMapScale();

         return new Point((int)(p.X * tileSize * mapScale), (int)(p.Y * tileSize * mapScale));
      }

      class DrawHorizRoofSection : Tool
      {
         RoofTool parentTool;
         Point start, end;
         public DrawHorizRoofSection(RoofTool parentTool, Point start, Point end)
         {
            this.start = new Point(start.X, start.Y);
            this.end = new Point(end.X, start.Y+1);
            this.parentTool = parentTool;
         }

         public void onClick(int x, int y)
         {
            if (y > start.Y)
            {
               end.Y = y+1;
            }
            parentTool.horizRoofs.Add(new HorizRoof(start, end));
            parentTool.mapData.invalidateDraw();
            parentTool.subTool = new DrawRoofLine(parentTool);
         }

         public void mouseMove(int x, int y)
         {
            if (y >= start.Y)
            {
               end.Y = y+1;
            }
            parentTool.mapData.invalidateDraw();
         }

         public void mouseUp(int x, int y)
         {
            
         }

         public void onDraw(Graphics g)
         {
            Pen pen = new Pen(Color.Blue, 2.0f);
            var p1 = parentTool.scale(start);
            var p2 = parentTool.scale(end);
            int width = p2.X-p1.X;
            int height = p2.Y-p1.Y;
            g.DrawRectangle(pen, p1.X, p1.Y, width, height); 
         }

         public bool equals(Tool rhs)
         {
            return false;  
         }
         public bool handleRightClick(int x, int y)
         {
            return false;
         }
      }


      class DrawVertRoofSection : Tool
      {
         RoofTool parentTool;
         Point start;
         int length;
         int rightSize, leftSize;
         public DrawVertRoofSection(RoofTool parentTool, Point start, Point end)
         {
            this.start = new Point(start.X, start.Y);
            this.length = end.Y - start.Y;
            this.parentTool = parentTool;
            leftSize = 0;
            rightSize = 1;
         }

         public void onClick(int x, int y)
         {
            if (leftSize == 0)
            {
               leftSize = 1;
            }
            else
            {
               parentTool.subTool = new DrawRoofLine(parentTool);
               parentTool.vertRoofs.Add(new VertRoof(start, length, leftSize, rightSize));
            }
            parentTool.mapData.invalidateDraw();
         }

         public void mouseMove(int x, int y)
         {
            if (leftSize == 0)
            {
               if (x >= start.X)
               {
                  rightSize = (x- start.X) + 1;
               }
            }
            else
            {
               if (x <= start.X)
               {
                  leftSize = (start.X - x) + 1;
               }
            }
            parentTool.mapData.invalidateDraw();
         }

         public void mouseUp(int x, int y)
         {

         }

         public void onDraw(Graphics g)
         {
            Pen pen = new Pen(Color.Blue, 2.0f);


            //draw center line...
            
            var p1 = parentTool.scale(start);
            p1.X += parentTool.getHalfScale();
            p1.Y += parentTool.getHalfScale();
            var p2 = parentTool.scale(new Point(start.X, start.Y + length));
            p2.X += parentTool.getHalfScale();
            p2.Y -= parentTool.getHalfScale();

            if (leftSize > 0)
            {
               var o1 = parentTool.scale(new Point(start.X - leftSize, start.Y + leftSize + 1));
               var o2 = parentTool.scale(new Point(start.X - leftSize, start.Y + leftSize + length));

               g.DrawLine(pen, p1, o1);
               g.DrawLine(pen, o1, o2);
               g.DrawLine(pen, o2, p2);   
            }

            if (rightSize > 0)
            {
               var o1 = parentTool.scale(new Point(start.X + rightSize + 1, start.Y + rightSize + 1));
               var o2 = parentTool.scale(new Point(start.X + rightSize + 1, start.Y + rightSize + length));

               g.DrawLine(pen, p1, o1);
               g.DrawLine(pen, o1, o2);
               g.DrawLine(pen, o2, p2);   
            }

            g.DrawLine(pen, p1, p2);
         }

         public bool equals(Tool rhs)
         {
            return false;
         }
         public bool handleRightClick(int x, int y)
         {
            return false;
         }
      }

      class DrawRoofLine : Tool
      {
         Nullable<Point> firstClicked;
         Point mousePos;
         RoofTool parentTool;
         MapData mapData;
         public DrawRoofLine(RoofTool parentTool)
         {
            this.parentTool = parentTool;
            this.mapData = parentTool.mapData;
         }
         public void onClick(int x, int y)
         {
            if (firstClicked == null)
            {
               firstClicked = new Point(x, y);
               mousePos = firstClicked.GetValueOrDefault();
            }
            else
            {
               //oh shit
               var startPt = firstClicked.Value;
               int xOffset = mousePos.X - startPt.X;
               int yOffset = mousePos.Y - startPt.Y;
               if (Math.Abs(xOffset) >= Math.Abs(yOffset)) //horiz
               {
                  if (xOffset < 0)
                  {
                     --xOffset;
                     startPt.X++;
                  }
                  else ++xOffset;
                  var nextPt = new Point(startPt.X + xOffset, startPt.Y);
                  if (xOffset < 0)
                  {
                     parentTool.subTool = new DrawHorizRoofSection(parentTool, nextPt, startPt);
                  }
                  else
                  {
                     parentTool.subTool = new DrawHorizRoofSection(parentTool, startPt, nextPt);
                  }
                  parentTool.mapData.invalidateDraw();
               }
               else //vert
               {
                  if (yOffset < 0)
                  {
                     --yOffset;
                     startPt.Y++;
                  }
                  else ++yOffset;
                  var nextPt = new Point(startPt.X, startPt.Y + yOffset);
                  if (yOffset < 0)
                  {
                     parentTool.subTool = new DrawVertRoofSection(parentTool, nextPt, startPt);
                  }
                  else
                  {
                     parentTool.subTool = new DrawVertRoofSection(parentTool, startPt, nextPt);
                  }
               }
            }
            mapData.invalidateDraw();
         }

         public void mouseMove(int x, int y)
         {
            if (firstClicked != null)
            {
               mousePos = new Point(x, y);
               mapData.invalidateDraw();
            }
         }

         public void mouseUp(int x, int y)
         {
            
         }
         
         public void onDraw(Graphics g)
         {
            Pen pen = new Pen(Color.Blue, 2.0f);

            if (firstClicked == null) return;
            var startPt = firstClicked.Value;
            int xOffset = mousePos.X - startPt.X;
            int yOffset = mousePos.Y - startPt.Y;
            if (Math.Abs(xOffset) >= Math.Abs(yOffset)) //horiz
            {
               if (xOffset < 0)
               {
                  --xOffset;
                  startPt.X++;
               }
               else ++xOffset;
               var nextPt = new Point(startPt.X + xOffset, startPt.Y);
               g.DrawLine(pen, parentTool.scale(startPt), parentTool.scale(nextPt));
            }
            else //vert
            {
               if (yOffset < 0)
               {
                  --yOffset;
                  startPt.Y++;
               }
               else ++yOffset;

               var firstPt = parentTool.scale(startPt);
               var nextPt = parentTool.scale(new Point(startPt.X, startPt.Y + yOffset));
               nextPt.X += parentTool.getHalfScale();
               firstPt.X += parentTool.getHalfScale();

               if (yOffset < 0)
               {
                  firstPt.Y -= parentTool.getHalfScale();
                  nextPt.Y += parentTool.getHalfScale();
               }
               else
               {
                  firstPt.Y += parentTool.getHalfScale();
                  nextPt.Y -= parentTool.getHalfScale();
               }
               
               g.DrawLine(pen, firstPt, nextPt);
            }
         }

         public bool equals(Tool rhs)
         {
            return false;
         }
         public bool handleRightClick(int x, int y)
         {
            return false;
         }
      }

      class HorizRoof : IComparable<HorizRoof>
      {
         public HorizRoof(Point topLeft, Point bottomRight)
         {
            this.topLeft = topLeft;
            this.bottomRight = bottomRight;
         }
         public Point topLeft;
         public Point bottomRight;

         public int CompareTo(HorizRoof other)
         {
            if (topLeft.Y < other.topLeft.Y) return -1;
            if (topLeft.Y > other.topLeft.Y) return 1;
            return topLeft.X - other.topLeft.X;
         }
      }

      class VertRoof
      {
         public VertRoof(Point top,
         int length,
         int left,
         int right)
         {
            this.top = top;
            this.length = length;
            this.left = left;
            this.right = right;
         }
         public Point top;
         public int length;
         public int right;
         public int left;
      }

      List<HorizRoof> horizRoofs;
      List<VertRoof> vertRoofs;

      Point lastPointAdded;
      bool mouseDown = false;

      public RoofTool(MapData mapData, TileSelectionData tileSelection)
      {
         this.mapData = mapData;
         this.tileSelection = tileSelection;
         subTool = new DrawRoofLine(this);
         horizRoofs = new List<HorizRoof>();
         vertRoofs= new List<VertRoof>();
      }
      public void onClick(int x, int y)
      {
         lastPointAdded = new Point(x, y);
         subTool.onClick(x, y);
         mouseDown = true;
      }

      public void mouseMove(int x, int y)
      {
         if ((x != lastPointAdded.X ||
             y != lastPointAdded.Y))
         {
            lastPointAdded = new Point(x, y);
            subTool.mouseMove(x, y);
         }
      }
      void addTile(int x, int y, int bmpX, int bmpY)
      {
         var map = mapData.getMap();
         string tileset = tileSelection.selectedTile().tileset;
         var layer = mapData.getCurrentLayer();



         foreach (var writeTile in Tile.getTilesetTiles(tileset, x, y, bmpX, bmpY, layer, Tile.Type.Wall))
         {
            map.writeTile(writeTile, mapData.isAnimationFrame(), tileset, bmpX, bmpY);
         }
      }
      void addRoof(HorizRoof roof)
      {
         for (int y = roof.topLeft.Y; y < roof.bottomRight.Y; ++y)
         {
            for (int x = roof.topLeft.X; x < roof.bottomRight.X; ++x)
            {
               int bmpY = 1;
               if (y == roof.topLeft.Y) bmpY = 0;
               else if (y == roof.bottomRight.Y-1) bmpY = 2;
               addTile(x, y, 0, bmpY);
            }
         }
      }
      void writeVerticalColumn(int centerX, int x, int yStart, int length, int bmpX, int heightOffset)
      {
         for (int i = 0; i < length; ++i)
         {
            int y = yStart + i;
            bool culled = false;
            int bmpY = 3;
            if (i == 0) bmpY = 2;
            else if (i == length-1) bmpY = 4;

            foreach (var roof in horizRoofs)
            {
               int heightDiff = 0;
               //handle offset X starts
               if (centerX < roof.topLeft.X)
               {
                  heightDiff = roof.topLeft.X - centerX;
               }
               if (centerX >= roof.bottomRight.X)
               {
                  heightDiff = 1+ centerX - roof.bottomRight.X;
               }
               //handle vertical from inside wall.
               if ((yStart - heightOffset) > roof.topLeft.Y)
               {
                  heightDiff = roof.topLeft.Y - yStart + heightOffset;
               }
               if (x >= roof.topLeft.X && x < roof.bottomRight.X &&
                   y >= roof.topLeft.Y && y < roof.bottomRight.Y)  //this point is inside of another roof!!!
               {
                  int rhsHeight = y - roof.topLeft.Y + heightDiff;
                  if (heightOffset > rhsHeight)
                  {
                     culled = true;
                     break; //behind this horizontal wall!!
                  }
                  else if (heightOffset == rhsHeight)
                  {
                     if (bmpY == 4)  ///????
                     {
                        culled = true;
                        break;
                     }
                     if (y == roof.topLeft.Y)
                     {
                        bmpY = 0;
                     }
                     else
                     {
                        bmpY = 1;
                     }
                  }
               }
            }
            if (culled) continue;

            addTile(x, y, bmpX, bmpY);
         }
      }
      void addRoof(VertRoof roof)
      {
         //int maxHeight = Math.Max(roof.left, roof.right);
         int xOffset = roof.left;
         //left section
         for (int i = xOffset; i > 0; --i)
         {
            int startX = roof.top.X - i;
            int startY = roof.top.Y + i;
            writeVerticalColumn(roof.top.X, startX, startY, roof.length, i == xOffset ? 1 : 2, i);
         }
         //center section
         writeVerticalColumn(roof.top.X, roof.top.X, roof.top.Y, roof.length, 3, 0);
         //right section
         xOffset = roof.right;
         for (int i = 1; i <= xOffset; ++i)
         {
            int startX = roof.top.X + i;
            int startY = roof.top.Y + i;
            writeVerticalColumn(roof.top.X, startX, startY, roof.length, i == xOffset ? 5 : 4, i);
         }
      }
      public bool handleRightClick(int x, int y)
      {
         if (horizRoofs.Count == 0 && vertRoofs.Count == 0)
         {
            return false; //fall through if there are no roofs.
         }
         //flush data
         horizRoofs.Sort();

         foreach (var roof in horizRoofs)
         {
            addRoof(roof);
         }

         foreach (var roof in vertRoofs)
         {
            addRoof(roof);
         }

         vertRoofs = new List<VertRoof>();
         horizRoofs = new List<HorizRoof>();
         subTool = new DrawRoofLine(this);
         mapData.invalidateDraw();
         return true;
      }
      public void mouseUp(int x, int y)
      {
         mouseDown = false;
      }

      public void onDraw(Graphics g) 
      {
         subTool.onDraw(g);
         //also draw all held roof objects.

         foreach (var horizRoof in horizRoofs)
         {
            Pen pen = new Pen(Color.Blue, 2.0f);
            var p1 = scale(horizRoof.topLeft);
            var p2 = scale(horizRoof.bottomRight);
            int width = p2.X - p1.X;
            int height = p2.Y - p1.Y;
            g.DrawRectangle(pen, p1.X, p1.Y, width, height); 
         }

         foreach (var roof in vertRoofs)
         {
            Pen pen = new Pen(Color.Blue, 2.0f);


            //draw center line...

            var p1 = scale(roof.top);
            p1.X += getHalfScale();
            p1.Y += getHalfScale();
            var p2 = scale(new Point(roof.top.X, roof.top.Y + roof.length));
            p2.X += getHalfScale();
            p2.Y -= getHalfScale();

            if (roof.left > 0)
            {
               var o1 = scale(new Point(roof.top.X - roof.left, roof.top.Y + roof.left + 1));
               var o2 = scale(new Point(roof.top.X - roof.left, roof.top.Y + roof.left + roof.length));

               g.DrawLine(pen, p1, o1);
               g.DrawLine(pen, o1, o2);
               g.DrawLine(pen, o2, p2);
            }

            if (roof.right > 0)
            {
               var o1 = scale(new Point(roof.top.X + roof.right + 1, roof.top.Y + roof.right + 1));
               var o2 = scale(new Point(roof.top.X + roof.right + 1, roof.top.Y + roof.right + roof.length));

               g.DrawLine(pen, p1, o1);
               g.DrawLine(pen, o1, o2);
               g.DrawLine(pen, o2, p2);
            }

            g.DrawLine(pen, p1, p2);
         }
      }

      public bool equals(Tool rhs)
      {
         if (rhs == null) return false;
         var thisType = rhs as RoofTool;
         return thisType != null;  
      }
   }
}
