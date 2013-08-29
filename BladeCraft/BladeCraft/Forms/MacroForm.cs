using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace BladeCraft.Forms
{
   public partial class MacroForm : Form
   {
      private class NewMacroData
      {
         public NewMacroData(string path)
         {
            this.path = path;
         }
         public string path;
      }
      void addToDirectory(string currentDirectory, TreeNodeCollection nodes)
      {
         TreeNode toRemove = null;
         foreach (TreeNode node in nodes)
         {
            if (node.Tag == null) //directory.
            {
               string nextDirectory = currentDirectory + node.Text + "/";
               addToDirectory(nextDirectory, node.Nodes);
               node.Nodes.Add("New macro...");
               node.Tag = new NewMacroData(nextDirectory);
            }
            else if (node.Tag is NewMacroData)
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
         addToDirectory("", MacroTreeView.Nodes);
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

      private void MacroTreeView_NodeMouseClick(object sender, TreeNodeMouseClickEventArgs e)
      {
         var node = e.Node;
         if (node.Tag is NewMacroData)
         {
            //aw shiiiiiiiiiiiiiiiiet
            //modal dialoggggu
         }
         else if (node.Tag is string) //existing macro polo
         {
            //load
         }
      }
   }
}
