using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actPath : Action
    {
        public Path path;

        public actPath(string target, bool wait)
        {
            this.path = new Path(target, wait);
            actionType = type.Path;
        }

        public actPath(Path path)
        {
            this.path = path;
            actionType = type.Path;
        }

        public actPath(actPath other)
        {
            this.path = new Path(other.path);
            actionType = type.Path;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action path " +
                path.target + " " +
                path.wait.ToString());

            foreach (string str in path.actions)
                writer.WriteLine(str);

            writer.WriteLine("endpath");

        }
    }
}
