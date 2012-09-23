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
    public partial class formModifyGold : Form
    {
        public actModifyGold action;

        public formModifyGold()
        {
            InitializeComponent();
            action = new actModifyGold(0);
        }

        public formModifyGold(actModifyGold action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formModifyGold_Load(object sender, EventArgs e)
        {
            gold.Value = action.gold;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.gold = (int)gold.Value;
            Close();

        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}
