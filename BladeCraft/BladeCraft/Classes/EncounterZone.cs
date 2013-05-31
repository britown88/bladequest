using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;
using System.IO;

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

    }


}
