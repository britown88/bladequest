using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using BladeCraft.Forms;

namespace BladeCraft.Classes.Tools
{
   class AddTileTool : Tool
   {
      MapData mapData;
      TileSelectionData tileSelection;
      Point lastPointAdded;
      bool mouseDown = false;

      public AddTileTool(MapData mapData, TileSelectionData tileSelection)
      {
         this.mapData = mapData;
         this.tileSelection = tileSelection;
      }

      private void addTile(int x, int y)
      {
         BQMap map = mapData.getMap();
         if (map != null)
         {
            if (tileSelection.eraseSelected())
               map.deleteTile(x, y, mapData.getCurrentLayer());
            else
            {
               Tile t = tileSelection.selectedTile();
               if (mapData.isAnimationFrame())
                  map.animateTile(x, y, t.bmpX, t.bmpY, t.tileset, mapData.getCurrentLayer());
               else
               {
                  map.addTile(new Tile(x, y, t.bmpX, t.bmpY, t.tileset, mapData.getCurrentLayer()));
                  var tile = map.getTile(x, y, mapData.getCurrentLayer());
                  if (tile.layer == 0 && tile.tileset != null)
                  {
                     var bmpImage = Bitmaps.bitmaps[tile.tileset];
                     var tileData = bmpImage.tiles[tile.bmpX + tile.bmpY * bmpImage.xPixels / MapForm.tileSize];

                     tile.collSides[0] = tileData.colLeft;
                     tile.collSides[1] = tileData.colRight;
                     tile.collSides[2] = tileData.colTop;
                     tile.collSides[3] = tileData.colBottom;
                  }
               }

            }

            lastPointAdded = new Point(x, y);
            mapData.invalidateDraw();
         }
      }



      public void onClick(int x, int y)
      {
         addTile(x, y);
         mouseDown = true;
      }

      public void mouseMove(int x, int y)
      {
         if (mouseDown &&  
             (x != lastPointAdded.X ||
             y != lastPointAdded.Y))
         {
            addTile(x, y);
         }
      }

      public void mouseUp(int x, int y)
      {
         mouseDown = false;
      }

      public void onDraw(Graphics g) { }

      public bool equals(Tool rhs)
      {
         if (rhs == null) return false;
         var thisType = rhs as AddTileTool;
         return thisType != null;  
      }
   }
}
