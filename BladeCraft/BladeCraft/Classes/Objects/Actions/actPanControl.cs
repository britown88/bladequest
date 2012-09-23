using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actPanControl : Action
    {
        public int x, y, speed;
        public bool wait;

        public actPanControl(int x, int y, int speed, bool wait)
        {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.wait = wait;
            actionType = type.PanControl;
        }

        public actPanControl(actPanControl other)
        {
            this.x = other.x;
            this.y = other.y;
            this.speed = other.speed;
            this.wait = other.wait;
            actionType = type.PanControl;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action pan " +
                x.ToString() + " " +
                y.ToString() + " " +
                speed.ToString() + " " +
                wait.ToString());

        }
    }
}
