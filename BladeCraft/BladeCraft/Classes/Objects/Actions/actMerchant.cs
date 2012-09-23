using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actMerchant : Action
    {
        public string merchName;
        public float discount;

        public actMerchant(string merchName, float discount)
        {
            this.merchName = merchName;
            this.discount = discount;
            actionType = type.Merchant;
        }

        public actMerchant(actMerchant other)
        {
            this.merchName = other.merchName;
            this.discount = other.discount;
            actionType = type.Merchant;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action merchant " +
                merchName + " " +
                discount.ToString());

        }
    }
}
