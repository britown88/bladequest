using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actModifyGold : Action
    {
        public int gold;

        public actModifyGold(int gold)
        {
            this.gold = gold;
            actionType = type.ModifyGold;
        }

        public actModifyGold(actModifyGold other)
        {
            this.gold = other.gold;
            actionType = type.ModifyGold;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action gold " + gold.ToString());

        }
    }
}
