using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actModifyParty : Action
    {
        public string charName;
        public bool remove;

        public actModifyParty(string charName, bool remove)
        {
            this.charName = charName;
            this.remove = remove;
            actionType = type.ModifyParty;
        }

        public actModifyParty(actModifyParty other)
        {
            this.charName = other.charName;
            this.remove = other.remove;
            actionType = type.ModifyParty;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action party " +
                charName + " " +
                remove.ToString());

        }
    }
}
