using System;
using System.Drawing;
using System.Collections.Generic;
using BladeCraft.Forms;

namespace BladeCraft.Classes
{
    public class Tile
    {
        public static IEnumerable<Tile> getTilesetTiles(string tileSet, int x, int y, int bmpX, int bmpY, int layer, Tile.Type type)
        {
           var image = Bitmaps.bitmaps[tileSet];
           int xSize = image.xPixels / MapForm.tileSize;

           foreach (var bmp in image.tiles[bmpX + xSize * bmpY].bitmaps)
           {
              int offsetLayer = bmp.layerOffset + layer;
              if (offsetLayer < 8)
              {
                 var t = new Tile(x, y, bmp.x, bmp.y, bmp.bitmapPath, offsetLayer);
                 t.tileType = type;
                 yield return t;
              }
           }
        }

        //don't change order, stored numerically ;____________________;
        public enum Type
        {
            Singular,
            Material,
            Wall,
            Object,
            Roof,
            Staircase,
            Pipe
        };

        public static int sideLeft = 0;
        public static int sideTop = 1;
        public static int sideRight = 2;
        public static int sideBottom = 3;
        public static int numSides = 4;

        public int x, y, bmpX, bmpY, animBmpX, animBmpY;
        public int layer;
        public bool[] collSides;

        public Type tileType;

        public bool animated;
        public string tileset;

        public Tile()
        {
           collSides = new bool[4];
           animated = false;
           tileType = Type.Singular;
        }

        public Tile(int x, int y, int bmpX, int bmpY, string tileset, int layer)
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

            tileType = Type.Singular;

            animated = false;
            this.tileset = tileset;
        }

        public void animate(int animBmpX, int animBmpY)
        {
            animated = true;
            this.animBmpX = animBmpX;
            this.animBmpY = animBmpY;
        }

        public void addToMaterial()
        {
            tileType = Type.Material;
        }

        public bool IsMaterial() { return tileType == Type.Material; }
       public bool hasSameMaterial(string tileset)
       {
          return IsMaterial() && tileset.Equals(this.tileset);
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
            this.tileType = t.tileType;
            this.layer = t.layer;
            this.tileset = t.tileset;
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
