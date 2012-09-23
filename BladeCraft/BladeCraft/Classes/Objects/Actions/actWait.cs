using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actWait : Action
    {
        public float seconds;

        public actWait(float seconds)
        {
            this.seconds = seconds;
            actionType = type.Wait;
        }

        public actWait(actWait other)
        {
            this.seconds = other.seconds;
            actionType = type.Wait;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine("action wait " + seconds.ToString());
            actionType = type.Wait;
        }
    }
}
