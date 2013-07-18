using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Xml;
using System.Drawing;

using System.Linq;
using BladeCraft.Forms;

using System.Text.RegularExpressions;

namespace BladeCraft.Classes
{
   public interface MapWriter : IDisposable
   {
      void writeComment(String comment);
      void startSection(String sectionName);
      void writeAttribute(String attributeName, String value);
      void endSection();

      void writeElement(String elementName, String element);
   }

   public class BQMap
   {
      public class XMLMapWriter : MapWriter
      {
         XmlTextWriter xwriter;
         public XMLMapWriter(String name)
         {
            xwriter = new XmlTextWriter("bcfiles\\" + name + ".xml", null);
            xwriter.WriteStartDocument();
            
         }
         public void writeComment(String comment)
         {
            xwriter.WriteComment(comment);
         }

         public void startSection(String sectionName)
         {
            xwriter.WriteStartElement(sectionName);
         }

         public void writeAttribute(String attributeName, String value)
         {
            xwriter.WriteAttributeString(attributeName, value);
         }
         public void writeElement(String elementName, String element)
         {
            xwriter.WriteElementString(elementName, element);
         }

         public void endSection()
         {
            xwriter.WriteEndElement();
         }

         public void Dispose()
         {
            xwriter.WriteEndDocument();
            xwriter.Close();
         }
      }

      public class Memento
      {
         public Memento()
         {
            this.sections = new List<Section>();
         }
         public class Section
         {
            public Section()
            {
               attributes = new Dictionary<String, String>();
               elements = new Dictionary<String, String>();
               sections = new List<Section>();
            }
            public String name;
            public IDictionary<String, String> attributes;
            public IDictionary<String, String> elements;
            public List<Section> sections;
         }
         public List<Section> sections;
      }

      public class MementoWriter : MapWriter 
      {
         Memento memento;
         Memento.Section activeSection;
         List<Memento.Section> sectionStack;
         public MementoWriter(Memento memento)
         {
            this.memento = memento;
            sectionStack = new List<Memento.Section>();
         }
         public void writeComment(string comment)
         {
            //nice try obama
         }

         public void startSection(string sectionName)
         {
            if (activeSection != null)
            {
               sectionStack.Add(activeSection);
            }
            activeSection = new Memento.Section();
            activeSection.name = sectionName;
         }

         public void writeAttribute(string attributeName, string value)
         {
            activeSection.attributes[attributeName] = value;
         }

         public void endSection()
         {
            if (sectionStack.Any())
            {
               sectionStack[sectionStack.Count - 1].sections.Add(activeSection);
               activeSection = sectionStack[sectionStack.Count - 1];
               sectionStack.RemoveAt(sectionStack.Count - 1);
            }
            else
            {
               memento.sections.Add(activeSection);
               activeSection = null;
            }
         }

         public void writeElement(string elementName, string element)
         {
            activeSection.elements[elementName] = element;
         }

         public void Dispose()
         {
            //sure
         }
      }

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
      private string name, path, displayName, BGM;
      private bool save;

      public int layerCount = 8;

      private Tile[][] tileList;

      public List<EncounterZone> zones;
      public List<GameObject> objects;

      public string Header { get; set;}
      private LoadTypes loadType;

      private ObjectHeader headerForm;
      private Point[] materialPoints;

      public List<Memento> undoList, redoList;

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

         tileList = new Tile[layerCount][];
         for (int i = 0; i < layerCount; ++i)
            tileList[i] = new Tile[x * y];

         zones = new List<EncounterZone>();
         objects = new List<GameObject>();
         MatList = new List<Point>();
         undoList = new List<Memento>();
         redoList = new List<Memento>();
         buildMaterialLocations();
      }

