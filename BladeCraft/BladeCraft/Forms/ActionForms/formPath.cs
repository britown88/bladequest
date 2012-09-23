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
    public partial class formPath : Form
    {
        public actPath action;
        public formPath()
        {
            InitializeComponent();
            action = new actPath("", true);
        }

        public formPath(actPath action)
        {
            InitializeComponent();
            this.action = action;
        }

        private void formPath_Load(object sender, EventArgs e)
        {
            foreach(string str in action.path.actions)
                pathActions.Items.Add(str);

            target.Text = action.path.target;
            wait.Checked = action.path.wait;

        }

        private void button20_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button18_Click(object sender, EventArgs e)
        {
            action.path.target = target.Text;
            action.path.wait = wait.Checked;

            action.path.actions.Clear();
            foreach (ListViewItem item in pathActions.Items)
                action.path.actions.Add(item.Text);
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (pathActions.SelectedItems.Count > 0 && pathActions.SelectedIndices[0] > 0)
            {
                string str = pathActions.SelectedItems[0].Text;
                int index = pathActions.SelectedIndices[0];

                pathActions.Items.RemoveAt(index);
                pathActions.Items.Insert(index - 1, str);
                pathActions.SelectedIndices.Add(index - 1);
                
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            if (pathActions.SelectedItems.Count > 0 && pathActions.SelectedIndices[0] < pathActions.Items.Count - 1)
            {
                string str = pathActions.SelectedItems[0].Text;
                int index = pathActions.SelectedIndices[0];

                pathActions.Items.RemoveAt(index);
                pathActions.Items.Insert(index + 1, str);
                pathActions.SelectedIndices.Add(index + 1);

            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if (pathActions.SelectedItems.Count > 0)
            {
                int index = pathActions.SelectedIndices[0];

                pathActions.Items.RemoveAt(index);
                pathActions.SelectedIndices.Add(index);

            }
        }

        private void addPathItem(string str)
        {
            if (pathActions.SelectedItems.Count > 0)
            {
                int index = pathActions.SelectedIndices[0];

                pathActions.Items.Insert(index, str);
            }
            else
                pathActions.Items.Add(str);
        }

        private void button4_Click(object sender, EventArgs e)
        {
            addPathItem("moveleft");           
        }

        private void button5_Click(object sender, EventArgs e)
        {
            addPathItem("moveup");  
        }

        private void button6_Click(object sender, EventArgs e)
        {
            addPathItem("moveright");  
        }

        private void button7_Click(object sender, EventArgs e)
        {
            addPathItem("movedown");  
        }

        private void button8_Click(object sender, EventArgs e)
        {
            addPathItem("faceleft");  
        }

        private void button9_Click(object sender, EventArgs e)
        {
            addPathItem("faceup");  
        }

        private void button10_Click(object sender, EventArgs e)
        {
            addPathItem("faceright");  
        }

        private void button11_Click(object sender, EventArgs e)
        {
            addPathItem("facedown");  
        }

        private void button12_Click(object sender, EventArgs e)
        {
            addPathItem("lockfacing");  
        }

        private void button13_Click(object sender, EventArgs e)
        {
            addPathItem("unlockfacing");  
        }

        private void button17_Click(object sender, EventArgs e)
        {
            addPathItem("increasemovespeed");  
        }

        private void button16_Click(object sender, EventArgs e)
        {
            addPathItem("decreasemovespeed");  
        }

        private void button15_Click(object sender, EventArgs e)
        {
            addPathItem("hide");  
        }

        private void button14_Click(object sender, EventArgs e)
        {
            addPathItem("show");  
        }

        private void button19_Click(object sender, EventArgs e)
        {
            addPathItem("wait");  
        }
    }
}
