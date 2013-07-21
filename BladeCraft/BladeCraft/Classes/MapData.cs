using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using BladeCraft.Classes;

namespace BladeCraft.Classes
{
   public interface TileSelectionData
   {
      Tile selectedTile();
      bool eraseSelected();
   }

   public interface MapData
   {
      bool isAnimationFrame();
      int getCurrentLayer();

      BQMap getMap();


      float getTileSize();
      float getMapScale();

      void invalidateDraw();
   }
}
