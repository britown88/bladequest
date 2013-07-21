using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace BladeCraft.Classes.Tools
{
   class FillTool : Tool
   {
      MapData mapData;
      TileSelectionData selectionData;
      bool mouseDown = false;

      public FillTool(MapData mapData, TileSelectionData selectionData)
      {
         this.mapData = mapData;
         this.selectionData = selectionData;
      }
      
      public void onClick(int x, int y)
      {
      }

      public void mouseMove(int x, int y)
      {
      }

      public void mouseUp(int x, int y)
      {
         mapData.getMap().fill(x, y, selectionData.selectedTile(), mapData.isAnimationFrame(), mapData.getCurrentLayer());
         mapData.invalidateDraw();
      }
      public void onDraw(Graphics g) 
      {
      }

      public bool equals(Tool rhs)
      {
         if (rhs == null) return false;
         var thisType = rhs as FillTool;
         return thisType != null;  
      }
   }
}
