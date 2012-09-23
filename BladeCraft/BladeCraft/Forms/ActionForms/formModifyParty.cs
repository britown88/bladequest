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
    public partial class formModifyParty : Form
    {
        public actModifyParty action;

        public formModifyParty()
        {
            InitializeComponent();
            action = new actModifyParty("", false);
        }
        public formModifyParty(actModifyParty action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formModifyParty_Load(object sender, EventArgs e)
        {
            character.Text = action.charName;
            remove.Checked = action.remove;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.charName = character.Text;
            action.remove = remove.Checked;
            Close();
        }
    }
}
