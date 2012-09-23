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
    public partial class formFade : Form
    {
        public actFade action;

        public formFade()
        {
            InitializeComponent();
            action = new actFade(0, 255, 0, 0, 0, false, true);
        }

        public formFade(actFade action)
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
            action.fadeSpeed = (int)fadeSpeed.Value;
            action.a = (int)a.Value;
            action.r = (int)r.Value;
            action.g = (int)g.Value;
            action.b = (int)b.Value;
            action.fadeout = fadeOut.Checked;
            action.wait = wait.Checked;

            Close();
        }

        private void formFade_Load(object sender, EventArgs e)
        {
            if (action != null)
            {
                fadeSpeed.Value = action.fadeSpeed;
                a.Value = action.a;
                r.Value = action.r;
                g.Value = action.g;
                b.Value = action.b;
                fadeOut.Checked = action.fadeout;
                wait.Checked = action.wait;
            }
        }




    }
}
