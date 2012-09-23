using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actForkYes : Action
    {


        public actForkYes()
        {

            actionType = type.YesFork;
        }

        public actForkYes(actForkYes other)
        {

            actionType = type.YesFork;
        }

        public override void write(StreamWriter writer)
        {
            writer.WriteLine("action yesfork");

        }
    }
}
