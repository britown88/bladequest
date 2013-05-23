namespace BladeCraft.Forms.ActionForms
{
   partial class formReactionBubble
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
         this.label1 = new System.Windows.Forms.Label();
         this.txtTarget = new System.Windows.Forms.TextBox();
         this.label2 = new System.Windows.Forms.Label();
         this.cmbBubble = new System.Windows.Forms.ComboBox();
         this.label3 = new System.Windows.Forms.Label();
         this.numDuration = new System.Windows.Forms.NumericUpDown();
         this.chkLoop = new System.Windows.Forms.CheckBox();
         this.chkWait = new System.Windows.Forms.CheckBox();
         this.chkClose = new System.Windows.Forms.CheckBox();
         this.button1 = new System.Windows.Forms.Button();
         this.button2 = new System.Windows.Forms.Button();
         ((System.ComponentModel.ISupportInitialize)(this.numDuration)).BeginInit();
         this.SuspendLayout();
         // 
         // label1
         // 
         this.label1.AutoSize = true;
         this.label1.Location = new System.Drawing.Point(13, 13);
         this.label1.Name = "label1";
         this.label1.Size = new System.Drawing.Size(38, 13);
         this.label1.TabIndex = 0;
         this.label1.Text = "Target";
         // 
         // txtTarget
         // 
         this.txtTarget.Location = new System.Drawing.Point(13, 30);
         this.txtTarget.Name = "txtTarget";
         this.txtTarget.Size = new System.Drawing.Size(121, 20);
         this.txtTarget.TabIndex = 1;
         // 
         // label2
         // 
         this.label2.AutoSize = true;
         this.label2.Location = new System.Drawing.Point(13, 57);
         this.label2.Name = "label2";
         this.label2.Size = new System.Drawing.Size(40, 13);
         this.label2.TabIndex = 2;
         this.label2.Text = "Bubble";
         // 
         // cmbBubble
         // 
         this.cmbBubble.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
         this.cmbBubble.FormattingEnabled = true;
         this.cmbBubble.Items.AddRange(new object[] {
            "sleep",
            "qmark",
            "angry",
            "dots",
            "exclam",
            "sweat"});
         this.cmbBubble.Location = new System.Drawing.Point(13, 74);
         this.cmbBubble.Name = "cmbBubble";
         this.cmbBubble.Size = new System.Drawing.Size(121, 21);
         this.cmbBubble.TabIndex = 3;
         // 
         // label3
         // 
         this.label3.AutoSize = true;
         this.label3.Location = new System.Drawing.Point(13, 98);
         this.label3.Name = "label3";
         this.label3.Size = new System.Drawing.Size(47, 13);
         this.label3.TabIndex = 4;
         this.label3.Text = "Duration";
         // 
         // numDuration
         // 
         this.numDuration.DecimalPlaces = 2;
         this.numDuration.Location = new System.Drawing.Point(13, 114);
         this.numDuration.Minimum = new decimal(new int[] {
            1,
            0,
            0,
            -2147483648});
         this.numDuration.Name = "numDuration";
         this.numDuration.Size = new System.Drawing.Size(75, 20);
         this.numDuration.TabIndex = 5;
         // 
         // chkLoop
         // 
         this.chkLoop.AutoSize = true;
         this.chkLoop.Location = new System.Drawing.Point(13, 141);
         this.chkLoop.Name = "chkLoop";
         this.chkLoop.Size = new System.Drawing.Size(56, 17);
         this.chkLoop.TabIndex = 6;
         this.chkLoop.Text = "Loop?";
         this.chkLoop.UseVisualStyleBackColor = true;
         // 
         // chkWait
         // 
         this.chkWait.AutoSize = true;
         this.chkWait.Location = new System.Drawing.Point(13, 165);
         this.chkWait.Name = "chkWait";
         this.chkWait.Size = new System.Drawing.Size(54, 17);
         this.chkWait.TabIndex = 7;
         this.chkWait.Text = "Wait?";
         this.chkWait.UseVisualStyleBackColor = true;
         // 
         // chkClose
         // 
         this.chkClose.AutoSize = true;
         this.chkClose.Location = new System.Drawing.Point(140, 32);
         this.chkClose.Name = "chkClose";
         this.chkClose.Size = new System.Drawing.Size(52, 17);
         this.chkClose.TabIndex = 8;
         this.chkClose.Text = "Close";
         this.chkClose.UseVisualStyleBackColor = true;
         this.chkClose.CheckedChanged += new System.EventHandler(this.chkClose_CheckedChanged);
         // 
         // button1
         // 
         this.button1.Location = new System.Drawing.Point(13, 189);
         this.button1.Name = "button1";
         this.button1.Size = new System.Drawing.Size(75, 23);
         this.button1.TabIndex = 9;
         this.button1.Text = "OK";
         this.button1.UseVisualStyleBackColor = true;
         this.button1.Click += new System.EventHandler(this.button1_Click);
         // 
         // button2
         // 
         this.button2.Location = new System.Drawing.Point(110, 189);
         this.button2.Name = "button2";
         this.button2.Size = new System.Drawing.Size(75, 23);
         this.button2.TabIndex = 10;
         this.button2.Text = "Cancel";
         this.button2.UseVisualStyleBackColor = true;
         this.button2.Click += new System.EventHandler(this.button2_Click);
         // 
         // formReactionBubble
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.ClientSize = new System.Drawing.Size(197, 223);
         this.ControlBox = false;
         this.Controls.Add(this.button2);
         this.Controls.Add(this.button1);
         this.Controls.Add(this.chkClose);
         this.Controls.Add(this.chkWait);
         this.Controls.Add(this.chkLoop);
         this.Controls.Add(this.numDuration);
         this.Controls.Add(this.label3);
         this.Controls.Add(this.cmbBubble);
         this.Controls.Add(this.label2);
         this.Controls.Add(this.txtTarget);
         this.Controls.Add(this.label1);
         this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
         this.Name = "formReactionBubble";
         this.Text = "Reaction Bubble";
         this.Load += new System.EventHandler(this.formReactionBubble_Load);
         ((System.ComponentModel.ISupportInitialize)(this.numDuration)).EndInit();
         this.ResumeLayout(false);
         this.PerformLayout();

      }

      #endregion

      private System.Windows.Forms.Label label1;
      private System.Windows.Forms.TextBox txtTarget;
      private System.Windows.Forms.Label label2;
      private System.Windows.Forms.ComboBox cmbBubble;
      private System.Windows.Forms.Label label3;
      private System.Windows.Forms.NumericUpDown numDuration;
      private System.Windows.Forms.CheckBox chkLoop;
      private System.Windows.Forms.CheckBox chkWait;
      private System.Windows.Forms.CheckBox chkClose;
      private System.Windows.Forms.Button button1;
      private System.Windows.Forms.Button button2;
   }
}