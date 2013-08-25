namespace BladeCraft.Forms
{
   partial class CollisionForm
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
         System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(CollisionForm));
         this.CollisionFrame = new System.Windows.Forms.Panel();
         this.CollisionPanel = new BladeCraft.Classes.DBPanel();
         this.TileSetTreeView = new System.Windows.Forms.TreeView();
         this.toolStrip1 = new System.Windows.Forms.ToolStrip();
         this.saveButton1 = new System.Windows.Forms.ToolStripButton();
         this.toolStripDropDownButton1 = new System.Windows.Forms.ToolStripDropDownButton();
         this.leftToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.rightToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.topToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.bottomToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.toolStripSeparator3 = new System.Windows.Forms.ToolStripSeparator();
         this.allToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.noneToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.CollisionFrame.SuspendLayout();
         this.toolStrip1.SuspendLayout();
         this.SuspendLayout();
         // 
         // CollisionFrame
         // 
         this.CollisionFrame.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
         this.CollisionFrame.AutoScroll = true;
         this.CollisionFrame.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
         this.CollisionFrame.Controls.Add(this.CollisionPanel);
         this.CollisionFrame.Location = new System.Drawing.Point(348, 36);
         this.CollisionFrame.Name = "CollisionFrame";
         this.CollisionFrame.Size = new System.Drawing.Size(436, 485);
         this.CollisionFrame.TabIndex = 3;
         // 
         // CollisionPanel
         // 
         this.CollisionPanel.Location = new System.Drawing.Point(4, 4);
         this.CollisionPanel.Name = "CollisionPanel";
         this.CollisionPanel.Size = new System.Drawing.Size(200, 100);
         this.CollisionPanel.TabIndex = 0;
         this.CollisionPanel.Paint += new System.Windows.Forms.PaintEventHandler(this.CollisionPanel_Paint);
         this.CollisionPanel.MouseClick += new System.Windows.Forms.MouseEventHandler(this.CollisionPanel_MouseClick);
         // 
         // TileSetTreeView
         // 
         this.TileSetTreeView.Location = new System.Drawing.Point(22, 36);
         this.TileSetTreeView.Name = "TileSetTreeView";
         this.TileSetTreeView.Size = new System.Drawing.Size(320, 485);
         this.TileSetTreeView.TabIndex = 9;
         this.TileSetTreeView.NodeMouseClick += new System.Windows.Forms.TreeNodeMouseClickEventHandler(this.TileSetTreeView_NodeMouseClick);
         // 
         // toolStrip1
         // 
         this.toolStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.saveButton1,
            this.toolStripDropDownButton1});
         this.toolStrip1.Location = new System.Drawing.Point(0, 0);
         this.toolStrip1.Name = "toolStrip1";
         this.toolStrip1.Size = new System.Drawing.Size(796, 25);
         this.toolStrip1.TabIndex = 10;
         this.toolStrip1.Text = "toolStrip1";
         // 
         // saveButton1
         // 
         this.saveButton1.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
         this.saveButton1.Image = ((System.Drawing.Image)(resources.GetObject("saveButton1.Image")));
         this.saveButton1.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.saveButton1.Name = "saveButton1";
         this.saveButton1.Size = new System.Drawing.Size(23, 22);
         this.saveButton1.Text = "toolStripButton1";
         this.saveButton1.ToolTipText = "Save Map";
         this.saveButton1.Click += new System.EventHandler(this.saveButton1_Click);
         // 
         // toolStripDropDownButton1
         // 
         this.toolStripDropDownButton1.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
         this.toolStripDropDownButton1.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.leftToolStripMenuItem,
            this.rightToolStripMenuItem,
            this.topToolStripMenuItem,
            this.bottomToolStripMenuItem,
            this.toolStripSeparator3,
            this.allToolStripMenuItem,
            this.noneToolStripMenuItem});
         this.toolStripDropDownButton1.Image = ((System.Drawing.Image)(resources.GetObject("toolStripDropDownButton1.Image")));
         this.toolStripDropDownButton1.ImageTransparentColor = System.Drawing.Color.Magenta;
         this.toolStripDropDownButton1.Name = "toolStripDropDownButton1";
         this.toolStripDropDownButton1.Size = new System.Drawing.Size(29, 22);
         this.toolStripDropDownButton1.Text = "toolStripDropDownButton1";
         // 
         // leftToolStripMenuItem
         // 
         this.leftToolStripMenuItem.CheckOnClick = true;
         this.leftToolStripMenuItem.Name = "leftToolStripMenuItem";
         this.leftToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
         this.leftToolStripMenuItem.Text = "Left";
         // 
         // rightToolStripMenuItem
         // 
         this.rightToolStripMenuItem.CheckOnClick = true;
         this.rightToolStripMenuItem.Name = "rightToolStripMenuItem";
         this.rightToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
         this.rightToolStripMenuItem.Text = "Right";
         // 
         // topToolStripMenuItem
         // 
         this.topToolStripMenuItem.CheckOnClick = true;
         this.topToolStripMenuItem.Name = "topToolStripMenuItem";
         this.topToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
         this.topToolStripMenuItem.Text = "Top";
         // 
         // bottomToolStripMenuItem
         // 
         this.bottomToolStripMenuItem.CheckOnClick = true;
         this.bottomToolStripMenuItem.Name = "bottomToolStripMenuItem";
         this.bottomToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
         this.bottomToolStripMenuItem.Text = "Bottom";
         // 
         // toolStripSeparator3
         // 
         this.toolStripSeparator3.Name = "toolStripSeparator3";
         this.toolStripSeparator3.Size = new System.Drawing.Size(149, 6);
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
         // CollisionForm
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.ClientSize = new System.Drawing.Size(796, 533);
         this.Controls.Add(this.toolStrip1);
         this.Controls.Add(this.TileSetTreeView);
         this.Controls.Add(this.CollisionFrame);
         this.Name = "CollisionForm";
         this.Text = "Default Collision";
         this.CollisionFrame.ResumeLayout(false);
         this.toolStrip1.ResumeLayout(false);
         this.toolStrip1.PerformLayout();
         this.ResumeLayout(false);
         this.PerformLayout();

      }

      #endregion

      private System.Windows.Forms.Panel CollisionFrame;
      private Classes.DBPanel CollisionPanel;
      private System.Windows.Forms.TreeView TileSetTreeView;
      private System.Windows.Forms.ToolStrip toolStrip1;
      private System.Windows.Forms.ToolStripButton saveButton1;
      private System.Windows.Forms.ToolStripDropDownButton toolStripDropDownButton1;
      private System.Windows.Forms.ToolStripMenuItem leftToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem rightToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem topToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem bottomToolStripMenuItem;
      private System.Windows.Forms.ToolStripSeparator toolStripSeparator3;
      private System.Windows.Forms.ToolStripMenuItem allToolStripMenuItem;
      private System.Windows.Forms.ToolStripMenuItem noneToolStripMenuItem;

   }
}