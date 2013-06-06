using System;


namespace BladeCraft.Classes
{
    public class Tile
    {
        public int x, y, bmpX, bmpY, animBmpX, animBmpY;
        public int layer;
        public bool[] collSides;

        public bool isMaterial;
        public int matX, matY;

        public bool animated;

        public Tile()
        {
           collSides = new bool[4];
           animated = false;
        }

        public Tile(int x, int y, int bmpX, int bmpY, int layer)
        {
            this.x = x;
            this.y = y;
            this.bmpX = bmpX;
            this.bmpY = bmpY;
            this.layer = layer;
           // this.layer = layer;
            collSides = new bool[4];
            for (int i = 0; i < 4; ++i)
                collSides[i] = false;

            this.isMaterial = false;

            animated = false;
        }

        public void animate(int animBmpX, int animBmpY)
        {
            animated = true;
            this.animBmpX = animBmpX;
            this.animBmpY = animBmpY;
        }

        public void addToMaterial(int matX, int matY)
        {
           this.isMaterial = true;
           this.matX = matX;
           this.matY = matY;
        }

       public bool IsMaterial() { return this.isMaterial; }
       public bool hasSameMaterial(int matX, int matY)
       {
          return isMaterial && matX == this.matX && matY == this.matY;
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
            this.matX = t.matX;
            this.matY = t.matY;
            this.isMaterial = t.isMaterial;
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
