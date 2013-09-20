using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace BladeCraft.Classes.Tools
{
    public interface CollisionMapData
    {
        bool getSide(int side);
        MapData getMapData();
    }


    public class CollisionTool : Tool
    {
        Point lastPointAdded;
        CollisionMapData mapData;
        bool mousedown = false;
        public CollisionTool(CollisionMapData mapData)
        {
            this.mapData = mapData;
        }

        private void setCollision(int x, int y)
        {
           BQMap map = mapData.getMapData().getMap();
            if (map == null) return;
            Tile t = map.getTile(x, y, 0, 0);
            if (t != null)
            {
                for (int i = 0; i < Tile.numSides; ++i)
                {
                    t.setCollision(i,mapData.getSide(i));
                }
            }
            lastPointAdded = new Point(x, y);
            mapData.getMapData().invalidateDraw();
        }


        public void onClick(int x, int y)
        {
            setCollision(x, y);
            mousedown = true;
        }

        public void mouseMove(int x, int y)
        {
            if (mousedown &&
                lastPointAdded.X  != x ||
                lastPointAdded.Y != y)
            {
                setCollision(x, y);
            }
        }
        public bool handleRightClick(int x, int y)
        {
           return false;
        }
        public void onDraw(Graphics g) { }

        public void mouseUp(int x, int y)
        {
            mousedown = false;
        }
        public bool equals(Tool rhs)
        {
            if (rhs == null) return false;
            var thisType = rhs as CollisionTool;
            return thisType != null;   
        }
    }
}
