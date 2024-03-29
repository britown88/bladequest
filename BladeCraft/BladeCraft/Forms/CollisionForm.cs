﻿using System;
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
   public partial class CollisionForm : Form
   {

      private class CollisionData
      {
         public CollisionData(bool left, bool right, bool top, bool bottom)
         {
            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
         }
         public bool top, bottom, left, right;
      }
      string activePath;
      TileImage tileset;
      List<CollisionData> tileCollision;
      float scale;
      Bitmap collArrows;
      Rectangle[] collArrowRects;

      public CollisionForm()
      {
         InitializeComponent();

         collArrows = Bitmaps.loadRawBitmap("misc/collarrows.png");
         collArrowRects = new Rectangle[4];
         collArrowRects[0] = new Rectangle(0, 0, 16, 16);
         collArrowRects[1] = new Rectangle(16, 0, 16, 16);
         collArrowRects[2] = new Rectangle(32, 0, 16, 16);
         collArrowRects[3] = new Rectangle(48, 0, 16, 16);

         tileset = null;
         activePath = null;


         ImageTreeViewBuilder.BuildImageTreeView(
            new ElementHierarchyStrategy((node, basePath, path) => node.Tag = path),
            TileSetTreeView.Nodes
         );

         scale = 2.0f;
      }

      private void saveButton1_Click(object sender, EventArgs e)
      {
         //SAVE THE CURRENTLY CLICKED SHIT HERE.  FUCK YEAR
         if (activePath == null) return;

         var path = activePath.Substring(0, activePath.Length - 3) + "dat";
         using (var xwriter = new XmlTextWriter(path, null))
         {
            xwriter.WriteStartDocument();

            xwriter.WriteComment("TILE COLLISION");

            xwriter.WriteStartElement("Map");

            Bitmaps.bitmaps[activePath] = tileset;
            int tileSize = MapForm.tileSize;
            int xSize = tileset.xPixels / MapForm.tileSize;
            int ySize = tileset.yPixels / MapForm.tileSize;

            int i = 0;
            for (int y = 0; y < ySize; ++y)
            {
               for (int x = 0; x < xSize; ++x)
               {
                  var tile = tileset.tiles[i++];

                  xwriter.WriteStartElement("Tile");

                  xwriter.WriteAttributeString("X", x.ToString());
                  xwriter.WriteAttributeString("Y", y.ToString());

                  xwriter.WriteAttributeString("colLeft", tile.colLeft.ToString());
                  xwriter.WriteAttributeString("colRight", tile.colRight.ToString());
                  xwriter.WriteAttributeString("colTop", tile.colTop.ToString());
                  xwriter.WriteAttributeString("colBottom", tile.colBottom.ToString());

                  xwriter.WriteEndElement();
               }
            }

            xwriter.WriteEndElement();

            xwriter.WriteEndDocument();
            xwriter.Close();
         }

         MessageBox.Show("Collision saved!");
      }

      private void TileSetTreeView_NodeMouseClick(object sender, TreeNodeMouseClickEventArgs e)
      {
         if (e.Node.Tag != null && e.Node.Tag is string)  //jesus shitballs, I'm an element nodeeeeee
         {
            activePath = (string)e.Node.Tag;

            tileset = new TileImage(Bitmaps.bitmaps[activePath]);

            int xSize = tileset.xPixels / MapForm.tileSize;
            int ySize = tileset.yPixels / MapForm.tileSize;

            tileCollision = new List<CollisionData>();

            CollisionPanel.Width = (int)(tileset.xPixels * scale);
            CollisionPanel.Height = (int)(tileset.yPixels * scale);

            CollisionPanel.Invalidate();
         }
      }


      private void drawCollision(Graphics g/*, Rectangle frame*/)
      {
         if (activePath == null)
         {
            return;
         }

         int xSize = tileset.xPixels / MapForm.tileSize;
         int ySize = tileset.yPixels / MapForm.tileSize;
         int tileSize = MapForm.tileSize;
         /*for (int x = frame.Left; x < Math.Min(xSize, frame.Right); ++x)
         {
            for (int y = frame.Top; y < Math.Min(ySize, frame.Bottom); ++y)
            {*/
         for (int x = 0; x < xSize; ++x)
         {
            for (int y = 0; y < ySize; ++y)
            {
               var t = tileset.tiles[x + y * xSize];
               Rectangle destRect = new Rectangle((int)(x * tileSize * scale), (int)(y * tileSize * scale), (int)(tileSize * scale), (int)(tileSize * scale));
               if (t.colTop) g.DrawImage(collArrows, destRect, collArrowRects[Tile.sideTop], GraphicsUnit.Pixel);
               if (t.colRight) g.DrawImage(collArrows, destRect, collArrowRects[Tile.sideRight], GraphicsUnit.Pixel);
               if (t.colBottom) g.DrawImage(collArrows, destRect, collArrowRects[Tile.sideBottom], GraphicsUnit.Pixel);
               if (t.colLeft) g.DrawImage(collArrows, destRect, collArrowRects[Tile.sideLeft], GraphicsUnit.Pixel);
            }
         }
      }
      private void CollisionPanel_Paint(object sender, PaintEventArgs e)
      {
         Graphics g = e.Graphics;
         Pen selPen = new Pen(Color.White);
         selPen.Width = 4;

         if (activePath != null)
         {
            int tileSize = MapForm.tileSize;
            int xSize = tileset.xPixels / MapForm.tileSize;
            int ySize = tileset.yPixels / MapForm.tileSize;
            for (int layer = 0; layer < 8; ++layer)
            {
               for (int j = 0; j < ySize; ++j)
               {
                  for (int i = 0; i < xSize; ++i)
                  {
                     var t = tileset.tiles[i + j * xSize];
                     Rectangle destRect = new Rectangle((int)(i * tileSize * scale), (int)(j * tileSize * scale), (int)(tileSize * scale), (int)(tileSize * scale));
                     foreach (var bmp in t.bitmaps)
                     {
                        if (bmp.layerOffset != layer) continue;
                        g.DrawImage(bmp.bitmap,
                           destRect,
                           bmp.x * tileSize,
                           bmp.y * tileSize,
                           tileSize, tileSize, GraphicsUnit.Pixel);
                     }
                  }
               }
            }
            drawCollision(g);
         }
      }


      Point clickedPoint(MouseEventArgs e)
      {
         Point gridPoint = new Point();
         gridPoint.X = (int)(e.X / (MapForm.tileSize * scale));
         gridPoint.Y = (int)(e.Y / (MapForm.tileSize * scale));

         return gridPoint;
      }

      private void allToolStripMenuItem_Click(object sender, EventArgs e)
      {
         leftToolStripMenuItem.Checked = true;
         topToolStripMenuItem.Checked = true;
         rightToolStripMenuItem.Checked = true;
         bottomToolStripMenuItem.Checked = true;
      }

      private void noneToolStripMenuItem_Click(object sender, EventArgs e)
      {
         leftToolStripMenuItem.Checked = false;
         topToolStripMenuItem.Checked = false;
         rightToolStripMenuItem.Checked = false;
         bottomToolStripMenuItem.Checked = false;
      }

      private bool mouseDown = false;

      private void CollisionPanel_MouseUp(object sender, MouseEventArgs e)
      {
         mouseDown = false;
      }

      private void CollisionPanel_MouseDown(object sender, MouseEventArgs e)
      {
         mouseDown = true;
         CollisionPanel_MouseMove(sender, e);
      }

      private void CollisionPanel_MouseMove(object sender, MouseEventArgs e)
      {
         if (activePath == null || !mouseDown)
         {
            return;
         }

         var pt = clickedPoint(e);

         int xSize = tileset.xPixels / MapForm.tileSize;
         int ySize = tileset.yPixels / MapForm.tileSize;

         if (pt.X < 0 || pt.X >= xSize ||
             pt.Y < 0 || pt.Y >= ySize) return;

         var tile = tileset.tiles[pt.X + pt.Y * xSize];

         tile.colBottom = bottomToolStripMenuItem.Checked;
         tile.colTop = topToolStripMenuItem.Checked;
         tile.colLeft = leftToolStripMenuItem.Checked;
         tile.colRight = rightToolStripMenuItem.Checked;

         CollisionPanel.Invalidate();
      }


   }
}
