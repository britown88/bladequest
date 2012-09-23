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
    public partial class formPanControl : Form
    {
        public actPanControl action;
        public formPanControl()
        {
            InitializeComponent();
            action = new actPanControl(0, 0, 0, true);
        }
        public formPanControl(actPanControl action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formPanControl_Load(object sender, EventArgs e)
        {
            X.Value = action.x;
            Y.Value = action.y;
            panSpeed.Value = action.speed;
            wait.Checked = action.wait;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.x = (int)X.Value;
            action.y = (int)Y.Value;
            action.speed = (int)panSpeed.Value;
            action.wait = wait.Checked;
            Close();
        }
    }
}
