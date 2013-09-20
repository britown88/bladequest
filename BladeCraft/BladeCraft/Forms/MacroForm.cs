using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Xml;

using BladeCraft.Classes;

namespace BladeCraft.Forms
{
   public partial class MacroForm : Form
   {

      int offsetLayer = 0;
      private static float scale = 2.0f;
      string activePath;
      string activePaint;
      TileImage activeImage;
      int selX, selY;

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
               node.Nodes[node.Nodes.Count-1].Tag = new NewMacroData(nextDirectory);
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
         addToDirectory("assets/drawable/", MacroTreeView.Nodes);
      }

      class MacroFilteredStrategy : ImageHierarchyStrategy
      {
          string pathFilter;
          public MacroFilteredStrategy(string pathFilter)
          {
              this.pathFilter = pathFilter;
          }
          public void onNode(TreeNode node, string baseFolder, string relativePath)
          {
              node.Tag = relativePath;
          }

          public void onDirectory(TreeNode node, string baseFolder, string relativePath)
          {
          }

          public bool filter(string pathName)
          {
              return pathName.Substring(pathName.Length - 3) == pathFilter;
          }

          public void onAnimatedDirectory(TreeNode node, string baseFolder, string relativePath)
          {
          }
      }


      public MacroForm()
      {
         InitializeComponent();

         ImageTreeViewBuilder.BuildImageTreeView(new MacroFilteredStrategy("png"), TileSetTreeView.Nodes);
         ImageTreeViewBuilder.BuildImageTreeView(new MacroFilteredStrategy("mif"), MacroTreeView.Nodes);

         addNewMacroItems();

         this.Focus();
      }

      private void writeEmptyMacro(string path, int xSize, int ySize)
      {
         using (var xwriter = new XmlTextWriter(path, null))
         {
            xwriter.WriteStartDocument();

            xwriter.WriteComment("MACRO");

            xwriter.WriteStartElement("Macro");

            xwriter.WriteAttributeString("X", xSize.ToString());
            xwriter.WriteAttributeString("Y", ySize.ToString());

            xwriter.WriteEndElement();

            xwriter.WriteEndDocument();
            xwriter.Close();
         }
      }

      private void MacroTreeView_NodeMouseClick(object sender, TreeNodeMouseClickEventArgs e)
      {
         var node = e.Node;
         if (node.Tag is NewMacroData)
         {
            NewMacroData nodeTag = node.Tag as NewMacroData;
            //aw shiiiiiiiiiiiiiiiiet
            //modal dialoggggu
            var data = MacroCreateForm.getNewMacro();
            if (data != null)
            {
               node.Parent.Nodes.Add(data.name);
               node.Parent.Nodes[node.Parent.Nodes.Count - 1].Tag = nodeTag.path + data.name + ".mif";

               //create empty macro....
               writeEmptyMacro(node.Parent.Nodes[node.Parent.Nodes.Count - 1].Tag as string, data.x, data.y);

               addNewMacroItems();
            }
         }
         else if (node.Tag is string) //existing macro polo
         {
            //load
            activePath = node.Tag as string;
            activeImage = main.loadMacroImage(activePath);

            macroPanel.Width = (int)(activeImage.xPixels * scale);
            macroPanel.Height = (int)(activeImage.yPixels * scale);
            macroPanel.Invalidate();
         }
      }

      Point clickedPoint(MouseEventArgs e)
      {
         Point gridPoint = new Point();
         gridPoint.X = (int)(e.X / (MapForm.tileSize * scale));
         gridPoint.Y = (int)(e.Y / (MapForm.tileSize * scale));

         return gridPoint;
      }


      private void saveButton1_Click(object sender, EventArgs e)
      {
         if (activePath != null)
         {
            Bitmaps.bitmaps[activePath] = activeImage;

            int xSize = activeImage.xPixels / MapForm.tileSize;
            int ySize = activeImage.yPixels / MapForm.tileSize;
            //save the macro out.
            using (var xwriter = new XmlTextWriter(activePath, null))
            {
               xwriter.WriteStartDocument();

               xwriter.WriteComment("MACRO");

               xwriter.WriteStartElement("Macro");

               xwriter.WriteAttributeString("X", xSize.ToString());
               xwriter.WriteAttributeString("Y", ySize.ToString());

               for (int j = 0; j < ySize; ++j)
               {
                  for (int i = 0; i < xSize; ++i)
                  {
                     foreach (var t in activeImage.tiles[i + j * xSize].bitmaps)
                     {
                        xwriter.WriteStartElement("Tile");

                        xwriter.WriteAttributeString("X", i.ToString());
                        xwriter.WriteAttributeString("Y", j.ToString());

                        xwriter.WriteAttributeString("BmpX", t.x.ToString());
                        xwriter.WriteAttributeString("BmpY", t.y.ToString());


                        xwriter.WriteAttributeString("Image", t.bitmapPath);
                        xwriter.WriteAttributeString("Offset", t.layerOffset.ToString());
                        

                        xwriter.WriteEndElement();
                     }
                  }
               }
               

               xwriter.WriteEndElement();

               xwriter.WriteEndDocument();
               xwriter.Close();
            }
            MessageBox.Show("Macro saved!");
         }
      }

