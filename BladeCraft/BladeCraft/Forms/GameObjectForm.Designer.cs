namespace BladeCraft.Forms
{
    partial class GameObjectForm
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
         System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(GameObjectForm));
         System.Windows.Forms.ListViewItem listViewItem1 = new System.Windows.Forms.ListViewItem(new string[] {
            "",
            "<New Action>"}, -1);
         this.label1 = new System.Windows.Forms.Label();
         this.txtObjectName = new System.Windows.Forms.TextBox();
         this.groupBox1 = new System.Windows.Forms.GroupBox();
         this.objY = new System.Windows.Forms.NumericUpDown();
         this.label3 = new System.Windows.Forms.Label();
         this.objX = new System.Windows.Forms.NumericUpDown();
         this.label2 = new System.Windows.Forms.Label();
         this.groupBox2 = new System.Windows.Forms.GroupBox();
         this.btnCopyState = new System.Windows.Forms.Button();
         this.btnDeleteState = new System.Windows.Forms.Button();
         this.btnAddState = new System.Windows.Forms.Button();
         this.lvwActions = new System.Windows.Forms.ListView();
         this.columnHeader1 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
         this.columnHeader2 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
         this.panel1 = new System.Windows.Forms.Panel();
         this.btnCancel = new System.Windows.Forms.Button();
         this.btnSave = new System.Windows.Forms.Button();
         this.groupBox7 = new System.Windows.Forms.GroupBox();
         this.numMoveSpeed = new System.Windows.Forms.NumericUpDown();
         this.label6 = new System.Windows.Forms.Label();
         this.numMoveRange = new System.Windows.Forms.NumericUpDown();
         this.label5 = new System.Windows.Forms.Label();
         this.chkFaceOnActivate = new System.Windows.Forms.CheckBox();
         this.chkFaceOnMove = new System.Windows.Forms.CheckBox();
         this.groupBox6 = new System.Windows.Forms.GroupBox();
         this.label4 = new System.Windows.Forms.Label();
         this.txtSpriteName = new System.Windows.Forms.TextBox();
         this.lblImageIndex = new System.Windows.Forms.Label();
         this.numImageIndex = new System.Windows.Forms.NumericUpDown();
         this.chkAnimated = new System.Windows.Forms.CheckBox();
         this.radRight = new System.Windows.Forms.RadioButton();
         this.radLeft = new System.Windows.Forms.RadioButton();
         this.radDown = new System.Windows.Forms.RadioButton();
         this.radUp = new System.Windows.Forms.RadioButton();
         this.groupBox5 = new System.Windows.Forms.GroupBox();
         this.btnCollNone = new System.Windows.Forms.Button();
         this.btnCollAll = new System.Windows.Forms.Button();
         this.chkCollBottom = new System.Windows.Forms.CheckBox();
         this.chkCollRight = new System.Windows.Forms.CheckBox();
         this.chkCollTop = new System.Windows.Forms.CheckBox();
         this.chkCollLeft = new System.Windows.Forms.CheckBox();
         this.groupBox4 = new System.Windows.Forms.GroupBox();
         this.chkForeground = new System.Windows.Forms.CheckBox();
         this.chkAllowMovement = new System.Windows.Forms.CheckBox();
         this.chkAutoStart = new System.Windows.Forms.CheckBox();
         this.groupBox3 = new System.Windows.Forms.GroupBox();
         this.btnDeleteItemCond = new System.Windows.Forms.Button();
         this.btnAddItemCond = new System.Windows.Forms.Button();
         this.lvwItemConditions = new System.Windows.Forms.ListView();
         this.ItemName = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
         this.ItemCount = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
         this.btnDeleteSwitchCond = new System.Windows.Forms.Button();
         this.btnAddSwitchCond = new System.Windows.Forms.Button();
         this.lvwSwitchConditions = new System.Windows.Forms.ListView();
         this.SwitchName = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
         this.SwitchState = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
         this.statesStrip = new System.Windows.Forms.ToolStrip();
         this.rightClick = new System.Windows.Forms.ContextMenuStrip(this.components);
         this.deleteToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.insertToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
         this.cmbBubble = new System.Windows.Forms.ComboBox();
         this.groupBox1.SuspendLayout();
         ((System.ComponentModel.ISupportInitialize)(this.objY)).BeginInit();
         ((System.ComponentModel.ISupportInitialize)(this.objX)).BeginInit();
         this.groupBox2.SuspendLayout();
         this.panel1.SuspendLayout();
         this.groupBox7.SuspendLayout();
         ((System.ComponentModel.ISupportInitialize)(this.numMoveSpeed)).BeginInit();
         ((System.ComponentModel.ISupportInitialize)(this.numMoveRange)).BeginInit();
         this.groupBox6.SuspendLayout();
         ((System.ComponentModel.ISupportInitialize)(this.numImageIndex)).BeginInit();
         this.groupBox5.SuspendLayout();
         this.groupBox4.SuspendLayout();
         this.groupBox3.SuspendLayout();
         this.rightClick.SuspendLayout();
         this.SuspendLayout();
         // 
         // label1
         // 
         this.label1.AutoSize = true;
         this.label1.Location = new System.Drawing.Point(6, 22);
         this.label1.Name = "label1";
         this.label1.Size = new System.Drawing.Size(38, 13);
         this.label1.TabIndex = 0;
         this.label1.Text = "Name:";
         // 
         // txtObjectName
         // 
         this.txtObjectName.Location = new System.Drawing.Point(50, 19);
         this.txtObjectName.Name = "txtObjectName";
         this.txtObjectName.Size = new System.Drawing.Size(100, 20);
         this.txtObjectName.TabIndex = 1;
         // 
         // groupBox1
         // 
         this.groupBox1.Controls.Add(this.objY);
         this.groupBox1.Controls.Add(this.label3);
         this.groupBox1.Controls.Add(this.objX);
         this.groupBox1.Controls.Add(this.label2);
         this.groupBox1.Controls.Add(this.txtObjectName);
         this.groupBox1.Controls.Add(this.label1);
         this.groupBox1.Location = new System.Drawing.Point(12, 12);
         this.groupBox1.Name = "groupBox1";
         this.groupBox1.Size = new System.Drawing.Size(317, 51);
         this.groupBox1.TabIndex = 2;
         this.groupBox1.TabStop = false;
         this.groupBox1.Text = "Object Info";
         // 
         // objY
         // 
         this.objY.Location = new System.Drawing.Point(259, 20);
         this.objY.Maximum = new decimal(new int[] {
            250,
            0,
            0,
            0});
         this.objY.Name = "objY";
         this.objY.Size = new System.Drawing.Size(50, 20);
         this.objY.TabIndex = 5;
         // 
         // label3
         // 
         this.label3.AutoSize = true;
         this.label3.Location = new System.Drawing.Point(236, 22);
         this.label3.Name = "label3";
         this.label3.Size = new System.Drawing.Size(17, 13);
         this.label3.TabIndex = 4;
         this.label3.Text = "Y:";
         // 
         // objX
         // 
         this.objX.Location = new System.Drawing.Point(179, 20);
         this.objX.Maximum = new decimal(new int[] {
            250,
            0,
            0,
            0});
         this.objX.Name = "objX";
         this.objX.Size = new System.Drawing.Size(50, 20);
         this.objX.TabIndex = 3;
         // 
         // label2
         // 
         this.label2.AutoSize = true;
         this.label2.Location = new System.Drawing.Point(156, 22);
         this.label2.Name = "label2";
         this.label2.Size = new System.Drawing.Size(17, 13);
         this.label2.TabIndex = 2;
         this.label2.Text = "X:";
         // 
         // groupBox2
         // 
         this.groupBox2.Controls.Add(this.btnCopyState);
         this.groupBox2.Controls.Add(this.btnDeleteState);
         this.groupBox2.Controls.Add(this.btnAddState);
         this.groupBox2.Location = new System.Drawing.Point(336, 13);
         this.groupBox2.Name = "groupBox2";
         this.groupBox2.Size = new System.Drawing.Size(99, 50);
         this.groupBox2.TabIndex = 4;
         this.groupBox2.TabStop = false;
         this.groupBox2.Text = "State Control";
         // 
         // btnCopyState
         // 
         this.btnCopyState.Image = ((System.Drawing.Image)(resources.GetObject("btnCopyState.Image")));
         this.btnCopyState.Location = new System.Drawing.Point(68, 19);
         this.btnCopyState.Name = "btnCopyState";
         this.btnCopyState.Size = new System.Drawing.Size(25, 25);
         this.btnCopyState.TabIndex = 8;
         this.btnCopyState.UseVisualStyleBackColor = true;
         // 
         // btnDeleteState
         // 
         this.btnDeleteState.Image = ((System.Drawing.Image)(resources.GetObject("btnDeleteState.Image")));
         this.btnDeleteState.Location = new System.Drawing.Point(37, 19);
         this.btnDeleteState.Name = "btnDeleteState";
         this.btnDeleteState.Size = new System.Drawing.Size(25, 25);
         this.btnDeleteState.TabIndex = 7;
         this.btnDeleteState.UseVisualStyleBackColor = true;
         this.btnDeleteState.Click += new System.EventHandler(this.btnDeleteState_Click);
         // 
         // btnAddState
         // 
         this.btnAddState.Image = ((System.Drawing.Image)(resources.GetObject("btnAddState.Image")));
         this.btnAddState.Location = new System.Drawing.Point(6, 19);
         this.btnAddState.Name = "btnAddState";
         this.btnAddState.Size = new System.Drawing.Size(25, 25);
         this.btnAddState.TabIndex = 6;
         this.btnAddState.UseVisualStyleBackColor = true;
         this.btnAddState.Click += new System.EventHandler(this.btnAddState_Click);
         // 
         // lvwActions
         // 
         this.lvwActions.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
         this.lvwActions.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader1,
            this.columnHeader2});
         this.lvwActions.FullRowSelect = true;
         this.lvwActions.GridLines = true;
         this.lvwActions.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
         this.lvwActions.Items.AddRange(new System.Windows.Forms.ListViewItem[] {
            listViewItem1});
         this.lvwActions.Location = new System.Drawing.Point(370, 98);
         this.lvwActions.Name = "lvwActions";
         this.lvwActions.RightToLeft = System.Windows.Forms.RightToLeft.No;
         this.lvwActions.Size = new System.Drawing.Size(262, 396);
         this.lvwActions.TabIndex = 6;
         this.lvwActions.UseCompatibleStateImageBehavior = false;
         this.lvwActions.View = System.Windows.Forms.View.Details;
         this.lvwActions.MouseClick += new System.Windows.Forms.MouseEventHandler(this.lvwActions_MouseClick);
         this.lvwActions.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.lvwActions_MouseDoubleClick);
         // 
         // columnHeader1
         // 
         this.columnHeader1.Text = "Index";
         this.columnHeader1.Width = 38;
         // 
         // columnHeader2
         // 
         this.columnHeader2.Text = "Action";
         this.columnHeader2.Width = 219;
         // 
         // panel1
         // 
         this.panel1.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left)));
         this.panel1.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
         this.panel1.Controls.Add(this.btnCancel);
         this.panel1.Controls.Add(this.btnSave);
         this.panel1.Controls.Add(this.groupBox7);
         this.panel1.Controls.Add(this.groupBox6);
         this.panel1.Controls.Add(this.groupBox5);
         this.panel1.Controls.Add(this.groupBox4);
         this.panel1.Controls.Add(this.groupBox3);
         this.panel1.Location = new System.Drawing.Point(12, 98);
         this.panel1.Name = "panel1";
         this.panel1.Size = new System.Drawing.Size(352, 396);
         this.panel1.TabIndex = 5;
         // 
         // btnCancel
         // 
         this.btnCancel.Location = new System.Drawing.Point(260, 357);
         this.btnCancel.Name = "btnCancel";
         this.btnCancel.Size = new System.Drawing.Size(75, 23);
         this.btnCancel.TabIndex = 6;
         this.btnCancel.Text = "Cancel";
         this.btnCancel.UseVisualStyleBackColor = true;
         this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
         // 
         // btnSave
         // 
         this.btnSave.Location = new System.Drawing.Point(175, 357);
         this.btnSave.Name = "btnSave";
         this.btnSave.Size = new System.Drawing.Size(75, 23);
         this.btnSave.TabIndex = 5;
         this.btnSave.Text = "Save";
         this.btnSave.UseVisualStyleBackColor = true;
         this.btnSave.Click += new System.EventHandler(this.btnSave_Click);
         // 
         // groupBox7
         // 
         this.groupBox7.Controls.Add(this.numMoveSpeed);
         this.groupBox7.Controls.Add(this.label6);
         this.groupBox7.Controls.Add(this.numMoveRange);
         this.groupBox7.Controls.Add(this.label5);
         this.groupBox7.Controls.Add(this.chkFaceOnActivate);
         this.groupBox7.Controls.Add(this.chkFaceOnMove);
         this.groupBox7.Location = new System.Drawing.Point(165, 216);
         this.groupBox7.Name = "groupBox7";
         this.groupBox7.Size = new System.Drawing.Size(180, 127);
         this.groupBox7.TabIndex = 4;
         this.groupBox7.TabStop = false;
         this.groupBox7.Text = "Movement Control";
         // 
         // numMoveSpeed
         // 
         this.numMoveSpeed.Location = new System.Drawing.Point(104, 96);
         this.numMoveSpeed.Name = "numMoveSpeed";
         this.numMoveSpeed.Size = new System.Drawing.Size(43, 20);
         this.numMoveSpeed.TabIndex = 5;
         // 
         // label6
         // 
         this.label6.AutoSize = true;
         this.label6.Location = new System.Drawing.Point(6, 98);
         this.label6.Name = "label6";
         this.label6.Size = new System.Drawing.Size(84, 13);
         this.label6.TabIndex = 4;
         this.label6.Text = "Movement Freq.";
         // 
         // numMoveRange
         // 
         this.numMoveRange.Location = new System.Drawing.Point(104, 70);
         this.numMoveRange.Name = "numMoveRange";
         this.numMoveRange.Size = new System.Drawing.Size(43, 20);
         this.numMoveRange.TabIndex = 3;
         // 
         // label5
         // 
         this.label5.AutoSize = true;
         this.label5.Location = new System.Drawing.Point(6, 72);
         this.label5.Name = "label5";
         this.label5.Size = new System.Drawing.Size(95, 13);
         this.label5.TabIndex = 2;
         this.label5.Text = "Movement Range:";
         // 
         // chkFaceOnActivate
         // 
         this.chkFaceOnActivate.AutoSize = true;
         this.chkFaceOnActivate.Location = new System.Drawing.Point(6, 42);
         this.chkFaceOnActivate.Name = "chkFaceOnActivate";
         this.chkFaceOnActivate.Size = new System.Drawing.Size(107, 17);
         this.chkFaceOnActivate.TabIndex = 1;
         this.chkFaceOnActivate.Text = "Face on Activate";
         this.chkFaceOnActivate.UseVisualStyleBackColor = true;
         // 
         // chkFaceOnMove
         // 
         this.chkFaceOnMove.AutoSize = true;
         this.chkFaceOnMove.Location = new System.Drawing.Point(6, 19);
         this.chkFaceOnMove.Name = "chkFaceOnMove";
         this.chkFaceOnMove.Size = new System.Drawing.Size(95, 17);
         this.chkFaceOnMove.TabIndex = 0;
         this.chkFaceOnMove.Text = "Face on Move";
         this.chkFaceOnMove.UseVisualStyleBackColor = true;
         // 
         // groupBox6
         // 
         this.groupBox6.Controls.Add(this.cmbBubble);
         this.groupBox6.Controls.Add(this.label4);
         this.groupBox6.Controls.Add(this.txtSpriteName);
         this.groupBox6.Controls.Add(this.lblImageIndex);
         this.groupBox6.Controls.Add(this.numImageIndex);
         this.groupBox6.Controls.Add(this.chkAnimated);
         this.groupBox6.Controls.Add(this.radRight);
         this.groupBox6.Controls.Add(this.radLeft);
         this.groupBox6.Controls.Add(this.radDown);
         this.groupBox6.Controls.Add(this.radUp);
         this.groupBox6.Location = new System.Drawing.Point(4, 216);
         this.groupBox6.Name = "groupBox6";
         this.groupBox6.Size = new System.Drawing.Size(155, 172);
         this.groupBox6.TabIndex = 3;
         this.groupBox6.TabStop = false;
         this.groupBox6.Text = "Sprite";
         // 
         // label4
         // 
         this.label4.AutoSize = true;
         this.label4.Location = new System.Drawing.Point(7, 53);
         this.label4.Name = "label4";
         this.label4.Size = new System.Drawing.Size(40, 13);
         this.label4.TabIndex = 10;
         this.label4.Text = "Bubble";
         // 
         // txtSpriteName
         // 
         this.txtSpriteName.Location = new System.Drawing.Point(12, 26);
         this.txtSpriteName.Name = "txtSpriteName";
         this.txtSpriteName.Size = new System.Drawing.Size(137, 20);
         this.txtSpriteName.TabIndex = 9;
         // 
         // lblImageIndex
         // 
         this.lblImageIndex.AutoSize = true;
         this.lblImageIndex.Enabled = false;
         this.lblImageIndex.Location = new System.Drawing.Point(73, 146);
         this.lblImageIndex.Name = "lblImageIndex";
         this.lblImageIndex.Size = new System.Drawing.Size(33, 13);
         this.lblImageIndex.TabIndex = 8;
         this.lblImageIndex.Text = "Index";
         // 
         // numImageIndex
         // 
         this.numImageIndex.Enabled = false;
         this.numImageIndex.Location = new System.Drawing.Point(108, 144);
         this.numImageIndex.Maximum = new decimal(new int[] {
            1,
            0,
            0,
            0});
         this.numImageIndex.Name = "numImageIndex";
         this.numImageIndex.Size = new System.Drawing.Size(39, 20);
         this.numImageIndex.TabIndex = 7;
         // 
         // chkAnimated
         // 
         this.chkAnimated.AutoSize = true;
         this.chkAnimated.Checked = true;
         this.chkAnimated.CheckState = System.Windows.Forms.CheckState.Checked;
         this.chkAnimated.Location = new System.Drawing.Point(6, 145);
         this.chkAnimated.Name = "chkAnimated";
         this.chkAnimated.Size = new System.Drawing.Size(70, 17);
         this.chkAnimated.TabIndex = 6;
         this.chkAnimated.Text = "Animated";
         this.chkAnimated.UseVisualStyleBackColor = true;
         this.chkAnimated.CheckedChanged += new System.EventHandler(this.chkAnimated_CheckedChanged);
         // 
         // radRight
         // 
         this.radRight.AutoSize = true;
         this.radRight.Location = new System.Drawing.Point(91, 121);
         this.radRight.Name = "radRight";
         this.radRight.Size = new System.Drawing.Size(50, 17);
         this.radRight.TabIndex = 5;
         this.radRight.Text = "Right";
         this.radRight.UseVisualStyleBackColor = true;
         // 
         // radLeft
         // 
         this.radLeft.AutoSize = true;
         this.radLeft.Location = new System.Drawing.Point(91, 98);
         this.radLeft.Name = "radLeft";
         this.radLeft.Size = new System.Drawing.Size(43, 17);
         this.radLeft.TabIndex = 4;
         this.radLeft.Text = "Left";
         this.radLeft.UseVisualStyleBackColor = true;
         // 
         // radDown
         // 
         this.radDown.AutoSize = true;
         this.radDown.Checked = true;
         this.radDown.Location = new System.Drawing.Point(12, 121);
         this.radDown.Name = "radDown";
         this.radDown.Size = new System.Drawing.Size(53, 17);
         this.radDown.TabIndex = 3;
         this.radDown.TabStop = true;
         this.radDown.Text = "Down";
         this.radDown.UseVisualStyleBackColor = true;
         // 
         // radUp
         // 
         this.radUp.AutoSize = true;
         this.radUp.Location = new System.Drawing.Point(12, 99);
         this.radUp.Name = "radUp";
         this.radUp.Size = new System.Drawing.Size(39, 17);
         this.radUp.TabIndex = 2;
         this.radUp.Text = "Up";
         this.radUp.UseVisualStyleBackColor = true;
         // 
         // groupBox5
         // 
         this.groupBox5.Controls.Add(this.btnCollNone);
         this.groupBox5.Controls.Add(this.btnCollAll);
         this.groupBox5.Controls.Add(this.chkCollBottom);
         this.groupBox5.Controls.Add(this.chkCollRight);
         this.groupBox5.Controls.Add(this.chkCollTop);
         this.groupBox5.Controls.Add(this.chkCollLeft);
         this.groupBox5.Location = new System.Drawing.Point(201, 103);
         this.groupBox5.Name = "groupBox5";
         this.groupBox5.Size = new System.Drawing.Size(144, 107);
         this.groupBox5.TabIndex = 2;
         this.groupBox5.TabStop = false;
         this.groupBox5.Text = "Collision Data";
         // 
         // btnCollNone
         // 
         this.btnCollNone.Location = new System.Drawing.Point(93, 48);
         this.btnCollNone.Name = "btnCollNone";
         this.btnCollNone.Size = new System.Drawing.Size(45, 23);
         this.btnCollNone.TabIndex = 5;
         this.btnCollNone.Text = "None";
         this.btnCollNone.UseVisualStyleBackColor = true;
         this.btnCollNone.Click += new System.EventHandler(this.btnCollNone_Click);
         // 
         // btnCollAll
         // 
         this.btnCollAll.Location = new System.Drawing.Point(93, 19);
         this.btnCollAll.Name = "btnCollAll";
         this.btnCollAll.Size = new System.Drawing.Size(45, 23);
         this.btnCollAll.TabIndex = 4;
         this.btnCollAll.Text = "All";
         this.btnCollAll.UseVisualStyleBackColor = true;
         this.btnCollAll.Click += new System.EventHandler(this.btnCollAll_Click);
         // 
         // chkCollBottom
         // 
         this.chkCollBottom.AutoSize = true;
         this.chkCollBottom.Location = new System.Drawing.Point(6, 88);
         this.chkCollBottom.Name = "chkCollBottom";
         this.chkCollBottom.Size = new System.Drawing.Size(59, 17);
         this.chkCollBottom.TabIndex = 3;
         this.chkCollBottom.Text = "Bottom";
         this.chkCollBottom.UseVisualStyleBackColor = true;
         // 
         // chkCollRight
         // 
         this.chkCollRight.AutoSize = true;
         this.chkCollRight.Location = new System.Drawing.Point(6, 65);
         this.chkCollRight.Name = "chkCollRight";
         this.chkCollRight.Size = new System.Drawing.Size(51, 17);
         this.chkCollRight.TabIndex = 2;
         this.chkCollRight.Text = "Right";
         this.chkCollRight.UseVisualStyleBackColor = true;
         // 
         // chkCollTop
         // 
         this.chkCollTop.AutoSize = true;
         this.chkCollTop.Location = new System.Drawing.Point(6, 42);
         this.chkCollTop.Name = "chkCollTop";
         this.chkCollTop.Size = new System.Drawing.Size(45, 17);
         this.chkCollTop.TabIndex = 1;
         this.chkCollTop.Text = "Top";
         this.chkCollTop.UseVisualStyleBackColor = true;
         // 
         // chkCollLeft
         // 
         this.chkCollLeft.AutoSize = true;
         this.chkCollLeft.Location = new System.Drawing.Point(7, 19);
         this.chkCollLeft.Name = "chkCollLeft";
         this.chkCollLeft.Size = new System.Drawing.Size(44, 17);
         this.chkCollLeft.TabIndex = 0;
         this.chkCollLeft.Text = "Left";
         this.chkCollLeft.UseVisualStyleBackColor = true;
         // 
         // groupBox4
         // 
         this.groupBox4.Controls.Add(this.chkForeground);
         this.groupBox4.Controls.Add(this.chkAllowMovement);
         this.groupBox4.Controls.Add(this.chkAutoStart);
         this.groupBox4.Location = new System.Drawing.Point(201, 4);
         this.groupBox4.Name = "groupBox4";
         this.groupBox4.Size = new System.Drawing.Size(144, 92);
         this.groupBox4.TabIndex = 1;
         this.groupBox4.TabStop = false;
         this.groupBox4.Text = "State Options";
         // 
         // chkForeground
         // 
         this.chkForeground.AutoSize = true;
         this.chkForeground.Location = new System.Drawing.Point(7, 68);
         this.chkForeground.Name = "chkForeground";
         this.chkForeground.Size = new System.Drawing.Size(80, 17);
         this.chkForeground.TabIndex = 2;
         this.chkForeground.Text = "Foreground";
         this.chkForeground.UseVisualStyleBackColor = true;
         // 
         // chkAllowMovement
         // 
         this.chkAllowMovement.AutoSize = true;
         this.chkAllowMovement.Location = new System.Drawing.Point(7, 44);
         this.chkAllowMovement.Name = "chkAllowMovement";
         this.chkAllowMovement.Size = new System.Drawing.Size(105, 17);
         this.chkAllowMovement.TabIndex = 1;
         this.chkAllowMovement.Text = "Wait on Activate";
         this.chkAllowMovement.UseVisualStyleBackColor = true;
         // 
         // chkAutoStart
         // 
         this.chkAutoStart.AutoSize = true;
         this.chkAutoStart.Location = new System.Drawing.Point(7, 20);
         this.chkAutoStart.Name = "chkAutoStart";
         this.chkAutoStart.Size = new System.Drawing.Size(70, 17);
         this.chkAutoStart.TabIndex = 0;
         this.chkAutoStart.Text = "AutoStart";
         this.chkAutoStart.UseVisualStyleBackColor = true;
         // 
         // groupBox3
         // 
         this.groupBox3.Controls.Add(this.btnDeleteItemCond);
         this.groupBox3.Controls.Add(this.btnAddItemCond);
         this.groupBox3.Controls.Add(this.lvwItemConditions);
         this.groupBox3.Controls.Add(this.btnDeleteSwitchCond);
         this.groupBox3.Controls.Add(this.btnAddSwitchCond);
         this.groupBox3.Controls.Add(this.lvwSwitchConditions);
         this.groupBox3.Location = new System.Drawing.Point(4, 4);
         this.groupBox3.Name = "groupBox3";
         this.groupBox3.Size = new System.Drawing.Size(190, 206);
         this.groupBox3.TabIndex = 0;
         this.groupBox3.TabStop = false;
         this.groupBox3.Text = "State Conditions";
         // 
         // btnDeleteItemCond
         // 
         this.btnDeleteItemCond.Image = ((System.Drawing.Image)(resources.GetObject("btnDeleteItemCond.Image")));
         this.btnDeleteItemCond.Location = new System.Drawing.Point(159, 142);
         this.btnDeleteItemCond.Name = "btnDeleteItemCond";
         this.btnDeleteItemCond.Size = new System.Drawing.Size(25, 25);
         this.btnDeleteItemCond.TabIndex = 5;
         this.btnDeleteItemCond.UseVisualStyleBackColor = true;
         this.btnDeleteItemCond.Click += new System.EventHandler(this.btnDeleteItemCond_Click);
         // 
         // btnAddItemCond
         // 
         this.btnAddItemCond.Image = ((System.Drawing.Image)(resources.GetObject("btnAddItemCond.Image")));
         this.btnAddItemCond.Location = new System.Drawing.Point(159, 111);
         this.btnAddItemCond.Name = "btnAddItemCond";
         this.btnAddItemCond.Size = new System.Drawing.Size(25, 25);
         this.btnAddItemCond.TabIndex = 4;
         this.btnAddItemCond.UseVisualStyleBackColor = true;
         this.btnAddItemCond.Click += new System.EventHandler(this.btnAddItemCond_Click);
         // 
         // lvwItemConditions
         // 
         this.lvwItemConditions.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.ItemName,
            this.ItemCount});
         this.lvwItemConditions.FullRowSelect = true;
         this.lvwItemConditions.GridLines = true;
         this.lvwItemConditions.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
         this.lvwItemConditions.Location = new System.Drawing.Point(7, 111);
         this.lvwItemConditions.Name = "lvwItemConditions";
         this.lvwItemConditions.Size = new System.Drawing.Size(146, 85);
         this.lvwItemConditions.TabIndex = 3;
         this.lvwItemConditions.UseCompatibleStateImageBehavior = false;
         this.lvwItemConditions.View = System.Windows.Forms.View.Details;
         this.lvwItemConditions.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.lvwItemConditions_MouseDoubleClick);
         // 
         // ItemName
         // 
         this.ItemName.Text = "Item";
         this.ItemName.Width = 100;
         // 
         // ItemCount
         // 
         this.ItemCount.Text = "Count";
         this.ItemCount.Width = 40;
         // 
         // btnDeleteSwitchCond
         // 
         this.btnDeleteSwitchCond.Image = ((System.Drawing.Image)(resources.GetObject("btnDeleteSwitchCond.Image")));
         this.btnDeleteSwitchCond.Location = new System.Drawing.Point(159, 51);
         this.btnDeleteSwitchCond.Name = "btnDeleteSwitchCond";
         this.btnDeleteSwitchCond.Size = new System.Drawing.Size(25, 25);
         this.btnDeleteSwitchCond.TabIndex = 2;
         this.btnDeleteSwitchCond.UseVisualStyleBackColor = true;
         this.btnDeleteSwitchCond.Click += new System.EventHandler(this.btnDeleteSwitchCond_Click);
         // 
         // btnAddSwitchCond
         // 
         this.btnAddSwitchCond.Image = ((System.Drawing.Image)(resources.GetObject("btnAddSwitchCond.Image")));
         this.btnAddSwitchCond.Location = new System.Drawing.Point(159, 20);
         this.btnAddSwitchCond.Name = "btnAddSwitchCond";
         this.btnAddSwitchCond.Size = new System.Drawing.Size(25, 25);
         this.btnAddSwitchCond.TabIndex = 1;
         this.btnAddSwitchCond.UseVisualStyleBackColor = true;
         this.btnAddSwitchCond.Click += new System.EventHandler(this.btnAddSwitchCond_Click);
         // 
         // lvwSwitchConditions
         // 
         this.lvwSwitchConditions.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.SwitchName,
            this.SwitchState});
         this.lvwSwitchConditions.FullRowSelect = true;
         this.lvwSwitchConditions.GridLines = true;
         this.lvwSwitchConditions.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.Nonclickable;
         this.lvwSwitchConditions.Location = new System.Drawing.Point(7, 20);
         this.lvwSwitchConditions.Name = "lvwSwitchConditions";
         this.lvwSwitchConditions.Size = new System.Drawing.Size(146, 85);
         this.lvwSwitchConditions.TabIndex = 0;
         this.lvwSwitchConditions.UseCompatibleStateImageBehavior = false;
         this.lvwSwitchConditions.View = System.Windows.Forms.View.Details;
         this.lvwSwitchConditions.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.lvwSwitchConditions_MouseDoubleClick);
         // 
         // SwitchName
         // 
         this.SwitchName.Text = "Switch";
         this.SwitchName.Width = 100;
         // 
         // SwitchState
         // 
         this.SwitchState.Text = "State";
         this.SwitchState.Width = 40;
         // 
         // statesStrip
         // 
         this.statesStrip.Dock = System.Windows.Forms.DockStyle.None;
         this.statesStrip.GripStyle = System.Windows.Forms.ToolStripGripStyle.Hidden;
         this.statesStrip.Location = new System.Drawing.Point(15, 70);
         this.statesStrip.Name = "statesStrip";
         this.statesStrip.Size = new System.Drawing.Size(102, 25);
         this.statesStrip.TabIndex = 7;
         // 
         // rightClick
         // 
         this.rightClick.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.deleteToolStripMenuItem,
            this.insertToolStripMenuItem});
         this.rightClick.Name = "rightClick";
         this.rightClick.Size = new System.Drawing.Size(108, 48);
         // 
         // deleteToolStripMenuItem
         // 
         this.deleteToolStripMenuItem.Name = "deleteToolStripMenuItem";
         this.deleteToolStripMenuItem.Size = new System.Drawing.Size(107, 22);
         this.deleteToolStripMenuItem.Text = "Delete";
         this.deleteToolStripMenuItem.Click += new System.EventHandler(this.deleteToolStripMenuItem_Click);
         // 
         // insertToolStripMenuItem
         // 
         this.insertToolStripMenuItem.Name = "insertToolStripMenuItem";
         this.insertToolStripMenuItem.Size = new System.Drawing.Size(107, 22);
         this.insertToolStripMenuItem.Text = "Insert";
         this.insertToolStripMenuItem.Click += new System.EventHandler(this.insertToolStripMenuItem_Click);
         // 
         // cmbBubble
         // 
         this.cmbBubble.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
         this.cmbBubble.FormattingEnabled = true;
         this.cmbBubble.Items.AddRange(new object[] {
            "",
            "sleep",
            "qmark",
            "angry",
            "dots",
            "exclam",
            "sweat"});
         this.cmbBubble.Location = new System.Drawing.Point(12, 70);
         this.cmbBubble.Name = "cmbBubble";
         this.cmbBubble.Size = new System.Drawing.Size(121, 21);
         this.cmbBubble.TabIndex = 11;
         // 
         // GameObjectForm
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.ClientSize = new System.Drawing.Size(644, 506);
         this.Controls.Add(this.statesStrip);
         this.Controls.Add(this.lvwActions);
         this.Controls.Add(this.panel1);
         this.Controls.Add(this.groupBox2);
         this.Controls.Add(this.groupBox1);
         this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
         this.MaximizeBox = false;
         this.MinimizeBox = false;
         this.Name = "GameObjectForm";
         this.ShowIcon = false;
         this.ShowInTaskbar = false;
         this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
         this.Text = "New Object";
         this.Load += new System.EventHandler(this.GameObjectForm_Load);
         this.groupBox1.ResumeLayout(false);
         this.groupBox1.PerformLayout();
         ((System.ComponentModel.ISupportInitialize)(this.objY)).EndInit();
         ((System.ComponentModel.ISupportInitialize)(this.objX)).EndInit();
         this.groupBox2.ResumeLayout(false);
         this.panel1.ResumeLayout(false);
         this.groupBox7.ResumeLayout(false);
         this.groupBox7.PerformLayout();
         ((System.ComponentModel.ISupportInitialize)(this.numMoveSpeed)).EndInit();
         ((System.ComponentModel.ISupportInitialize)(this.numMoveRange)).EndInit();
         this.groupBox6.ResumeLayout(false);
         this.groupBox6.PerformLayout();
         ((System.ComponentModel.ISupportInitialize)(this.numImageIndex)).EndInit();
         this.groupBox5.ResumeLayout(false);
         this.groupBox5.PerformLayout();
         this.groupBox4.ResumeLayout(false);
         this.groupBox4.PerformLayout();
         this.groupBox3.ResumeLayout(false);
         this.rightClick.ResumeLayout(false);
         this.ResumeLayout(false);
         this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox txtObjectName;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.NumericUpDown objY;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.NumericUpDown objX;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.Button btnCopyState;
        private System.Windows.Forms.Button btnDeleteState;
        private System.Windows.Forms.Button btnAddState;
        private System.Windows.Forms.ListView lvwActions;
        private System.Windows.Forms.ColumnHeader columnHeader1;
        private System.Windows.Forms.ColumnHeader columnHeader2;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.GroupBox groupBox7;
        private System.Windows.Forms.NumericUpDown numMoveSpeed;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.NumericUpDown numMoveRange;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.CheckBox chkFaceOnActivate;
        private System.Windows.Forms.CheckBox chkFaceOnMove;
        private System.Windows.Forms.GroupBox groupBox6;
        private System.Windows.Forms.Label lblImageIndex;
        private System.Windows.Forms.NumericUpDown numImageIndex;
        private System.Windows.Forms.CheckBox chkAnimated;
        private System.Windows.Forms.RadioButton radRight;
        private System.Windows.Forms.RadioButton radLeft;
        private System.Windows.Forms.RadioButton radDown;
        private System.Windows.Forms.RadioButton radUp;
        private System.Windows.Forms.GroupBox groupBox5;
        private System.Windows.Forms.Button btnCollNone;
        private System.Windows.Forms.Button btnCollAll;
        private System.Windows.Forms.CheckBox chkCollBottom;
        private System.Windows.Forms.CheckBox chkCollRight;
        private System.Windows.Forms.CheckBox chkCollTop;
        private System.Windows.Forms.CheckBox chkCollLeft;
        private System.Windows.Forms.GroupBox groupBox4;
        private System.Windows.Forms.CheckBox chkForeground;
        private System.Windows.Forms.CheckBox chkAllowMovement;
        private System.Windows.Forms.CheckBox chkAutoStart;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.Button btnDeleteItemCond;
        private System.Windows.Forms.Button btnAddItemCond;
        private System.Windows.Forms.ListView lvwItemConditions;
        private System.Windows.Forms.ColumnHeader ItemName;
        private System.Windows.Forms.ColumnHeader ItemCount;
        private System.Windows.Forms.Button btnDeleteSwitchCond;
        private System.Windows.Forms.Button btnAddSwitchCond;
        private System.Windows.Forms.ListView lvwSwitchConditions;
        private System.Windows.Forms.ColumnHeader SwitchName;
        private System.Windows.Forms.ColumnHeader SwitchState;
        private System.Windows.Forms.ToolStrip statesStrip;
        private System.Windows.Forms.Button btnCancel;
        private System.Windows.Forms.Button btnSave;
        private System.Windows.Forms.TextBox txtSpriteName;
        private System.Windows.Forms.ContextMenuStrip rightClick;
        private System.Windows.Forms.ToolStripMenuItem deleteToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem insertToolStripMenuItem;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.ComboBox cmbBubble;
    }
}