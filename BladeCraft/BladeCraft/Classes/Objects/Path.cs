using System;
using System.Collections.Generic;
using System.Text;

namespace BladeCraft.Classes.Objects
{
    public class Path
    {
        public string target;
        public bool wait;
        public List<string> actions;

        public Path(string target, bool wait)
        {
            this.target = target;
            this.wait = wait;
            actions = new List<string>();
        }

        public Path(Path other)
        {
            this.target = other.target;
            this.wait = other.wait;
            actions = new List<string>();
            foreach(String str in other.actions)
                actions.Add(str);
        }
    }
}
