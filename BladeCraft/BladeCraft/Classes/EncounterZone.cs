using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;
using System.IO;
using System.Xml;

namespace BladeCraft.Classes
{
    public class EncounterZone
    {
        public List<string> encounters;
        public float encounterRate;
        public Rectangle zone;

        public EncounterZone()
        {
            encounters = new List<string>();
        }

        public void write(BinaryWriter writer)
        {
           //4 shorts - x,y,width, height
           writer.Write((short)zone.X);
           writer.Write((short)zone.Y);
           writer.Write((short)zone.Width);
           writer.Write((short)zone.Height);

           //float - encounter rate.
           writer.Write(encounterRate);


           //int - # of encounters.
           writer.Write(encounters.Count);
           foreach (string str in encounters)
           {
              BQMap.writeString(writer, str);
           }
        }

        public void write(MapWriter writer)
        {

           writer.startSection("EncounterZone");
           writer.writeAttribute("X", zone.X.ToString());
           writer.writeAttribute("Y", zone.Y.ToString());
           writer.writeAttribute("Width", zone.Width.ToString());
           writer.writeAttribute("Height", zone.Height.ToString());
           writer.writeAttribute("EncounterRate", encounterRate.ToString());

           foreach (string str in encounters)
              writer.writeElement("Encounter", str);

           writer.endSection();
        }

    }


}
