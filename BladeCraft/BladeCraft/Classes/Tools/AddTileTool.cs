using System;
using System.Drawing;
using System.Collections.Generic;
using System.Text;

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

               foreach (var writeTile in Tile.getTilesetTiles(t.tileset, x, y, t.bmpX, t.bmpY, mapData.getCurrentLayer(), Tile.Type.Singular))
               {
                  map.writeTile(writeTile, mapData.isAnimationFrame(), t.tileset, t.bmpX, t.bmpY);
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
      public bool handleRightClick(int x, int y)
      {
         return false;
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
