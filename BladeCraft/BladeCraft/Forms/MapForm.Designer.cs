﻿namespace BladeCraft.Forms
{
    partial class MapForm
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
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MapForm));
            this.toolstrip = new System.Windows.Forms.ToolStrip();
            this.btnDraw = new System.Windows.Forms.ToolStripButton();
            this.tsbShowGrid = new System.Windows.Forms.ToolStripButton();
            this.toolStripSeparator5 = new System.Windows.Forms.ToolStripSeparator();
            this.toolStripLabel2 = new System.Windows.Forms.ToolStripLabel();
            this.tsbLayer = new System.Windows.Forms.ToolStripTextBox();
            this.tsbSwapLayers = new System.Windows.Forms.ToolStripButton();
            this.tsbLayerSwapTo = new System.Windows.Forms.ToolStripTextBox();
            this.toolStripSeparator2 = new System.Windows.Forms.ToolStripSeparator();
            this.tsbCollision = new System.Windows.Forms.ToolStripButton();
            this.toolStripDropDownButton1 = new System.Windows.Forms.ToolStripDropDownButton();
            this.leftToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.rightToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.topToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.bottomToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator3 = new System.Windows.Forms.ToolStripSeparator();
            this.allToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.noneToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator4 = new System.Windows.Forms.ToolStripSeparator();
            this.tsbObjectLayer = new System.Windows.Forms.ToolStripButton();
            this.toolStripButton2 = new System.Windows.Forms.ToolStripButton();
            this.tsbEncounters = new System.Windows.Forms.ToolStripButton();
            this.tsbOptions = new System.Windows.Forms.ToolStripButton();
            this.toolStripButton1 = new System.Windows.Forms.ToolStripButton();
            this.tilesetPanel = new System.Windows.Forms.Panel();
            this.mapFrame = new System.Windows.Forms.Panel();
            this.numMapZoom = new System.Windows.Forms.NumericUpDown();
            this.label1 = new System.Windows.Forms.Label();
            this.rightClickMenu = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.newObjectToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator7 = new System.Windows.Forms.ToolStripSeparator();
            this.deleteToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.deleteZoneToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator6 = new System.Windows.Forms.ToolStripSeparator();
            this.copyToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.pasteToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.TileSetTreeView = new System.Windows.Forms.TreeView();
            this.RedrawTimer = new System.Windows.Forms.Timer(this.components);
            this.mapPanel = new BladeCraft.Classes.DBPanel();
            this.tsPanel = new BladeCraft.Classes.DBPanel();
            this.toolstrip.SuspendLayout();
            this.tilesetPanel.SuspendLayout();
            this.mapFrame.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.numMapZoom)).BeginInit();
            this.rightClickMenu.SuspendLayout();
            this.SuspendLayout();
            // 
            // toolstrip
            // 
            this.toolstrip.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.btnDraw,
            this.tsbShowGrid,
            this.toolStripSeparator5,
            this.toolStripLabel2,
            this.tsbLayer,
            this.tsbSwapLayers,
            this.tsbLayerSwapTo,
            this.toolStripSeparator2,
            this.tsbCollision,
            this.toolStripDropDownButton1,
            this.toolStripSeparator4,
            this.tsbObjectLayer,
            this.toolStripButton2,
            this.tsbEncounters,
            this.tsbOptions,
            this.toolStripButton1});
            this.toolstrip.Location = new System.Drawing.Point(0, 0);
            this.toolstrip.Name = "toolstrip";
            this.toolstrip.Size = new System.Drawing.Size(752, 25);
            this.toolstrip.TabIndex = 0;
            this.toolstrip.Text = "toolStrip1";
            // 
            // btnDraw
            // 
            this.btnDraw.Checked = true;
            this.btnDraw.CheckOnClick = true;
            this.btnDraw.CheckState = System.Windows.Forms.CheckState.Checked;
            this.btnDraw.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.btnDraw.Image = ((System.Drawing.Image)(resources.GetObject("btnDraw.Image")));
            this.btnDraw.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.btnDraw.Name = "btnDraw";
            this.btnDraw.Size = new System.Drawing.Size(23, 22);
            this.btnDraw.Text = "btnDraw";
            // 
            // tsbShowGrid
            // 
            this.tsbShowGrid.Checked = true;
            this.tsbShowGrid.CheckOnClick = true;
            this.tsbShowGrid.CheckState = System.Windows.Forms.CheckState.Checked;
            this.tsbShowGrid.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.tsbShowGrid.Image = ((System.Drawing.Image)(resources.GetObject("tsbShowGrid.Image")));
            this.tsbShowGrid.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.tsbShowGrid.Name = "tsbShowGrid";
            this.tsbShowGrid.Size = new System.Drawing.Size(23, 22);
            this.tsbShowGrid.Text = "Show/Hide Grid";
            this.tsbShowGrid.CheckStateChanged += new System.EventHandler(this.tsbShowGrid_CheckStateChanged);
            // 
            // toolStripSeparator5
            // 
            this.toolStripSeparator5.Name = "toolStripSeparator5";
            this.toolStripSeparator5.Size = new System.Drawing.Size(6, 25);
            // 
            // toolStripLabel2
            // 
            this.toolStripLabel2.Name = "toolStripLabel2";
            this.toolStripLabel2.Size = new System.Drawing.Size(35, 22);
            this.toolStripLabel2.Text = "Layer";
            // 
            // tsbLayer
            // 
            this.tsbLayer.MaxLength = 1;
            this.tsbLayer.Name = "tsbLayer";
            this.tsbLayer.Size = new System.Drawing.Size(25, 25);
            this.tsbLayer.Text = "0";
            this.tsbLayer.TextChanged += new System.EventHandler(this.tsbLayer_TextChanged);
            // 
            // tsbSwapLayers
            // 
            this.tsbSwapLayers.CheckOnClick = true;
            this.tsbSwapLayers.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.tsbSwapLayers.Image = ((System.Drawing.Image)(resources.GetObject("tsbSwapLayers.Image")));
            this.tsbSwapLayers.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.tsbSwapLayers.Name = "tsbSwapLayers";
            this.tsbSwapLayers.Size = new System.Drawing.Size(23, 22);
            this.tsbSwapLayers.Text = "tsbSwapLayer";
            this.tsbSwapLayers.CheckStateChanged += new System.EventHandler(this.tsbSwapLayers_CheckStateChanged);
            // 
            // tsbLayerSwapTo
            // 
            this.tsbLayerSwapTo.MaxLength = 1;
            this.tsbLayerSwapTo.Name = "tsbLayerSwapTo";
            this.tsbLayerSwapTo.Size = new System.Drawing.Size(25, 25);
            this.tsbLayerSwapTo.Text = "0";
            this.tsbLayerSwapTo.TextChanged += new System.EventHandler(this.tsbLayerSwapTo_TextChanged);
            // 
            // toolStripSeparator2
            // 
            this.toolStripSeparator2.Name = "toolStripSeparator2";
            this.toolStripSeparator2.Size = new System.Drawing.Size(6, 25);
            // 
            // tsbCollision
            // 
            this.tsbCollision.CheckOnClick = true;
            this.tsbCollision.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Text;
            this.tsbCollision.Image = ((System.Drawing.Image)(resources.GetObject("tsbCollision.Image")));
            this.tsbCollision.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.tsbCollision.Name = "tsbCollision";
            this.tsbCollision.Size = new System.Drawing.Size(57, 22);
            this.tsbCollision.Text = "Collision";
            this.tsbCollision.CheckedChanged += new System.EventHandler(this.tsbCollision_CheckedChanged);
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
            this.leftToolStripMenuItem.Size = new System.Drawing.Size(114, 22);
            this.leftToolStripMenuItem.Text = "Left";
            this.leftToolStripMenuItem.CheckedChanged += new System.EventHandler(this.bottomToolStripMenuItem_CheckedChanged);
            // 
            // rightToolStripMenuItem
            // 
            this.rightToolStripMenuItem.CheckOnClick = true;
            this.rightToolStripMenuItem.Name = "rightToolStripMenuItem";
            this.rightToolStripMenuItem.Size = new System.Drawing.Size(114, 22);
            this.rightToolStripMenuItem.Text = "Right";
            this.rightToolStripMenuItem.CheckedChanged += new System.EventHandler(this.bottomToolStripMenuItem_CheckedChanged);
            // 
            // topToolStripMenuItem
            // 
            this.topToolStripMenuItem.CheckOnClick = true;
            this.topToolStripMenuItem.Name = "topToolStripMenuItem";
            this.topToolStripMenuItem.Size = new System.Drawing.Size(114, 22);
            this.topToolStripMenuItem.Text = "Top";
            this.topToolStripMenuItem.CheckedChanged += new System.EventHandler(this.bottomToolStripMenuItem_CheckedChanged);
            // 
            // bottomToolStripMenuItem
            // 
            this.bottomToolStripMenuItem.CheckOnClick = true;
            this.bottomToolStripMenuItem.Name = "bottomToolStripMenuItem";
            this.bottomToolStripMenuItem.Size = new System.Drawing.Size(114, 22);
            this.bottomToolStripMenuItem.Text = "Bottom";
            this.bottomToolStripMenuItem.CheckedChanged += new System.EventHandler(this.bottomToolStripMenuItem_CheckedChanged);
            // 
            // toolStripSeparator3
            // 
            this.toolStripSeparator3.Name = "toolStripSeparator3";
            this.toolStripSeparator3.Size = new System.Drawing.Size(111, 6);
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
            // toolStripSeparator4
            // 
            this.toolStripSeparator4.Name = "toolStripSeparator4";
            this.toolStripSeparator4.Size = new System.Drawing.Size(6, 25);
            // 
            // tsbObjectLayer
            // 
            this.tsbObjectLayer.CheckOnClick = true;
            this.tsbObjectLayer.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.tsbObjectLayer.Image = ((System.Drawing.Image)(resources.GetObject("tsbObjectLayer.Image")));
            this.tsbObjectLayer.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.tsbObjectLayer.Name = "tsbObjectLayer";
            this.tsbObjectLayer.Size = new System.Drawing.Size(23, 22);
            this.tsbObjectLayer.Text = "Object Layer";
            this.tsbObjectLayer.ToolTipText = "Object Layer";
            this.tsbObjectLayer.CheckedChanged += new System.EventHandler(this.tsbViewMap_CheckedChanged);
            // 
            // toolStripButton2
            // 
            this.toolStripButton2.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.toolStripButton2.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton2.Image")));
            this.toolStripButton2.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.toolStripButton2.Name = "toolStripButton2";
            this.toolStripButton2.Size = new System.Drawing.Size(23, 22);
            this.toolStripButton2.Text = "Object Header";
            this.toolStripButton2.Click += new System.EventHandler(this.toolStripButton2_Click);
            // 
            // tsbEncounters
            // 
            this.tsbEncounters.CheckOnClick = true;
            this.tsbEncounters.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.tsbEncounters.Image = ((System.Drawing.Image)(resources.GetObject("tsbEncounters.Image")));
            this.tsbEncounters.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.tsbEncounters.Name = "tsbEncounters";
            this.tsbEncounters.Size = new System.Drawing.Size(23, 22);
            this.tsbEncounters.Text = "tsbEncounters";
            this.tsbEncounters.ToolTipText = "Encounter Zones";
            this.tsbEncounters.CheckedChanged += new System.EventHandler(this.tsbEncounters_CheckedChanged);
            // 
            // tsbOptions
            // 
            this.tsbOptions.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.tsbOptions.Image = ((System.Drawing.Image)(resources.GetObject("tsbOptions.Image")));
            this.tsbOptions.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.tsbOptions.Name = "tsbOptions";
            this.tsbOptions.Size = new System.Drawing.Size(23, 22);
            this.tsbOptions.Text = "toolStripButton1";
            this.tsbOptions.ToolTipText = "Map Options";
            this.tsbOptions.Click += new System.EventHandler(this.tsbOptions_Click);
            // 
            // toolStripButton1
            // 
            this.toolStripButton1.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.toolStripButton1.Image = ((System.Drawing.Image)(resources.GetObject("toolStripButton1.Image")));
            this.toolStripButton1.ImageTransparentColor = System.Drawing.Color.Magenta;
            this.toolStripButton1.Name = "toolStripButton1";
            this.toolStripButton1.Size = new System.Drawing.Size(23, 22);
            this.toolStripButton1.Text = "toolStripButton1";
            this.toolStripButton1.ToolTipText = "Save Map";
            this.toolStripButton1.Click += new System.EventHandler(this.toolStripButton1_Click);
            // 
            // tilesetPanel
            // 
            this.tilesetPanel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left)));
            this.tilesetPanel.AutoScroll = true;
            this.tilesetPanel.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.tilesetPanel.Controls.Add(this.tsPanel);
            this.tilesetPanel.Location = new System.Drawing.Point(12, 469);
            this.tilesetPanel.Name = "tilesetPanel";
            this.tilesetPanel.Size = new System.Drawing.Size(206, 153);
            this.tilesetPanel.TabIndex = 1;
            // 
            // mapFrame
            // 
            this.mapFrame.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.mapFrame.AutoScroll = true;
            this.mapFrame.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.mapFrame.Controls.Add(this.mapPanel);
            this.mapFrame.Location = new System.Drawing.Point(224, 29);
            this.mapFrame.Name = "mapFrame";
            this.mapFrame.Size = new System.Drawing.Size(516, 593);
            this.mapFrame.TabIndex = 2;
            this.mapFrame.Scroll += new System.Windows.Forms.ScrollEventHandler(this.mapFrame_Scroll);
            this.mapFrame.Resize += new System.EventHandler(this.mapFrame_Resize);
            // 
            // numMapZoom
            // 
            this.numMapZoom.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.numMapZoom.Increment = new decimal(new int[] {
            50,
            0,
            0,
            0});
            this.numMapZoom.Location = new System.Drawing.Point(693, 628);
            this.numMapZoom.Maximum = new decimal(new int[] {
            300,
            0,
            0,
            0});
            this.numMapZoom.Minimum = new decimal(new int[] {
            50,
            0,
            0,
            0});
            this.numMapZoom.Name = "numMapZoom";
            this.numMapZoom.ReadOnly = true;
            this.numMapZoom.Size = new System.Drawing.Size(47, 20);
            this.numMapZoom.TabIndex = 3;
            this.numMapZoom.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.numMapZoom.Value = new decimal(new int[] {
            100,
            0,
            0,
            0});
            this.numMapZoom.ValueChanged += new System.EventHandler(this.numMapZoom_ValueChanged);
            // 
            // label1
            // 
            this.label1.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(653, 630);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(34, 13);
            this.label1.TabIndex = 4;
            this.label1.Text = "Zoom";
            // 
            // rightClickMenu
            // 
            this.rightClickMenu.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.newObjectToolStripMenuItem,
            this.toolStripSeparator7,
            this.deleteToolStripMenuItem,
            this.deleteZoneToolStripMenuItem,
            this.toolStripSeparator6,
            this.copyToolStripMenuItem,
            this.pasteToolStripMenuItem});
            this.rightClickMenu.Name = "rightClickMenu";
            this.rightClickMenu.Size = new System.Drawing.Size(141, 126);
            // 
            // newObjectToolStripMenuItem
            // 
            this.newObjectToolStripMenuItem.Name = "newObjectToolStripMenuItem";
            this.newObjectToolStripMenuItem.Size = new System.Drawing.Size(140, 22);
            this.newObjectToolStripMenuItem.Text = "Object Form";
            this.newObjectToolStripMenuItem.Click += new System.EventHandler(this.newObjectToolStripMenuItem_Click);
            // 
            // toolStripSeparator7
            // 
            this.toolStripSeparator7.Name = "toolStripSeparator7";
            this.toolStripSeparator7.Size = new System.Drawing.Size(137, 6);
            // 
            // deleteToolStripMenuItem
            // 
            this.deleteToolStripMenuItem.Name = "deleteToolStripMenuItem";
            this.deleteToolStripMenuItem.Size = new System.Drawing.Size(140, 22);
            this.deleteToolStripMenuItem.Text = "Delete";
            this.deleteToolStripMenuItem.Click += new System.EventHandler(this.deleteToolStripMenuItem_Click);
            // 
            // deleteZoneToolStripMenuItem
            // 
            this.deleteZoneToolStripMenuItem.Enabled = false;
            this.deleteZoneToolStripMenuItem.Name = "deleteZoneToolStripMenuItem";
            this.deleteZoneToolStripMenuItem.Size = new System.Drawing.Size(140, 22);
            this.deleteZoneToolStripMenuItem.Text = "Delete Zone";
            this.deleteZoneToolStripMenuItem.Click += new System.EventHandler(this.deleteZoneToolStripMenuItem_Click);
            // 
            // toolStripSeparator6
            // 
            this.toolStripSeparator6.Name = "toolStripSeparator6";
            this.toolStripSeparator6.Size = new System.Drawing.Size(137, 6);
            // 
            // copyToolStripMenuItem
            // 
            this.copyToolStripMenuItem.Name = "copyToolStripMenuItem";
            this.copyToolStripMenuItem.Size = new System.Drawing.Size(140, 22);
            this.copyToolStripMenuItem.Text = "Copy";
            this.copyToolStripMenuItem.Click += new System.EventHandler(this.copyToolStripMenuItem_Click);
            // 
            // pasteToolStripMenuItem
            // 
            this.pasteToolStripMenuItem.Name = "pasteToolStripMenuItem";
            this.pasteToolStripMenuItem.Size = new System.Drawing.Size(140, 22);
            this.pasteToolStripMenuItem.Text = "Paste";
            this.pasteToolStripMenuItem.Click += new System.EventHandler(this.pasteToolStripMenuItem_Click);
            // 
            // TileSetTreeView
            // 
            this.TileSetTreeView.Location = new System.Drawing.Point(18, 35);
            this.TileSetTreeView.Name = "TileSetTreeView";
            this.TileSetTreeView.Size = new System.Drawing.Size(200, 428);
            this.TileSetTreeView.TabIndex = 8;
            this.TileSetTreeView.NodeMouseClick += new System.Windows.Forms.TreeNodeMouseClickEventHandler(this.TileSetTreeView_NodeMouseClick);
            // 
            // RedrawTimer
            // 
            this.RedrawTimer.Enabled = true;
            this.RedrawTimer.Tick += new System.EventHandler(this.RedrawTimer_Tick);
            // 
            // mapPanel
            // 
            this.mapPanel.Location = new System.Drawing.Point(4, 4);
            this.mapPanel.Name = "mapPanel";
            this.mapPanel.Size = new System.Drawing.Size(200, 100);
            this.mapPanel.TabIndex = 0;
            this.mapPanel.Paint += new System.Windows.Forms.PaintEventHandler(this.mapPanel_Paint);
            this.mapPanel.MouseClick += new System.Windows.Forms.MouseEventHandler(this.mapPanel_MouseClick);
            this.mapPanel.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.mapPanel_MouseDoubleClick);
            this.mapPanel.MouseDown += new System.Windows.Forms.MouseEventHandler(this.mapPanel_MouseDown);
            this.mapPanel.MouseMove += new System.Windows.Forms.MouseEventHandler(this.mapPanel_MouseMove);
            this.mapPanel.MouseUp += new System.Windows.Forms.MouseEventHandler(this.mapPanel_MouseUp);
            // 
            // tsPanel
            // 
            this.tsPanel.Location = new System.Drawing.Point(4, 5);
            this.tsPanel.Name = "tsPanel";
            this.tsPanel.Size = new System.Drawing.Size(140, 100);
            this.tsPanel.TabIndex = 0;
            this.tsPanel.Paint += new System.Windows.Forms.PaintEventHandler(this.tsPanel_Paint);
            this.tsPanel.MouseClick += new System.Windows.Forms.MouseEventHandler(this.tsPanel_MouseClick);
            // 
            // MapForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(752, 660);
            this.Controls.Add(this.TileSetTreeView);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.numMapZoom);
            this.Controls.Add(this.mapFrame);
            this.Controls.Add(this.tilesetPanel);
            this.Controls.Add(this.toolstrip);
            this.DoubleBuffered = true;
            this.KeyPreview = true;
            this.Name = "MapForm";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Map Editor";
            this.Load += new System.EventHandler(this.MapForm_Load);
            this.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.MapForm_KeyPress);
            this.toolstrip.ResumeLayout(false);
            this.toolstrip.PerformLayout();
            this.tilesetPanel.ResumeLayout(false);
            this.mapFrame.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.numMapZoom)).EndInit();
            this.rightClickMenu.ResumeLayout(false);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ToolStrip toolstrip;
        private System.Windows.Forms.ToolStripButton tsbCollision;
        private System.Windows.Forms.Panel tilesetPanel;
        private System.Windows.Forms.Panel mapFrame;
        private Classes.DBPanel tsPanel;
        private Classes.DBPanel mapPanel;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator2;
        private System.Windows.Forms.ToolStripDropDownButton toolStripDropDownButton1;
        private System.Windows.Forms.ToolStripMenuItem leftToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem rightToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem topToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem bottomToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator3;
        private System.Windows.Forms.ToolStripMenuItem allToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem noneToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator4;
        private System.Windows.Forms.ToolStripButton tsbObjectLayer;
        private System.Windows.Forms.NumericUpDown numMapZoom;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ToolStripButton btnDraw;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator5;
        private System.Windows.Forms.ContextMenuStrip rightClickMenu;
        private System.Windows.Forms.ToolStripMenuItem newObjectToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator7;
        private System.Windows.Forms.ToolStripMenuItem deleteToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator6;
        private System.Windows.Forms.ToolStripMenuItem copyToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem pasteToolStripMenuItem;
        private System.Windows.Forms.ToolStripButton tsbEncounters;
        private System.Windows.Forms.ToolStripMenuItem deleteZoneToolStripMenuItem;
        private System.Windows.Forms.ToolStripButton tsbOptions;
        private System.Windows.Forms.ToolStripButton toolStripButton1;
        private System.Windows.Forms.ToolStripButton toolStripButton2;
        private System.Windows.Forms.ToolStripButton tsbSwapLayers;
        private System.Windows.Forms.ToolStripLabel toolStripLabel2;
        private System.Windows.Forms.ToolStripTextBox tsbLayer;
        private System.Windows.Forms.ToolStripTextBox tsbLayerSwapTo;
        private System.Windows.Forms.ToolStripButton tsbShowGrid;
        private System.Windows.Forms.TreeView TileSetTreeView;
        private System.Windows.Forms.Timer RedrawTimer;
    }
}