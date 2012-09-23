using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actPauseMusic : Action
    {
        public actPauseMusic() { actionType = type.PauseMusic; }
        public actPauseMusic(actPauseMusic other) { actionType = type.PauseMusic; }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine("action pausemusic");

        }
    }
}
