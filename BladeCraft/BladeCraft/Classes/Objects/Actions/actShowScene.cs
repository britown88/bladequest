using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actShowScene : Action
    {
        public string sceneName;

        public actShowScene(string sceneName)
        {
            this.sceneName = sceneName;
            actionType = type.ShowScene;
        }

        public actShowScene(actShowScene other)
        {
            this.sceneName = other.sceneName;
            actionType = type.ShowScene;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine("action showscene " + sceneName);

        }
    }
}
