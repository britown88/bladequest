using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;
using System.Drawing.Imaging;

using System.Windows.Forms;
using System.IO;



namespace BladeCraft.Classes
{
    class BQGameData
    {
        private Dictionary<string, Bitmap> bitmaps;

        public BQGameData()
        {
            bitmaps = new Dictionary<string, Bitmap>();

        }

        public void load()
        {
            loadBitmaps();
        }

        public Dictionary<string, Bitmap> GetBitmaps() { return bitmaps; }

        private void loadBitmaps(string dir = "\\assets\\drawable")
        {
            string[] bmpFiles = Directory.GetFiles(Application.StartupPath + dir);
            string[] dirs = Directory.GetDirectories(Application.StartupPath + dir);

            foreach (string path in bmpFiles)
            {
                string bmpName = path.Replace(Application.StartupPath + "\\assets\\drawable\\", "").Replace(".png", "");
                bitmaps.Add(bmpName, new Bitmap(path));

            }

            foreach (string path in dirs)
                loadBitmaps(path.Replace(Application.StartupPath, "")); 

        }
        
    }
}
