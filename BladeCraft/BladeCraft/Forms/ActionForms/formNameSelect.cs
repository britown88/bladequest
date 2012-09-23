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
    public partial class formNameSelect : Form
    {
        public actNameSelect action;

        public formNameSelect()
        {
            InitializeComponent();
            action = new actNameSelect("");
        }

        public formNameSelect(actNameSelect action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formNameSelect_Load(object sender, EventArgs e)
        {
            charName.Text = action.charname;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();

        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.charname = charName.Text;
            Close();
        }
    }
}
