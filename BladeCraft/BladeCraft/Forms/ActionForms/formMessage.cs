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
    

    public partial class formMessage : Form
    {
        public actMessage action;
        public formMessage()
        {
            InitializeComponent();
            action = new actMessage("");

        }
        public formMessage(actMessage action)
        {
            InitializeComponent();
            this.action = action;
            

        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnOK_Click(object sender, EventArgs e)
        {
            string str = text.Text;
            
            action.msg = str;
            action.yesNoOpt = yesnoopt.Checked;
            Close();
        }

        private void formMessage_Load(object sender, EventArgs e)
        {
            text.Text = action.msg;
            yesnoopt.Checked = action.yesNoOpt;
        }


    }
}
