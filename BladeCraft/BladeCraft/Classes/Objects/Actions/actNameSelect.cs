using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actNameSelect : Action
    {
        public string charname;


        public actNameSelect(string charname)
        {
            this.charname = charname;

            actionType = type.NameSelect;
        }

        public actNameSelect(actNameSelect other)
        {
            this.charname = other.charname;

            actionType = type.NameSelect;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action nameselect " +
                charname);

        }
    }
}
