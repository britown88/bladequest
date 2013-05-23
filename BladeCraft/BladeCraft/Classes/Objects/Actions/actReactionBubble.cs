using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
   public class actReactionBubble : Action
    {
        public string bubble, target;
        public bool loop, wait;
        public bool close;
        public float duration;

        public actReactionBubble(string bubble, string target, float duration, bool loop, bool wait)
        {
            this.bubble = bubble;
            this.duration = duration;
            this.target = target;
            this.loop = loop;
            this.wait = wait;
            this.close = false;
            actionType = type.ReactionBubble;
        }

        public actReactionBubble(string target)
        {
           this.target = target;
           this.close = true;
           actionType = type.ReactionBubble;
        }

        public actReactionBubble(actReactionBubble other)
        {
           this.bubble = other.bubble;
           this.duration = other.duration;
           this.target = other.target;
           this.loop = other.loop;
           this.wait = other.wait;
           this.close = other.close;
           actionType = type.ReactionBubble;
        }

        public override void write(StreamWriter writer) 
        {
           if (close)           
               writer.WriteLine(
                   "action closebubble " + target);
           else
              writer.WriteLine(
                   "action openbubble " + 
                   bubble + " " + target
                   + " " + duration.ToString() 
                   + " " + loop.ToString() 
                   + " " + wait.ToString());

        }
    }
}
