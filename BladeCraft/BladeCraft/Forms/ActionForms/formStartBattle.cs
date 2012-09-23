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
    public partial class formStartBattle : Form
    {
        public actStartBattle action;

        public formStartBattle()
        {
            InitializeComponent();
            action = new actStartBattle("");
        }
        public formStartBattle(actStartBattle action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formStartBattle_Load(object sender, EventArgs e)
        {
            encounter.Text = action.encounter;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.encounter = encounter.Text;
            Close();
        }
    }
}
