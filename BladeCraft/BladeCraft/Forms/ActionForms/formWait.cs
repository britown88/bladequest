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
    public partial class formWait : Form
    {
        public actWait action;

        public formWait()
        {
            InitializeComponent();
            action = new actWait(0);
        }

        public formWait(actWait action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formWait_Load(object sender, EventArgs e)
        {
            seconds.Value =(decimal)action.seconds;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.seconds = (float)seconds.Value;
            Close();
        }
    }
}
