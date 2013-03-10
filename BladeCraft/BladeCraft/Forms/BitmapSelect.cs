using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace BladeCraft.Forms
{
    public partial class BitmapSelect : Form
    {

        private Dictionary<string, Bitmap> bitmaps;
        public String SelectedBitmap { get; private set; }


        public BitmapSelect(Dictionary<string, Bitmap> bitmaps)
        {
            InitializeComponent();

            this.bitmaps = bitmaps;
        }

        private void BitmapSelect_Load(object sender, EventArgs e)
        {
            //keep track of folder names created
            Dictionary<String, TreeNode> folderList = new Dictionary<string, TreeNode>();
            foreach (String path in bitmaps.Keys)
            {
                List<String> expandedPath = new List<string>(path.Split('\\'));
                if(expandedPath.Count > 1)
                {
                    String bmpName = expandedPath[expandedPath.Count - 1];
                    Bitmap bmp = bitmaps[path];

                    //remove bmpname from list
                    expandedPath.Remove(bmpName);

                    //loop through each path node, see if that node has been made and create or add to
                    String pathPlace = ""; //maintain where we are in the list
                    TreeNode currentNode = null;

                    foreach (String currentPathNode in expandedPath)
                    {
                        if (currentPathNode != expandedPath[0])
                            pathPlace += "\\";
                        pathPlace += currentPathNode;

                        if (folderList.ContainsKey(pathPlace))
                            currentNode = folderList[pathPlace];
                        else
                        {
                            TreeNode newNode = new TreeNode(currentPathNode);

                            if (currentNode == null)
                                tvwFiles.Nodes.Add(newNode);
                            else
                                currentNode.Nodes.Add(newNode);

                            folderList[pathPlace] = newNode;

                            currentNode = newNode;                                
                        }
                    }

                    TreeNode fileNode = new TreeNode(bmpName);
                    fileNode.Tag = path;
                    currentNode.Nodes.Add(fileNode);
                }
                else
                {
                    //add a node at the base level
                    String bmpName = expandedPath[0];
                    TreeNode baseNode = new TreeNode(bmpName);
                    baseNode.Tag = path;
                    tvwFiles.Nodes.Add(baseNode);
                }
            }
        }


        private void bmpPanel_Paint(object sender, PaintEventArgs e)
        {
            Graphics g = e.Graphics;

            if (SelectedBitmap != null)
                g.DrawImage(bitmaps[SelectedBitmap], new Point(0, 0));
        }

        private void tvwFiles_AfterSelect(object sender, TreeViewEventArgs e)
        {
            if (e.Node.Tag != null)
            {
                SelectedBitmap = (String)e.Node.Tag;
                bmpPanel.Invalidate();
            }
            
        }
    }
}
