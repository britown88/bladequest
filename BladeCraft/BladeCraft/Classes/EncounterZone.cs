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

        public void write(StreamWriter writer)
        {
            writer.WriteLine(
                "zone " +
                zone.X.ToString() + " " +
                zone.Y.ToString() + " " +
                zone.Width.ToString() + " " +
                zone.Height.ToString() + " " +
                encounterRate.ToString());

            foreach (string str in encounters)
                writer.WriteLine("encounter " + str);

            writer.WriteLine("endzone");
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
