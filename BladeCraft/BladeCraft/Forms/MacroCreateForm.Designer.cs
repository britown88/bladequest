namespace BladeCraft.Forms
{
   partial class MacroCreateForm
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
         this.numY = new System.Windows.Forms.NumericUpDown();
         this.numX = new System.Windows.Forms.NumericUpDown();
         this.btnCancel = new System.Windows.Forms.Button();
         this.btnSave = new System.Windows.Forms.Button();
         this.label6 = new System.Windows.Forms.Label();
         this.label5 = new System.Windows.Forms.Label();
         this.MacroNameTxt = new System.Windows.Forms.TextBox();
         this.label2 = new System.Windows.Forms.Label();
         this.label1 = new System.Windows.Forms.Label();
         ((System.ComponentModel.ISupportInitialize)(this.numY)).BeginInit();
         ((System.ComponentModel.ISupportInitialize)(this.numX)).BeginInit();
         this.SuspendLayout();
         // 
         // numY
         // 
         this.numY.Location = new System.Drawing.Point(227, 38);
         this.numY.Maximum = new decimal(new int[] {
            250,
            0,
            0,
            0});
         this.numY.Minimum = new decimal(new int[] {
            1,
            0,
            0,
            0});
         this.numY.Name = "numY";
         this.numY.Size = new System.Drawing.Size(45, 20);
         this.numY.TabIndex = 31;
         this.numY.Value = new decimal(new int[] {
            2,
            0,
            0,
            0});
         // 
         // numX
         // 
         this.numX.Location = new System.Drawing.Point(155, 38);
         this.numX.Maximum = new decimal(new int[] {
            250,
            0,
            0,
            0});
         this.numX.Minimum = new decimal(new int[] {
            1,
            0,
            0,
            0});
         this.numX.Name = "numX";
         this.numX.Size = new System.Drawing.Size(45, 20);
         this.numX.TabIndex = 30;
         this.numX.Value = new decimal(new int[] {
            2,
            0,
            0,
            0});
         // 
         // btnCancel
         // 
         this.btnCancel.Location = new System.Drawing.Point(197, 64);
         this.btnCancel.Name = "btnCancel";
         this.btnCancel.Size = new System.Drawing.Size(75, 23);
         this.btnCancel.TabIndex = 29;
         this.btnCancel.Text = "Cancel";
         this.btnCancel.UseVisualStyleBackColor = true;
         this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
         // 
         // btnSave
         // 
         this.btnSave.Location = new System.Drawing.Point(116, 64);
         this.btnSave.Name = "btnSave";
         this.btnSave.Size = new System.Drawing.Size(75, 23);
         this.btnSave.TabIndex = 28;
         this.btnSave.Text = "Save";
         this.btnSave.UseVisualStyleBackColor = true;
         this.btnSave.Click += new System.EventHandler(this.btnSave_Click);
         // 
         // label6
         // 
         this.label6.AutoSize = true;
         this.label6.Location = new System.Drawing.Point(209, 41);
         this.label6.Name = "label6";
         this.label6.Size = new System.Drawing.Size(17, 13);
         this.label6.TabIndex = 26;
         this.label6.Text = "Y:";
         // 
         // label5
         // 
         this.label5.AutoSize = true;
         this.label5.Location = new System.Drawing.Point(132, 41);
         this.label5.Name = "label5";
         this.label5.Size = new System.Drawing.Size(17, 13);
         this.label5.TabIndex = 25;
         this.label5.Text = "X:";
         // 
         // MacroNameTxt
         // 
         this.MacroNameTxt.Location = new System.Drawing.Point(135, 12);
         this.MacroNameTxt.MaxLength = 25;
         this.MacroNameTxt.Name = "MacroNameTxt";
         this.MacroNameTxt.Size = new System.Drawing.Size(137, 20);
         this.MacroNameTxt.TabIndex = 24;
         // 
         // label2
         // 
         this.label2.AutoSize = true;
         this.label2.Location = new System.Drawing.Point(10, 41);
         this.label2.Name = "label2";
         this.label2.Size = new System.Drawing.Size(27, 13);
         this.label2.TabIndex = 22;
         this.label2.Text = "Size";
         // 
         // label1
         // 
         this.label1.AutoSize = true;
         this.label1.Location = new System.Drawing.Point(10, 15);
         this.label1.Name = "label1";
         this.label1.Size = new System.Drawing.Size(68, 13);
         this.label1.TabIndex = 21;
         this.label1.Text = "Macro Name";
         // 
         // MacroCreateForm
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.ClientSize = new System.Drawing.Size(284, 98);
         this.Controls.Add(this.numY);
         this.Controls.Add(this.numX);
         this.Controls.Add(this.btnCancel);
         this.Controls.Add(this.btnSave);
         this.Controls.Add(this.label6);
         this.Controls.Add(this.label5);
         this.Controls.Add(this.MacroNameTxt);
         this.Controls.Add(this.label2);
         this.Controls.Add(this.label1);
         this.Name = "MacroCreateForm";
         this.Text = "New Macro";
         ((System.ComponentModel.ISupportInitialize)(this.numY)).EndInit();
         ((System.ComponentModel.ISupportInitialize)(this.numX)).EndInit();
         this.ResumeLayout(false);
         this.PerformLayout();

      }

      #endregion

      private System.Windows.Forms.NumericUpDown numY;
      private System.Windows.Forms.NumericUpDown numX;
      private System.Windows.Forms.Button btnCancel;
      private System.Windows.Forms.Button btnSave;
      private System.Windows.Forms.Label label6;
      private System.Windows.Forms.Label label5;
      private System.Windows.Forms.TextBox MacroNameTxt;
      private System.Windows.Forms.Label label2;
      private System.Windows.Forms.Label label1;
   }
}