      void reset()
      {
         string[] values = new string[20];
         zones = new List<EncounterZone>();
         MatList = new List<Point>();
      }
      public BQMap(string filename)
      {
         objects = new List<GameObject>();  //hack - not affected by undo-redo!
         undoList = new List<Memento>();
         redoList = new List<Memento>();

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
     

      public void updateSize(int x, int y)
      {
         for (int i = 0; i < layerCount; ++i)
         {
            Tile[] newTileList = new Tile[x * y];

            for (int j = 0; j < sizeX * sizeY && j < x * y; ++j)
               newTileList[j] = tileList[i][j];

            tileList[i] = newTileList;
         }

         sizeX = x;
         sizeY = y;

      }

      public void setSize(int x, int y)
      {
         sizeX = x;
         sizeY = y;
         tileList = new Tile[layerCount][];
         for (int i = 0; i < layerCount; ++i)
            tileList[i] = new Tile[x * y];
      }

      public Tile addTile(Tile tile)
      {
         int index = tile.y * sizeX + tile.x;

         if (tile.x >= 0 && tile.x < sizeX && tile.y >= 0 && tile.y < sizeY)
            tileList[tile.layer][index] = tile;

         return tile;
      }

      public Tile animateTile(int x, int y, int bmpX, int bmpY, int layer)
      {
         int index = y * sizeX + x;
         if (x >= 0 && x < sizeX && y >= 0 && y < sizeY)
            if (tileList[layer][index] != null)
            {
               tileList[layer][index].animate(bmpX, bmpY);
               return tileList[layer][index];
            }

         return null;
               
      }

      public void deleteTile(int x, int y, int layer)
      {
         int index = y * sizeX + x;

         if (x >= 0 && x < sizeX && y >= 0 && y < sizeY)
            tileList[layer][index] = null;
      }

      public Tile getTile(int x, int y, int layer) 
      { 
         if (x >= 0 && x < sizeX && y >= 0 && y < sizeY)
            return tileList[layer][y*sizeX+x]; 
         else 
            return null;
      }

      private bool[] fillChecked;

      public void fill(int x, int y, Tile t, bool frameTwo, int layer)
      {
         Tile[] tlist = tileList[layer];
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

            fillStep(fillingTile, p.X, p.Y, t, frameTwo, layer, pList);
            
         }
      }

