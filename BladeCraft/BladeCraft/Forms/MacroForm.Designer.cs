namespace BladeCraft.Forms
{
   partial class MacroForm
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
         System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MacroForm));
         this.toolStrip1 = new System.Windows.Forms.ToolStrip();
         this.saveButton1 = new System.Windows.Forms.ToolStripButton();
         this.TileSetTreeView = new System.Windows.Forms.TreeView();
         this.macroFrame = new System.Windows.Forms.Panel();
         this.macroPanel = new BladeCraft.Classes.DBPanel();
         this.tsPanel = new BladeCraft.Classes.DBPanel();
         this.tilesetPanel = new System.Windows.Forms.Panel();
         this.MacroTreeView = new System.Windows.Forms.TreeView();
         this.toolStrip1.SuspendLayout();
         this.macroFrame.SuspendLayout();
         this.tilesetPanel.SuspendLayout();
         this.SuspendLayout();
         // 
         // toolStrip1
         // 
         this.toolStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.saveButton1});
         this.toolStrip1.Location = new System.Drawing.Point(0, 0);
         this.toolStrip1.Name = "toolStrip1";
         this.toolStrip1.Size = new System.Drawing.Size(1079, 25);
         this.toolStrip1.TabIndex = 13;
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
         // 
         // TileSetTreeView
         // 
         this.TileSetTreeView.Location = new System.Drawing.Point(326, 28);
         this.TileSetTreeView.Name = "TileSetTreeView";
         this.TileSetTreeView.Size = new System.Drawing.Size(320, 364);
         this.TileSetTreeView.TabIndex = 12;
         // 
         // macroFrame
         // 
         this.macroFrame.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
         this.macroFrame.AutoScroll = true;
         this.macroFrame.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
         this.macroFrame.Controls.Add(this.macroPanel);
         this.macroFrame.Location = new System.Drawing.Point(652, 28);
         this.macroFrame.Name = "macroFrame";
         this.macroFrame.Size = new System.Drawing.Size(416, 594);
         this.macroFrame.TabIndex = 11;
         // 
         // macroPanel
         // 
         this.macroPanel.Location = new System.Drawing.Point(4, 4);
         this.macroPanel.Name = "macroPanel";
         this.macroPanel.Size = new System.Drawing.Size(200, 100);
         this.macroPanel.TabIndex = 0;
         // 
         // tsPanel
         // 
         this.tsPanel.Location = new System.Drawing.Point(4, 4);
         this.tsPanel.Name = "tsPanel";
         this.tsPanel.Size = new System.Drawing.Size(84, 100);
         this.tsPanel.TabIndex = 0;
         // 
         // tilesetPanel
         // 
         this.tilesetPanel.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
         this.tilesetPanel.AutoScroll = true;
         this.tilesetPanel.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
         this.tilesetPanel.Controls.Add(this.tsPanel);
         this.tilesetPanel.Location = new System.Drawing.Point(326, 398);
         this.tilesetPanel.Name = "tilesetPanel";
         this.tilesetPanel.Size = new System.Drawing.Size(320, 224);
         this.tilesetPanel.TabIndex = 14;
         // 
         // MacroTreeView
         // 
         this.MacroTreeView.Location = new System.Drawing.Point(0, 28);
         this.MacroTreeView.Name = "MacroTreeView";
         this.MacroTreeView.Size = new System.Drawing.Size(320, 588);
         this.MacroTreeView.TabIndex = 15;
         // 
         // MacroForm
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.ClientSize = new System.Drawing.Size(1079, 628);
         this.Controls.Add(this.MacroTreeView);
         this.Controls.Add(this.tilesetPanel);
         this.Controls.Add(this.toolStrip1);
         this.Controls.Add(this.TileSetTreeView);
         this.Controls.Add(this.macroFrame);
         this.Name = "MacroForm";
         this.Text = "MacroForm";
         this.toolStrip1.ResumeLayout(false);
         this.toolStrip1.PerformLayout();
         this.macroFrame.ResumeLayout(false);
         this.tilesetPanel.ResumeLayout(false);
         this.ResumeLayout(false);
         this.PerformLayout();

      }

      #endregion

      private System.Windows.Forms.ToolStrip toolStrip1;
      private System.Windows.Forms.ToolStripButton saveButton1;
      private System.Windows.Forms.TreeView TileSetTreeView;
      private System.Windows.Forms.Panel macroFrame;
      private Classes.DBPanel macroPanel;
      private Classes.DBPanel tsPanel;
      private System.Windows.Forms.Panel tilesetPanel;
      private System.Windows.Forms.TreeView MacroTreeView;
   }
}