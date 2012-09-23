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
    public partial class formSwitch : Form
    {
        public actSwitch action;
        public formSwitch()
        {
            InitializeComponent();
            action = new actSwitch("", true);
        }

        public formSwitch(actSwitch action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formSwitch_Load(object sender, EventArgs e)
        {
            switchName.Text = action.switchName;
            switchState.Checked = action.state;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.switchName = switchName.Text;
            action.state = switchState.Checked;
            Close();
        }
    }
}
