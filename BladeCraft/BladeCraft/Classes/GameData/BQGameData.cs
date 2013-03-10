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
        public Dictionary<string, Bitmap> Bitmaps { get; private set; }
        public List<Sprite> BattleSprites { get; private set; }
        public List<Sprite> EnemySprites { get; private set; }
        public List<Sprite> WorldSprites { get; private set; }

        public BQGameData()
        {
            Bitmaps = new Dictionary<string, Bitmap>();
            BattleSprites = new List<Sprite>();
            EnemySprites = new List<Sprite>();
            WorldSprites = new List<Sprite>();

        }

        public void load()
        {
            loadBitmaps();

            
        }

        private void loadBitmaps(string dir = "\\assets\\drawable")
        {
            string[] bmpFiles = Directory.GetFiles(Application.StartupPath + dir);
            string[] dirs = Directory.GetDirectories(Application.StartupPath + dir);

            foreach (string path in bmpFiles)
            {
                string bmpName = path.Replace(Application.StartupPath + "\\assets\\drawable\\", "").ToLower().Replace(".png", "");
                Bitmaps.Add(bmpName, new Bitmap(path));

            }

            foreach (string path in dirs)
                loadBitmaps(path.Replace(Application.StartupPath, "")); 

        }
        
    }
}
