using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes
{
   public class GameObject
   {
      public int X { get; set; }
      public int Y { get; set; }
      public string Script { get; set; }
      
      public GameObject(int x, int y)
      {
         X = x;
         Y = y;
      }

      public GameObject(GameObject other)
      {
         this.X = other.X;
         this.Y = other.Y;
      }

      public void write(StreamWriter writer)
      {
         writer.WriteLine("#!OBJ " + X.ToString() + " " + Y.ToString());

         string s = Script.Replace("$", "$"+X.ToString()+"x"+Y.ToString()+"$");
         writer.Write(s);

      }
   }
}
