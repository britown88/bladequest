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
    public partial class formShowScene : Form
    {
        public actShowScene action;

        public formShowScene()
        {
            InitializeComponent();
            action = new actShowScene("", "", 0, 0, 0, 0, 0, false);
        }

        public formShowScene(actShowScene action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formShowScene_Load(object sender, EventArgs e)
        {
           sceneName.Text = action.sceneName;
           numR.Value = action.r;
           numG.Value = action.g;
           numB.Value = action.b;
           numTime.Value = (Decimal)action.endTimer;
           numFade.Value = action.fadeSpeed;

           if (action.endTrigger.Equals("Input"))
              radInput.Checked = true;
           else
              radTimer.Checked = true;

           chkWait.Checked = action.wait;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.sceneName = sceneName.Text;
            action.r = (int)numR.Value;
            action.g = (int)numG.Value;
            action.b = (int)numB.Value;
            action.endTimer = (float)numTime.Value;
            action.fadeSpeed = (int)numFade.Value;
            action.endTrigger = radInput.Checked ? "Input" : "Timer";
            action.wait = chkWait.Checked;
            Close();
        }

        private void radInput_CheckedChanged(object sender, EventArgs e)
        {
           radTimer.Checked = !radInput.Checked;
           numTime.Enabled = radTimer.Checked;
        }

        private void radTimer_CheckedChanged(object sender, EventArgs e)
        {
           radInput.Checked = !radTimer.Checked;
           numTime.Enabled = radTimer.Checked;
        }
    }
}
