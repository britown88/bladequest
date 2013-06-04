using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Xml;
using System.Drawing;


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
      private enum corners
      {
         TopRight = 1 << 0,
         Top = 1 << 1,
         TopLeft = 1 << 2,
         Left = 1 << 3,
         BottomLeft = 1 << 4,
         Bottom = 1 << 5,
         BottomRight = 1 << 6,
         Right = 1 << 7
      };
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
      private Point[] materialPoints;

      public List<Point> MatList;

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
         MatList = new List<Point>();
         buildMaterialLocations();
      }

      public BQMap(string filename)
      {
         StreamReader reader = new StreamReader(filename);
         string[] values= new string[20];
         zones = new List<EncounterZone>();
         objects = new List<GameObject>();
         MatList = new List<Point>();

         this.path = filename;

         name = filename.Remove(filename.Length - 4);
         name = name.Substring(name.LastIndexOf('\\') + 1);

         readXML(filename);
         readObjectFile();
         buildMaterialLocations();
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

      private void buildMaterialLocations()
      {
         materialPoints = new Point[1 << 8];


         materialPoints[(char)corners.Bottom | (char)corners.Right | (char)corners.BottomRight] = new Point(0, 0);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.BottomRight | (char)corners.BottomLeft] = new Point(1, 0);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.BottomLeft] = new Point(2, 0);
         materialPoints[(char)corners.Bottom | (char)corners.Right] = new Point(3, 0);
         materialPoints[(char)corners.Left | (char)corners.Right] = new Point(4, 0);
         materialPoints[(char)corners.Bottom | (char)corners.Left] = new Point(5, 0);
         materialPoints[(char)corners.Right] = new Point(6, 0);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right] = new Point(7, 0);
         materialPoints[(char)corners.Bottom] = new Point(8, 0);

         materialPoints[(char)corners.Bottom | (char)corners.Right | (char)corners.Top | (char)corners.TopRight | (char)corners.BottomRight] = new Point(0, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.TopRight | (char)corners.BottomRight | (char)corners.BottomLeft] = new Point(1, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Top | (char)corners.TopLeft | (char)corners.BottomLeft] = new Point(2, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Top] = new Point(3, 1);
         materialPoints[0] = new Point(4, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Top] = new Point(5, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Right | (char)corners.Top] = new Point(6, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top] = new Point(7, 1);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Top] = new Point(8, 1);

         materialPoints[(char)corners.Right | (char)corners.Top | (char)corners.TopRight] = new Point(0, 2);
         materialPoints[(char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.TopRight] = new Point(1, 2);
         materialPoints[(char)corners.Left | (char)corners.Top | (char)corners.TopLeft] = new Point(2, 2);
         materialPoints[(char)corners.Right | (char)corners.Top] = new Point(3, 2);
         materialPoints[(char)corners.Left | (char)corners.Right] = new Point(4, 2);
         materialPoints[(char)corners.Left | (char)corners.Top] = new Point(5, 2);
         materialPoints[(char)corners.Top] = new Point(6, 2);
         materialPoints[(char)corners.Left | (char)corners.Right | (char)corners.Top] = new Point(7, 2);
         materialPoints[(char)corners.Left] = new Point(8, 2);

         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopRight | (char)corners.BottomRight | (char)corners.BottomLeft] = new Point(0, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.BottomRight | (char)corners.BottomLeft] = new Point(1, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.BottomRight | (char)corners.BottomLeft] = new Point(2, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopRight | (char)corners.BottomLeft] = new Point(3, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.BottomRight] = new Point(4, 3);
         materialPoints[(char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopRight] = new Point(5, 3);
         materialPoints[(char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft] = new Point(6, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.BottomRight] = new Point(7, 3);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.BottomLeft] = new Point(8, 3);

         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopRight | (char)corners.BottomRight] = new Point(0, 4);
         //materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top] = new Point(1, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.BottomLeft] = new Point(2, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.BottomRight] = new Point(3, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.BottomLeft] = new Point(4, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Top | (char)corners.BottomLeft] = new Point(5, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Right | (char)corners.Top | (char)corners.BottomRight] = new Point(6, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Top | (char)corners.TopLeft] = new Point(7, 4);
         materialPoints[(char)corners.Bottom | (char)corners.Right | (char)corners.Top | (char)corners.TopRight] = new Point(8, 4);

         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.TopRight | (char)corners.BottomRight] = new Point(0, 5);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.TopRight] = new Point(1, 5);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft | (char)corners.TopRight | (char)corners.BottomLeft] = new Point(2, 5);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopRight] = new Point(3, 5);
         materialPoints[(char)corners.Bottom | (char)corners.Left | (char)corners.Right | (char)corners.Top | (char)corners.TopLeft] = new Point(4, 5);

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

      public void fill(int x, int y, Tile t, bool frameTwo, bool foreground)
      {
         Tile[] tlist = foreground ? fgtileList : bgtileList;
         Tile fillingTile = tlist[y * sizeX + x];
         if (fillingTile != null)
            fillingTile = new Tile(fillingTile);

         fillChecked = new bool[sizeX * sizeY];

         List<Point> pList = new List<Point>();
         pList.Add(new Point(x, y));

         while (pList.Count > 0)
         {
            Point p = pList[pList.Count - 1];
            pList.RemoveAt(pList.Count - 1);

            fillStep(fillingTile, p.X, p.Y, t, frameTwo, foreground, pList);
            
         }
      }

      private void fillStep(Tile fillingTile, int x, int y, Tile fillTo, bool frameTwo, bool foreground, List<Point> pList)
      {
         Tile[] tlist = foreground ? fgtileList : bgtileList;
         Tile t = tlist[y * sizeX + x];
         bool fill = false;

         fillChecked[y * sizeX + x] = true;

         if (fillingTile == null && t == null)
         {
            tlist[y * sizeX + x] = new Tile(x, y, fillTo.bmpX, fillTo.bmpY);

            t = tlist[y * sizeX + x];            

            if (fillTo.isMaterial)
               addMaterial(t.x, t.y, fillTo.matX, fillTo.matY, frameTwo, foreground);
            else if (frameTwo)
               t.animate(fillTo.bmpX, fillTo.animBmpY);

            fill = true;
         }
         else if (fillingTile != null && fillingTile.isMaterial && t.isMaterial && fillingTile.matX == t.matX && fillingTile.matY == t.matY)
         {
            if (fillTo.isMaterial)
               addMaterial(t.x, t.y, fillTo.matX, fillTo.matY, frameTwo, foreground);
            else
            {
               if (frameTwo)
               {
                  t.animBmpX = fillTo.bmpX;
                  t.animBmpY = fillTo.bmpY;
               }
               else
               {
                  tlist[y * sizeX + x] = new Tile(t.x, t.y, fillTo.bmpX, fillTo.bmpY); 
               }
               
            }
            fill = true;
         }
         else if (fillingTile != null && !fillingTile.isMaterial && t != null &&
            t.bmpX == fillingTile.bmpX && t.bmpY == fillingTile.bmpY)
         {
            if (fillTo.isMaterial)
               addMaterial(t.x, t.y, fillTo.matX, fillTo.matY, frameTwo, foreground);
            else
            {
               if (frameTwo)
               {
                  t.animBmpX = fillTo.bmpX;
                  t.animBmpY = fillTo.bmpY;
               }
               else
               {
                  tlist[y * sizeX + x] = new Tile(t.x, t.y, fillTo.bmpX, fillTo.bmpY); 
               }
            }
            
            fill = true;
         }

         if(fill)
         {
            if (x + 1 < sizeX && !fillChecked[y * sizeX + x + 1])
               pList.Add(new Point(x + 1, y));
            if (x - 1 >= 0 && !fillChecked[y * sizeX + x-1])
               pList.Add(new Point(x - 1, y));
            if (y + 1 < sizeY && !fillChecked[(y + 1) * sizeX + x])
               pList.Add(new Point(x, y + 1));
            if (y - 1 >= 0 && !fillChecked[(y - 1) * sizeX + x])
               pList.Add(new Point(x, y - 1));
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


      public void addMaterial(int x, int y, int matX, int matY, bool frameTwo, bool foreground)
      {
         int index = y * sizeX + x;
         Tile[] tlist = foreground ? fgtileList : bgtileList;

         if (tlist[index] == null)
            addTile(new Tile(x, y, 0, 0), foreground);
                  
         tlist[index].addToMaterial(matX, matY);

         updateMaterialTile(x - 1, y - 1, matX, matY, foreground, frameTwo);
         updateMaterialTile(x, y - 1, matX, matY, foreground, frameTwo);
         updateMaterialTile(x + 1, y - 1, matX, matY, foreground, frameTwo);
         updateMaterialTile(x - 1, y, matX, matY, foreground, frameTwo);
         updateMaterialTile(x, y, matX, matY, foreground, frameTwo);
         updateMaterialTile(x + 1, y, matX, matY, foreground, frameTwo);
         updateMaterialTile(x - 1, y + 1, matX, matY, foreground, frameTwo);
         updateMaterialTile(x, y + 1, matX, matY, foreground, frameTwo);
         updateMaterialTile(x + 1, y + 1, matX, matY, foreground, frameTwo);

      }

      private bool hasSameMaterial(int x, int y, int matX, int matY, bool foreground)
      {
         if (x < 0 || x >= sizeX || y < 0 || y >= sizeY)
            return true;

         int index = y * sizeX + x;
         Tile[] tlist = foreground ? fgtileList : bgtileList;
         if (tlist[index] != null)
            return tlist[index].hasSameMaterial(matX,  matY);
         else
            return false;
      }

      public void swapLayer(int x, int y, bool foreground)
      {
         Tile[] fromList = foreground ? fgtileList : bgtileList;
         Tile[] toList = foreground ? bgtileList : fgtileList;
         int index = y * sizeX + x;
         if(fromList[index] != null)
         {
            toList[index] = fromList[index];
            fromList[index] = null;
         }
         

         //fromList[index].
      }

      private void updateMaterialTile(int x, int y, int matX, int matY, bool foreground, bool frameTwo)
      {
         int index = y * sizeX + x;
         Tile[] tlist = foreground ? fgtileList : bgtileList;

         if (x < 0 || y < 0 || x >= sizeX || y >= sizeY || tlist[index] == null || !tlist[index].hasSameMaterial(matX, matY))
            return;

         bool top = y > 0 ? hasSameMaterial(x, y - 1, matX, matY, foreground) : true;
         bool left = x > 0 ? hasSameMaterial(x - 1, y, matX, matY, foreground) : true;
         bool right = x < sizeX - 1 ? hasSameMaterial(x + 1, y, matX, matY, foreground) : true;
         bool bottom = y < sizeY - 1 ? hasSameMaterial(x, y + 1, matX, matY, foreground) : true;

         bool topLeft = top && left && hasSameMaterial(x - 1, y - 1, matX, matY, foreground);
         bool bottomLeft = bottom && left && hasSameMaterial(x - 1, y + 1, matX, matY, foreground);
         bool bottomRight = bottom && right && hasSameMaterial(x + 1, y + 1, matX, matY, foreground);
         bool topRight = top && right && hasSameMaterial(x + 1, y - 1, matX, matY, foreground);

         char flags = (char)((top ? (char)corners.Top : (char)0) | (bottom ? (char)corners.Bottom : (char)0) |
                  (right ? (char)corners.Right : (char)0) | (left ? (char)corners.Left : (char)0) |
                  (topRight ? (char)corners.TopRight : (char)0) | (bottomRight ? (char)corners.BottomRight : (char)0) |
                  (topLeft ? (char)corners.TopLeft : (char)0) | (bottomLeft ? (char)corners.BottomLeft : (char)0));


         Point p = materialPoints[flags];

         p.X += matX;
         p.Y += matY;

         if (frameTwo)
            animateTile(x, y, p.X, p.Y, foreground);
         else
         {
            tlist[index].bmpX = p.X;
            tlist[index].bmpY = p.Y;
         }
      }

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
         xwriter.WriteAttributeString("Down", t.collSides[3].ToString());
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
                     case "Material":
                        MatList.Add(new Point(
                           Convert.ToInt32(r.GetAttribute("X")),
                           Convert.ToInt32(r.GetAttribute("Y"))));
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

         r.Close();

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
         if (MatList.Count > 0)
            foreach (Point p in MatList)
            {
               xwriter.WriteStartElement("Material");
               xwriter.WriteAttributeString("X", p.X.ToString());
               xwriter.WriteAttributeString("Y", p.Y.ToString());
               xwriter.WriteEndElement();
            }
      
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
