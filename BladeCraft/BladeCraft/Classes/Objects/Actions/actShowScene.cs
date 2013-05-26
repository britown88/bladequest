using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actShowScene : Action
    {
        public string sceneName;
        public string endTrigger;
        public int r, g, b;
        public int fadeSpeed;
       public float endTimer;
        public bool wait;


        public actShowScene(string sceneName, string trigger, float endTimer, int r, int g, int b, int fadeSpeed, bool wait)
        {
            this.sceneName = sceneName;
            this.endTrigger = trigger;
            this.endTimer = endTimer;
            this.r = r;
            this.g = g;
            this.b = b;
            this.fadeSpeed = fadeSpeed;
            this.wait = wait;
            actionType = type.ShowScene;
        }

        public actShowScene(actShowScene other)
        {
            this.sceneName = other.sceneName;
            this.endTrigger = other.endTrigger;
            this.r = other.r;
            this.g = other.g;
            this.b = other.b;
            this.fadeSpeed = other.fadeSpeed;
            this.wait = other.wait;
            this.endTimer = other.endTimer;
            actionType = type.ShowScene;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine("action showscene " + 
                      sceneName + " " + 
                      endTrigger + " " +
                      endTimer.ToString() + " " +
                      r.ToString() + " " + 
                      g.ToString() + " " + 
                      b.ToString() + " " + 
                      fadeSpeed.ToString() + " " + 
                      wait.ToString());

        }
    }
}
