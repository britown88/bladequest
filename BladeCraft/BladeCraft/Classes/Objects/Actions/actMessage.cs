using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actMessage : Action
    {
        public string msg;
        public bool yesNoOpt;

        public actMessage(string msg)
        {
            this.msg = msg;
            this.yesNoOpt = false;
            actionType = type.Message;
        }

        public actMessage(string msg, bool yesNoOpt)
        {
            this.msg = msg;
            this.yesNoOpt = yesNoOpt;
            actionType = type.Message;
        }

        public actMessage(actMessage other)
        {
            this.msg = other.msg;
            this.yesNoOpt = other.yesNoOpt;
            actionType = type.Message;
        }

        public override void write(StreamWriter writer) 
        {

            string txt = msg.Replace("\r\n", "\\n");
            writer.WriteLine(
                "action message " +
                "\"" + txt  +"\"" + (yesNoOpt ? " " + yesNoOpt.ToString() : ""));

        }
    }
}
