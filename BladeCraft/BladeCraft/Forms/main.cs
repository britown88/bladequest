using System;
using System.Xml;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using BladeCraft.Forms;
using BladeCraft.Classes;

namespace BladeCraft
{


    public partial class main : Form
    {
        //private BQMap map;
        private MapInfoForm infoForm;



        public main()
        {
            InitializeComponent();
            //mapForm = new MapForm();
            //map = new BQMap("newmap", 30, 20, "New Map", "", false);

            
        }

       private class TileDataElement
       {
          public TileDataElement(XmlTextReader reader, string elementType)
          {
             this.reader = reader;
             this.elementType = elementType;
          }
          public string getAttribute(string name)
          {
             return reader.GetAttribute(name);
          }
          XmlTextReader reader;
          public string elementType;
       }

        IEnumerable<TileDataElement> getNodes(XmlTextReader reader)
        {
           while (reader.Read())
           {
              if (XmlNodeType.Element == reader.NodeType)
              {
                 yield return new TileDataElement(reader, reader.LocalName);
              }
           }
        }

        private TileImage readTileImageData(string path, XmlTextReader xmlReader)
        {
           var bmp = new TileImage(new Bitmap(path));


           foreach (var node in getNodes(xmlReader))
           {
              if (node.elementType == "Map") continue;
              //only element type right now is "tile", deal with it nerds.
              int x = Convert.ToInt32(node.getAttribute("X"));
              int y = Convert.ToInt32(node.getAttribute("Y"));

              bool leftCol = Convert.ToBoolean(node.getAttribute("colLeft"));
              bool rightCol = Convert.ToBoolean(node.getAttribute("colRight"));
              bool topCol = Convert.ToBoolean(node.getAttribute("colTop"));
              bool bottomCol = Convert.ToBoolean(node.getAttribute("colBottom"));

              bmp.setCollision(x, y, leftCol, rightCol, topCol, bottomCol);
           }

           return bmp;
        }

        private void loadBitmapFolder(string folder)
        {
           loadBitmapFolderRecursive(Application.StartupPath + "\\assets\\drawable\\" + folder);
        }
        private void loadBitmapFolderRecursive(string folder)
        {
           foreach (var directory in System.IO.Directory.GetDirectories(folder))
           {
              loadBitmapFolderRecursive(directory);
           }
           foreach (var path in System.IO.Directory.GetFiles(folder))
           {
              var ext = path.Substring(path.Length - 3);
              if (ext == "png")
              {
                 try
                 {
                    using (var reader = new XmlTextReader(path.Substring(0, path.Length - 3) + "dat"))
                    {
                       Bitmaps.bitmaps.Add(path.Substring(Application.StartupPath.Length + 1), readTileImageData(path, reader));
                    }
                 }
                 catch (System.Exception ex)
                 {
                    //file not found LOL DICKS
                    Bitmaps.bitmaps.Add(path.Substring(Application.StartupPath.Length + 1), new TileImage(new Bitmap(path)));
                 }
              }
           }
        }

        private void loadBitmaps()
        {
            Bitmaps.bitmaps = new Dictionary<string, TileImage>();
            loadBitmapFolder("stairs");
            loadBitmapFolder("walls");
            loadBitmapFolder("materials");
            loadBitmapFolder("objects");
        }

        private void main_Load(object sender, EventArgs e)
        {
            loadBitmaps();
            readMaps();

            //bitmapselect selectform = new bitmapselect(gamedata.bitmaps);
            //var result = selectform.showdialog();

            //mapForm.MdiParent = this;
            //mapForm.Dock = DockStyle.Fill;
            //mapForm.StartPosition = FormStartPosition.CenterScreen;
            //mapForm.Show();

            //mapForm.updateMap();
        }

        public void readMaps()
        {
            string[] mapFiles = System.IO.Directory.GetFiles(Application.StartupPath + "\\bcfiles");


            foreach (String path in mapFiles)
            {
                MapForm mf = new MapForm(new BQMap(path));
                mf.MdiParent = this;

                string mapName = path.Remove(0, path.LastIndexOf('\\') + 1);
                mapName = mapName.Remove(mapName.LastIndexOf('.'));

                //check existing items
                Boolean found = false;

                foreach (ListViewItem lvi in lvwMaps.Items)
                    if (lvi.Text.Equals(mapName))
                    {
                        found = true;
                        break;
                    }

                if (!found)
                {
                    ListViewItem item = new ListViewItem(mapName);

                    item.SubItems.Add(mf.getMap().getDisplayName());
                    item.Tag = mf;
                    lvwMaps.Items.Add(item);

                }

                
            }

        }
        private void newMapToolStripMenuItem_Click(object sender, EventArgs e)
        {
            infoForm = new MapInfoForm();
            //infoForm.MdiParent = this;
            infoForm.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.MapInfoForm_FormClosed);
            infoForm.ShowDialog();

            
        }

