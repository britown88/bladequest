using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

using BladeCraft.Forms;

using System.Text.RegularExpressions;

namespace BladeCraft.Classes
{
   public class BQMap
   {
      private enum LoadTypes
      {
         Header,
         Object,
         Unknown
      }

      private int sizeX, sizeY;
      private string name, path, tileset, displayName, BGM;
      private bool save;

      private Tile[] bgtileList;
      private Tile[] fgtileList;

      public List<EncounterZone> zones;
      public List<GameObject> objects;

      public string Header { get; set;}
      private LoadTypes loadType;

      private ObjectHeader headerForm;

      public void openHeaderForm(System.Windows.Forms.Form parent)
      {
         if (headerForm == null || headerForm.IsDisposed)
         {
            headerForm = new ObjectHeader(this);
            headerForm.MdiParent = parent;
            headerForm.Show();
         }
         else
         {
            headerForm.BringToFront();
         }
      }


      public BQMap(string name, int x, int y, string displayName, string BGM, bool save)
      {
         this.sizeX = x;
         this.sizeY = y;
         this.name = name;
         this.displayName = displayName;
         this.save = save;
         this.BGM = BGM;
         bgtileList = new Tile[x * y];
         fgtileList = new Tile[x * y];

         zones = new List<EncounterZone>();
         objects = new List<GameObject>();
      }

      public BQMap(string filename)
      {
         StreamReader reader = new StreamReader(filename);
         string line, item;
         string[] values= new string[20];
         zones = new List<EncounterZone>();
         objects = new List<GameObject>();
         int i, index;

         this.path = filename;

         name = filename.Remove(filename.Length - 4);
         name = name.Substring(name.LastIndexOf('\\') + 1);

         do
         {
            i = 0;
            index = 0;
            line = reader.ReadLine();
            item = readWord(line, index);
            index += item.Length;

            //reset array
            for (int j = 0; j < 20; ++j)
               values[j] = "";

            do
            {
               bool usesQuotes = index+1 < line.Length ? line[index+1] == '\"' : false;
               values[i] = readWord(line, index);
               index += values[i].Length;
               values[i] = values[i].TrimStart(' ');
               if (usesQuotes)
                  index += 2;
            } while (values[i++] != "");

            loadLine(item, values);
         }
         while (reader.Peek() != -1);
         reader.Close();

         readObjectFile();
      }

      private void readObjectFile()
      {
         StreamReader reader;
         try {
           reader = new StreamReader("assets\\maps\\omaps\\" + name + ".omap");
         }catch (FileNotFoundException e) { return; }
         
         string line;
         loadType = LoadTypes.Unknown;
         Header = "";

         GameObject workingObj = null;

         do
         {
            line = reader.ReadLine();

            line = Regex.Replace(line, "\\$[0-9]*[x][0-9]*\\$", "$");

            if (line.StartsWith("#!HEAD"))
               loadType = LoadTypes.Header;
            else if (line.StartsWith("#!OBJ"))
            {
               string sub;
               string x = (sub = line.Substring(line.IndexOf(' ') + 1)).Substring(0, sub.IndexOf(' '));
               string y = line.Substring(line.IndexOf(x) + x.Length + 1);

               if (workingObj != null)
                  objects.Add(workingObj);

               loadType = LoadTypes.Object;

               workingObj = new GameObject(Convert.ToInt32(x), Convert.ToInt32(y));
            }
            else
            {
               switch (loadType)
               {
                  case LoadTypes.Header:
                     Header += line + "\r\n";
                     break;
                  case LoadTypes.Object:
                     workingObj.Script += line + "\r\n";
                     break;
               };
            }


         }
         while (reader.Peek() != -1);

         if (workingObj != null)
                  objects.Add(workingObj);

         reader.Close();

      }

      public List<GameObject> getObjects(){ return objects; }
      public void addObject(GameObject obj) { objects.Add(obj); }
      public void deleteObject(int X, int Y)
      {
         foreach (var obj in objects)
            if (obj.X == X && obj.Y == Y)
            {
               objects.Remove(obj);
               return;
            }
      }
      public GameObject getObjectAt(int X, int Y)
      {
         foreach (var obj in objects)
            if (obj.X == X && obj.Y == Y)
               return obj;

         return null;

      }

      public string getMapPath() { return path; }      

      private EncounterZone loadzone = null;

