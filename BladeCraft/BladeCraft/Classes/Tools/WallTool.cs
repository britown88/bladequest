using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using BladeCraft.Forms;

namespace BladeCraft.Classes.Tools
{
    class WallTool : Tool
    {
        MapData mapData;
        TileSelectionData selectionData;
        IList<Point> polyPts;
        Nullable<Point> mousePoint;


        int height;
        bool selectingHeight;

        Point getPoint(int x, int y)
        {
            if (!polyPts.Any())
            {
                return new Point(x, y);
            }

            x += 1;
            //got to get one that's either diagonal or straight on X.

            Point prevPt = polyPts[polyPts.Count - 1];

            int offsetY = y - prevPt.Y;
            int offsetX = x - prevPt.X;

            //if (offsetX > 0) ++x;
          //  else if (offsetX < 0) --x;

            if (Math.Abs(offsetY) > Math.Abs(offsetX/2))
            {
                if (offsetY < 0)
                    y = prevPt.Y - Math.Abs(offsetX);
                else
                    y = prevPt.Y + Math.Abs(offsetX);
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


            var tile = new Tile(x, y, bmpX, bmpY, tileset, layer);

            if (tile.layer == 0 && tile.tileset != null)
            {
               var bmpImage = Bitmaps.bitmaps[tile.tileset];
               var tileData = bmpImage.tiles[tile.bmpX + tile.bmpY * bmpImage.xPixels / MapForm.tileSize];

               tile.collSides[0] = tileData.colLeft;
               tile.collSides[1] = tileData.colRight;
               tile.collSides[2] = tileData.colTop;
               tile.collSides[3] = tileData.colBottom;
            }
            tile.tileType = Tile.Type.Wall;
            map.addTile(tile);
        }
        void writeSubsection(Point p, bool diagForward, bool diagBack, bool leftWall, bool rightWall)
        {
            if (!diagForward && !diagBack)
            {
                if (height == 1)
                {
                    if (leftWall && rightWall)
                    {
                        addTile(p.X, p.Y, 3, 0);
                    }
                    else if (leftWall)
                    {
                        addTile(p.X, p.Y, 0, 0);
                    }
                    else if (rightWall)
                    {
                        addTile(p.X, p.Y, 2, 0);
                    }
                    else
                    {
                        addTile(p.X, p.Y, 1, 0);
                    }
                }
                else
                {
                    for (int y = p.Y; y < p.Y + height; ++y)
                    {
                        int bmpY = 2;
                        if (y == p.Y) bmpY = 1;
                        if (y == p.Y + height - 1) bmpY = 3;
                        if (leftWall && rightWall)
                        {
                            addTile(p.X, y, 3, bmpY);
                        }
                        else if (leftWall)
                        {
                            addTile(p.X, y, 0, bmpY);
                        }
                        else if (rightWall)
                        {
                            addTile(p.X, y, 2, bmpY);
                        }
                        else
                        {
                            addTile(p.X, y, 1, bmpY);
                        }
                    }
                }
            }
            else
            {
                //you never are both top and bottom here!!
                //diagonal walls always use at least two tiles, even if they are just one high.  This is because they're half in one tile, half in another.
                //you also can't have rightwall && top or leftwall && bottom!

                //0,0 <- leftWall && !top && !bottom
                //1,0 <- rightWall && !top && !bottom
                //2,0 <- no walls && !top && !bottom
                //3,0 <- leftWall && top
                //0,1 <- no walls && top
                //1,1 <- rightWall && bottom
                //2,1 <- useless
                //3,1 <- no walls && bottom

                //the second set of walls are *mirrored!*
                if (diagBack) p.Y--;
                int baseY = diagForward ? 4 : 6;
                int wallHeight = height + 1;  //we use an extra tile because one tile is split into two halves.
                for (int y = p.Y; y < p.Y + wallHeight; ++y)
                {
                    int bmpY = 0, bmpX = 0;
                    bool top = y == p.Y;
                    bool bot = y == (p.Y + wallHeight - 1);
                    bool checkLeft = (diagForward ? leftWall : rightWall) && !bot;
                    bool checkRight = (diagForward ? rightWall : leftWall) && !top;

                    if (checkLeft)
                    {
                        if (top)
                        {
                            bmpX = 3;
                            bmpY = 0;
                        }
                        else
                        {
                            bmpX = 0;
                            bmpY = 0;
                        }
                    }
                    else if (checkRight)
                    {
                        if (bot)
                        {
                            bmpX = 1;
                            bmpY = 1;
                        }
                        else
                        {
                            bmpX = 1;
                            bmpY = 0;
                        }
                    }
                    else
                    {
                        if (top)
                        {
                            bmpX = 0;
                            bmpY = 1;
                        }
                        else if (bot)
                        {
                            bmpX = 3;
                            bmpY = 1;
                        }
                        else
                        {
                            bmpX = 2;
                            bmpY = 0;
                        }
                    }
                    if (diagBack)
                    {
                        bmpX = 3 - bmpX;
                    }
                    addTile(p.X,y,bmpX, baseY + bmpY);
                }
            }
        }
        void writeSection(Point first, Point second, bool leftWall, bool rightWall)
        {
            //always go left to right!!!!!1111!!11!11!!11!11!1!s
            if (first.X > second.X)
            {
                var temp = first;
                first = second;
                second = temp;

                var wallTemp = leftWall;
                leftWall =  rightWall;
                rightWall = wallTemp;
            }

            bool diagForward = second.Y > first.Y;
            bool diagBack = second.Y < first.Y;
            int x = first.X;
            int y = first.Y;
            while (x < second.X)
            {
                writeSubsection(new Point(x, y), diagForward, diagBack, x == first.X && leftWall, (x == (second.X-1)) && rightWall);
                ++x;
                if (diagForward) ++y;
                if (diagBack) --y;
            }
        }
        void writeWall()
        {
            int i = 0;
            Nullable<Point> prev = null;
            foreach (Point p in polyPts)
            {
                if (prev != null)
                {
                    writeSection(prev.Value, p, i == 1, i == polyPts.Count-1);
                }
                prev = p;
                ++i;
            }
        }


        public WallTool(MapData mapData, TileSelectionData selectionData)
        {
            this.mapData = mapData;
            this.selectionData = selectionData;
            polyPts = new List<Point>();
            mousePoint = null;
            selectingHeight = false;
        }

        public void onClick(int x, int y)
        {
            if (!selectingHeight)
            {
                Point nextPt = getPoint(x, y);
                if (polyPts.Any() && polyPts[polyPts.Count - 1].Equals(nextPt))
                {
                    selectingHeight = true;
                    height = 0;
                }
                else
                {
                    polyPts.Add(nextPt);
                }
            }
            else
            {
                selectingHeight = false;
                writeWall();
                polyPts.Clear();
            }
            mapData.invalidateDraw();
        }

        public void mouseMove(int x, int y)
        {
            if (selectingHeight)
            {
                height = Math.Max(0, y - polyPts[polyPts.Count - 1].Y);
            }
            else
            {
                mousePoint = getPoint(x, y);
            }
            mapData.invalidateDraw();
        }

        public void mouseUp(int x, int y)
        {
            
        }
        Point scale(Point p)
        {
            float tileSize = mapData.getTileSize();
            float mapScale = mapData.getMapScale();

            return new Point((int)(p.X * tileSize * mapScale), (int)(p.Y * tileSize * mapScale));
        }
        public void onDraw(Graphics g)
        {
            Nullable<Point> prev = null;
            Pen pen = new Pen(Color.Blue, 2.0f);

            foreach( Point p in polyPts)
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
            if (selectingHeight && height != 0)
            {
                prev = null;

                g.DrawLine(pen, scale(polyPts[0]), scale(new Point(polyPts[0].X, polyPts[0].Y + height)));
                g.DrawLine(pen, scale(polyPts[polyPts.Count - 1]), scale(new Point(polyPts[polyPts.Count - 1].X, polyPts[polyPts.Count - 1].Y + height)));

                foreach (Point p in polyPts)
                {
                    Point p2 = new Point(p.X, p.Y + height);
                    if (prev != null)
                    {
                        g.DrawLine(pen, scale(prev.Value), scale(p2));
                    }
                    prev = p2;
                }
                if (prev != null && mousePoint != null)
                {
                    g.DrawLine(pen, scale(prev.Value), scale(new Point(mousePoint.Value.X, mousePoint.Value.Y + height)));
                }
            }
        }

        public bool equals(Tool rhs)
        {
            if (rhs == null) return false;
            var thisType = rhs as WallTool;
            return thisType != null;  
        }
    }
}