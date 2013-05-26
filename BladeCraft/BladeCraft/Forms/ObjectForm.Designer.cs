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
         this.toolStripSeparator3 = new System.Windows.Forms.ToolStripSeparator();
         this.toolStripLabel3 = new System.Windows.Forms.ToolStripLabel();
         this.cmbActions = new System.Windows.Forms.ToolStripComboBox();
         this.toolStripLabel4 = new System.Windows.Forms.ToolStripLabel();
         this.comPath = new System.Windows.Forms.ToolStripComboBox();
         this.txtScript = new ScintillaNET.Scintilla();
         this.toolStrip1.SuspendLayout();
         ((System.ComponentModel.ISupportInitialize)(this.txtScript)).BeginInit();
         this.SuspendLayout();
         // 
         // btnSave
         // 
         this.btnSave.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
         this.btnSave.Location = new System.Drawing.Point(513, 342);
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
         this.btnCancel.Location = new System.Drawing.Point(594, 342);
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
            this.toolStripSeparator3,
            this.toolStripLabel3,
            this.cmbActions,
            this.toolStripLabel4,
            this.comPath});
         this.toolStrip1.Location = new System.Drawing.Point(0, 0);
         this.toolStrip1.Name = "toolStrip1";
         this.toolStrip1.Size = new System.Drawing.Size(681, 25);
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
         this.allToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
         this.allToolStripMenuItem.Text = "All";
         this.allToolStripMenuItem.Click += new System.EventHandler(this.allToolStripMenuItem_Click);
         // 
         // noneToolStripMenuItem
         // 
         this.noneToolStripMenuItem.Name = "noneToolStripMenuItem";
         this.noneToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
         this.noneToolStripMenuItem.Text = "None";
         this.noneToolStripMenuItem.Click += new System.EventHandler(this.noneToolStripMenuItem_Click);
         // 
         // manualToolStripMenuItem
         // 
         this.manualToolStripMenuItem.Name = "manualToolStripMenuItem";
         this.manualToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
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
         // toolStripSeparator3
         // 
         this.toolStripSeparator3.Name = "toolStripSeparator3";
         this.toolStripSeparator3.Size = new System.Drawing.Size(6, 25);
         // 
         // toolStripLabel3
         // 
         this.toolStripLabel3.Name = "toolStripLabel3";
         this.toolStripLabel3.Size = new System.Drawing.Size(50, 22);
         this.toolStripLabel3.Text = "Actions:";
         // 
         // cmbActions
         // 
         this.cmbActions.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
         this.cmbActions.Items.AddRange(new object[] {
            "closeBubble",
            "createPath",
            "fadeControl",
            "message",
            "messageWithYesNo",
            "modifyGold",
            "modifyInventory",
            "modifyParty",
            "openBubble",
            "openMerchant",
            "openNameSelect",
            "openSaveMenu",
            "panControl",
            "pauseMusic",
            "playMusic",
            "resetgame",
            "restoreParty",
            "shakeControl",
            "showScene",
            "startBattle",
            "switchControl",
            "teleportParty",
            "wait seconds"});
         this.cmbActions.Name = "cmbActions";
         this.cmbActions.Size = new System.Drawing.Size(75, 25);
         this.cmbActions.ToolTipText = "Add Action";
         this.cmbActions.DropDownClosed += new System.EventHandler(this.cmbActions_DropDownClosed);
         // 
         // toolStripLabel4
         // 
         this.toolStripLabel4.Name = "toolStripLabel4";
         this.toolStripLabel4.Size = new System.Drawing.Size(51, 22);
         this.toolStripLabel4.Text = "Pathing:";
         // 
         // comPath
         // 
         this.comPath.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
         this.comPath.Items.AddRange(new object[] {
            "MoveLeft",
            "MoveUp",
            "MoveRight",
            "MoveDown",
            "FaceLeft",
            "FaceUp",
            "FaceRight",
            "FaceDown",
            "LockFacing",
            "UnlockFacing",
            "IncreaseMoveSpeed",
            "DecreaseMoveSpeed",
            "Hide",
            "Show",
            "Wait"});
         this.comPath.Name = "comPath";
         this.comPath.Size = new System.Drawing.Size(75, 25);
         this.comPath.DropDownClosed += new System.EventHandler(this.comPath_DropDownClosed);
         this.comPath.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.comPath_KeyPress);
         // 
         // txtScript
         // 
         this.txtScript.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
         this.txtScript.Font = new System.Drawing.Font("Consolas", 13F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
         this.txtScript.Location = new System.Drawing.Point(13, 29);
         this.txtScript.Margins.Margin0.Width = 20;
         this.txtScript.Name = "txtScript";
         this.txtScript.Scrolling.HorizontalWidth = 200;
         this.txtScript.Size = new System.Drawing.Size(656, 307);
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
         // ObjectForm
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.ClientSize = new System.Drawing.Size(681, 377);
         this.Controls.Add(this.txtScript);
         this.Controls.Add(this.toolStrip1);
         this.Controls.Add(this.btnCancel);
         this.Controls.Add(this.btnSave);
         this.Name = "ObjectForm";
         this.ShowIcon = false;
         this.Text = "Game Object Editor";
         this.Load += new System.EventHandler(this.ObjectForm_Load);
         this.toolStrip1.ResumeLayout(false);
         this.toolStrip1.PerformLayout();
         ((System.ComponentModel.ISupportInitialize)(this.txtScript)).EndInit();
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
      private System.Windows.Forms.ToolStripComboBox cmbActions;
      private System.Windows.Forms.ToolStripLabel toolStripLabel2;
      private System.Windows.Forms.ToolStripTextBox txtTileX;
      private System.Windows.Forms.ToolStripTextBox txtTileY;
      private System.Windows.Forms.ToolStripButton tsbSpriteToTile;
      private System.Windows.Forms.ToolStripSeparator toolStripSeparator3;
      private System.Windows.Forms.ToolStripLabel toolStripLabel3;
      private ScintillaNET.Scintilla txtScript;
      private System.Windows.Forms.ToolStripLabel toolStripLabel4;
      private System.Windows.Forms.ToolStripComboBox comPath;
      private System.Windows.Forms.ToolStripDropDownButton tsbCollision;
      private System.Windows.Forms.ToolStripMenuItem allToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem noneToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem manualToolStripMenuItem;
   }
}