using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actResetGame : Action
    {
        public actResetGame() { actionType = type.RestartGame; }
        public actResetGame(actResetGame other) { actionType = type.RestartGame; }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine("action resetgame");

        }
    }
}
