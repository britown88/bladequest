using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actSwitch : Action
    {
        public string switchName;
        public bool state;

        public actSwitch(string switchName, bool state)
        {
            this.switchName = switchName;
            this.state = state;
            actionType = type.Switch;
        }

        public actSwitch(actSwitch other)
        {
            this.switchName = other.switchName;
            this.state = other.state;
            actionType = type.Switch;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action switch " +
                switchName + " " +
                state.ToString());

        }
    }
}
