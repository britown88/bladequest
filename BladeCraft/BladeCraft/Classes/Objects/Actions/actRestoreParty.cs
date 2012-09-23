using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actRestoreParty : Action
    {
        public actRestoreParty() { actionType = type.RestoreParty; }
        public actRestoreParty(actRestoreParty other) { actionType = type.RestoreParty; }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine("action restore");

        }
    }
}
