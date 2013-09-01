using System;
using System.Collections.Generic;
using System.Drawing;
using System.Text;

using BladeCraft.Forms;

namespace BladeCraft.Classes.Tools
{
   class MaterialTool : Tool
   {
      MapData mapData;
      TileSelectionData selectionData;
      Point lastPointAdded;
      bool mouseDown;

      public MaterialTool(MapData mapData, TileSelectionData selectionData)
      {
         this.mapData = mapData;
         this.selectionData = selectionData;
         this.mouseDown = false;
      }
      public bool handleRightClick(int x, int y)
      {
         return false;
      }
      private void addMaterial(int x, int y)
      {
         BQMap map = mapData.getMap();
         if (selectionData.eraseSelected())
            map.deleteTile(x, y, mapData.getCurrentLayer());
         else
         {
            Tile t = selectionData.selectedTile();
            map.addMaterial(x, y, t.tileset,
               mapData.isAnimationFrame(), mapData.getCurrentLayer());

            map.writeDefaultCollision(x, y, mapData.getCurrentLayer());

            lastPointAdded = new Point(x, y);
            mapData.invalidateDraw();
         }
      }

      public void onClick(int x, int y)
      {
         addMaterial(x, y);
         mouseDown = true;
      }

      public void mouseMove(int x, int y)
      {
         if ((x != lastPointAdded.X ||
             y != lastPointAdded.Y) &&
            mouseDown)
         {
            addMaterial(x, y);
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
         var thisType = rhs as MaterialTool;
         return thisType != null;  
      }
   }
}
