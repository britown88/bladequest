using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace BladeCraft.Classes
{
    public class Sprite
    {
        public enum SpriteType
        {
            Battle = 0,
            Enemy = 1,
            World = 2
        }

        public SpriteType Type { get; set; }
        public String Name { get; set; }
        public String Bitmap { get; set; }
        public Point Pos;
        public int SrcSize { get; set; }
        public int DestSize { get; set; }

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
            Bitmap = "characters\\herobattlers";
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

        public Sprite(Sprite other)
        {
            Type = other.Type;
            Bitmap = other.Bitmap;
            Name = other.Name;
            Pos = new Point(other.Pos.X, other.Pos.Y);
            SrcSize = other.SrcSize;
            DestSize = other.DestSize;
        }

    }
}
