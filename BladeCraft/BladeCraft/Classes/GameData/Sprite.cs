using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace BladeCraft.Classes
{
    class Sprite
    {
        public enum SpriteType
        {
            Battle = 0,
            Enemy = 1,
            World = 2
        }

        public SpriteType Type { get; private set; }
        public String Name { get; private set; }
        public String Bitmap { get; private set; }
        public Point Pos { get; private set; }
        public int SrcSize { get; private set; }
        public int DestSize { get; private set; }

        public Sprite(String bitmap, String name, int x, int y)
        {
            Type = SpriteType.World;
            Bitmap = bitmap;
            Name = name;
            Pos = new Point(x, y);
        }

        public Sprite(String name, int x, int y)
        {
            Type = SpriteType.Battle;
            Name = name;
            Pos = new Point(x, y);
        }

        public Sprite(String name, String bitmap, int destSize, int srcSize, int x, int y)
        {
            Type = SpriteType.Enemy;
            Bitmap = bitmap;
            Name = name;
            Pos = new Point(x, y);
            SrcSize = srcSize;
            DestSize = destSize;
        }

    }
}
