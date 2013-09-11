using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace BladeCraft.Classes
{

   //functionally - 
   public interface IAnimatedImage
   {
      IList<string> spritePaths();
   }

   public interface ISprite
   {
      IAnimatedImage getImage();
      Point getPosition();
      Point getSize();
   }

   public class Animatedimage : IAnimatedImage
   {
      List<string> paths;
      public Animatedimage(string directoryPath)
      {
         paths = new List<String>();
         foreach (var p in System.IO.Directory.GetFiles(directoryPath))
         {
            var path = Path.sanitize(p);
            var ext = path.Substring(path.Length - 3);
            if (ext == "png")
            {
               paths.Add(p.Substring(directoryPath.Length + 1, p.Length - (directoryPath.Length + 5)));
            }
         }
         paths.Sort();
         for (int i = 0; i < paths.Count; ++i)
         {
            paths[i] = paths[i] + ".png";
         }
      }
      public IList<string> spritePaths()
      {
         return paths;
      }
   }

   public class Sprite : ISprite
   {
      Animatedimage animatedImage;
      Point point, size;
      public Sprite(string directory, Point point, Point size)
      {
         this.point = point;
         this.size = size;
         animatedImage = new Animatedimage(directory);
      }
      public IAnimatedImage getImage()
      {
         return animatedImage;
      }

      public Point getPosition()
      {
         return point;
      }

      public Point getSize()
      {
         return size;
      }
   }
}
