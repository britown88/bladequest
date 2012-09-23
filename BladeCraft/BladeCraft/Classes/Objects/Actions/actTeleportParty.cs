using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actTeleportParty : Action
    {
        public int x, y;
        public string mapName;

        public actTeleportParty(int x, int y, string mapName)
        {
            this.x = x;
            this.y = y;
            this.mapName = mapName;
            actionType = type.TeleportParty;
        }

        public actTeleportParty(actTeleportParty other)
        {
            this.x = other.x;
            this.y = other.y;
            this.mapName = other.mapName;
            actionType = type.TeleportParty;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action teleport " +
                x.ToString() + " " +
                y.ToString() + " " +
                mapName.ToString());

        }
    }
}
