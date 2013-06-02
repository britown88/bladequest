using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Xml;


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
         this.path = "bcfiles\\" + name + ".xml";
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
         string[] values= new string[20];
         zones = new List<EncounterZone>();
         objects = new List<GameObject>();

         this.path = filename;

         name = filename.Remove(filename.Length - 4);
         name = name.Substring(name.LastIndexOf('\\') + 1);

         readXML(filename);
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

      public Tile addTile(Tile tile, bool foreground)
      {
         int index = tile.y * sizeX + tile.x;

         if(tile.x >= 0 && tile.x < sizeX && tile.y >= 0 && tile.y < sizeY)
            if(foreground)
               fgtileList[index] = tile;
            else
               bgtileList[index] = tile;

         return tile;
      }

      public Tile animateTile(int x, int y, int bmpX, int bmpY, bool foreground)
      {
         int index = y * sizeX + x;
         if (x >= 0 && x < sizeX && y >= 0 && y < sizeY)
            if (foreground && fgtileList[index] != null)
            {
               fgtileList[index].animate(bmpX, bmpY);
               return fgtileList[index];
            }
            else if (bgtileList[index] != null)
            {
               bgtileList[index].animate(bmpX, bmpY);
               return bgtileList[index];
            }

         return null;
               
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

      private void writeTile(Tile t, XmlTextWriter xwriter, string layer)
      {
         xwriter.WriteStartElement("Tile");

         xwriter.WriteStartElement("WorldPosition");
         xwriter.WriteAttributeString("X", t.x.ToString());
         xwriter.WriteAttributeString("Y", t.y.ToString());
         xwriter.WriteEndElement();

         xwriter.WriteStartElement("BitmapCoordinates");
         xwriter.WriteAttributeString("X", t.bmpX.ToString());
         xwriter.WriteAttributeString("Y", t.bmpY.ToString());
         xwriter.WriteEndElement();

         xwriter.WriteElementString("Layer", layer);

         xwriter.WriteStartElement("CollisionData");
         xwriter.WriteAttributeString("Left", t.collSides[0].ToString());
         xwriter.WriteAttributeString("Top", t.collSides[1].ToString());
         xwriter.WriteAttributeString("Right", t.collSides[2].ToString());
         xwriter.WriteAttributeString("Bottom", t.collSides[3].ToString());
         xwriter.WriteEndElement();

         if (t.animated)
         {
            xwriter.WriteStartElement("AnimatedBitmapCoordinates");
            xwriter.WriteAttributeString("X", t.animBmpX.ToString());
            xwriter.WriteAttributeString("Y", t.animBmpY.ToString());
            xwriter.WriteEndElement();
         }

         if (t.IsMaterial())
         {
            xwriter.WriteStartElement("MaterialCoordinates");
            xwriter.WriteAttributeString("X", t.matX.ToString());
            xwriter.WriteAttributeString("Y", t.matY.ToString());
            xwriter.WriteEndElement();
         }


         xwriter.WriteEndElement();
      }
      
      private void readXML(string path)
      {
         XmlTextReader r = new XmlTextReader(path);
         Tile newTile = new Tile();
         EncounterZone newZone = new EncounterZone();
         string layer = "";

         while (r.Read())
         {
            switch (r.NodeType)
            {
               case XmlNodeType.Element:
                  switch (r.LocalName)
                  {
                     case "Map":
                        BGM = r.GetAttribute("BGM");
                        displayName = r.GetAttribute("DisplayName");
                        tileset = r.GetAttribute("TileSet");

                        sizeX = Convert.ToInt32(r.GetAttribute("Width"));
                        sizeY = Convert.ToInt32(r.GetAttribute("Height"));

                        bgtileList = new Tile[sizeX * sizeY];
                        fgtileList = new Tile[sizeX * sizeY];
                        break;
                     case "Tile":
                        newTile = new Tile();
                        break;
                     case "WorldPosition":
                        newTile.x = Convert.ToInt32(r.GetAttribute("X"));
                        newTile.y = Convert.ToInt32(r.GetAttribute("Y"));
                        break;
                     case "BitmapCoordinates":
                        newTile.bmpX = Convert.ToInt32(r.GetAttribute("X"));
                        newTile.bmpY = Convert.ToInt32(r.GetAttribute("Y"));
                        break;
                     case "Layer":
                        layer = r.ReadString();
                        break;
                     case "CollisionData":
                        newTile.collSides[0] = Convert.ToBoolean(r.GetAttribute("Left"));
                        newTile.collSides[1] = Convert.ToBoolean(r.GetAttribute("Top"));
                        newTile.collSides[2] = Convert.ToBoolean(r.GetAttribute("Right"));
                        newTile.collSides[3] = Convert.ToBoolean(r.GetAttribute("Down"));
                        break;
                     case "AnimatedBitmapCoordinates":
                        newTile.animate(Convert.ToInt32(r.GetAttribute("X")), Convert.ToInt32(r.GetAttribute("Y")));
                        break;
                     case "MaterialCoordinates":
                        newTile.addToMaterial(Convert.ToInt32(r.GetAttribute("X")), Convert.ToInt32(r.GetAttribute("Y")));
                        break;
                     case "EncounterZone":
                        newZone = new EncounterZone();
                        newZone.zone.Width = Convert.ToInt32(r.GetAttribute("Width"));
                        newZone.zone.Height = Convert.ToInt32(r.GetAttribute("Height"));
                        newZone.zone.X = Convert.ToInt32(r.GetAttribute("X"));
                        newZone.zone.Y = Convert.ToInt32(r.GetAttribute("Y"));
                        newZone.encounterRate = (int)Convert.ToDecimal(r.GetAttribute("EncounterRate"));
                        break;
                     case "Encounter":
                        newZone.encounters.Add(r.ReadString());
                        break;
                     

                  }
                  break;
               case XmlNodeType.EndElement:
                  switch (r.LocalName)
                  {
                     case "Tile":
                        addTile(newTile, layer == "Above");
                        break;
                     case "EncounterZone":
                        zones.Add(newZone);
                        break;
                        
                  }
                  break;
            };
         }

      }

      public void write()
      {
         XmlTextWriter xwriter = new XmlTextWriter("bcfiles\\" + name + ".xml", null);
         xwriter.WriteStartDocument();
         xwriter.WriteComment("BladeCraft Map File for assets\\maps\\" + name + ".map");
         xwriter.WriteStartElement("Map");
         xwriter.WriteAttributeString("Height", sizeY.ToString());
         xwriter.WriteAttributeString("Width", sizeX.ToString());
         xwriter.WriteAttributeString("TileSet", tileset);
         xwriter.WriteAttributeString("DisplayName", displayName);
         xwriter.WriteAttributeString("BGM", BGM);
         foreach (Tile t in bgtileList) if (t != null) writeTile(t, xwriter, "Below");
         foreach (Tile t in fgtileList) if (t != null) writeTile(t, xwriter, "Above");
         foreach (EncounterZone ez in zones)
            ez.write(xwriter);
         xwriter.WriteEndElement();
         xwriter.WriteEndDocument();
         xwriter.Close();

         StreamWriter writer = new StreamWriter("assets\\maps\\" + name + ".map");
         writer.WriteLine("[header]");
         writer.WriteLine("size " + sizeX + " " + sizeY);
         writer.WriteLine("tileset " + tileset);
         writer.WriteLine("displayname \"" + displayName +"\"");
         writer.WriteLine("BGM " + BGM);         


         //write map file
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
