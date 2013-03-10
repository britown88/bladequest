using System;
using System.Collections.Generic;
using System.Text;
using System.Drawing;
using System.Drawing.Imaging;

using System.Windows.Forms;
using System.IO;



namespace BladeCraft.Classes
{
    public class BQGameData
    {
        delegate void FileAction(String filepath);

        public Dictionary<string, Bitmap> Bitmaps { get; private set; }
        public List<Sprite> BattleSprites { get; private set; }
        public List<Sprite> EnemySprites { get; private set; }
        public List<Sprite> WorldSprites { get; private set; }

        private FileLineReader lineReader = new FileLineReader();

        private Dictionary<String, Dictionary<String, FileLineReader.LineAction>> lineFunctionDictionaries = new Dictionary<string, Dictionary<string, FileLineReader.LineAction>>();
        private Dictionary<String, FileLineReader.LineAction> lineFunctions;

        private Dictionary<String, FileLineReader.LineAction> spriteLineFunctions = new Dictionary<string, FileLineReader.LineAction>();
        private Dictionary<String, FileLineReader.LineAction> musicLineFunctions = new Dictionary<string, FileLineReader.LineAction>();
        private Dictionary<String, FileLineReader.LineAction> merchantLineFunctions = new Dictionary<string, FileLineReader.LineAction>();
        private Dictionary<String, FileLineReader.LineAction> itemLineFunctions = new Dictionary<string, FileLineReader.LineAction>();
        private Dictionary<String, FileLineReader.LineAction> enemyLineFunctions = new Dictionary<string, FileLineReader.LineAction>();
        private Dictionary<String, FileLineReader.LineAction> charLineFunctions = new Dictionary<string, FileLineReader.LineAction>();
        private Dictionary<String, FileLineReader.LineAction> encounterLineFunctions = new Dictionary<string, FileLineReader.LineAction>();
        private Dictionary<String, FileLineReader.LineAction> animLineFunctions = new Dictionary<string, FileLineReader.LineAction>();
        private Dictionary<String, FileLineReader.LineAction> abilityLineFunctions = new Dictionary<string, FileLineReader.LineAction>();

        public BQGameData()
        {
            Bitmaps = new Dictionary<string, Bitmap>();
            BattleSprites = new List<Sprite>();
            EnemySprites = new List<Sprite>();
            WorldSprites = new List<Sprite>();

            lineFunctionDictionaries["[abilities]"] = abilityLineFunctions;
            lineFunctionDictionaries["[battleanims]"] = animLineFunctions;
            lineFunctionDictionaries["[encounters]"] = encounterLineFunctions;
            lineFunctionDictionaries["[characters]"] = charLineFunctions;
            lineFunctionDictionaries["[enemies]"] = enemyLineFunctions;
            lineFunctionDictionaries["[items]"] = itemLineFunctions;
            lineFunctionDictionaries["[merchants]"] = merchantLineFunctions;
            lineFunctionDictionaries["[music]"] = musicLineFunctions;
            lineFunctionDictionaries["[sprites]"] = spriteLineFunctions;

            spriteLineFunctions["battlesprite"] = lfSpritesBattlesprite;
            spriteLineFunctions["enemysprite"] = lfSpritesEnemysprite;
            spriteLineFunctions["worldsprite"] = lfSpritesWorldsprite;

        }

        public void load()
        {
            loadFiles("\\assets\\drawable", fileActionBitmap);
            loadFiles("\\assets\\data", fileActionData);

            
        }

        private void fileActionBitmap(String filepath)
        {
            string bmpName = filepath.Replace(Application.StartupPath + "\\assets\\drawable\\", "").ToLower().Replace(".png", "");
            Bitmap bmp = new Bitmap(filepath);
            Bitmaps[bmpName] = bmp;
        }

        private void fileActionData(String filepath)
        {
            lineReader.readFile(filepath, loadFileLine);
        }

        private void loadFiles(string dir, FileAction actionFunction)
        {
            string[] files = Directory.GetFiles(Application.StartupPath + dir);
            string[] dirs = Directory.GetDirectories(Application.StartupPath + dir);

            foreach (string path in files)
                actionFunction(path);

            foreach (string path in dirs)
                loadFiles(path.Replace(Application.StartupPath, ""), actionFunction); 

        }

        private void loadFileLine(String item, String[] values)
        {
            if (item.Length > 0)
            {
                if (item[0] == '[')
                {
                    if (lineFunctionDictionaries.ContainsKey(item))
                        lineFunctions = lineFunctionDictionaries[item];
                }
                else
                    if (lineFunctions != null && lineFunctions.ContainsKey(item))
                        lineFunctions[item](item, values);
            }
            
                
        }

        private void lfSpritesWorldsprite(String item, String[] values)
        {
            String bitmap = values[0];
            String name = values[1];
            Point pos = new Point(Convert.ToInt32(values[2]), Convert.ToInt32(values[3]));

            WorldSprites.Add(new Sprite(bitmap, name, pos.X, pos.Y));

        }

        private void lfSpritesBattlesprite(String item, String[] values)
        {
            String name = values[0];
            Point pos = new Point(Convert.ToInt32(values[1]), Convert.ToInt32(values[2]));

            WorldSprites.Add(new Sprite(name, pos.X, pos.Y));
        }

        private void lfSpritesEnemysprite(String item, String[] values)
        {
            String bitmap = values[0];
            String name = values[1];
            int destSize = Convert.ToInt32(values[2]);
            int srcSize = Convert.ToInt32(values[3]);
            Point pos = new Point(Convert.ToInt32(values[4]), Convert.ToInt32(values[5]));

            WorldSprites.Add(new Sprite(bitmap, name, destSize, srcSize, pos.X, pos.Y));
        }

        
    }
}
