using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actShake : Action
    {
        public float duration;
        public int intensity;
        public bool wait;

        public actShake(float duration, int intensity, bool wait)
        {
            this.duration = duration;
            this.intensity = intensity;
            this.wait = wait;
            actionType = type.Shake;
        }

        public actShake(actShake other)
        {
            this.duration = other.duration;
            this.intensity = other.intensity;
            this.wait = other.wait;
            actionType = type.Shake;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action shake " +
                duration.ToString() + " " +
                intensity.ToString() + " " +
                wait.ToString());

        }
    }
}
