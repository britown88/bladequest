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


        public BitmapSelect(Dictionary<string, Bitmap> bitmaps)
        {
            InitializeComponent();

            this.bitmaps = bitmaps;
        }

        private void BitmapSelect_Load(object sender, EventArgs e)
        {

            //lvwFiles.Items.Add(new ListViewItem(

        }
    }
}
