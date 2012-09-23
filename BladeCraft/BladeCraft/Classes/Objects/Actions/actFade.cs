using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actFade : Action
    {
        public int fadeSpeed, a, r, g, b;
        public bool fadeout, wait;

        public actFade(int fadeSpeed, int a, int r, int g, int b, bool fadeout, bool wait)
        {
            this.fadeSpeed = fadeSpeed;
            this.a = a;
            this.r = r;
            this.g = g;
            this.b = b;
            this.fadeout = fadeout;
            this.wait = wait;

            actionType = type.Fade;
        }

        public actFade(actFade other)
        {
            this.fadeSpeed = other.fadeSpeed;
            this.a = other.a;
            this.r = other.r;
            this.g = other.g;
            this.b = other.b;
            this.fadeout = other.fadeout;
            this.wait = other.wait;

            actionType = type.Fade;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action fade " +
                fadeSpeed.ToString() + " " +
                a.ToString() + " " +
                r.ToString() + " " +
                g.ToString() + " " +
                b.ToString() + " " +
                fadeout.ToString() + " " +
                wait.ToString());

        }
    }
}
