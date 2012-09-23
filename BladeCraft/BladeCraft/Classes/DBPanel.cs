using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;

namespace BladeCraft.Classes
{
    class DBPanel:Panel
    {
        public DBPanel()
        {
            this.SetStyle(ControlStyles.AllPaintingInWmPaint | ControlStyles.UserPaint | ControlStyles.DoubleBuffer, true);
        
        }
    }
}
