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
            action = new actShowScene("");
        }

        public formShowScene(actShowScene action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formShowScene_Load(object sender, EventArgs e)
        {
            sceneName.Text = action.sceneName;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.sceneName = sceneName.Text;
            Close();
        }
    }
}
