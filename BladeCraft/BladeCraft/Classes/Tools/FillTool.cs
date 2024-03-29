﻿using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;

namespace BladeCraft.Classes.Tools
{
   class FillTool : Tool
   {
      MapData mapData;
      TileSelectionData selectionData;

      public FillTool(MapData mapData, TileSelectionData selectionData)
      {
         this.mapData = mapData;
         this.selectionData = selectionData;
      }
      
      public void onClick(int x, int y)
      {
      }
      public bool handleRightClick(int x, int y)
      {
         return false;
      }
      public void mouseMove(int x, int y)
      {
      }

      public void mouseUp(int x, int y)
      {
         mapData.getMap().fill(x, y, selectionData.selectedTile(), mapData.getCurrentLayer());
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
