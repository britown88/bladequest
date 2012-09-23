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
    public partial class formModifyInventory : Form
    {
        public actModifyInventory action;
        public formModifyInventory()
        {
            InitializeComponent();
            action = new actModifyInventory("", 0, false);
        }

        public formModifyInventory(actModifyInventory action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formModifyInventory_Load(object sender, EventArgs e)
        {
            itemName.Text = action.item;
            itemCount.Value = action.count;
            remove.Checked = action.remove;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.item =  itemName.Text;
            action.count = (int)itemCount.Value;
            action.remove = remove.Checked;
            Close();

        }
    }
}
