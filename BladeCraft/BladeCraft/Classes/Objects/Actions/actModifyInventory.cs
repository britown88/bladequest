using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actModifyInventory : Action
    {
        public int count;
        public string item;
        public bool remove;

        public actModifyInventory(string item, int count, bool remove)
        {
            this.count = count;
            this.item = item;
            this.remove = remove;
            actionType = type.ModifyInventory;
        }

        public actModifyInventory(actModifyInventory other)
        {
            this.count = other.count;
            this.item = other.item;
            this.remove = other.remove;
            actionType = type.ModifyInventory;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action inventory " +
                item + " " +
                count.ToString() + " " +
                remove.ToString());

        }
    }
}
