namespace BladeCraft.Forms
{
   partial class ObjectForm
   {
      /// <summary>
      /// Required designer variable.
      /// </summary>
      private System.ComponentModel.IContainer components = null;

      /// <summary>
      /// Clean up any resources being used.
      /// </summary>
      /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
      protected override void Dispose(bool disposing)
      {
         if (disposing && (components != null))
         {
            components.Dispose();
         }
         base.Dispose(disposing);
      }

      #region Windows Form Designer generated code

      /// <summary>
      /// Required method for Designer support - do not modify
      /// the contents of this method with the code editor.
      /// </summary>
      private void InitializeComponent()
      {
         System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(ObjectForm));
         this.btnSave = new System.Windows.Forms.Button();
         this.btnCancel = new System.Windows.Forms.Button();
         this.toolStrip1 = new System.Windows.Forms.ToolStrip();
         this.toolStripLabel1 = new System.Windows.Forms.ToolStripLabel();
         this.txtX = new System.Windows.Forms.ToolStripTextBox();
         this.txtY = new System.Windows.Forms.ToolStripTextBox();
         this.tsbAddObject = new System.Windows.Forms.ToolStripButton();
         this.toolStripLabel2 = new System.Windows.Forms.ToolStripLabel();
         this.txtTileX = new System.Windows.Forms.ToolStripTextBox();
         this.txtTileY = new System.Windows.Forms.ToolStripTextBox();
         this.tsbSpriteToTile = new System.Windows.Forms.ToolStripButton();
         this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
         this.tsbHeader = new System.Windows.Forms.ToolStripButton();
         this.toolStripSeparator2 = new System.Windows.Forms.ToolStripSeparator();
         this.tsbNewState = new System.Windows.Forms.ToolStripButton();
         this.tsbOutline = new System.Windows.Forms.ToolStripButton();
         this.tsbCollision = new System.Windows.Forms.ToolStripDropDownButton();
         this.allToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.noneToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.manualToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.tsbBranchAction = new System.Windows.Forms.ToolStripButton();
         this.tsbLoopBranch = new System.Windows.Forms.ToolStripButton();
         this.chkWrap = new System.Windows.Forms.ToolStripButton();
         this.txtScript = new ScintillaNET.Scintilla();
         this.menuStrip1 = new System.Windows.Forms.MenuStrip();
         this.fileToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.actionsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.movementToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.changeElevationToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.createPathToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.teleportPartyToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.setFloatingToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.partyToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.restorePartyToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.modifyPartyToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.modifyInventoryToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.modifyGoldToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.effectsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.closeBubbleToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.fadeControlToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.openBubbleToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.panControlToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.pauseMusicToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.playMusicToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.shakeControlToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.showSceneToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.unloadSceneToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.uIToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.waitToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.resetGameToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.expectInputToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.switchControlToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.startBattleToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.uIToolStripMenuItem1 = new System.Windows.Forms.ToolStripMenuItem();
         this.openSaveMenuToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.openNameSelectToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.openMerchantToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.messageWithYesNoToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.messageToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.flashToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.allowSavingToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.messageTopToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.playAnimationToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.playAnimationStoppedShortToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.clearAnimationsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.toolStrip1.SuspendLayout();
         ((System.ComponentModel.ISupportInitialize)(this.txtScript)).BeginInit();
         this.menuStrip1.SuspendLayout();
         this.SuspendLayout();
         // 
         // btnSave
         // 
         this.btnSave.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
         this.btnSave.Location = new System.Drawing.Point(493, 648);
         this.btnSave.Name = "btnSave";
         this.btnSave.Size = new System.Drawing.Size(75, 23);
         this.btnSave.TabIndex = 4;
         this.btnSave.Text = "Save";
         this.btnSave.UseVisualStyleBackColor = true;
         this.btnSave.Click += new System.EventHandler(this.btnSave_Click);
         // 
         // btnCancel
         // 
         this.btnCancel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
         this.btnCancel.Location = new System.Drawing.Point(574, 648);
         this.btnCancel.Name = "btnCancel";
         this.btnCancel.Size = new System.Drawing.Size(75, 23);
         this.btnCancel.TabIndex = 5;
         this.btnCancel.Text = "Cancel";
         this.btnCancel.UseVisualStyleBackColor = true;
         this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
         // 
         // toolStrip1
         // 
         this.toolStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.toolStripLabel1,
            this.txtX,
            this.txtY,
            this.tsbAddObject,
            this.toolStripLabel2,
            this.txtTileX,
            this.txtTileY,
            this.tsbSpriteToTile,
            this.toolStripSeparator1,
            this.tsbHeader,
            this.toolStripSeparator2,
            this.tsbNewState,
            this.tsbOutline,
            this.tsbCollision,
            this.tsbBranchAction,
            this.tsbLoopBranch,
            this.chkWrap});
         this.toolStrip1.Location = new System.Drawing.Point(0, 24);
         this.toolStrip1.Name = "toolStrip1";
         this.toolStrip1.Size = new System.Drawing.Size(661, 25);
         this.toolStrip1.TabIndex = 6;
         this.toolStrip1.Text = "toolStrip1";
         // 
         // toolStripLabel1
         // 
         this.toolStripLabel1.Name = "toolStripLabel1";
         this.toolStripLabel1.Size = new System.Drawing.Size(56, 22);
         this.toolStripLabel1.Text = "Location:";
         // 
         // txtX
         // 
         this.txtX.Name = "txtX";
         this.txtX.Size = new System.Drawing.Size(30, 25);
         // 
         // txtY
         // 
         this.txtY.Name = "txtY";
         this.txtY.Size = new System.Drawing.Size(30, 25);
         // 
         // tsbAddObject
         // 
         this.tsbAddObject.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
         this.tsbAddObject.Image = ((System.Drawing.Image)(resources.GetObject("tsbAddObject.Image")));
         this.tsbAddObject.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.tsbAddObject.Name = "tsbAddObject";
         this.tsbAddObject.Size = new System.Drawing.Size(23, 22);
         this.tsbAddObject.Text = "Add Object at Location";
         this.tsbAddObject.Click += new System.EventHandler(this.tsbAddObject_Click);
         // 
         // toolStripLabel2
         // 
         this.toolStripLabel2.Name = "toolStripLabel2";
         this.toolStripLabel2.Size = new System.Drawing.Size(29, 22);
         this.toolStripLabel2.Text = "Tile:";
         // 
         // txtTileX
         // 
         this.txtTileX.Enabled = false;
         this.txtTileX.Name = "txtTileX";
         this.txtTileX.Size = new System.Drawing.Size(30, 25);
         // 
         // txtTileY
         // 
         this.txtTileY.Enabled = false;
         this.txtTileY.Name = "txtTileY";
         this.txtTileY.Size = new System.Drawing.Size(30, 25);
         // 
         // tsbSpriteToTile
         // 
         this.tsbSpriteToTile.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
         this.tsbSpriteToTile.Image = ((System.Drawing.Image)(resources.GetObject("tsbSpriteToTile.Image")));
         this.tsbSpriteToTile.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.tsbSpriteToTile.Name = "tsbSpriteToTile";
         this.tsbSpriteToTile.Size = new System.Drawing.Size(23, 22);
         this.tsbSpriteToTile.Text = "Set sprite to tile";
         this.tsbSpriteToTile.Click += new System.EventHandler(this.tsbSpriteToTile_Click);
         // 
         // toolStripSeparator1
         // 
         this.toolStripSeparator1.Name = "toolStripSeparator1";
         this.toolStripSeparator1.Size = new System.Drawing.Size(6, 25);
         // 
         // tsbHeader
         // 
         this.tsbHeader.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
         this.tsbHeader.Image = ((System.Drawing.Image)(resources.GetObject("tsbHeader.Image")));
         this.tsbHeader.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.tsbHeader.Name = "tsbHeader";
         this.tsbHeader.Size = new System.Drawing.Size(23, 22);
         this.tsbHeader.Text = "toolStripButton1";
         this.tsbHeader.ToolTipText = "Map Header";
         this.tsbHeader.Click += new System.EventHandler(this.tsbHeader_Click);
         // 
         // toolStripSeparator2
         // 
         this.toolStripSeparator2.Name = "toolStripSeparator2";
         this.toolStripSeparator2.Size = new System.Drawing.Size(6, 25);
         // 
         // tsbNewState
         // 
         this.tsbNewState.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
         this.tsbNewState.Image = ((System.Drawing.Image)(resources.GetObject("tsbNewState.Image")));
         this.tsbNewState.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.tsbNewState.Name = "tsbNewState";
         this.tsbNewState.Size = new System.Drawing.Size(23, 22);
         this.tsbNewState.Text = "Add New State";
         this.tsbNewState.Click += new System.EventHandler(this.tsbNewState_Click);
         // 
         // tsbOutline
         // 
         this.tsbOutline.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
         this.tsbOutline.Image = ((System.Drawing.Image)(resources.GetObject("tsbOutline.Image")));
         this.tsbOutline.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.tsbOutline.Name = "tsbOutline";
         this.tsbOutline.Size = new System.Drawing.Size(23, 22);
         this.tsbOutline.Text = "State Outline";
         this.tsbOutline.Click += new System.EventHandler(this.tsbImageOutline_Click);
         // 
         // tsbCollision
         // 
         this.tsbCollision.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
         this.tsbCollision.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.allToolStripMenuItem,
            this.noneToolStripMenuItem,
            this.manualToolStripMenuItem});
         this.tsbCollision.Image = ((System.Drawing.Image)(resources.GetObject("tsbCollision.Image")));
         this.tsbCollision.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.tsbCollision.Name = "tsbCollision";
         this.tsbCollision.Size = new System.Drawing.Size(29, 22);
         this.tsbCollision.Text = "Collision Data";
         // 
         // allToolStripMenuItem
         // 
         this.allToolStripMenuItem.Name = "allToolStripMenuItem";
         this.allToolStripMenuItem.Size = new System.Drawing.Size(114, 22);
         this.allToolStripMenuItem.Text = "All";
         this.allToolStripMenuItem.Click += new System.EventHandler(this.allToolStripMenuItem_Click);
         // 
         // noneToolStripMenuItem
         // 
         this.noneToolStripMenuItem.Name = "noneToolStripMenuItem";
         this.noneToolStripMenuItem.Size = new System.Drawing.Size(114, 22);
         this.noneToolStripMenuItem.Text = "None";
         this.noneToolStripMenuItem.Click += new System.EventHandler(this.noneToolStripMenuItem_Click);
         // 
         // manualToolStripMenuItem
         // 
         this.manualToolStripMenuItem.Name = "manualToolStripMenuItem";
         this.manualToolStripMenuItem.Size = new System.Drawing.Size(114, 22);
         this.manualToolStripMenuItem.Text = "Manual";
         this.manualToolStripMenuItem.Click += new System.EventHandler(this.manualToolStripMenuItem_Click);
         // 
         // tsbBranchAction
         // 
         this.tsbBranchAction.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
         this.tsbBranchAction.Image = ((System.Drawing.Image)(resources.GetObject("tsbBranchAction.Image")));
         this.tsbBranchAction.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.tsbBranchAction.Name = "tsbBranchAction";
         this.tsbBranchAction.Size = new System.Drawing.Size(23, 22);
         this.tsbBranchAction.Text = "Add Branch Action";
         this.tsbBranchAction.Click += new System.EventHandler(this.tsbBranchAction_Click);
         // 
         // tsbLoopBranch
         // 
         this.tsbLoopBranch.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
         this.tsbLoopBranch.Image = ((System.Drawing.Image)(resources.GetObject("tsbLoopBranch.Image")));
         this.tsbLoopBranch.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.tsbLoopBranch.Name = "tsbLoopBranch";
         this.tsbLoopBranch.Size = new System.Drawing.Size(23, 22);
         this.tsbLoopBranch.Text = "Loop Branch";
         this.tsbLoopBranch.Click += new System.EventHandler(this.tsbLoopBranch_Click);
         // 
         // chkWrap
         // 
         this.chkWrap.Checked = true;
         this.chkWrap.CheckOnClick = true;
         this.chkWrap.CheckState = System.Windows.Forms.CheckState.Checked;
         this.chkWrap.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
         this.chkWrap.Image = ((System.Drawing.Image)(resources.GetObject("chkWrap.Image")));
         this.chkWrap.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.chkWrap.Name = "chkWrap";
         this.chkWrap.Size = new System.Drawing.Size(23, 22);
         this.chkWrap.Text = "Wrap Action in Add";
         // 
         // txtScript
         // 
         this.txtScript.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
         this.txtScript.Font = new System.Drawing.Font("Consolas", 13F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
         this.txtScript.Location = new System.Drawing.Point(12, 52);
         this.txtScript.Margins.Margin0.Width = 20;
         this.txtScript.Name = "txtScript";
         this.txtScript.Scrolling.HorizontalWidth = 200;
         this.txtScript.Size = new System.Drawing.Size(637, 590);
         this.txtScript.Styles.BraceBad.Size = 13F;
         this.txtScript.Styles.BraceLight.Size = 13F;
         this.txtScript.Styles.ControlChar.Size = 13F;
         this.txtScript.Styles.Default.BackColor = System.Drawing.SystemColors.Window;
         this.txtScript.Styles.Default.Size = 13F;
         this.txtScript.Styles.IndentGuide.Size = 13F;
         this.txtScript.Styles.LastPredefined.Size = 13F;
         this.txtScript.Styles.LineNumber.Size = 13F;
         this.txtScript.Styles.Max.Size = 13F;
         this.txtScript.TabIndex = 7;
         // 
         // menuStrip1
         // 
         this.menuStrip1.AllowMerge = false;
         this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.fileToolStripMenuItem,
            this.actionsToolStripMenuItem});
         this.menuStrip1.Location = new System.Drawing.Point(0, 0);
         this.menuStrip1.Name = "menuStrip1";
         this.menuStrip1.Size = new System.Drawing.Size(661, 24);
         this.menuStrip1.TabIndex = 9;
         this.menuStrip1.Text = "menuStrip1";
         // 
         // fileToolStripMenuItem
         // 
         this.fileToolStripMenuItem.Name = "fileToolStripMenuItem";
         this.fileToolStripMenuItem.Size = new System.Drawing.Size(37, 20);
         this.fileToolStripMenuItem.Text = "File";
         // 
         // actionsToolStripMenuItem
         // 
         this.actionsToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.movementToolStripMenuItem,
            this.partyToolStripMenuItem,
            this.effectsToolStripMenuItem,
            this.uIToolStripMenuItem,
            this.uIToolStripMenuItem1});
         this.actionsToolStripMenuItem.Name = "actionsToolStripMenuItem";
         this.actionsToolStripMenuItem.Size = new System.Drawing.Size(59, 20);
         this.actionsToolStripMenuItem.Text = "Actions";
         // 
         // movementToolStripMenuItem
         // 
         this.movementToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.changeElevationToolStripMenuItem,
            this.createPathToolStripMenuItem,
            this.teleportPartyToolStripMenuItem,
            this.setFloatingToolStripMenuItem});
         this.movementToolStripMenuItem.Name = "movementToolStripMenuItem";
         this.movementToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
         this.movementToolStripMenuItem.Text = "Movement";
         // 
         // changeElevationToolStripMenuItem
         // 
         this.changeElevationToolStripMenuItem.Name = "changeElevationToolStripMenuItem";
         this.changeElevationToolStripMenuItem.Size = new System.Drawing.Size(183, 22);
         this.changeElevationToolStripMenuItem.Text = "changeElevation";
         this.changeElevationToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // createPathToolStripMenuItem
         // 
         this.createPathToolStripMenuItem.Name = "createPathToolStripMenuItem";
         this.createPathToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.P)));
         this.createPathToolStripMenuItem.Size = new System.Drawing.Size(183, 22);
         this.createPathToolStripMenuItem.Text = "createPath";
         this.createPathToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // teleportPartyToolStripMenuItem
         // 
         this.teleportPartyToolStripMenuItem.Name = "teleportPartyToolStripMenuItem";
         this.teleportPartyToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.T)));
         this.teleportPartyToolStripMenuItem.Size = new System.Drawing.Size(183, 22);
         this.teleportPartyToolStripMenuItem.Text = "teleportParty";
         this.teleportPartyToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // setFloatingToolStripMenuItem
         // 
         this.setFloatingToolStripMenuItem.Name = "setFloatingToolStripMenuItem";
         this.setFloatingToolStripMenuItem.Size = new System.Drawing.Size(183, 22);
         this.setFloatingToolStripMenuItem.Text = "setFloating";
         this.setFloatingToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // partyToolStripMenuItem
         // 
         this.partyToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.allowSavingToolStripMenuItem,
            this.restorePartyToolStripMenuItem,
            this.modifyPartyToolStripMenuItem,
            this.modifyInventoryToolStripMenuItem,
            this.modifyGoldToolStripMenuItem});
         this.partyToolStripMenuItem.Name = "partyToolStripMenuItem";
         this.partyToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
         this.partyToolStripMenuItem.Text = "Party";
         // 
         // restorePartyToolStripMenuItem
         // 
         this.restorePartyToolStripMenuItem.Name = "restorePartyToolStripMenuItem";
         this.restorePartyToolStripMenuItem.Size = new System.Drawing.Size(162, 22);
         this.restorePartyToolStripMenuItem.Text = "restoreParty";
         this.restorePartyToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // modifyPartyToolStripMenuItem
         // 
         this.modifyPartyToolStripMenuItem.Name = "modifyPartyToolStripMenuItem";
         this.modifyPartyToolStripMenuItem.Size = new System.Drawing.Size(162, 22);
         this.modifyPartyToolStripMenuItem.Text = "modifyParty";
         this.modifyPartyToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // modifyInventoryToolStripMenuItem
         // 
         this.modifyInventoryToolStripMenuItem.Name = "modifyInventoryToolStripMenuItem";
         this.modifyInventoryToolStripMenuItem.Size = new System.Drawing.Size(162, 22);
         this.modifyInventoryToolStripMenuItem.Text = "modifyInventory";
         this.modifyInventoryToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // modifyGoldToolStripMenuItem
         // 
         this.modifyGoldToolStripMenuItem.Name = "modifyGoldToolStripMenuItem";
         this.modifyGoldToolStripMenuItem.Size = new System.Drawing.Size(162, 22);
         this.modifyGoldToolStripMenuItem.Text = "modifyGold";
         this.modifyGoldToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // effectsToolStripMenuItem
         // 
         this.effectsToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.clearAnimationsToolStripMenuItem,
            this.closeBubbleToolStripMenuItem,
            this.fadeControlToolStripMenuItem,
            this.flashToolStripMenuItem,
            this.openBubbleToolStripMenuItem,
            this.panControlToolStripMenuItem,
            this.pauseMusicToolStripMenuItem,
            this.playAnimationToolStripMenuItem,
            this.playAnimationStoppedShortToolStripMenuItem,
            this.playMusicToolStripMenuItem,
            this.shakeControlToolStripMenuItem,
            this.showSceneToolStripMenuItem,
            this.unloadSceneToolStripMenuItem});
         this.effectsToolStripMenuItem.Name = "effectsToolStripMenuItem";
         this.effectsToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
         this.effectsToolStripMenuItem.Text = "Effects";
         // 
         // closeBubbleToolStripMenuItem
         // 
         this.closeBubbleToolStripMenuItem.Name = "closeBubbleToolStripMenuItem";
         this.closeBubbleToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.closeBubbleToolStripMenuItem.Text = "closeBubble";
         this.closeBubbleToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // fadeControlToolStripMenuItem
         // 
         this.fadeControlToolStripMenuItem.Name = "fadeControlToolStripMenuItem";
         this.fadeControlToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.F)));
         this.fadeControlToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.fadeControlToolStripMenuItem.Text = "fadeControl";
         this.fadeControlToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // openBubbleToolStripMenuItem
         // 
         this.openBubbleToolStripMenuItem.Name = "openBubbleToolStripMenuItem";
         this.openBubbleToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.openBubbleToolStripMenuItem.Text = "openBubble";
         this.openBubbleToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // panControlToolStripMenuItem
         // 
         this.panControlToolStripMenuItem.Name = "panControlToolStripMenuItem";
         this.panControlToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.panControlToolStripMenuItem.Text = "panControl";
         this.panControlToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // pauseMusicToolStripMenuItem
         // 
         this.pauseMusicToolStripMenuItem.Name = "pauseMusicToolStripMenuItem";
         this.pauseMusicToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.pauseMusicToolStripMenuItem.Text = "pauseMusic";
         this.pauseMusicToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // playMusicToolStripMenuItem
         // 
         this.playMusicToolStripMenuItem.Name = "playMusicToolStripMenuItem";
         this.playMusicToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.playMusicToolStripMenuItem.Text = "playMusic";
         this.playMusicToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // shakeControlToolStripMenuItem
         // 
         this.shakeControlToolStripMenuItem.Name = "shakeControlToolStripMenuItem";
         this.shakeControlToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.shakeControlToolStripMenuItem.Text = "shakeControl";
         this.shakeControlToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // showSceneToolStripMenuItem
         // 
         this.showSceneToolStripMenuItem.Name = "showSceneToolStripMenuItem";
         this.showSceneToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.showSceneToolStripMenuItem.Text = "showScene";
         this.showSceneToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // unloadSceneToolStripMenuItem
         // 
         this.unloadSceneToolStripMenuItem.Name = "unloadSceneToolStripMenuItem";
         this.unloadSceneToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.unloadSceneToolStripMenuItem.Text = "unloadScene";
         this.unloadSceneToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // uIToolStripMenuItem
         // 
         this.uIToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.waitToolStripMenuItem,
            this.resetGameToolStripMenuItem,
            this.expectInputToolStripMenuItem,
            this.switchControlToolStripMenuItem,
            this.startBattleToolStripMenuItem});
         this.uIToolStripMenuItem.Name = "uIToolStripMenuItem";
         this.uIToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
         this.uIToolStripMenuItem.Text = "System";
         // 
         // waitToolStripMenuItem
         // 
         this.waitToolStripMenuItem.Name = "waitToolStripMenuItem";
         this.waitToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.W)));
         this.waitToolStripMenuItem.Size = new System.Drawing.Size(188, 22);
         this.waitToolStripMenuItem.Text = "wait";
         this.waitToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // resetGameToolStripMenuItem
         // 
         this.resetGameToolStripMenuItem.Name = "resetGameToolStripMenuItem";
         this.resetGameToolStripMenuItem.Size = new System.Drawing.Size(188, 22);
         this.resetGameToolStripMenuItem.Text = "resetGame";
         this.resetGameToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // expectInputToolStripMenuItem
         // 
         this.expectInputToolStripMenuItem.Name = "expectInputToolStripMenuItem";
         this.expectInputToolStripMenuItem.Size = new System.Drawing.Size(188, 22);
         this.expectInputToolStripMenuItem.Text = "expectInput";
         this.expectInputToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // switchControlToolStripMenuItem
         // 
         this.switchControlToolStripMenuItem.Name = "switchControlToolStripMenuItem";
         this.switchControlToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.S)));
         this.switchControlToolStripMenuItem.Size = new System.Drawing.Size(188, 22);
         this.switchControlToolStripMenuItem.Text = "switchControl";
         this.switchControlToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // startBattleToolStripMenuItem
         // 
         this.startBattleToolStripMenuItem.Name = "startBattleToolStripMenuItem";
         this.startBattleToolStripMenuItem.Size = new System.Drawing.Size(188, 22);
         this.startBattleToolStripMenuItem.Text = "startBattle";
         this.startBattleToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // uIToolStripMenuItem1
         // 
         this.uIToolStripMenuItem1.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.openSaveMenuToolStripMenuItem,
            this.openNameSelectToolStripMenuItem,
            this.openMerchantToolStripMenuItem,
            this.messageToolStripMenuItem,
            this.messageTopToolStripMenuItem,
            this.messageWithYesNoToolStripMenuItem});
         this.uIToolStripMenuItem1.Name = "uIToolStripMenuItem1";
         this.uIToolStripMenuItem1.Size = new System.Drawing.Size(152, 22);
         this.uIToolStripMenuItem1.Text = "UI";
         // 
         // openSaveMenuToolStripMenuItem
         // 
         this.openSaveMenuToolStripMenuItem.Name = "openSaveMenuToolStripMenuItem";
         this.openSaveMenuToolStripMenuItem.Size = new System.Drawing.Size(179, 22);
         this.openSaveMenuToolStripMenuItem.Text = "openSaveMenu";
         this.openSaveMenuToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // openNameSelectToolStripMenuItem
         // 
         this.openNameSelectToolStripMenuItem.Name = "openNameSelectToolStripMenuItem";
         this.openNameSelectToolStripMenuItem.Size = new System.Drawing.Size(179, 22);
         this.openNameSelectToolStripMenuItem.Text = "openNameSelect";
         this.openNameSelectToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // openMerchantToolStripMenuItem
         // 
         this.openMerchantToolStripMenuItem.Name = "openMerchantToolStripMenuItem";
         this.openMerchantToolStripMenuItem.Size = new System.Drawing.Size(179, 22);
         this.openMerchantToolStripMenuItem.Text = "openMerchant";
         this.openMerchantToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // messageWithYesNoToolStripMenuItem
         // 
         this.messageWithYesNoToolStripMenuItem.Name = "messageWithYesNoToolStripMenuItem";
         this.messageWithYesNoToolStripMenuItem.Size = new System.Drawing.Size(179, 22);
         this.messageWithYesNoToolStripMenuItem.Text = "messageWithYesNo";
         this.messageWithYesNoToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // messageToolStripMenuItem
         // 
         this.messageToolStripMenuItem.Name = "messageToolStripMenuItem";
         this.messageToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.M)));
         this.messageToolStripMenuItem.Size = new System.Drawing.Size(179, 22);
         this.messageToolStripMenuItem.Text = "message";
         this.messageToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // flashToolStripMenuItem
         // 
         this.flashToolStripMenuItem.Name = "flashToolStripMenuItem";
         this.flashToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.flashToolStripMenuItem.Text = "flash";
         this.flashToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // allowSavingToolStripMenuItem
         // 
         this.allowSavingToolStripMenuItem.Name = "allowSavingToolStripMenuItem";
         this.allowSavingToolStripMenuItem.Size = new System.Drawing.Size(162, 22);
         this.allowSavingToolStripMenuItem.Text = "allowSaving";
         this.allowSavingToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // messageTopToolStripMenuItem
         // 
         this.messageTopToolStripMenuItem.Name = "messageTopToolStripMenuItem";
         this.messageTopToolStripMenuItem.Size = new System.Drawing.Size(179, 22);
         this.messageTopToolStripMenuItem.Text = "messageTop";
         this.messageTopToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // playAnimationToolStripMenuItem
         // 
         this.playAnimationToolStripMenuItem.Name = "playAnimationToolStripMenuItem";
         this.playAnimationToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.playAnimationToolStripMenuItem.Text = "playAnimation";
         this.playAnimationToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // playAnimationStoppedShortToolStripMenuItem
         // 
         this.playAnimationStoppedShortToolStripMenuItem.Name = "playAnimationStoppedShortToolStripMenuItem";
         this.playAnimationStoppedShortToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.playAnimationStoppedShortToolStripMenuItem.Text = "playAnimationStoppedShort";
         this.playAnimationStoppedShortToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // clearAnimationsToolStripMenuItem
         // 
         this.clearAnimationsToolStripMenuItem.Name = "clearAnimationsToolStripMenuItem";
         this.clearAnimationsToolStripMenuItem.Size = new System.Drawing.Size(224, 22);
         this.clearAnimationsToolStripMenuItem.Text = "clearAnimations";
         this.clearAnimationsToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // ObjectForm
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.ClientSize = new System.Drawing.Size(661, 683);
         this.Controls.Add(this.txtScript);
         this.Controls.Add(this.toolStrip1);
         this.Controls.Add(this.menuStrip1);
         this.Controls.Add(this.btnCancel);
         this.Controls.Add(this.btnSave);
         this.MainMenuStrip = this.menuStrip1;
         this.Name = "ObjectForm";
         this.ShowIcon = false;
         this.Text = "Game Object Editor";
         this.Load += new System.EventHandler(this.ObjectForm_Load);
         this.toolStrip1.ResumeLayout(false);
         this.toolStrip1.PerformLayout();
         ((System.ComponentModel.ISupportInitialize)(this.txtScript)).EndInit();
         this.menuStrip1.ResumeLayout(false);
         this.menuStrip1.PerformLayout();
         this.ResumeLayout(false);
         this.PerformLayout();

      }

      #endregion

      private System.Windows.Forms.Button btnSave;
      private System.Windows.Forms.Button btnCancel;
      private System.Windows.Forms.ToolStrip toolStrip1;
      private System.Windows.Forms.ToolStripLabel toolStripLabel1;
      private System.Windows.Forms.ToolStripTextBox txtX;
      private System.Windows.Forms.ToolStripTextBox txtY;
      private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
      private System.Windows.Forms.ToolStripButton tsbHeader;
      private System.Windows.Forms.ToolStripSeparator toolStripSeparator2;
      private System.Windows.Forms.ToolStripButton tsbAddObject;
      private System.Windows.Forms.ToolStripButton tsbNewState;
      private System.Windows.Forms.ToolStripButton tsbOutline;
      private System.Windows.Forms.ToolStripButton tsbBranchAction;
      private System.Windows.Forms.ToolStripLabel toolStripLabel2;
      private System.Windows.Forms.ToolStripTextBox txtTileX;
      private System.Windows.Forms.ToolStripTextBox txtTileY;
      private System.Windows.Forms.ToolStripButton tsbSpriteToTile;
      private ScintillaNET.Scintilla txtScript;
      private System.Windows.Forms.ToolStripDropDownButton tsbCollision;
      private System.Windows.Forms.ToolStripMenuItem allToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem noneToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem manualToolStripMenuItem;
      private System.Windows.Forms.MenuStrip menuStrip1;
      private System.Windows.Forms.ToolStripMenuItem fileToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem actionsToolStripMenuItem;
      private System.Windows.Forms.ToolStripButton tsbLoopBranch;
      private System.Windows.Forms.ToolStripButton chkWrap;
      private System.Windows.Forms.ToolStripMenuItem movementToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem changeElevationToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem createPathToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem teleportPartyToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem setFloatingToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem partyToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem restorePartyToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem modifyPartyToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem modifyInventoryToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem modifyGoldToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem effectsToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem closeBubbleToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem fadeControlToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem openBubbleToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem panControlToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem pauseMusicToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem playMusicToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem shakeControlToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem showSceneToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem unloadSceneToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem uIToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem waitToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem resetGameToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem expectInputToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem switchControlToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem uIToolStripMenuItem1;
      private System.Windows.Forms.ToolStripMenuItem openSaveMenuToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem openNameSelectToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem openMerchantToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem messageWithYesNoToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem messageToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem startBattleToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem flashToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem allowSavingToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem messageTopToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem playAnimationToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem playAnimationStoppedShortToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem clearAnimationsToolStripMenuItem;
   }
}