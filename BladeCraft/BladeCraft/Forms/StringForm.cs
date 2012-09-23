using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace BladeCraft.Forms
{
    public partial class StringForm : Form
    {
        public string textString;
        private ListViewItem item;

        public StringForm()
        {
            InitializeComponent();
        }

        public StringForm(ListViewItem item)
        {
            InitializeComponent();
            this.item = item;
            textString = item.Text;
            txtString.Text = item.Text;
        }

        private void btnOK_Click(object sender, EventArgs e)
        {
            textString = txtString.Text;
            if (item != null)
                item.Text = textString;
            Close();
        }

        private void txtString_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == (char)Keys.Enter)
                btnOK_Click(sender, e);
        }

    }
}
