using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actStartBattle : Action
    {
        public string encounter;

        public actStartBattle(string encounter)
        {
            this.encounter = encounter;
            actionType = type.StartBattle;
        }

        public actStartBattle(actStartBattle other)
        {
            this.encounter = other.encounter;
            actionType = type.StartBattle;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine("action battle " + encounter);

        }
    }
}
