using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BladeCraft.Classes
{
   public class Path
   {
      public static string sanitize(string path)
      {
         string output = "";
         foreach (var c in path.ToCharArray())
         {
            if (c == '\\')
            {
               output = output + '/';
            }
            else
            {
               output = output + c;
            }
         }
         return output;
      }
   }
}