        public void MapInfoForm_FormClosed(object sender, FormClosingEventArgs e)
        {
            //mapForm.updateMap();
            if (infoForm.getMap() != null)
            {
                MapForm mf = new MapForm(infoForm.getMap());
                mf.MdiParent = this;
                mf.updateMap();
                mf.Show();
                mf.Focus();
            }

        }

        private void lvwMaps_DoubleClick(object sender, EventArgs e)
        {
            MapForm mf = (MapForm)lvwMaps.SelectedItems[0].Tag;

            if (mf.IsDisposed)
            {
                MapForm newMF = new MapForm(new BQMap(mf.getMap().getMapPath()));
                newMF.MdiParent = this;
                mf = newMF;
            }
            mf.updateMap();
            mf.Show();
            mf.Focus();

        }

        private void deleteToolStripMenuItem_Click(object sender, EventArgs e)
        {
            MapForm mf = ((MapForm)lvwMaps.SelectedItems[0].Tag);
            BQMap selectedMap = mf.getMap();

            DialogResult result = MessageBox.Show("Are you super sure?  This can't be undone!", selectedMap.getName() + ":\"" + selectedMap.getDisplayName() + "\"", MessageBoxButtons.YesNo);

            if (result == System.Windows.Forms.DialogResult.Yes)
            {
                System.IO.File.Delete(selectedMap.getMapPath());
                lvwMaps.Items.Remove(lvwMaps.SelectedItems[0]);
                if (!mf.IsDisposed)
                    mf.Close();
            }
        }

        private void lvwMaps_MouseClick(object sender, MouseEventArgs e)
        {
            if (e.Button == System.Windows.Forms.MouseButtons.Right)
            {
                //rightClickMenu.Show(mapPanel.PointToScreen(new Point(e.X, e.Y)));
                mapRightClick.Show(lvwMaps.PointToScreen(new Point(e.X, e.Y)));
            }
        }

        private void gameDataToolStripMenuItem_Click(object sender, EventArgs e)
        {

        }

        private void defaultCollisionToolStripMenuItem_Click(object sender, EventArgs e)
        {
           var colForm = new CollisionForm();
           colForm.Show();
           colForm.Focus();
        }

