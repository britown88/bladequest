using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    
    public class actForkNo : Action
    {


        public actForkNo()
        {

            actionType = type.EndFork;
        }

        public actForkNo(actForkNo other)
        {

            actionType = type.NoFork;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine("action nofork");

        }
    }
}
