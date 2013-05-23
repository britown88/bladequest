using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

using BladeCraft.Classes.Objects.Actions;

namespace BladeCraft.Forms
{
    public partial class NewActionForm : Form
    {
        public Action.type selectedType;
        public bool actionChosen;

        public NewActionForm()
        {
            InitializeComponent();
        }

        private void button14_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.Fade;
            actionChosen = true;
            Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.Message;
            actionChosen = true;
            Close();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.ModifyGold;
            actionChosen = true;
            Close();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.ModifyInventory;
            actionChosen = true;
            Close();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.ModifyParty;
            actionChosen = true;
            Close();
        }

        private void button6_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.PanControl;
            actionChosen = true;
            Close();
        }

        private void button7_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.Path;
            actionChosen = true;
            Close();
        }

        private void button8_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.RestoreParty;
            actionChosen = true;
            Close();
        }

        private void button9_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.Shake;
            actionChosen = true;
            Close();
        }

        private void button10_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.StartBattle;
            actionChosen = true;
            Close();
        }

        private void button11_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.Switch;
            actionChosen = true;
            Close();
        }

        private void button12_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.TeleportParty;
            actionChosen = true;
            Close();
        }

        private void button13_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.Wait;
            actionChosen = true;
            Close();
        }

        private void button15_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.PlayMusic;
            actionChosen = true;
            Close();
        }

        private void button16_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.PauseMusic;
            actionChosen = true;
            Close();
        }

        private void button17_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.RestartGame;
            actionChosen = true;
            Close();
        }

        private void button18_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.ShowScene;
            actionChosen = true;
            Close();
        }

        private void button21_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.Merchant;
            actionChosen = true;
            Close();
        }

        private void button20_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.NameSelect;
            actionChosen = true;
            Close();
        }

        private void button19_Click(object sender, EventArgs e)
        {
            selectedType = Action.type.SaveMenu;
            actionChosen = true;
            Close();
        }

        private void button22_Click(object sender, EventArgs e)
        {
           selectedType = Action.type.ReactionBubble;
           actionChosen = true;
           Close();
        }
    }
}
