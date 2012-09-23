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
    public partial class formMerchant : Form
    {
        public actMerchant action;

        public formMerchant()
        {
            InitializeComponent();
            action = new actMerchant("", 0.0f);
        }

        public formMerchant(actMerchant action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formMerchant_Load(object sender, EventArgs e)
        {
            merchName.Text = action.merchName;
            discount.Value = (Decimal)action.discount;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.merchName = merchName.Text;
            action.discount = (float)discount.Value;
            Close();
        }
    }
}