      private void loadLine(string item, string[] values)
      {
         if (item == "size")
         {
            sizeX = Convert.ToInt32(values[0]);
            sizeY = Convert.ToInt32(values[1]);

            bgtileList = new Tile[sizeX * sizeY];
            fgtileList = new Tile[sizeX * sizeY];
            return;
         }
         if (item == "tileset")
         {
            tileset = values[0];
            return;
         }
         if (item == "displayname")
         {
            displayName = values[0];
            return;
         }
         if (item == "BGM")
         {
            BGM = values[0];
            return;
         } 
         if (item == "t")
         {
            bool fg = values[4] == "a";
            Tile t = 
               new Tile(
               Convert.ToInt32(values[0]), 
               Convert.ToInt32(values[1]), 
               Convert.ToInt32(values[2]), 
               Convert.ToInt32(values[3]), 
               values[4]);
            t.collSides[0] = values[5] == "1";
            t.collSides[1] = values[6] == "1";
            t.collSides[2] = values[7] == "1";
            t.collSides[3] = values[8] == "1";

            if (values[9] == "t")
               t.animate(Convert.ToInt32(values[10]), Convert.ToInt32(values[11]));

            addTile(t, fg);

            return;
         }
         if (item == "zone")
         {
            loadzone = new EncounterZone();
            loadzone.zone = new System.Drawing.Rectangle(
               Convert.ToInt32(values[0]),
               Convert.ToInt32(values[1]),
               Convert.ToInt32(values[2]),
               Convert.ToInt32(values[3]));

            loadzone.encounterRate = Convert.ToInt32(values[4]);
            return;
         }
         if (item == "encounter")
         {
            loadzone.encounters.Add(values[0]);
            return;
         }
         if (item == "endzone")
         {
            zones.Add(loadzone);
            return;
         }


      }

      private string readWord(string line, int index)
      {
         string s = "";
         bool openString = false;

         do
         {
            if (index < line.Length)
            {
               if (line[index] == '\"')
               {
                  if (openString)
                  {
                     index++;
                     break;
                  }
                     
                  else
                     openString = true;
               }
               else
                  s += line[index];

               index++;
            }            
               
         } while (index < line.Length && ((line[index] != ' ' && line[index] != '\0') ||openString));


         return s;
      }


      public void setTileset(string ts)
      {
         tileset = ts;
      }

      public void updateSize(int x, int y)
      {
         Tile[] newbgTileList = new Tile[x * y];
         Tile[] newfgTileList = new Tile[x * y]; 

         for(int i = 0; i < x; ++i)
            for (int j = 0; j < y; ++j)
            {
               if (i < sizeX && j < sizeY)
               {
                  newbgTileList[j * x + i] = bgtileList[j * sizeX + i];
                  newfgTileList[j * x + i] = fgtileList[j * sizeX + i];

               }
            }

         sizeX = x;
         sizeY = y;
         bgtileList = newbgTileList;
         fgtileList = newfgTileList;

      }

      public void setSize(int x, int y)
      {
         sizeX = x;
         sizeY = y;
         bgtileList = new Tile[x * y];
         fgtileList = new Tile[x * y];
      }

      public void addTile(Tile tile, bool foreground)
      {
         int index = tile.y * sizeX + tile.x;

         if(tile.x >= 0 && tile.x < sizeX && tile.y >= 0 && tile.y < sizeY)
            if(foreground)
               fgtileList[index] = tile;
            else
               bgtileList[index] = tile;
      }

      public void animateTile(int x, int y, int bmpX, int bmpY, bool foreground)
      {
         int index = y * sizeX + x;
         if (x >= 0 && x < sizeX && y >= 0 && y < sizeY)
            if (foreground && fgtileList[index] != null)
               fgtileList[index].animate(bmpX, bmpY);
            else if(bgtileList[index] != null)
               bgtileList[index].animate(bmpX, bmpY);
      }

      public void deleteTile(int x, int y, bool foreground)
      {
         int index = y * sizeX + x;

         if (x >= 0 && x < sizeX && y >= 0 && y < sizeY)
            if (foreground)
               fgtileList[index] = null;
            else
               bgtileList[index] = null;
      }

      public Tile getTile(int x, int y, bool foreground) 
      { 
         if (x >= 0 && x < sizeX && y >= 0 && y < sizeY)
            return foreground ? fgtileList[y * sizeX + x] : bgtileList[y * sizeX + x]; 
         else 
            return null;
      }

      private bool[] fillChecked;

      public void fill(int x, int y, int bmpX, int bmpY)
      {
         Tile fillingTile = bgtileList[y * sizeX + x];
         if (fillingTile != null)
            fillingTile = new Tile(fillingTile);

         fillChecked = new bool[sizeX * sizeY];

         fillStep(fillingTile, x, y, bmpX, bmpY);
      }

