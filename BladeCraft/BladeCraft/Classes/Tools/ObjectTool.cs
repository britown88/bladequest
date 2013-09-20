using System;
using System.Drawing;
using System.Collections.Generic;
using System.Text;

using BladeCraft.Forms;

namespace BladeCraft.Classes.Tools
{
    class ObjectTool : Tool
    {
        MapData mapData;
        TileSelectionData tileSelection;
        bool mouseDown;
        Point lastPointAdded;

        public ObjectTool(MapData mapData, TileSelectionData tileSelection)
        {
            this.mapData = mapData;
            this.tileSelection = tileSelection;
            mouseDown = false;
        }
        public bool handleRightClick(int x, int y)
        {
           return false;
        }
        private void addTile(int x, int y)
        {
            BQMap map = mapData.getMap();
            if (map != null)
            {
                Tile t = tileSelection.selectedTile();

                TileImage tileImage = Bitmaps.bitmaps[t.tileset];

                int xSize = (int)(tileImage.xPixels / mapData.getTileSize());
                int ySize = (int)(tileImage.yPixels / mapData.getTileSize());

                for (int j = 0; j < ySize; ++j)
                {
                    for (int i = 0; i < xSize; ++i)
                    {
                       map.deleteTile(x+i, y+j, mapData.getCurrentLayer());
                       foreach (var writeTile in Tile.getTilesetTiles(t.tileset, x + i, y + j, i , j, mapData.getCurrentLayer(), Tile.Type.Object))
                       {
                          map.writeTile(writeTile, t.tileset, i, j);
                       }
                    }
                }

                lastPointAdded = new Point(x, y);
                mapData.invalidateDraw();
            }
        }



        public void onClick(int x, int y)
        {
            addTile(x, y);
            mouseDown = true;
        }

        public void mouseMove(int x, int y)
        {
            if (mouseDown &&
                (x != lastPointAdded.X ||
                y != lastPointAdded.Y))
            {
                addTile(x, y);
            }
        }

        public void mouseUp(int x, int y)
        {
            mouseDown = false;
        }

        public void onDraw(Graphics g) { }

        public bool equals(Tool rhs)
        {
            if (rhs == null) return false;
            var thisType = rhs as ObjectTool;
            return thisType != null;
        }
    }
}
