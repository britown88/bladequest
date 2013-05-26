using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using BladeCraft.Classes;

namespace BladeCraft.Forms
{
   public partial class ObjectHeader : Form
   {
      BQMap map;
      public ObjectHeader(BQMap map)
      {
         InitializeComponent();

         this.map = map;
      }

      private void ObjectHeader_Load(object sender, EventArgs e)
      {
         txtScript.Text = map.Header;
      }

      private void btnCancel_Click(object sender, EventArgs e)
      {
         Close();
      }

      private void btnSave_Click(object sender, EventArgs e)
      {
         map.Header = txtScript.Text;
      }


   }
}
