using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actForkEnd : Action
    {


        public actForkEnd()
        {

            actionType = type.EndFork;
        }

        public actForkEnd(actForkEnd other)
        {

            actionType = type.EndFork;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine("action endfork");

        }
    }
}
