using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

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
                  map.animateTile(x, y, t.bmpX, t.bmpY, mapData.getCurrentLayer());
               else
                  map.addTile(new Tile(x, y, t.bmpX, t.bmpY, mapData.getCurrentLayer()));

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
         if (mouseDown ||  
             x != lastPointAdded.X ||
             y != lastPointAdded.Y)
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
