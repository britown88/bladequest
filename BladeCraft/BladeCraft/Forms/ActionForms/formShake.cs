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
    public partial class formShake : Form
    {
        public actShake action;
        public formShake()
        {
            InitializeComponent();
            action = new actShake(0, 0, true);
        }

        public formShake(actShake action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formShake_Load(object sender, EventArgs e)
        {
            intensity.Value = action.intensity;
            duration.Value = (decimal)action.duration;
            wait.Checked = action.wait;

        }

        private void button1_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void OK_Click(object sender, EventArgs e)
        {
            action.wait = wait.Checked;
            action.intensity = (int)intensity.Value;
            action.duration = (float)duration.Value;
            Close();
        }
    }
}
