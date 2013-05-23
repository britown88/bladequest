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
   public partial class formReactionBubble : Form
   {
      public actReactionBubble action;

      public formReactionBubble()
      {
         InitializeComponent();

         action = new actReactionBubble("sleep", "", 0, false, false);
      }

      public formReactionBubble(actReactionBubble bubble)
      {
         InitializeComponent();
         this.action = bubble;
      }

      private void button2_Click(object sender, EventArgs e)
      {
         Close();
      }

      private void button1_Click(object sender, EventArgs e)
      {
         action.close = chkClose.Checked;
         action.target = txtTarget.Text;
         
         action.loop = chkLoop.Checked;
         action.wait = chkWait.Checked;

         action.bubble = (string)cmbBubble.SelectedItem;

         action.duration = (float)numDuration.Value;

         Close();
      }

      private void formReactionBubble_Load(object sender, EventArgs e)
      {
         chkClose.Checked = action.close;
         txtTarget.Text = action.target;

         chkLoop.Checked = action.loop;
         chkWait.Checked = action.wait;

         cmbBubble.SelectedItem = action.bubble;

         numDuration.Value = (decimal)action.duration;
         
      }

      private void chkClose_CheckedChanged(object sender, EventArgs e)
      {
         chkLoop.Enabled = !chkClose.Checked;
         chkWait.Enabled = !chkClose.Checked;
         cmbBubble.Enabled = !chkClose.Checked;
         numDuration.Enabled = !chkClose.Checked;
      }
   }
}
