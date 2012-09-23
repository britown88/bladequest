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
    public partial class MapInfoForm : Form
    {
        private bool newMap = false;
        private BQMap map;

        public MapInfoForm()
        {
            InitializeComponent();
            newMap = true;
        }

        public MapInfoForm(BQMap objMap)
        {
            InitializeComponent();
            newMap = false;
            this.map = objMap;
        }

        public BQMap getMap() { return map; }

        private void MapInfoForm_Load(object sender, EventArgs e)
        {
            if (!newMap)
            {
                txtMapName.Text = map.getName();
                txtMapName.Enabled = false;
                numX.Value = map.width();
                numY.Value = map.height();
                txtDisplayName.Text = map.getDisplayName();
                txtBGM.Text = map.getBGM();
            }

        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            this.Close();
            //Close();
        }

        private void btnSave_Click(object sender, EventArgs e)
        {
            if(txtMapName.Text == "")
                MessageBox.Show("Must include Map Name!");
            else
            {
                if (newMap)
                {
                    map = new BQMap(
                        txtMapName.Text,
                        (int)numX.Value,
                        (int)numY.Value,
                        txtDisplayName.Text,
                        txtBGM.Text,
                        chkSave.Checked);

                }                    
                else
                {
                    map.setName(txtMapName.Text);
                    map.updateSize((int)numX.Value, (int)numY.Value);
                    map.setDisplayName(txtDisplayName.Text);
                    map.setBGM(txtBGM.Text);
                   // map.setSize((int)numX.Value, (int)numY.Value);
                }

                Close();
            }

            

            
        }

        

    }
}
