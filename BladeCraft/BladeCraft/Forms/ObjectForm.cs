using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Text.RegularExpressions;
using System.Collections;

using BladeCraft.Classes;

namespace BladeCraft.Forms
{
   public partial class ObjectForm : Form
   {
      private BQMap map;
      private GameObject obj;
      private Boolean newObj;

      private Dictionary<String, String> actionParams;
      private int stateCount;

      public ObjectForm(BQMap map, int x, int y, int tileX, int tileY)
      {
         InitializeComponent();
         this.map = map;

         txtTileX.Text = tileX.ToString();
         txtTileY.Text = tileY.ToString();

         obj = new GameObject(x, y);
         newObj = true;

         

      }

      public ObjectForm(BQMap map, GameObject go, int tileX, int tileY)
      {
         InitializeComponent();
         this.map = map;
         txtTileX.Text = tileX.ToString();
         txtTileY.Text = tileY.ToString();

         obj = go;
      }

      private void ObjectForm_Load(object sender, EventArgs e)
      {
         txtX.Text = obj.X.ToString();
         txtY.Text = obj.Y.ToString();

         txtScript.Text = obj.Script;
         stateCount = 0;

         actionParams = new Dictionary<string, string>();

         actionParams.Add("fadeControl", "fadeSpeed a r g b fadeOut wait");
         actionParams.Add("messageWithYesNo", "\"message\"");
         actionParams.Add("message", "\"message\"");
         actionParams.Add("modifyGold", "gold");
         actionParams.Add("modifyInventory", "\"name\" count remove");
         actionParams.Add("modifyParty", "\"name\" remove");
         actionParams.Add("showScene", "\"name\"");
         actionParams.Add("openMerchant", "\"name\" discount");
         actionParams.Add("openNameSelect", "\"charName\"");
         actionParams.Add("resetGame", "0");
         actionParams.Add("playMusic", "\"song\" playIntro loop fadeTime");
         actionParams.Add("pauseMusic", "fadeTime");
         actionParams.Add("openBubble", "\"name\" \"target\" duration loop wait");
         actionParams.Add("closeBubble", "\"target\"");
         actionParams.Add("panControl", "x y speed wait");
         actionParams.Add("createPath", "\"target\" wait \"LURD\"");
         actionParams.Add("restoreParty", "0");
         actionParams.Add("shakeControl", "duration intensity wait");
         actionParams.Add("startBattle", "\"encounter\" allowGameOver");
         actionParams.Add("switchControl", "\"switchName\" state");
         actionParams.Add("openSaveMenu", "0");
         actionParams.Add("teleportParty", "parent x y \"mapname\"");
         actionParams.Add("wait", "seconds");
         actionParams.Add("unloadScene", "0");
         actionParams.Add("expectInput", "delay");
      }

      private void btnCancel_Click(object sender, EventArgs e)
      {
         Close();
      }

      private void btnSave_Click(object sender, EventArgs e)
      {
         obj.Script = txtScript.Text;

         obj.X = Convert.ToInt32(txtX.Text);
         obj.Y = Convert.ToInt32(txtY.Text);

         if (newObj) map.addObject(obj);

         Close();
      }

      private void insertMacro(string text)
      {
         int start = txtScript.Selection.Start;
         if (txtScript.Selection.Length > 0)
            txtScript.Text = txtScript.Text.Replace(txtScript.Selection.Text, text);
         else
            txtScript.Text = txtScript.Text.Insert(txtScript.Selection.Start, text);

         txtScript.Selection.Start = start + text.Length;
         txtScript.Scrolling.ScrollToCaret();
      }

      private void tsbAddObject_Click(object sender, EventArgs e)
      {
         insertMacro("\\objName$ <- \"name\"\r\n\\name$ <- newObject objName$ $X $Y\r\n\r\n");
      }

      private void tsbSpriteToTile_Click(object sender, EventArgs e)
      {
         insertMacro("setSpriteFromTile " + txtTileX.Text + " " + txtTileY.Text + " >\r\n");
      }

      private void tsbHeader_Click(object sender, EventArgs e)
      {
         map.openHeaderForm(MdiParent);      
      }

      private void tsbNewState_Click(object sender, EventArgs e)
      {
         string stateName = "state" + (++stateCount).ToString() + "$";
         insertMacro(stateName + " 0\r\nname$ > createState >\r\n#addSwitchCondition \"switchName\" >\r\n#addItemCondition \"itemName\" >\r\n\r\n\r\n\\run" + stateName + " <- " + stateName + " 0\r\n");
      }

      private void tsbImageOutline_Click(object sender, EventArgs e)
      {
         insertMacro("#setImageIndex index >\r\n#setAnimated animated >\r\n#setFace \"face\" > \r\n#setLayer \"AboveBelow\" > \r\n#setSprite \"spriteName\" > \r\n#setBubble \"bubbleName\" >\r\n#setMovement range delay >\r\n#setOptions waitOnActivate faceOnMove faceOnActivate > \r\n#setAutoStart autostart >\r\n");
      }


      private void allToolStripMenuItem_Click(object sender, EventArgs e)
      {
         insertMacro("setCollisionData true true true true >\r\n");
      }
      private void noneToolStripMenuItem_Click(object sender, EventArgs e)
      {
         insertMacro("setCollisionData false false false false >\r\n");
      }
      private void manualToolStripMenuItem_Click(object sender, EventArgs e)
      {
         insertMacro("setCollisionData left top right bottom >\r\n");
      }

      private void tsbBranchAction_Click(object sender, EventArgs e)
      {
         insertMacro("addToBranch index (actToAdd) >\r\n");
      }

      private void actionClick(Object sender, EventArgs e)
      {
         string line = ((ToolStripItem)sender).Text + " " + actionParams[((ToolStripItem)sender).Text];
         if(chkWrap.Checked)
            insertMacro("addAction (" + line + ") >\r\n");
         else
            insertMacro(line);

      }

      private void tsbLoopBranch_Click(object sender, EventArgs e)
      {
         insertMacro("setBranchLoop index truefalse >\r\n");
         
      }
   }
}
