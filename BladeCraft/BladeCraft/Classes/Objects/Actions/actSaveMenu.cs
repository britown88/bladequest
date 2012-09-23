using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actSaveMenu : Action
    {


        public actSaveMenu()
        {

            actionType = type.SaveMenu;
        }

        public actSaveMenu(actSaveMenu other)
        {

            actionType = type.SaveMenu;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine("action savemenu");

        }
    }
}
