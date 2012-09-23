﻿using System;


namespace BladeCraft.Classes
{
    public class Tile
    {
        public int x, y, bmpX, bmpY, animBmpX, animBmpY;
        private string layer;
        public bool[] collSides;

        public bool animated; 

        public Tile(int x, int y, int bmpX, int bmpY, string layer)
        {
            this.x = x;
            this.y = y;
            this.bmpX = bmpX;
            this.bmpY = bmpY;
            this.layer = layer;
            collSides = new bool[4];
            for (int i = 0; i < 4; ++i)
                collSides[i] = false;

            animated = false;
        }

        public void animate(int animBmpX, int animBmpY)
        {
            animated = true;
            this.animBmpX = animBmpX;
            this.animBmpY = animBmpY;
        }

        public Tile(Tile t)
        {
            this.x = t.x;
            this.y = t.y;
            this.bmpX = t.bmpX;
            this.bmpY = t.bmpY;
            this.animBmpX = t.animBmpX;
            this.animBmpY = t.animBmpY;
            this.animated = t.animated;
            this.layer = t.layer;
            collSides = new bool[4];
            for (int i = 0; i < 4; ++i)
                collSides[i] = t.collSides[i];

        }

        public void setCollision(int side, bool coll)
        {
            if (side >= 0 && side < 4)
            {
                collSides[side] = coll;
            }
        }


    }
}