      private void macroPanel_Paint(object sender, PaintEventArgs e)
      {
         Graphics g = e.Graphics;
         Pen selPen = new Pen(Color.White);
         selPen.Width = 4;

         if (activePath != null)
         {
            int tileSize = MapForm.tileSize;
            int xSize = activeImage.xPixels / MapForm.tileSize;
            int ySize = activeImage.yPixels / MapForm.tileSize;
            for (int layer = 0; layer < 8; ++layer)
            {
               for (int j = 0; j < ySize; ++j)
               {
                  for (int i = 0; i < xSize; ++i)
                  {
                     var t = activeImage.tiles[i + j * xSize];
                     Rectangle destRect = new Rectangle((int)(i * tileSize * scale), (int)(j * tileSize * scale), (int)(tileSize * scale), (int)(tileSize * scale));
                     foreach (var bmp in t.bitmaps)
                     {
                        if (bmp.layerOffset == layer)
                        {
                           g.DrawImage(bmp.bitmap,
                              destRect,
                              bmp.x * tileSize,
                              bmp.y * tileSize,
                              tileSize, tileSize, GraphicsUnit.Pixel);
                        }
                     }
                  }
               }
            }
         }
      }

      private void MacroForm_KeyPress(object sender, KeyPressEventArgs e)
      {
         if (e.KeyChar >= '1' && e.KeyChar <= '7')
         {
            offsetLayer = e.KeyChar - '0';
         }

         if (e.KeyChar == '`')
         {
            offsetLayer = 0;
         }
      }

      private void TileSetTreeView_NodeMouseClick(object sender, TreeNodeMouseClickEventArgs e)
      {
         var node = e.Node;
         if (node.Tag is string)
         {
            activePaint = node.Tag as string;
            selX = selY = 0;
            TileImage tileset = Bitmaps.bitmaps[activePaint];
            tsPanel.Width = (int)(tileset.xPixels * scale);
            tsPanel.Height = (int)(tileset.yPixels * scale);
            //set new image.
            tsPanel.Invalidate();
         }
      }

      private void tsPanel_Paint(object sender, PaintEventArgs e)
      {
         Graphics g = e.Graphics;
         Pen selPen = new Pen(Color.White);
         selPen.Width = 4;

         if (activePaint != null)
         {
            int tileSize = MapForm.tileSize;
            TileImage tileset = Bitmaps.bitmaps[activePaint];
            int xSize = tileset.xPixels / MapForm.tileSize;
            int ySize = tileset.yPixels / MapForm.tileSize;

            for (int j = 0; j < ySize; ++j)
            {
               for (int i = 0; i < xSize; ++i)
               {
                  var t = tileset.tiles[i + j * xSize];
                  Rectangle destRect = new Rectangle((int)(i * tileSize * scale), (int)(j * tileSize * scale), (int)(tileSize * scale), (int)(tileSize * scale));
                  foreach (var bmp in t.bitmaps)
                  {
                     g.DrawImage(bmp.bitmap,
                        destRect,
                        bmp.x * tileSize,
                        bmp.y * tileSize,
                        tileSize, tileSize, GraphicsUnit.Pixel);
                  }
               }
            }

            g.DrawRectangle(selPen,selX* tileSize * scale,
            selY * tileSize * scale, 
            tileSize * scale, tileSize * scale);
         }
      }

      private void tsPanel_MouseClick(object sender, MouseEventArgs e)
      {
         if (activePaint == null) return;

         var pos = clickedPoint(e);

         TileImage tileset = Bitmaps.bitmaps[activePaint];
         int xSize = tileset.xPixels / MapForm.tileSize;
         int ySize = tileset.yPixels / MapForm.tileSize;

         if (pos.X < 0 || pos.X >= xSize ||
             pos.Y < 0 || pos.Y >= ySize) return;

         selX = pos.X;
         selY = pos.Y;

         tsPanel.Invalidate();
      }

      private void macroPanel_MouseClick(object sender, MouseEventArgs e)
      {
         if (activePaint == null || activeImage == null) return;

         var pos = clickedPoint(e);

         TileImage tileset = Bitmaps.bitmaps[activePaint];
         int tX = tileset.xPixels / MapForm.tileSize;
         int xSize = activeImage.xPixels / MapForm.tileSize;
         int ySize = activeImage.yPixels / MapForm.tileSize;

         if (pos.X < 0 || pos.X >= xSize ||
             pos.Y < 0 || pos.Y >= ySize) return;

         var tileData = activeImage.tiles[pos.X + pos.Y * xSize];

         var bmp = new TileBitmap(tileset.tiles[selX + selY * tX].bitmaps[0].bitmap, selX, selY, tileset.tiles[selX + selY * tX].bitmaps[0].bitmapPath, offsetLayer, 0);

         foreach (var bitmap in tileData.bitmaps)
         {
            if (bitmap.layerOffset == offsetLayer)
            {
               tileData.bitmaps.Remove(bitmap);
               break;
            }
         }
         tileData.bitmaps.Add(bmp);

         macroPanel.Invalidate();
      }
   }

}
