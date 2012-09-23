using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using BladeCraft.Classes.Objects.Actions;

namespace BladeCraft.Forms.ActionForms
{
    public partial class formTeleportParty : Form
    {
        public actTeleportParty action;

        public formTeleportParty()
        {
            InitializeComponent();
            action = new actTeleportParty(0, 0, "");
        }

        public formTeleportParty(actTeleportParty action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formTeleportParty_Load(object sender, EventArgs e)
        {
            mapname.Text = action.mapName;
            X.Value = action.x;
            Y.Value = action.y;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.mapName = mapname.Text;
            action.x = (int)X.Value;
            action.y = (int)Y.Value;
            Close();
        }
    }
}
