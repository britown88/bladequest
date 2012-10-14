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
        private GameDataForm dataForm;

        public main()
        {
            InitializeComponent();
            //mapForm = new MapForm();
            //map = new BQMap("newmap", 30, 20, "New Map", "", false);
            
        }

        private void main_Load(object sender, EventArgs e)
        {

            readMaps();

            //mapForm.MdiParent = this;
            //mapForm.Dock = DockStyle.Fill;
            //mapForm.StartPosition = FormStartPosition.CenterScreen;
            //mapForm.Show();

            //mapForm.updateMap();
        }

        public void readMaps()
        {
            string[] mapFiles = System.IO.Directory.GetFiles(Application.StartupPath + "\\assets\\maps");


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
            if (dataForm == null || dataForm.IsDisposed)
            {
                dataForm = new GameDataForm();
                dataForm.MdiParent = this;
            }
            dataForm.Show();
            dataForm.Focus();
        }


    }
}
