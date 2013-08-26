using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace BladeCraft.Forms
{
   public partial class MacroForm : Form
   {
      void addToDirectory(TreeNodeCollection nodes)
      {
         TreeNode toRemove = null;
         foreach (TreeNode node in nodes)
         {
            if (node.Tag == null) //directory.
            {
               addToDirectory(node.Nodes);
               node.Nodes.Add("New macro...");
               node.Tag = "cocks";
            }
            else if (node.Tag is string)
            {
               toRemove = node;
            }
         }
         if (toRemove != null)
         {
            nodes.Remove(toRemove);
         }
      }
      void addNewMacroItems()
      {
         addToDirectory(MacroTreeView.Nodes);
      }
      public MacroForm()
      {
         InitializeComponent();

         Func<string, bool> filterPng = (path) => (path.Substring(path.Length - 3) == "png");
         Func<string, bool> filterMif = (path) => (path.Substring(path.Length - 3) == "mif");

         ImageTreeViewBuilder.BuildImageTreeView(
                     new ImageTreeViewArgs(TileSetTreeView.Nodes)
                     .setFilter(filterPng)
                     .onElement((node, basePath, path) => node.Tag = path)
                  );

         ImageTreeViewBuilder.BuildImageTreeView(
                     new ImageTreeViewArgs(MacroTreeView.Nodes)
                     .setFilter(filterMif)
                     .onElement((node, basePath, path) => node.Tag = path)
                  );

         addNewMacroItems();
      }
   }
}
