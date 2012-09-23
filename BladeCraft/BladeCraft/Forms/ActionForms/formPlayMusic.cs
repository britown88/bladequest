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
    public partial class formPlayMusic : Form
    {
        public actPlayMusic action;

        public formPlayMusic()
        {
            InitializeComponent();
            action = new actPlayMusic("", false, -1);
        }

        public formPlayMusic(actPlayMusic action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formPlayMusic_Load(object sender, EventArgs e)
        {
            songName.Text = action.song;
            playintro.Checked = action.playIntro;
            repeatCount.Value = action.repeatCount;
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            action.song = songName.Text;
            action.playIntro = playintro.Checked;
            action.repeatCount = (int)repeatCount.Value;
            Close();
        }
    }
}
