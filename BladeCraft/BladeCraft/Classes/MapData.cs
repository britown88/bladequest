using System;
using System.Collections.Generic;
using System.Text;
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
      int getCurrentLayer();

      BQMap getMap();


      float getTileSize();
      float getMapScale();

      void invalidateDraw();
   }
}
