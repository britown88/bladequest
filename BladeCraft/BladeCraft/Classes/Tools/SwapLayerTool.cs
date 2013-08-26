using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Drawing;

namespace BladeCraft.Classes.Tools
{
   public interface SwapLayerMapData
   {
      int getSwapToLayer();
      MapData getMapData();
   }

   public class SwapLayerTool : Tool
   {
        Point lastPointAdded;
        SwapLayerMapData mapData;
        bool mousedown = false;
        public SwapLayerTool(SwapLayerMapData mapData)
        {
            this.mapData = mapData;
        }

        private void swapLayer(int x, int y)
        {
           BQMap map = mapData.getMapData().getMap();
           if (map == null) return;

           
           mapData.getMapData().invalidateDraw();
           lastPointAdded = new Point(x, y);
        }


        public void onClick(int x, int y)
        {
           swapLayer(x, y);
           mousedown = true;
        }

        public void mouseMove(int x, int y)
        {
            if (mousedown &&
                lastPointAdded.X  != x ||
                lastPointAdded.Y != y)
            {
               swapLayer(x, y);
            }
        }

        public void mouseUp(int x, int y)
        {
            mousedown = false;
        }
        public bool equals(Tool rhs)
        {
            if (rhs == null) return false;
            var thisType = rhs as SwapLayerTool;
            return thisType != null;   
        }
        public void onDraw(Graphics g) { }
   }
}