      private void fillStep(Tile fillingTile, int x, int y, Tile fillTo, bool frameTwo, int layer, List<Point> pList)
      {
         Tile[] tlist = tileList[layer];
         Tile t = tlist[y * sizeX + x];
         bool fill = false;

         fillChecked[y * sizeX + x] = true;

         if (fillingTile == null && t == null)
         {
            tlist[y * sizeX + x] = new Tile(x, y, fillTo.bmpX, fillTo.bmpY, layer);

            t = tlist[y * sizeX + x];            

            if (fillTo.isMaterial)
               addMaterial(t.x, t.y, fillTo.matX, fillTo.matY, frameTwo, layer);
            else if (frameTwo)
               t.animate(fillTo.bmpX, fillTo.animBmpY);

            fill = true;
         }
         else if (fillingTile != null && fillingTile.isMaterial && t.isMaterial && fillingTile.matX == t.matX && fillingTile.matY == t.matY)
         {
            if (fillTo.isMaterial)
               addMaterial(t.x, t.y, fillTo.matX, fillTo.matY, frameTwo, layer);
            else
            {
               if (frameTwo)
               {
                  t.animBmpX = fillTo.bmpX;
                  t.animBmpY = fillTo.bmpY;
               }
               else
               {
                  tlist[y * sizeX + x] = new Tile(t.x, t.y, fillTo.bmpX, fillTo.bmpY, layer); 
               }
               
            }
            fill = true;
         }
         else if (fillingTile != null && !fillingTile.isMaterial && t != null &&
            t.bmpX == fillingTile.bmpX && t.bmpY == fillingTile.bmpY)
         {
            if (fillTo.isMaterial)
               addMaterial(t.x, t.y, fillTo.matX, fillTo.matY, frameTwo, layer);
            else
            {
               if (frameTwo)
               {
                  t.animBmpX = fillTo.bmpX;
                  t.animBmpY = fillTo.bmpY;
               }
               else
               {
                  tlist[y * sizeX + x] = new Tile(t.x, t.y, fillTo.bmpX, fillTo.bmpY, layer); 
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
      public void setName(string name) { this.name = name; }
      public void setDisplayName(String st) { displayName = st; }
      public String getDisplayName() { return displayName; }
      public void setBGM(String st) { BGM = st; }
      public String getBGM() { return BGM; }


      public void addMaterial(int x, int y, int matX, int matY, bool frameTwo, int layer)
      {
         int index = y * sizeX + x;
         Tile[] tlist = tileList[layer];

         if (tlist[index] == null)
            addTile(new Tile(x, y, 0, 0, layer));
                  
         tlist[index].addToMaterial(matX, matY);

         updateMaterialTile(x - 1, y - 1, matX, matY, layer, frameTwo);
         updateMaterialTile(x, y - 1, matX, matY, layer, frameTwo);
         updateMaterialTile(x + 1, y - 1, matX, matY, layer, frameTwo);
         updateMaterialTile(x - 1, y, matX, matY, layer, frameTwo);
         updateMaterialTile(x, y, matX, matY, layer, frameTwo);
         updateMaterialTile(x + 1, y, matX, matY, layer, frameTwo);
         updateMaterialTile(x - 1, y + 1, matX, matY, layer, frameTwo);
         updateMaterialTile(x, y + 1, matX, matY, layer, frameTwo);
         updateMaterialTile(x + 1, y + 1, matX, matY, layer, frameTwo);

      }

      private bool hasSameMaterial(int x, int y, int matX, int matY, int layer)
      {
         if (x < 0 || x >= sizeX || y < 0 || y >= sizeY)
            return true;

         int index = y * sizeX + x;
         Tile[] tlist = tileList[layer];
         if (tlist[index] != null)
            return tlist[index].hasSameMaterial(matX,  matY);
         else
            return false;
      }

      public void swapLayer(int x, int y, int fromLayer, int tolayer)
      {
         Tile[] fromList = tileList[fromLayer];
         Tile[] toList = tileList[tolayer];
         int index = y * sizeX + x;
         if(fromList[index] != null)
         {
            toList[index] = fromList[index];
            toList[index].layer = tolayer;
            fromList[index] = null;
         }
         

         //fromList[index].
      }

      private void updateMaterialTile(int x, int y, int matX, int matY, int layer, bool frameTwo)
      {
         int index = y * sizeX + x;
         Tile[] tlist = tileList[layer];

         if (x < 0 || y < 0 || x >= sizeX || y >= sizeY || tlist[index] == null || !tlist[index].hasSameMaterial(matX, matY))
            return;

         bool top = y > 0 ? hasSameMaterial(x, y - 1, matX, matY, layer) : true;
         bool left = x > 0 ? hasSameMaterial(x - 1, y, matX, matY, layer) : true;
         bool right = x < sizeX - 1 ? hasSameMaterial(x + 1, y, matX, matY, layer) : true;
         bool bottom = y < sizeY - 1 ? hasSameMaterial(x, y + 1, matX, matY, layer) : true;

         bool topLeft = top && left && hasSameMaterial(x - 1, y - 1, matX, matY, layer);
         bool bottomLeft = bottom && left && hasSameMaterial(x - 1, y + 1, matX, matY, layer);
         bool bottomRight = bottom && right && hasSameMaterial(x + 1, y + 1, matX, matY, layer);
         bool topRight = top && right && hasSameMaterial(x + 1, y - 1, matX, matY, layer);

         char flags = (char)((top ? (char)corners.Top : (char)0) | (bottom ? (char)corners.Bottom : (char)0) |
                  (right ? (char)corners.Right : (char)0) | (left ? (char)corners.Left : (char)0) |
                  (topRight ? (char)corners.TopRight : (char)0) | (bottomRight ? (char)corners.BottomRight : (char)0) |
                  (topLeft ? (char)corners.TopLeft : (char)0) | (bottomLeft ? (char)corners.BottomLeft : (char)0));


         Point p = materialPoints[flags];

         p.X += matX;
         p.Y += matY;

         if (frameTwo)
            animateTile(x, y, p.X, p.Y, layer);
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
            using (StreamWriter writer = new StreamWriter("assets\\maps\\omaps\\" + name + ".omap"))
            {
               writer.WriteLine("#!HEAD");
               writer.WriteLine(Header);

               foreach (GameObject obj in objects)
                  obj.write(writer);

               writer.Close();
            }
         }
         
      }

      private void writeTile(Tile t, MapWriter writer)
      {
         writer.startSection("Tile");

         writer.startSection("WorldPosition");
         writer.writeAttribute("X", t.x.ToString());
         writer.writeAttribute("Y", t.y.ToString());
         writer.endSection();

         writer.startSection("BitmapCoordinates");
         writer.writeAttribute("X", t.bmpX.ToString());
         writer.writeAttribute("Y", t.bmpY.ToString());
         writer.endSection();

         writer.writeElement("Layer", t.layer.ToString());

         writer.startSection("CollisionData");
         writer.writeAttribute("Left", t.collSides[0].ToString());
         writer.writeAttribute("Top", t.collSides[1].ToString());
         writer.writeAttribute("Right", t.collSides[2].ToString());
         writer.writeAttribute("Down", t.collSides[3].ToString());
         writer.endSection();

         if (t.animated)
         {
            writer.startSection("AnimatedBitmapCoordinates");
            writer.writeAttribute("X", t.animBmpX.ToString());
            writer.writeAttribute("Y", t.animBmpY.ToString());
            writer.endSection();
         }

         if (t.IsMaterial())
         {
            writer.startSection("MaterialCoordinates");
            writer.writeAttribute("X", t.matX.ToString());
            writer.writeAttribute("Y", t.matY.ToString());
            writer.endSection();
         }


         writer.endSection();
      }


      public interface MapNode
      {
         bool startNode();
         bool endNode();
         String name();
         String elementData();
         String getAttribute(String attrName);
      }
      public interface MapReader : IDisposable
      {
         IEnumerable<MapNode> getNodes();
      }


      public class XMLNode : MapNode 
      {
         XmlTextReader reader;
         public XMLNode(XmlTextReader reader)
         {
            this.reader = reader;
         }

         public bool startNode()
         {
            return XmlNodeType.Element == reader.NodeType;
         }
         public bool endNode()
         {
            return XmlNodeType.EndElement == reader.NodeType;
         }

         public string name()
         {
            return reader.LocalName;
         }

         public string elementData()
         {
            return reader.ReadString();
         }

         public string getAttribute(string attrName)
         {
            return reader.GetAttribute(attrName);
         }
      }

      public class XMLMapReader : MapReader
      {
         public XMLMapReader(string path)
         {
            reader = new XmlTextReader(path);
         }
         XmlTextReader reader;
      
         public void Dispose()
         {
            reader.Close();
            reader.Dispose();
         }
      
         public IEnumerable<MapNode> getNodes()
         {
            while (reader.Read())
            {
               yield return new XMLNode(reader);
            }
         }
      }



      public class MementoSectionNode : MapNode
      {
         Memento.Section section;
         bool start;
         public MementoSectionNode(Memento.Section section, bool start)
         {
            this.section = section;
            this.start = start;
         }

         public bool startNode()
         {
            return start == true;
         }
         public bool endNode()
         {
            return start == false;
         }

         public string name()
         {
            return section.name;
         }

         public string elementData()
         {
            return "";
         }

         public string getAttribute(string attrName)
         {
            return section.attributes[attrName];
         }
      }



      public class MementoElementNode : MapNode
      {
         String elemName, data;
         bool start;
         public MementoElementNode(String elemName, string data, bool start)
         {
            this.elemName = elemName;
            this.data = data;
            this.start = start;
         }

         public bool startNode()
         {
            return start == true;
         }
         public bool endNode()
         {
            return start == false;
         }

         public string name()
         {
            return elemName;
         }

         public string elementData()
         {
            return data;
         }

         public string getAttribute(string attrName)
         {
            return "";
         }
      }

      public class MementoReader : MapReader
      {
         public MementoReader(Memento memento)
         {
            this.memento = memento;
         }
         Memento memento;

         public void Dispose()
         {
         }

         public IEnumerable<MapNode> readSection(Memento.Section section)
         {
            yield return new MementoSectionNode(section, true);
            foreach (KeyValuePair<string, string> kvp in section.elements)
            {
               yield return new MementoElementNode(kvp.Key, kvp.Value, true);
               yield return new MementoElementNode(kvp.Key, kvp.Value, false);
            }
            foreach (var subsection in section.sections)
            {
               foreach (var node in readSection(subsection))
               {
                  yield return node;
               }
            }
            yield return new MementoSectionNode(section, false);
         }
         public IEnumerable<MapNode> getNodes()
         {
            foreach (var section in memento.sections)
            {
               foreach (var node in readSection(section))
               {
                  yield return node;
               }
            }
         }
      }


      


      private void readXML(string path)
      {
         using (XMLMapReader reader = new XMLMapReader(path))
         {
            read(reader);
         }
      }
      private void readMemento(Memento memento)
      {
         using (MementoReader reader = new MementoReader(memento))
         {
            read(reader);
         }
      }


      private void read(MapReader r)
      {
         reset();
         Tile newTile = new Tile();
         EncounterZone newZone = new EncounterZone();

         foreach (var node in r.getNodes())
         {
            if (node.startNode())
            {
                  switch (node.name())
                  {
                     case "Map":
                        BGM = node.getAttribute("BGM");
                        displayName = node.getAttribute("DisplayName");
                        //tileset = node.getAttribute("TileSet");
                        //TODO: Replace

                        sizeX = Convert.ToInt32(node.getAttribute("Width"));
                        sizeY = Convert.ToInt32(node.getAttribute("Height"));

                        tileList = new Tile[layerCount][];
                        for (int i = 0; i < layerCount; ++i)
                           tileList[i] = new Tile[sizeX * sizeY];
                        break;
                     case "Tile":
                        newTile = new Tile();
                        break;
                     case "WorldPosition":
                        newTile.x = Convert.ToInt32(node.getAttribute("X"));
                        newTile.y = Convert.ToInt32(node.getAttribute("Y"));
                        break;
                     case "BitmapCoordinates":
                        newTile.bmpX = Convert.ToInt32(node.getAttribute("X"));
                        newTile.bmpY = Convert.ToInt32(node.getAttribute("Y"));
                        break;
                     case "Material":
                        MatList.Add(new Point(
                           Convert.ToInt32(node.getAttribute("X")),
                           Convert.ToInt32(node.getAttribute("Y"))));
                        break;
                     case "Layer":
                        newTile.layer = Convert.ToInt32(node.elementData());
                        break;
                     case "CollisionData":
                        newTile.collSides[0] = Convert.ToBoolean(node.getAttribute("Left"));
                        newTile.collSides[1] = Convert.ToBoolean(node.getAttribute("Top"));
                        newTile.collSides[2] = Convert.ToBoolean(node.getAttribute("Right"));
                        newTile.collSides[3] = Convert.ToBoolean(node.getAttribute("Down"));
                        break;
                     case "AnimatedBitmapCoordinates":
                        newTile.animate(Convert.ToInt32(node.getAttribute("X")), Convert.ToInt32(node.getAttribute("Y")));
                        break;
                     case "MaterialCoordinates":
                        newTile.addToMaterial(Convert.ToInt32(node.getAttribute("X")), Convert.ToInt32(node.getAttribute("Y")));
                        break;
                     case "EncounterZone":
                        newZone = new EncounterZone();
                        newZone.zone.Width = Convert.ToInt32(node.getAttribute("Width"));
                        newZone.zone.Height = Convert.ToInt32(node.getAttribute("Height"));
                        newZone.zone.X = Convert.ToInt32(node.getAttribute("X"));
                        newZone.zone.Y = Convert.ToInt32(node.getAttribute("Y"));
                        newZone.encounterRate = (int)Convert.ToDecimal(node.getAttribute("EncounterRate"));
                        break;
                     case "Encounter":
                        newZone.encounters.Add(node.elementData());
                        break;
                  }
            }
            else if (node.endNode())
            {
               switch (node.name())
               {
                  case "Tile":
                     addTile(newTile);
                     break;
                  case "EncounterZone":
                     zones.Add(newZone);
                     break;

               }
            }
         }
      }


      public void writeBQData()
      {
         using (StreamWriter writer = new StreamWriter("assets\\maps\\" + name + ".map"))
         {
            writer.WriteLine("[header]");
            writer.WriteLine("size " + sizeX + " " + sizeY);
            //writer.WriteLine("tileset " + tileset);
            //TODO: replace
            writer.WriteLine("displayname \"" + displayName + "\"");
            writer.WriteLine("BGM " + BGM);


            //write map file
            writer.WriteLine("[tiles]");
            foreach (Tile[] tList in tileList)
               foreach (Tile t in tList)
                  if (t != null)
                     if (t.animated)
                        writer.WriteLine("t " + t.x + " " + t.y + " " + t.bmpX + " " + t.bmpY + " " + t.layer.ToString() + " " +
                        Convert.ToInt32(t.collSides[0]).ToString() + " " + Convert.ToInt32(t.collSides[1]).ToString() + " " +
                        Convert.ToInt32(t.collSides[2]).ToString() + " " + Convert.ToInt32(t.collSides[3]).ToString() + " " +
                        "t " + t.animBmpX + " " + t.animBmpY);
                     else
                        writer.WriteLine("t " + t.x + " " + t.y + " " + t.bmpX + " " + t.bmpY + " " + t.layer.ToString() + " " +
                        Convert.ToInt32(t.collSides[0]).ToString() + " " + Convert.ToInt32(t.collSides[1]).ToString() + " " +
                        Convert.ToInt32(t.collSides[2]).ToString() + " " + Convert.ToInt32(t.collSides[3]).ToString() + " ");


            writer.WriteLine("[encounters]");
            foreach (EncounterZone ez in zones)
               ez.write(writer);

            writer.Close();
         }
         

         writeObjects();
      }

      public void writeXML()
      {
         using (MapWriter m = new XMLMapWriter(name))
         {
            write(m);
         };
      }


      public void write(MapWriter writer)
      {
         writer.writeComment("BladeCraft Map File for assets\\maps\\" + name + ".map");
         writer.startSection("Map");
         writer.writeAttribute("Height", sizeY.ToString());
         writer.writeAttribute("Width", sizeX.ToString());
         //writer.writeAttribute("TileSet", tileset);
         //Todo: Replace
         writer.writeAttribute("DisplayName", displayName);
         writer.writeAttribute("BGM", BGM);

         foreach(Tile[] tList in tileList)
            foreach (Tile t in tList) 
               if (t != null) 
                  writeTile(t, writer);
         foreach (EncounterZone ez in zones)
            ez.write(writer);
         if (MatList.Count > 0)
            foreach (Point p in MatList)
            {
               writer.startSection("Material");
               writer.writeAttribute("X", p.X.ToString());
               writer.writeAttribute("Y", p.Y.ToString());
               writer.endSection();
            }

         writer.endSection();
      }


      public void writeMemento()
      {
         redoList.Clear();
         Memento m = new Memento();
         using (MementoWriter writer = new MementoWriter(m))
         {
            write(writer);
         }
         undoList.Add(m);
      }
      public void undo()
      {
         if (undoList.Count > 1)
         {
            Memento targetMemento = undoList[undoList.Count - 2];
            redoList.Add(undoList[undoList.Count - 1]);
            readMemento(targetMemento);
            undoList.RemoveAt(undoList.Count - 1);
         }
      }
      public void redo()
      {
         if (redoList.Any())
         {
            Memento targetMemento = redoList[redoList.Count - 1];
            undoList.Add(targetMemento);
            readMemento(targetMemento);
            redoList.RemoveAt(redoList.Count - 1);
         }
      }
   }
}
