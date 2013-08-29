namespace BladeCraft.Forms
{
   partial class ObjectHeader
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
         this.btnCancel = new System.Windows.Forms.Button();
         this.btnSave = new System.Windows.Forms.Button();
         this.txtScript = new System.Windows.Forms.RichTextBox();
         this.SuspendLayout();
         // 
         // btnCancel
         // 
         this.btnCancel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
         this.btnCancel.Location = new System.Drawing.Point(432, 349);
         this.btnCancel.Name = "btnCancel";
         this.btnCancel.Size = new System.Drawing.Size(75, 23);
         this.btnCancel.TabIndex = 1;
         this.btnCancel.Text = "Cancel";
         this.btnCancel.UseVisualStyleBackColor = true;
         this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
         // 
         // btnSave
         // 
         this.btnSave.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
         this.btnSave.Location = new System.Drawing.Point(351, 349);
         this.btnSave.Name = "btnSave";
         this.btnSave.Size = new System.Drawing.Size(75, 23);
         this.btnSave.TabIndex = 2;
         this.btnSave.Text = "Save";
         this.btnSave.UseVisualStyleBackColor = true;
         this.btnSave.Click += new System.EventHandler(this.btnSave_Click);
         // 
         // txtScript
         // 
         this.txtScript.Location = new System.Drawing.Point(12, 12);
         this.txtScript.Name = "txtScript";
         this.txtScript.Size = new System.Drawing.Size(495, 331);
         this.txtScript.TabIndex = 3;
         this.txtScript.Text = "";
         // 
         // ObjectHeader
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.ClientSize = new System.Drawing.Size(519, 384);
         this.Controls.Add(this.txtScript);
         this.Controls.Add(this.btnSave);
         this.Controls.Add(this.btnCancel);
         this.Name = "ObjectHeader";
         this.ShowIcon = false;
         this.ShowInTaskbar = false;
         this.Text = "Object Header";
         this.Load += new System.EventHandler(this.ObjectHeader_Load);
         this.ResumeLayout(false);

      }

      #endregion

      private System.Windows.Forms.Button btnCancel;
      private System.Windows.Forms.Button btnSave;
      private System.Windows.Forms.RichTextBox txtScript;
   }
}