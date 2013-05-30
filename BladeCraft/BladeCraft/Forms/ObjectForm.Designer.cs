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
         this.txtScript = new ScintillaNET.Scintilla();
         this.toolStrip2 = new System.Windows.Forms.ToolStrip();
         this.chkWrap = new System.Windows.Forms.ToolStripButton();
         this.toolStripSeparator4 = new System.Windows.Forms.ToolStripSeparator();
         this.toolStripButton2 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton24 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton23 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton22 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton21 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton20 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton19 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton18 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton17 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton16 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton15 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton14 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton13 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton12 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton11 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton10 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton9 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton8 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton7 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton6 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton5 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton4 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton3 = new System.Windows.Forms.ToolStripButton();
         this.menuStrip1 = new System.Windows.Forms.MenuStrip();
         this.fileToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.actionsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.messageToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.fadeControlToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.createPathToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.waitToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.teleportPartyToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.switchControlToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.tsbLoopBranch = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton1 = new System.Windows.Forms.ToolStripButton();
         this.toolStripButton25 = new System.Windows.Forms.ToolStripButton();
         this.toolStrip1.SuspendLayout();
         ((System.ComponentModel.ISupportInitialize)(this.txtScript)).BeginInit();
         this.toolStrip2.SuspendLayout();
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
            this.tsbLoopBranch});
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
         // txtScript
         // 
         this.txtScript.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
         this.txtScript.Font = new System.Drawing.Font("Consolas", 13F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
         this.txtScript.Location = new System.Drawing.Point(120, 52);
         this.txtScript.Margins.Margin0.Width = 20;
         this.txtScript.Name = "txtScript";
         this.txtScript.Scrolling.HorizontalWidth = 200;
         this.txtScript.Size = new System.Drawing.Size(529, 590);
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
         // toolStrip2
         // 
         this.toolStrip2.Dock = System.Windows.Forms.DockStyle.Left;
         this.toolStrip2.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.chkWrap,
            this.toolStripSeparator4,
            this.toolStripButton12,
            this.toolStripButton10,
            this.toolStripButton3,
            this.toolStripButton2,
            this.toolStripButton23,
            this.toolStripButton24,
            this.toolStripButton22,
            this.toolStripButton21,
            this.toolStripButton20,
            this.toolStripButton13,
            this.toolStripButton18,
            this.toolStripButton17,
            this.toolStripButton5,
            this.toolStripButton11,
            this.toolStripButton14,
            this.toolStripButton15,
            this.toolStripButton16,
            this.toolStripButton9,
            this.toolStripButton8,
            this.toolStripButton19,
            this.toolStripButton7,
            this.toolStripButton6,
            this.toolStripButton4,
            this.toolStripButton1,
            this.toolStripButton25});
         this.toolStrip2.LayoutStyle = System.Windows.Forms.ToolStripLayoutStyle.VerticalStackWithOverflow;
         this.toolStrip2.Location = new System.Drawing.Point(0, 49);
         this.toolStrip2.Name = "toolStrip2";
         this.toolStrip2.Size = new System.Drawing.Size(117, 634);
         this.toolStrip2.TabIndex = 8;
         this.toolStrip2.Text = "toolStrip2";
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
         this.chkWrap.Size = new System.Drawing.Size(114, 20);
         this.chkWrap.Text = "Wrap Action in Add";
         // 
         // toolStripSeparator4
         // 
         this.toolStripSeparator4.Name = "toolStripSeparator4";
         this.toolStripSeparator4.Size = new System.Drawing.Size(114, 6);
         // 
         // toolStripButton2
         // 
         this.toolStripButton2.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton2.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton2.Image")));
         this.toolStripButton2.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton2.Name = "toolStripButton2";
         this.toolStripButton2.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton2.Text = "fadeControl";
         this.toolStripButton2.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton24
         // 
         this.toolStripButton24.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton24.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton24.Image")));
         this.toolStripButton24.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton24.Name = "toolStripButton24";
         this.toolStripButton24.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton24.Text = "messageWithYesNo";
         this.toolStripButton24.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton23
         // 
         this.toolStripButton23.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton23.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton23.Image")));
         this.toolStripButton23.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton23.Name = "toolStripButton23";
         this.toolStripButton23.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton23.Text = "message";
         this.toolStripButton23.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton22
         // 
         this.toolStripButton22.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton22.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton22.Image")));
         this.toolStripButton22.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton22.Name = "toolStripButton22";
         this.toolStripButton22.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton22.Text = "modifyGold";
         this.toolStripButton22.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton21
         // 
         this.toolStripButton21.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton21.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton21.Image")));
         this.toolStripButton21.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton21.Name = "toolStripButton21";
         this.toolStripButton21.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton21.Text = "modifyInventory";
         this.toolStripButton21.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton20
         // 
         this.toolStripButton20.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton20.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton20.Image")));
         this.toolStripButton20.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton20.Name = "toolStripButton20";
         this.toolStripButton20.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton20.Text = "modifyParty";
         this.toolStripButton20.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton19
         // 
         this.toolStripButton19.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton19.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton19.Image")));
         this.toolStripButton19.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton19.Name = "toolStripButton19";
         this.toolStripButton19.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton19.Text = "showScene";
         this.toolStripButton19.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton18
         // 
         this.toolStripButton18.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton18.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton18.Image")));
         this.toolStripButton18.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton18.Name = "toolStripButton18";
         this.toolStripButton18.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton18.Text = "openMerchant";
         this.toolStripButton18.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton17
         // 
         this.toolStripButton17.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton17.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton17.Image")));
         this.toolStripButton17.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton17.Name = "toolStripButton17";
         this.toolStripButton17.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton17.Text = "openNameSelect";
         this.toolStripButton17.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton16
         // 
         this.toolStripButton16.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton16.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton16.Image")));
         this.toolStripButton16.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton16.Name = "toolStripButton16";
         this.toolStripButton16.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton16.Text = "resetGame";
         this.toolStripButton16.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton15
         // 
         this.toolStripButton15.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton15.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton15.Image")));
         this.toolStripButton15.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton15.Name = "toolStripButton15";
         this.toolStripButton15.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton15.Text = "playMusic";
         this.toolStripButton15.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton14
         // 
         this.toolStripButton14.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton14.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton14.Image")));
         this.toolStripButton14.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton14.Name = "toolStripButton14";
         this.toolStripButton14.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton14.Text = "pauseMusic";
         this.toolStripButton14.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton13
         // 
         this.toolStripButton13.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton13.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton13.Image")));
         this.toolStripButton13.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton13.Name = "toolStripButton13";
         this.toolStripButton13.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton13.Text = "openBubble";
         this.toolStripButton13.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton12
         // 
         this.toolStripButton12.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton12.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton12.Image")));
         this.toolStripButton12.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton12.Name = "toolStripButton12";
         this.toolStripButton12.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton12.Text = "closeBubble";
         this.toolStripButton12.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton11
         // 
         this.toolStripButton11.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton11.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton11.Image")));
         this.toolStripButton11.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton11.Name = "toolStripButton11";
         this.toolStripButton11.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton11.Text = "panControl";
         this.toolStripButton11.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton10
         // 
         this.toolStripButton10.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton10.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton10.Image")));
         this.toolStripButton10.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton10.Name = "toolStripButton10";
         this.toolStripButton10.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton10.Text = "createPath";
         this.toolStripButton10.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton9
         // 
         this.toolStripButton9.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton9.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton9.Image")));
         this.toolStripButton9.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton9.Name = "toolStripButton9";
         this.toolStripButton9.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton9.Text = "restoreParty";
         this.toolStripButton9.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton8
         // 
         this.toolStripButton8.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton8.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton8.Image")));
         this.toolStripButton8.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton8.Name = "toolStripButton8";
         this.toolStripButton8.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton8.Text = "shakeControl";
         this.toolStripButton8.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton7
         // 
         this.toolStripButton7.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton7.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton7.Image")));
         this.toolStripButton7.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton7.Name = "toolStripButton7";
         this.toolStripButton7.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton7.Text = "startBattle";
         this.toolStripButton7.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton6
         // 
         this.toolStripButton6.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton6.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton6.Image")));
         this.toolStripButton6.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton6.Name = "toolStripButton6";
         this.toolStripButton6.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton6.Text = "switchControl";
         this.toolStripButton6.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton5
         // 
         this.toolStripButton5.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton5.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton5.Image")));
         this.toolStripButton5.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton5.Name = "toolStripButton5";
         this.toolStripButton5.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton5.Text = "openSaveMenu";
         this.toolStripButton5.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton4
         // 
         this.toolStripButton4.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton4.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton4.Image")));
         this.toolStripButton4.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton4.Name = "toolStripButton4";
         this.toolStripButton4.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton4.Text = "teleportParty";
         this.toolStripButton4.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton3
         // 
         this.toolStripButton3.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton3.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton3.Image")));
         this.toolStripButton3.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton3.Name = "toolStripButton3";
         this.toolStripButton3.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton3.Text = "expectInput";
         this.toolStripButton3.Click += new System.EventHandler(this.actionClick);
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
            this.messageToolStripMenuItem,
            this.fadeControlToolStripMenuItem,
            this.createPathToolStripMenuItem,
            this.waitToolStripMenuItem,
            this.teleportPartyToolStripMenuItem,
            this.switchControlToolStripMenuItem});
         this.actionsToolStripMenuItem.Name = "actionsToolStripMenuItem";
         this.actionsToolStripMenuItem.Size = new System.Drawing.Size(59, 20);
         this.actionsToolStripMenuItem.Text = "Actions";
         // 
         // messageToolStripMenuItem
         // 
         this.messageToolStripMenuItem.Name = "messageToolStripMenuItem";
         this.messageToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.M)));
         this.messageToolStripMenuItem.Size = new System.Drawing.Size(188, 22);
         this.messageToolStripMenuItem.Text = "message";
         this.messageToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // fadeControlToolStripMenuItem
         // 
         this.fadeControlToolStripMenuItem.Name = "fadeControlToolStripMenuItem";
         this.fadeControlToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.F)));
         this.fadeControlToolStripMenuItem.Size = new System.Drawing.Size(188, 22);
         this.fadeControlToolStripMenuItem.Text = "fadeControl";
         this.fadeControlToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // createPathToolStripMenuItem
         // 
         this.createPathToolStripMenuItem.Name = "createPathToolStripMenuItem";
         this.createPathToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.P)));
         this.createPathToolStripMenuItem.Size = new System.Drawing.Size(188, 22);
         this.createPathToolStripMenuItem.Text = "createPath";
         this.createPathToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // waitToolStripMenuItem
         // 
         this.waitToolStripMenuItem.Name = "waitToolStripMenuItem";
         this.waitToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.W)));
         this.waitToolStripMenuItem.Size = new System.Drawing.Size(188, 22);
         this.waitToolStripMenuItem.Text = "wait";
         this.waitToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // teleportPartyToolStripMenuItem
         // 
         this.teleportPartyToolStripMenuItem.Name = "teleportPartyToolStripMenuItem";
         this.teleportPartyToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.T)));
         this.teleportPartyToolStripMenuItem.Size = new System.Drawing.Size(188, 22);
         this.teleportPartyToolStripMenuItem.Text = "teleportParty";
         this.teleportPartyToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
         // 
         // switchControlToolStripMenuItem
         // 
         this.switchControlToolStripMenuItem.Name = "switchControlToolStripMenuItem";
         this.switchControlToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.S)));
         this.switchControlToolStripMenuItem.Size = new System.Drawing.Size(188, 22);
         this.switchControlToolStripMenuItem.Text = "switchControl";
         this.switchControlToolStripMenuItem.Click += new System.EventHandler(this.actionClick);
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
         // toolStripButton1
         // 
         this.toolStripButton1.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton1.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton1.Image")));
         this.toolStripButton1.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton1.Name = "toolStripButton1";
         this.toolStripButton1.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton1.Text = "unloadScene";
         this.toolStripButton1.Click += new System.EventHandler(this.actionClick);
         // 
         // toolStripButton25
         // 
         this.toolStripButton25.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
         this.toolStripButton25.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton25.Image")));
         this.toolStripButton25.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripButton25.Name = "toolStripButton25";
         this.toolStripButton25.Size = new System.Drawing.Size(114, 19);
         this.toolStripButton25.Text = "wait";
         this.toolStripButton25.Click += new System.EventHandler(this.actionClick);
         // 
         // ObjectForm
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.ClientSize = new System.Drawing.Size(661, 683);
         this.Controls.Add(this.toolStrip2);
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
         this.toolStrip2.ResumeLayout(false);
         this.toolStrip2.PerformLayout();
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
      private System.Windows.Forms.ToolStrip toolStrip2;
      private System.Windows.Forms.ToolStripButton chkWrap;
      private System.Windows.Forms.ToolStripSeparator toolStripSeparator4;
      private System.Windows.Forms.ToolStripButton toolStripButton2;
      private System.Windows.Forms.ToolStripButton toolStripButton24;
      private System.Windows.Forms.ToolStripButton toolStripButton23;
      private System.Windows.Forms.ToolStripButton toolStripButton22;
      private System.Windows.Forms.ToolStripButton toolStripButton21;
      private System.Windows.Forms.ToolStripButton toolStripButton20;
      private System.Windows.Forms.ToolStripButton toolStripButton19;
      private System.Windows.Forms.ToolStripButton toolStripButton18;
      private System.Windows.Forms.ToolStripButton toolStripButton17;
      private System.Windows.Forms.ToolStripButton toolStripButton16;
      private System.Windows.Forms.ToolStripButton toolStripButton15;
      private System.Windows.Forms.ToolStripButton toolStripButton14;
      private System.Windows.Forms.ToolStripButton toolStripButton13;
      private System.Windows.Forms.ToolStripButton toolStripButton12;
      private System.Windows.Forms.ToolStripButton toolStripButton11;
      private System.Windows.Forms.ToolStripButton toolStripButton10;
      private System.Windows.Forms.ToolStripButton toolStripButton9;
      private System.Windows.Forms.ToolStripButton toolStripButton8;
      private System.Windows.Forms.ToolStripButton toolStripButton7;
      private System.Windows.Forms.ToolStripButton toolStripButton6;
      private System.Windows.Forms.ToolStripButton toolStripButton5;
      private System.Windows.Forms.ToolStripButton toolStripButton4;
      private System.Windows.Forms.ToolStripButton toolStripButton3;
      private System.Windows.Forms.MenuStrip menuStrip1;
      private System.Windows.Forms.ToolStripMenuItem fileToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem actionsToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem messageToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem fadeControlToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem createPathToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem waitToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem teleportPartyToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem switchControlToolStripMenuItem;
      private System.Windows.Forms.ToolStripButton tsbLoopBranch;
      private System.Windows.Forms.ToolStripButton toolStripButton1;
      private System.Windows.Forms.ToolStripButton toolStripButton25;
   }
}