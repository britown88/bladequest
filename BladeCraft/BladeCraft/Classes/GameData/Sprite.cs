using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;
using System.IO;

namespace BladeCraft.Classes
{
    public class Sprite
    {
        public enum SpriteTypes
        {
            Battle = 0,
            Enemy = 1,
            World = 2
        }

        public SpriteTypes SpriteType { get; set; }
        public String Name { get; set; }
        public String Bitmap { get; set; }
        public Point Pos;
        public int SrcSize { get; set; }
        public int DestSize { get; set; }

        public Sprite(String bitmap, String name, int x, int y)
        {
            SpriteType = SpriteTypes.World;
            Bitmap = bitmap;
            Name = name;
            Pos = new Point(x, y);
        }

        public Sprite(String name, int x, int y)
        {
            SpriteType = SpriteTypes.Battle;
            Bitmap = "characters\\herobattlers";
            Name = name;
            Pos = new Point(x, y);
        }

        public Sprite(String name, String bitmap, int destSize, int srcSize, int x, int y)
        {
            SpriteType = SpriteTypes.Enemy;
            Bitmap = bitmap;
            Name = name;
            Pos = new Point(x, y);
            SrcSize = srcSize;
            DestSize = destSize;
        }

        public Sprite(Sprite other)
        {
            SpriteType = other.SpriteType;
            Bitmap = other.Bitmap;
            Name = other.Name;
            Pos = new Point(other.Pos.X, other.Pos.Y);
            SrcSize = other.SrcSize;
            DestSize = other.DestSize;
        }

        public void write(StreamWriter writer)
        {

            switch (SpriteType)
            {
                case SpriteTypes.Battle:
                    writer.WriteLine(String.Format("battlesprite {0} {1} {2}", Name, Pos.X, Pos.Y));
                    break;

                case SpriteTypes.Enemy:
                    writer.WriteLine(String.Format("enemysprite {0} {1} {2} {3} {4} {5}", Name, Bitmap, DestSize, SrcSize, Pos.X, Pos.Y));
                    break;

                case SpriteTypes.World:
                    writer.WriteLine(String.Format("worldsprite {0} {1} {2} {3}", Bitmap, Name, Pos.X, Pos.Y));
                    break;
            }

        }

    }
}
