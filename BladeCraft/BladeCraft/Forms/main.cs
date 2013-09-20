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
        public string StartupPath;

        public main()
        {
            InitializeComponent();
            //mapForm = new MapForm();
            //map = new BQMap("newmap", 30, 20, "New Map", "", false);

            StartupPath = Path.sanitize(Application.StartupPath);
 
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

        static IEnumerable<TileDataElement> getNodes(XmlTextReader reader)
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
           var bmp = new TileImage(path, Bitmaps.loadRawBitmap(path));
           loadTileImageCollision(bmp, xmlReader);
           return bmp;
        }

        private void loadTileImageCollision(TileImage image, XmlTextReader xmlReader)
        {
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

              image.setCollision(x, y, leftCol, rightCol, topCol, bottomCol);
           }
        }


        public static TileImage loadMacroImage(string path)
        {
           TileImage output = null;
           
           int xSize = 0;
           List<TileBitmap> bmps = new List<TileBitmap>();

           using (var reader = new XmlTextReader(path))
           {
              foreach (var node in getNodes(reader))
              {
                 if (node.elementType == "Macro") 
                 {
                    xSize = Convert.ToInt32(node.getAttribute("X"));
                    int ySize = Convert.ToInt32(node.getAttribute("Y"));
                    output = new TileImage(xSize, ySize);
                 }
                 else
                 {
                    int x = Convert.ToInt32(node.getAttribute("X"));
                    int y = Convert.ToInt32(node.getAttribute("Y"));
                    int bmpx = Convert.ToInt32(node.getAttribute("BmpX"));
                    int bmpy = Convert.ToInt32(node.getAttribute("BmpY"));
                    string imagepath = node.getAttribute("Image");
                    int offset = Convert.ToInt32(node.getAttribute("Offset"));
                    int frame = 0;
                    try
                    {
                        frame = Convert.ToInt32(node.getAttribute("Frame"));
                    }
                    catch (System.Exception)
                    {
                     	
                    }                    

                    var tileData = output.tiles[y * xSize + x];
                    tileData.bitmaps.Add(new TileBitmap(Bitmaps.loadRawBitmap(imagepath), bmpx, bmpy, imagepath, offset, frame));
                 }
              }
           }
           return output;
        }


        private void loadBitmapFolder(string folder)
        {
           loadBitmapFolderRecursive(StartupPath + "/assets/drawable/" + folder);
        }
        private bool isNumeric(string path)
        {
            int num;
            return int.TryParse(ImageTreeViewBuilder.stripPath(path),  out num);
        }
        private void loadBitmapFolderRecursive(string folder)
        {
           foreach (var directory in System.IO.Directory.GetDirectories(folder))
           {
              loadBitmapFolderRecursive(Path.sanitize(directory));
           }
           bool animated = true;
           int count = 0;
           List<string> animPaths = new List<string>();
           foreach (var p in System.IO.Directory.GetFiles(folder))
           {
              var path = Path.sanitize(p);
              var ext = path.Substring(path.Length - 3);
              if (ext == "png")
              {
                 var relPath = path.Substring(StartupPath.Length + 1);
                 animPaths.Add(relPath);
                 ++count;
                 if (!isNumeric(relPath)) animated = false;
                 try
                 {
                    using (var reader = new XmlTextReader(path.Substring(0, path.Length - 3) + "dat"))
                    {
                       Bitmaps.bitmaps.Add(relPath, readTileImageData(relPath, reader));
                    }
                 }
                 catch (System.Exception )
                 {
                    //file not found LOL DICKS
                    Bitmaps.bitmaps.Add(relPath, new TileImage(relPath, Bitmaps.loadRawBitmap(path)));
                 }
              }
              else if (ext == "mif")
              {
                 animated = false;
                 var image = loadMacroImage(path);
                 try
                 {
                    using (var reader = new XmlTextReader(path.Substring(0, path.Length - 3) + "dat"))
                    {
                       loadTileImageCollision(image, reader);
                    }
                 }
                 catch (System.Exception )
                 {

                 }
                 Bitmaps.bitmaps.Add(path.Substring(StartupPath.Length + 1), image);
              }
           }
           if (animated && count > 0)
           {
               //generate animation from this folder!
               animPaths.Sort();
               var relPath = folder.Substring(StartupPath.Length + 1);
               if (count == 4 || count == 2)
               {
                   Bitmaps.bitmaps.Add(relPath, new TileImage(animPaths));
               }
           }
        }

        private void loadBitmaps()
        {
            Bitmaps.bitmaps = new Dictionary<string, TileImage>();
            Bitmaps.rawBitmaps = new Dictionary<string, Bitmap>();
            loadBitmapFolder("materials");
            loadBitmapFolder("pipes");
            loadBitmapFolder("objects");
            loadBitmapFolder("roofs");
            loadBitmapFolder("shadows");
            loadBitmapFolder("stairs");
            loadBitmapFolder("walls");
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
            string[] mapFiles = System.IO.Directory.GetFiles(StartupPath + "/bcfiles");


            foreach (String p in mapFiles)
            {
                String path = Path.sanitize(p);
                MapForm mf = new MapForm(new BQMap(path));
                mf.MdiParent = this;

                string mapName = path.Remove(0, path.LastIndexOf('/') + 1);
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
    public interface ImageHierarchyStrategy
    {
        void onNode(TreeNode node, string baseFolder, string relativePath);
        void onDirectory(TreeNode node, string baseFolder, string relativePath);
        bool filter(string pathName);
        void onAnimatedDirectory(TreeNode node, string baseFolder, string relativePath); //set after all are read.
    }

    class ElementHierarchyStrategy : ImageHierarchyStrategy
    {
        public ElementHierarchyStrategy(Action<TreeNode, string, string > action)
        {
            this.action = action;
        }
        Action<TreeNode, string , string > action;
        public void  onNode(TreeNode node, string baseFolder, string relativePath)
        {
 	        action(node, baseFolder, relativePath);
        }

        public void  onDirectory(TreeNode node, string baseFolder, string relativePath)
        {
        }

        public bool  filter(string pathName)
        {
            return true;
        }

        public void  onAnimatedDirectory(TreeNode node, string baseFolder, string relativePath)
        {
        }
    }

    public static class ImageTreeViewBuilder
    {
       public static string stripPath(string path)
       {

          string mapName = path.Remove(0, path.LastIndexOf('/') + 1);
          mapName = mapName.Remove(mapName.LastIndexOf('.'));

          return mapName;
       }

       static void addDrawNode(string folderName, ImageHierarchyStrategy strategy, TreeNodeCollection baseNodes)
       {
           addDrawNode(folderName, Path.sanitize(Application.StartupPath) + "/assets/drawable/" + folderName, strategy, baseNodes);
       }
       static void addDrawNode(string baseFolder, string folderName, ImageHierarchyStrategy strategy, TreeNodeCollection nodes)
       {
          int nodeCnt = nodes.Count;
          nodes.Add(folderName.Remove(0, folderName.LastIndexOf('/') + 1));
          strategy.onDirectory(nodes[nodeCnt], baseFolder, folderName);
          foreach (var directory in System.IO.Directory.GetDirectories(folderName))
          {
             addDrawNode(baseFolder, Path.sanitize(directory), strategy, nodes[nodeCnt].Nodes);
          }

          bool animatedDirectory = true;
          int imageCount = 0;
          int i = nodes[nodeCnt].Nodes.Count;
          foreach (var p in System.IO.Directory.GetFiles(folderName))
          {
             string path = Path.sanitize(p);
             string ext = path.Substring(path.Length - 3) ;
             if ((ext == "png" || ext == "mif") && (strategy.filter(path)))
             {
                ++imageCount;
                string strippedPath = stripPath(path);
                int num;
                bool isNumber = int.TryParse(strippedPath, out num);
                if (!isNumber) animatedDirectory = false;
                nodes[nodeCnt].Nodes.Add(strippedPath);
                strategy.onNode(nodes[nodeCnt].Nodes[i++], baseFolder, path.Substring(Application.StartupPath.Length + 1));
             }
          }
          if (animatedDirectory && imageCount > 1)
          {
              strategy.onAnimatedDirectory(nodes[nodeCnt], baseFolder, folderName.Substring(Application.StartupPath.Length + 1));
          }
       }
       public static void BuildImageTreeView(ImageHierarchyStrategy strategy, 
          TreeNodeCollection baseNodes)
       {
           addDrawNode("materials", strategy, baseNodes);
           addDrawNode("objects", strategy, baseNodes);
           addDrawNode("pipes", strategy, baseNodes);
           addDrawNode("roofs", strategy, baseNodes);
           addDrawNode("shadows", strategy, baseNodes);
           addDrawNode("stairs", strategy, baseNodes);
           addDrawNode("walls", strategy, baseNodes);
       }
    }

    public class TileBitmap
    {
       public TileBitmap(TileBitmap rhs)
       {
          this.x = rhs.x;
          this.y = rhs.y;
          this.bitmap = rhs.bitmap;
          this.bitmapPath = rhs.bitmapPath;
          this.layerOffset = rhs.layerOffset;
          this.frame = rhs.frame;
       }
       public TileBitmap(Bitmap bmp, int x,int y, string bitmapPath, int layerOffset, int frame)
       {
          this.x = x;
          this.y = y;
          this.bitmap = bmp;
          this.layerOffset = layerOffset;
          this.bitmapPath = bitmapPath;
          this.frame = frame;
       }
       public int frame;
       public int layerOffset;
       public int x,y;
       public string bitmapPath;
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
       public TileInfo(string bitmapPath, Bitmap bmp, int x, int y)
       {
          colLeft = colRight =  colTop = colBottom = false;
          bitmaps = new List<TileBitmap>();
          bitmaps.Add(new TileBitmap(bmp, x, y, bitmapPath,0, 0));
       }
       public TileInfo()
       {
          colLeft = colRight = colTop = colBottom = false;
          bitmaps = new List<TileBitmap>();
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
       public TileImage(int xSize, int ySize) 
       {
          xPixels = xSize * MapForm.tileSize;
          yPixels = ySize * MapForm.tileSize;

          tiles = new List<TileInfo>();

          //got to add tiles manually from here

          for (int j = 0; j < ySize; ++j)
          {
             for (int i = 0; i < xSize; ++i)
             {
                tiles.Add(new TileInfo());
             }
          }
       }

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
       void setSizeByBmp(Bitmap bmp)
       {
           GraphicsUnit pixels = GraphicsUnit.Pixel;
           xPixels = (int)bmp.GetBounds(ref pixels).Width;
           yPixels = (int)bmp.GetBounds(ref pixels).Height;
       }
       public TileImage(string bitmapPath, Bitmap bmp)
       {
          setSizeByBmp(bmp);
          int xSize = (int)(xPixels / MapForm.tileSize);
          int ySize = (int)(yPixels / MapForm.tileSize);
          tiles = new List<TileInfo>();
          for (int j = 0; j < ySize; ++j)
          {
             for (int i = 0; i < xSize; ++i)
             {
                tiles.Add(new TileInfo(bitmapPath, bmp, i, j));
             }
          }
       }
       public TileImage(List<string> bmpPaths)
       {
           int step = 1;
           if (bmpPaths.Count == 2) step = 2;
           setSizeByBmp(Bitmaps.loadRawBitmap(bmpPaths[0]));
           int xSize = (int)(xPixels / MapForm.tileSize);
           int ySize = (int)(yPixels / MapForm.tileSize);
           tiles = new List<TileInfo>();
           for (int j = 0; j < ySize; ++j)
           {
               for (int i = 0; i < xSize; ++i)
               {
                   tiles.Add(new TileInfo());
                   int frame = 0;
                   foreach (var path in bmpPaths)
                   {
                       tiles[i + j * xSize].bitmaps.Add(new TileBitmap(Bitmaps.loadRawBitmap(path), i, j, path, 0, frame));
                       frame += step;
                   }
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
       public static Dictionary<string, Bitmap> rawBitmaps;
       public static Bitmap loadRawBitmap(string path)
       {
           Bitmap outBitmap;
           if (rawBitmaps.TryGetValue(path, out outBitmap))
           {
               return outBitmap;
           }
           outBitmap = new Bitmap(path);
           rawBitmaps.Add(path, outBitmap);
           return outBitmap;
       }
    }
}
