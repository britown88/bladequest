using System;
using System.Drawing;
using System.Collections.Generic;
using System.Text;
using BladeCraft.Forms;

namespace BladeCraft.Classes.Tools
{
   class EncounterZoneTool : Tool
   {
      MapData mapData;
      Point startPoint, endPoint;
      bool mouseDown = false;

      public EncounterZoneTool(MapData mapData)
      {
         this.mapData = mapData;
      }


      public void onClick(int x, int y)
      {
         startPoint = new Point(x, y);
         mapData.invalidateDraw();
         mouseDown = true;
      }

      public void mouseMove(int x, int y)
      {
         endPoint = new Point(x, y);
         mapData.invalidateDraw();
      }

      public void mouseUp(int x, int y)
      {
         if (mouseDown)
         {
            endPoint = new Point(x, y);
            if (!startPoint.Equals(endPoint))
            {
               EncounterZone ez = new EncounterZone();
               ez.zone = new Rectangle(
                  Math.Min(endPoint.X, startPoint.X),
                  Math.Min(endPoint.Y, startPoint.Y),
                  Math.Abs(endPoint.X - startPoint.X),
                  Math.Abs(endPoint.Y - startPoint.Y));



               ZoneForm zf = new ZoneForm(mapData.getMap(), ez, true);
               zf.ShowDialog();
               mapData.invalidateDraw();
            }
            mouseDown = false;  
         }
      }
      public void onDraw(Graphics g) 
      {
         if (mouseDown)
         {
            Pen pen = new Pen(Color.Blue, 2.0f);

            float tileSize = mapData.getTileSize();
            float mapScale = mapData.getMapScale();

            Rectangle current = new Rectangle(
            Math.Min((int)(endPoint.X * tileSize * mapScale), (int)(startPoint.X * tileSize * mapScale)),
            Math.Min((int)(endPoint.Y * tileSize * mapScale), (int)(startPoint.Y * tileSize * mapScale)),
            Math.Abs((int)(endPoint.X * tileSize * mapScale - startPoint.X * tileSize * mapScale)),
            Math.Abs((int)(endPoint.Y * tileSize * mapScale - startPoint.Y * tileSize * mapScale)));

            g.DrawRectangle(pen, current);
         }
      }
      public bool equals(Tool rhs)
      {
         if (rhs == null) return false;
         var thisType = rhs as EncounterZoneTool;
         return thisType != null;  
      }
   }
}