      private void fillStep(Tile fillingTile, int x, int y, int bmpX, int bmpY)
      {
         Tile t = bgtileList[y * sizeX + x];
         bool fill = false;

         fillChecked[y * sizeX + x] = true;

         if (fillingTile == null && t == null)
         {
            bgtileList[y * sizeX + x] = new Tile(x, y, bmpX, bmpY, "below");
            t = bgtileList[y * sizeX + x];
            fill = true;
         }
         else if(fillingTile !=  null && t != null && 
            t.bmpX == fillingTile.bmpX && t.bmpY == fillingTile.bmpY)
         {
            t.bmpX = bmpX;
            t.bmpY = bmpY;
            //bgtileList[y * sizeX + x] = t;
            fill = true;
         }

         if(fill)
         {
            if (x + 1 < sizeX && !fillChecked[y * sizeX + x+1])
               fillStep(fillingTile, x + 1, y, bmpX, bmpY);
            if (x - 1 >= 0 && !fillChecked[y * sizeX + x-1])
               fillStep(fillingTile, x - 1, y, bmpX, bmpY);
            if (y + 1 < sizeY && !fillChecked[(y+1) * sizeX + x])
               fillStep(fillingTile, x, y + 1, bmpX, bmpY);
            if (y - 1 >= 0 && !fillChecked[(y-1) * sizeX + x])
               fillStep(fillingTile, x, y - 1, bmpX, bmpY);
         }

         
      }

      public int width() { return sizeX; }
      public int height() { return sizeY; }
      public string getName() { return name; }
      public string getTileset() { return tileset; }
      public void setName(string name) { this.name = name; }
      public void setDisplayName(String st) { displayName = st; }
      public String getDisplayName() { return displayName; }
      public void setBGM(String st) { BGM = st; }
      public String getBGM() { return BGM; }

      public void writeObjects()
      {
         if(objects.Count > 0)
         {
            StreamWriter writer = new StreamWriter("assets\\maps\\omaps\\" + name + ".omap");
            writer.WriteLine("#!HEAD");
            writer.WriteLine(Header);

            foreach(GameObject obj in objects)
               obj.write(writer);

            writer.Close();
         }
         
      }

      public void write()
      {
         StreamWriter writer = new StreamWriter(path == null ? "assets\\maps\\" + name + ".map" : path);
         writer.WriteLine("[header]");
         writer.WriteLine("size " + sizeX + " " + sizeY);
         writer.WriteLine("tileset " + tileset);
         writer.WriteLine("displayname \"" + displayName +"\"");
         writer.WriteLine("BGM " + BGM);
         

         writer.WriteLine("[tiles]");
         foreach (Tile t in bgtileList)
            if(t != null)
               if(t.animated)
                  writer.WriteLine("t " + t.x + " " + t.y + " " + t.bmpX + " " + t.bmpY + " b " +
                  Convert.ToInt32(t.collSides[0]).ToString() + " " + Convert.ToInt32(t.collSides[1]).ToString() + " " +
                  Convert.ToInt32(t.collSides[2]).ToString() + " " + Convert.ToInt32(t.collSides[3]).ToString() + " " + 
                  "t " + t.animBmpX + " " + t.animBmpY);
               else
                  writer.WriteLine("t " + t.x + " " + t.y + " " + t.bmpX + " " + t.bmpY + " b " +
                  Convert.ToInt32(t.collSides[0]).ToString() + " " + Convert.ToInt32(t.collSides[1]).ToString() + " " +
                  Convert.ToInt32(t.collSides[2]).ToString() + " " + Convert.ToInt32(t.collSides[3]).ToString() + " ");

               
         foreach (Tile t in fgtileList)
            if (t != null)
               if(t.animated)
                  writer.WriteLine("t " + t.x + " " + t.y + " " + t.bmpX + " " + t.bmpY + " a " +
                  Convert.ToInt32(t.collSides[0]).ToString() + " " + Convert.ToInt32(t.collSides[1]).ToString() + " " +
                  Convert.ToInt32(t.collSides[2]).ToString() + " " + Convert.ToInt32(t.collSides[3]).ToString() + " " +
                  "t " + t.animBmpX + " " + t.animBmpY);
               else
                  writer.WriteLine("t " + t.x + " " + t.y + " " + t.bmpX + " " + t.bmpY + " a " +
                  Convert.ToInt32(t.collSides[0]).ToString() + " " + Convert.ToInt32(t.collSides[1]).ToString() + " " +
                  Convert.ToInt32(t.collSides[2]).ToString() + " " + Convert.ToInt32(t.collSides[3]).ToString() + " ");
               

         writer.WriteLine("[encounters]");
         foreach (EncounterZone ez in zones)
            ez.write(writer);

         writer.Close();

         writeObjects();

      }
   }
}