        private void macrosToolStripMenuItem_Click(object sender, EventArgs e)
        {
           var macroForm = new MacroForm();
           macroForm.Show();
           macroForm.Focus();
        }
    }

    public class ImageTreeViewArgs
    {
       public Func<String, bool> filter;
       public Action<TreeNode, String, String> directorySetter;
       public Action<TreeNode, String, String> nodeSetter;
       public TreeNodeCollection baseNodes;
       public ImageTreeViewArgs(TreeNodeCollection baseNodes)
       {
          this.baseNodes = baseNodes;
       }
       public ImageTreeViewArgs setFilter(Func<String, bool> filter)
       {
          this.filter = filter;
          return this;
       }
       public ImageTreeViewArgs onDirectory(Action<TreeNode, String, String> directorySetter)
       {
          this.directorySetter = directorySetter;
          return this;
       }
       public ImageTreeViewArgs onElement(Action<TreeNode, String, String> nodeSetter)
       {
          this.nodeSetter = nodeSetter;
          return this;
       }
    }

    public static class ImageTreeViewBuilder
    {
       static string stripPath(string path)
       {

          string mapName = path.Remove(0, path.LastIndexOf('\\') + 1);
          mapName = mapName.Remove(mapName.LastIndexOf('.'));

          return mapName;
       }

       static void addDrawNode(string folderName, Func<String, bool> filter, Action<TreeNode, String, String> nodeSetter, Action<TreeNode, String, String> directorySetter, TreeNodeCollection baseNodes)
       {
          addDrawNode(folderName, Application.StartupPath + "\\assets\\drawable\\" + folderName, filter, nodeSetter, directorySetter, baseNodes);
       }
       static void addDrawNode(string baseFolder, string folderName, Func<String, bool> filter, Action<TreeNode, String, String> nodeSetter, Action<TreeNode, String, String> directorySetter, TreeNodeCollection nodes)
       {
          int nodeCnt = nodes.Count;
          nodes.Add(folderName.Remove(0, folderName.LastIndexOf('\\') + 1));
          if (directorySetter != null)
          {
             directorySetter(nodes[nodeCnt], baseFolder, folderName);
          }
          foreach (var directory in System.IO.Directory.GetDirectories(folderName))
          {
             addDrawNode(baseFolder, directory, filter, nodeSetter, directorySetter, nodes[nodeCnt].Nodes);
          }

          int i = nodes[nodeCnt].Nodes.Count;
          foreach (var path in System.IO.Directory.GetFiles(folderName))
          {
             if (path.Substring(path.Length - 3) == "png" && (filter == null || filter(path)))
             {
                nodes[nodeCnt].Nodes.Add(stripPath(path));
                if (nodeSetter != null)
                {
                  nodeSetter(nodes[nodeCnt].Nodes[i++], baseFolder, path.Substring(Application.StartupPath.Length + 1));
                }
             }
          }
       }
       static void BuildImageTreeNode(Func<String, bool> filter, 
          Action<TreeNode, String, String> directorySetter,
          Action<TreeNode, String, String> nodeSetter, 
          TreeNodeCollection baseNodes)
       {
          addDrawNode("materials", filter, nodeSetter, directorySetter, baseNodes);
          addDrawNode("walls", filter, nodeSetter, directorySetter, baseNodes);
          addDrawNode("stairs", filter, nodeSetter, directorySetter, baseNodes);
          addDrawNode("objects", filter, nodeSetter, directorySetter, baseNodes);

       }
       public static void BuildImageTreeView(ImageTreeViewArgs args)
       {
          if (args == null) return;
          BuildImageTreeNode(args.filter, args.directorySetter, args.nodeSetter, args.baseNodes);
       }
    }

    public class TileBitmap
    {
       public TileBitmap(TileBitmap rhs)
       {
          this.x = rhs.x;
          this.y = rhs.y;
          this.bitmap = rhs.bitmap;
          this.layerOffset = rhs.layerOffset;
       }
       public TileBitmap(Bitmap bmp, int x,int y, int layerOffset)
       {
          this.x = x;
          this.y = y;
          this.bitmap = bmp;
          this.layerOffset = layerOffset;
       }
       public int layerOffset;
       public int x,y;
       public Bitmap bitmap;
    }

    public class TileInfo
    {
       public TileInfo(TileInfo rhs)
       {
         colLeft = rhs.colLeft; 
         colRight = rhs.colRight; 
         colTop  = rhs.colTop;
         colBottom = rhs.colBottom;
         bitmaps = new List<TileBitmap>();
         foreach (TileBitmap t in rhs.bitmaps)
         {
            bitmaps.Add(new TileBitmap(t));
         }
       }
       public TileInfo(Bitmap bmp, int x, int y)
       {
          colLeft = colRight =  colTop = colBottom = false;
          bitmaps = new List<TileBitmap>();
          bitmaps.Add(new TileBitmap(bmp, x,y,0));
       }
       public void setCollision(bool left, bool right, bool top, bool bot)
       {
          colLeft = left;
          colRight = right;
          colTop = top;
          colBottom = bot;
       }
       public bool colLeft, colRight, colTop, colBottom;
       public List<TileBitmap> bitmaps;
    }

    public class TileImage
    {
       public TileImage(TileImage rhs)
       {
          this.xPixels = rhs.xPixels;
          this.yPixels = rhs.yPixels;
          this.tiles = new List<TileInfo>();
          foreach (var t in rhs.tiles)
          {
             this.tiles.Add(new TileInfo(t));
          }
       }
       public TileImage(Bitmap bmp)
       {
          GraphicsUnit pixels = GraphicsUnit.Pixel;
          xPixels = (int)bmp.GetBounds(ref pixels).Width;
          yPixels = (int)bmp.GetBounds(ref pixels).Height;
          int xSize = (int)(xPixels / MapForm.tileSize);
          int ySize = (int)(yPixels / MapForm.tileSize);

          tiles = new List<TileInfo>();
          for (int j = 0; j < ySize; ++j)
          {
             for (int i = 0; i < xSize; ++i)
             {
                tiles.Add(new TileInfo(bmp, i, j));
             }
          }
       }
       public void setCollision(int x, int y, bool left, bool right, bool top, bool bot)
       {
          int xSize = (int)(xPixels / MapForm.tileSize);
          tiles[x + xSize * y].setCollision(left, right, top, bot);
       }
       public List<TileInfo> tiles;
       public int xPixels, yPixels; //as opposed to # of tiles

    }

    public static class Bitmaps
    {
       public static Dictionary<string, TileImage> bitmaps;
    }
}
