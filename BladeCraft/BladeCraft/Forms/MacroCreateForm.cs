using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace BladeCraft.Forms
{
   public partial class MacroCreateForm : Form
   {
      MacroData macroData;
      public MacroCreateForm(MacroData macroData)
      {
         InitializeComponent();
         this.macroData = macroData;
      }
      public static MacroData getNewMacro()
      {
         MacroData data = new MacroData();
         MacroCreateForm form = new MacroCreateForm(data);
         form.ShowDialog();
         if (data.name == null) return null;
         return data;
      }

      private void btnCancel_Click(object sender, EventArgs e)
      {
         this.Close();
      }

      private void btnSave_Click(object sender, EventArgs e)
      {
         if (MacroNameTxt.Text == "")
            MessageBox.Show("Must specify a name!");
         else
         {
            macroData.name = MacroNameTxt.Text;
            macroData.x = (int)numX.Value;
            macroData.y = (int)numY.Value;
            this.Close();
         }
      }
   }

   public class MacroData
   {
      public string name;
      public int x, y;
   }
}
