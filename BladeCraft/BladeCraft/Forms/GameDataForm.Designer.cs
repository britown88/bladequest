namespace BladeCraft
{
    partial class GameDataForm
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
            System.Windows.Forms.ListViewGroup listViewGroup7 = new System.Windows.Forms.ListViewGroup("Battle Sprites", System.Windows.Forms.HorizontalAlignment.Left);
            System.Windows.Forms.ListViewGroup listViewGroup8 = new System.Windows.Forms.ListViewGroup("Enemy Sprites", System.Windows.Forms.HorizontalAlignment.Left);
            System.Windows.Forms.ListViewGroup listViewGroup9 = new System.Windows.Forms.ListViewGroup("World Sprites", System.Windows.Forms.HorizontalAlignment.Left);
            this.dataTabs = new System.Windows.Forms.TabControl();
            this.abilityTab = new System.Windows.Forms.TabPage();
            this.animTab = new System.Windows.Forms.TabPage();
            this.charTab = new System.Windows.Forms.TabPage();
            this.enemyTab = new System.Windows.Forms.TabPage();
            this.encounterTab = new System.Windows.Forms.TabPage();
            this.itemTab = new System.Windows.Forms.TabPage();
            this.merchantTab = new System.Windows.Forms.TabPage();
            this.musicTab = new System.Windows.Forms.TabPage();
            this.spriteTab = new System.Windows.Forms.TabPage();
            this.btnSave = new System.Windows.Forms.Button();
            this.btnCancel = new System.Windows.Forms.Button();
            this.label9 = new System.Windows.Forms.Label();
            this.PreviewPanel = new System.Windows.Forms.Panel();
            this.label8 = new System.Windows.Forms.Label();
            this.BitmapPanel = new System.Windows.Forms.Panel();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.label7 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.numDestSize = new System.Windows.Forms.NumericUpDown();
            this.numSrcSize = new System.Windows.Forms.NumericUpDown();
            this.groupbox = new System.Windows.Forms.GroupBox();
            this.label5 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.numPosX = new System.Windows.Forms.NumericUpDown();
            this.numPosY = new System.Windows.Forms.NumericUpDown();
            this.btnNew = new System.Windows.Forms.Button();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.btnSetBitmap = new System.Windows.Forms.Button();
            this.label3 = new System.Windows.Forms.Label();
            this.txtBitmap = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.txtSpriteName = new System.Windows.Forms.TextBox();
            this.cmbSpriteType = new System.Windows.Forms.ComboBox();
            this.lvwSprites = new System.Windows.Forms.ListView();
            this.columnHeader1 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.button1 = new System.Windows.Forms.Button();
            this.dataTabs.SuspendLayout();
            this.spriteTab.SuspendLayout();
            this.groupBox2.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.numDestSize)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.numSrcSize)).BeginInit();
            this.groupbox.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.numPosX)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.numPosY)).BeginInit();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // dataTabs
            // 
            this.dataTabs.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.dataTabs.Controls.Add(this.abilityTab);
            this.dataTabs.Controls.Add(this.animTab);
            this.dataTabs.Controls.Add(this.charTab);
            this.dataTabs.Controls.Add(this.enemyTab);
            this.dataTabs.Controls.Add(this.encounterTab);
            this.dataTabs.Controls.Add(this.itemTab);
            this.dataTabs.Controls.Add(this.merchantTab);
            this.dataTabs.Controls.Add(this.musicTab);
            this.dataTabs.Controls.Add(this.spriteTab);
            this.dataTabs.Location = new System.Drawing.Point(12, 12);
            this.dataTabs.Name = "dataTabs";
            this.dataTabs.SelectedIndex = 0;
            this.dataTabs.Size = new System.Drawing.Size(559, 451);
            this.dataTabs.TabIndex = 0;
            this.dataTabs.Selected += new System.Windows.Forms.TabControlEventHandler(this.dataTabs_Selected);
            this.dataTabs.Deselected += new System.Windows.Forms.TabControlEventHandler(this.dataTabs_Deselected);
            // 
            // abilityTab
            // 
            this.abilityTab.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.abilityTab.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.abilityTab.Location = new System.Drawing.Point(4, 22);
            this.abilityTab.Name = "abilityTab";
            this.abilityTab.Padding = new System.Windows.Forms.Padding(3);
            this.abilityTab.Size = new System.Drawing.Size(551, 425);
            this.abilityTab.TabIndex = 0;
            this.abilityTab.Text = "Abilities";
            // 
            // animTab
            // 
            this.animTab.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.animTab.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.animTab.Location = new System.Drawing.Point(4, 22);
            this.animTab.Name = "animTab";
            this.animTab.Padding = new System.Windows.Forms.Padding(3);
            this.animTab.Size = new System.Drawing.Size(551, 425);
            this.animTab.TabIndex = 1;
            this.animTab.Text = "Animations";
            // 
            // charTab
            // 
            this.charTab.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.charTab.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.charTab.Location = new System.Drawing.Point(4, 22);
            this.charTab.Name = "charTab";
            this.charTab.Size = new System.Drawing.Size(551, 425);
            this.charTab.TabIndex = 2;
            this.charTab.Text = "Characters";
            // 
            // enemyTab
            // 
            this.enemyTab.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.enemyTab.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.enemyTab.Location = new System.Drawing.Point(4, 22);
            this.enemyTab.Name = "enemyTab";
            this.enemyTab.Size = new System.Drawing.Size(551, 425);
            this.enemyTab.TabIndex = 3;
            this.enemyTab.Text = "Enemies";
            // 
            // encounterTab
            // 
            this.encounterTab.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.encounterTab.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.encounterTab.Location = new System.Drawing.Point(4, 22);
            this.encounterTab.Name = "encounterTab";
            this.encounterTab.Size = new System.Drawing.Size(551, 425);
            this.encounterTab.TabIndex = 4;
            this.encounterTab.Text = "Encounters";
            // 
            // itemTab
            // 
            this.itemTab.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.itemTab.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.itemTab.Location = new System.Drawing.Point(4, 22);
            this.itemTab.Name = "itemTab";
            this.itemTab.Size = new System.Drawing.Size(551, 425);
            this.itemTab.TabIndex = 5;
            this.itemTab.Text = "Items";
            // 
            // merchantTab
            // 
            this.merchantTab.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.merchantTab.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.merchantTab.Location = new System.Drawing.Point(4, 22);
            this.merchantTab.Name = "merchantTab";
            this.merchantTab.Size = new System.Drawing.Size(551, 425);
            this.merchantTab.TabIndex = 6;
            this.merchantTab.Text = "Merchants";
            // 
            // musicTab
            // 
            this.musicTab.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.musicTab.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.musicTab.Location = new System.Drawing.Point(4, 22);
            this.musicTab.Name = "musicTab";
            this.musicTab.Size = new System.Drawing.Size(551, 425);
            this.musicTab.TabIndex = 7;
            this.musicTab.Text = "Music";
            // 
            // spriteTab
            // 
            this.spriteTab.BackgroundImageLayout = System.Windows.Forms.ImageLayout.None;
            this.spriteTab.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.spriteTab.Controls.Add(this.btnSave);
            this.spriteTab.Controls.Add(this.btnCancel);
            this.spriteTab.Controls.Add(this.label9);
            this.spriteTab.Controls.Add(this.PreviewPanel);
            this.spriteTab.Controls.Add(this.label8);
            this.spriteTab.Controls.Add(this.BitmapPanel);
            this.spriteTab.Controls.Add(this.groupBox2);
            this.spriteTab.Controls.Add(this.groupbox);
            this.spriteTab.Controls.Add(this.btnNew);
            this.spriteTab.Controls.Add(this.groupBox1);
            this.spriteTab.Controls.Add(this.lvwSprites);
            this.spriteTab.Location = new System.Drawing.Point(4, 22);
            this.spriteTab.Name = "spriteTab";
            this.spriteTab.Size = new System.Drawing.Size(551, 425);
            this.spriteTab.TabIndex = 8;
            this.spriteTab.Text = "Sprites";
            // 
            // btnSave
            // 
            this.btnSave.Location = new System.Drawing.Point(350, 364);
            this.btnSave.Name = "btnSave";
            this.btnSave.Size = new System.Drawing.Size(191, 23);
            this.btnSave.TabIndex = 13;
            this.btnSave.Text = "Save";
            this.btnSave.UseVisualStyleBackColor = true;
            // 
            // btnCancel
            // 
            this.btnCancel.Location = new System.Drawing.Point(350, 393);
            this.btnCancel.Name = "btnCancel";
            this.btnCancel.Size = new System.Drawing.Size(191, 23);
            this.btnCancel.TabIndex = 12;
            this.btnCancel.Text = "Cancel";
            this.btnCancel.UseVisualStyleBackColor = true;
            // 
            // label9
            // 
            this.label9.AutoSize = true;
            this.label9.Location = new System.Drawing.Point(349, 145);
            this.label9.Name = "label9";
            this.label9.Size = new System.Drawing.Size(45, 13);
            this.label9.TabIndex = 11;
            this.label9.Text = "Preview";
            // 
            // PreviewPanel
            // 
            this.PreviewPanel.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.PreviewPanel.Location = new System.Drawing.Point(350, 161);
            this.PreviewPanel.Name = "PreviewPanel";
            this.PreviewPanel.Size = new System.Drawing.Size(191, 197);
            this.PreviewPanel.TabIndex = 10;
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Location = new System.Drawing.Point(154, 145);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(39, 13);
            this.label8.TabIndex = 9;
            this.label8.Text = "Bitmap";
            // 
            // BitmapPanel
            // 
            this.BitmapPanel.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.BitmapPanel.Location = new System.Drawing.Point(155, 161);
            this.BitmapPanel.Name = "BitmapPanel";
            this.BitmapPanel.Size = new System.Drawing.Size(189, 255);
            this.BitmapPanel.TabIndex = 8;
            this.BitmapPanel.Paint += new System.Windows.Forms.PaintEventHandler(this.BitmapPanel_Paint);
            // 
            // groupBox2
            // 
            this.groupBox2.Controls.Add(this.label7);
            this.groupBox2.Controls.Add(this.label6);
            this.groupBox2.Controls.Add(this.numDestSize);
            this.groupBox2.Controls.Add(this.numSrcSize);
            this.groupBox2.Location = new System.Drawing.Point(314, 76);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.Size = new System.Drawing.Size(227, 66);
            this.groupBox2.TabIndex = 7;
            this.groupBox2.TabStop = false;
            this.groupBox2.Text = "Enemy";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(126, 20);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(83, 13);
            this.label7.TabIndex = 10;
            this.label7.Text = "Destination Size";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(7, 20);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(64, 13);
            this.label6.TabIndex = 9;
            this.label6.Text = "Source Size";
            // 
            // numDestSize
            // 
            this.numDestSize.Location = new System.Drawing.Point(128, 36);
            this.numDestSize.Maximum = new decimal(new int[] {
            1024,
            0,
            0,
            0});
            this.numDestSize.Name = "numDestSize";
            this.numDestSize.Size = new System.Drawing.Size(93, 20);
            this.numDestSize.TabIndex = 8;
            // 
            // numSrcSize
            // 
            this.numSrcSize.Location = new System.Drawing.Point(10, 36);
            this.numSrcSize.Maximum = new decimal(new int[] {
            1024,
            0,
            0,
            0});
            this.numSrcSize.Name = "numSrcSize";
            this.numSrcSize.Size = new System.Drawing.Size(93, 20);
            this.numSrcSize.TabIndex = 6;
            // 
            // groupbox
            // 
            this.groupbox.Controls.Add(this.label5);
            this.groupbox.Controls.Add(this.label4);
            this.groupbox.Controls.Add(this.numPosX);
            this.groupbox.Controls.Add(this.numPosY);
            this.groupbox.Location = new System.Drawing.Point(155, 76);
            this.groupbox.Name = "groupbox";
            this.groupbox.Size = new System.Drawing.Size(152, 66);
            this.groupbox.TabIndex = 6;
            this.groupbox.TabStop = false;
            this.groupbox.Text = "Position";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(82, 20);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(14, 13);
            this.label5.TabIndex = 5;
            this.label5.Text = "Y";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(7, 20);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(14, 13);
            this.label4.TabIndex = 4;
            this.label4.Text = "X";
            // 
            // numPosX
            // 
            this.numPosX.Location = new System.Drawing.Point(6, 36);
            this.numPosX.Maximum = new decimal(new int[] {
            1024,
            0,
            0,
            0});
            this.numPosX.Name = "numPosX";
            this.numPosX.Size = new System.Drawing.Size(64, 20);
            this.numPosX.TabIndex = 3;
            // 
            // numPosY
            // 
            this.numPosY.Location = new System.Drawing.Point(82, 36);
            this.numPosY.Maximum = new decimal(new int[] {
            1024,
            0,
            0,
            0});
            this.numPosY.Name = "numPosY";
            this.numPosY.Size = new System.Drawing.Size(64, 20);
            this.numPosY.TabIndex = 4;
            // 
            // btnNew
            // 
            this.btnNew.Location = new System.Drawing.Point(7, 393);
            this.btnNew.Name = "btnNew";
            this.btnNew.Size = new System.Drawing.Size(141, 23);
            this.btnNew.TabIndex = 5;
            this.btnNew.Text = "New";
            this.btnNew.UseVisualStyleBackColor = true;
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.btnSetBitmap);
            this.groupBox1.Controls.Add(this.label3);
            this.groupBox1.Controls.Add(this.txtBitmap);
            this.groupBox1.Controls.Add(this.label2);
            this.groupBox1.Controls.Add(this.label1);
            this.groupBox1.Controls.Add(this.txtSpriteName);
            this.groupBox1.Controls.Add(this.cmbSpriteType);
            this.groupBox1.Location = new System.Drawing.Point(155, 4);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(386, 65);
            this.groupBox1.TabIndex = 2;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Options";
            // 
            // btnSetBitmap
            // 
            this.btnSetBitmap.Location = new System.Drawing.Point(357, 29);
            this.btnSetBitmap.Name = "btnSetBitmap";
            this.btnSetBitmap.Size = new System.Drawing.Size(23, 23);
            this.btnSetBitmap.TabIndex = 7;
            this.btnSetBitmap.Text = "...";
            this.btnSetBitmap.UseVisualStyleBackColor = true;
            this.btnSetBitmap.Click += new System.EventHandler(this.btnSetBitmap_Click);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(236, 16);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(39, 13);
            this.label3.TabIndex = 6;
            this.label3.Text = "Bitmap";
            // 
            // txtBitmap
            // 
            this.txtBitmap.Location = new System.Drawing.Point(239, 32);
            this.txtBitmap.Name = "txtBitmap";
            this.txtBitmap.ReadOnly = true;
            this.txtBitmap.Size = new System.Drawing.Size(112, 20);
            this.txtBitmap.TabIndex = 5;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(112, 15);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(31, 13);
            this.label2.TabIndex = 4;
            this.label2.Text = "Type";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(6, 16);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(65, 13);
            this.label1.TabIndex = 3;
            this.label1.Text = "Sprite Name";
            // 
            // txtSpriteName
            // 
            this.txtSpriteName.Location = new System.Drawing.Point(6, 32);
            this.txtSpriteName.Name = "txtSpriteName";
            this.txtSpriteName.Size = new System.Drawing.Size(100, 20);
            this.txtSpriteName.TabIndex = 2;
            // 
            // cmbSpriteType
            // 
            this.cmbSpriteType.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cmbSpriteType.FormattingEnabled = true;
            this.cmbSpriteType.Items.AddRange(new object[] {
            "Battle",
            "Enemy",
            "World"});
            this.cmbSpriteType.Location = new System.Drawing.Point(112, 31);
            this.cmbSpriteType.Name = "cmbSpriteType";
            this.cmbSpriteType.Size = new System.Drawing.Size(121, 21);
            this.cmbSpriteType.TabIndex = 1;
            this.cmbSpriteType.SelectedIndexChanged += new System.EventHandler(this.cmbSpriteType_SelectedIndexChanged);
            // 
            // lvwSprites
            // 
            this.lvwSprites.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader1});
            this.lvwSprites.FullRowSelect = true;
            listViewGroup7.Header = "Battle Sprites";
            listViewGroup7.Name = "battleSprites";
            listViewGroup8.Header = "Enemy Sprites";
            listViewGroup8.Name = "enemySprites";
            listViewGroup9.Header = "World Sprites";
            listViewGroup9.Name = "worldSprites";
            this.lvwSprites.Groups.AddRange(new System.Windows.Forms.ListViewGroup[] {
            listViewGroup7,
            listViewGroup8,
            listViewGroup9});
            this.lvwSprites.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
            this.lvwSprites.Location = new System.Drawing.Point(7, 3);
            this.lvwSprites.MultiSelect = false;
            this.lvwSprites.Name = "lvwSprites";
            this.lvwSprites.Size = new System.Drawing.Size(141, 384);
            this.lvwSprites.TabIndex = 0;
            this.lvwSprites.UseCompatibleStateImageBehavior = false;
            this.lvwSprites.View = System.Windows.Forms.View.Details;
            this.lvwSprites.SelectedIndexChanged += new System.EventHandler(this.lvwSprites_SelectedIndexChanged);
            // 
            // columnHeader1
            // 
            this.columnHeader1.Text = "Sprites";
            this.columnHeader1.Width = 120;
            // 
            // button1
            // 
            this.button1.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.button1.Location = new System.Drawing.Point(506, 5);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(53, 23);
            this.button1.TabIndex = 1;
            this.button1.Text = "Refresh";
            this.button1.UseVisualStyleBackColor = true;
            // 
            // GameDataForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(583, 475);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.dataTabs);
            this.Name = "GameDataForm";
            this.ShowIcon = false;
            this.Text = "Game Data";
            this.dataTabs.ResumeLayout(false);
            this.spriteTab.ResumeLayout(false);
            this.spriteTab.PerformLayout();
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.numDestSize)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.numSrcSize)).EndInit();
            this.groupbox.ResumeLayout(false);
            this.groupbox.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.numPosX)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.numPosY)).EndInit();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TabControl dataTabs;
        private System.Windows.Forms.TabPage abilityTab;
        private System.Windows.Forms.TabPage animTab;
        private System.Windows.Forms.TabPage charTab;
        private System.Windows.Forms.TabPage enemyTab;
        private System.Windows.Forms.TabPage encounterTab;
        private System.Windows.Forms.TabPage itemTab;
        private System.Windows.Forms.TabPage merchantTab;
        private System.Windows.Forms.TabPage musicTab;
        private System.Windows.Forms.TabPage spriteTab;
        private System.Windows.Forms.ListView lvwSprites;
        private System.Windows.Forms.ColumnHeader columnHeader1;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.ComboBox cmbSpriteType;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox txtSpriteName;
        private System.Windows.Forms.NumericUpDown numPosY;
        private System.Windows.Forms.NumericUpDown numPosX;
        private System.Windows.Forms.Button btnNew;
        private System.Windows.Forms.Button btnSetBitmap;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox txtBitmap;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.NumericUpDown numDestSize;
        private System.Windows.Forms.NumericUpDown numSrcSize;
        private System.Windows.Forms.GroupBox groupbox;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Button btnSave;
        private System.Windows.Forms.Button btnCancel;
        private System.Windows.Forms.Label label9;
        private System.Windows.Forms.Panel PreviewPanel;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.Panel BitmapPanel;
    }
}