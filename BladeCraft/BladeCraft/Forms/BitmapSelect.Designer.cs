namespace BladeCraft.Forms
{
    partial class BitmapSelect
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
            this.label1 = new System.Windows.Forms.Label();
            this.bmpPanel = new System.Windows.Forms.Panel();
            this.label2 = new System.Windows.Forms.Label();
            this.btnCancel = new System.Windows.Forms.Button();
            this.btnUse = new System.Windows.Forms.Button();
            this.serialPort1 = new System.IO.Ports.SerialPort(this.components);
            this.tvwFiles = new System.Windows.Forms.TreeView();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(9, 13);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(23, 13);
            this.label1.TabIndex = 1;
            this.label1.Text = "File";
            // 
            // bmpPanel
            // 
            this.bmpPanel.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.bmpPanel.Location = new System.Drawing.Point(165, 29);
            this.bmpPanel.Name = "bmpPanel";
            this.bmpPanel.Size = new System.Drawing.Size(228, 258);
            this.bmpPanel.TabIndex = 2;
            this.bmpPanel.Paint += new System.Windows.Forms.PaintEventHandler(this.bmpPanel_Paint);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(162, 13);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(48, 13);
            this.label2.TabIndex = 3;
            this.label2.Text = "Preview:";
            // 
            // btnCancel
            // 
            this.btnCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
            this.btnCancel.Location = new System.Drawing.Point(318, 293);
            this.btnCancel.Name = "btnCancel";
            this.btnCancel.Size = new System.Drawing.Size(75, 23);
            this.btnCancel.TabIndex = 4;
            this.btnCancel.Text = "Cancel";
            this.btnCancel.UseVisualStyleBackColor = true;
            // 
            // btnUse
            // 
            this.btnUse.DialogResult = System.Windows.Forms.DialogResult.OK;
            this.btnUse.Location = new System.Drawing.Point(237, 293);
            this.btnUse.Name = "btnUse";
            this.btnUse.Size = new System.Drawing.Size(75, 23);
            this.btnUse.TabIndex = 5;
            this.btnUse.Text = "Use";
            this.btnUse.UseVisualStyleBackColor = true;
            // 
            // tvwFiles
            // 
            this.tvwFiles.FullRowSelect = true;
            this.tvwFiles.Location = new System.Drawing.Point(12, 29);
            this.tvwFiles.Name = "tvwFiles";
            this.tvwFiles.Size = new System.Drawing.Size(147, 258);
            this.tvwFiles.TabIndex = 6;
            this.tvwFiles.AfterSelect += new System.Windows.Forms.TreeViewEventHandler(this.tvwFiles_AfterSelect);
            // 
            // BitmapSelect
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(405, 328);
            this.ControlBox = false;
            this.Controls.Add(this.tvwFiles);
            this.Controls.Add(this.btnUse);
            this.Controls.Add(this.btnCancel);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.bmpPanel);
            this.Controls.Add(this.label1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Name = "BitmapSelect";
            this.Text = "Select Bitmap";
            this.Load += new System.EventHandler(this.BitmapSelect_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Panel bmpPanel;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button btnCancel;
        private System.Windows.Forms.Button btnUse;
        private System.IO.Ports.SerialPort serialPort1;
        private System.Windows.Forms.TreeView tvwFiles;
    }
}