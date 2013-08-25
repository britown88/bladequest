using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

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
                        if (mapData.isAnimationFrame())
                        {
                            map.animateTile(x + i, y + j, i, j, t.tileset, mapData.getCurrentLayer());
                        }
                        else
                        {
                            var tile = new Tile(x + i, y + j, i, j, t.tileset, mapData.getCurrentLayer());
                            tile.tileType = Tile.Type.Object;
                            map.addTile(tile);
                            map.writeDefaultCollision(x + i, y + i, mapData.getCurrentLayer());
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
