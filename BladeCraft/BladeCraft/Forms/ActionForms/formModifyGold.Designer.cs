namespace BladeCraft.Forms.ActionForms
{
    partial class formModifyGold
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
            this.gold = new System.Windows.Forms.NumericUpDown();
            this.lblgold = new System.Windows.Forms.Label();
            this.button1 = new System.Windows.Forms.Button();
            this.button2 = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.gold)).BeginInit();
            this.SuspendLayout();
            // 
            // gold
            // 
            this.gold.Location = new System.Drawing.Point(12, 29);
            this.gold.Maximum = new decimal(new int[] {
            100000,
            0,
            0,
            0});
            this.gold.Minimum = new decimal(new int[] {
            100000,
            0,
            0,
            -2147483648});
            this.gold.Name = "gold";
            this.gold.Size = new System.Drawing.Size(156, 20);
            this.gold.TabIndex = 0;
            // 
            // lblgold
            // 
            this.lblgold.AutoSize = true;
            this.lblgold.Location = new System.Drawing.Point(12, 13);
            this.lblgold.Name = "lblgold";
            this.lblgold.Size = new System.Drawing.Size(29, 13);
            this.lblgold.TabIndex = 1;
            this.lblgold.Text = "Gold";
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(12, 69);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 23);
            this.button1.TabIndex = 2;
            this.button1.Text = "OK";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // button2
            // 
            this.button2.Location = new System.Drawing.Point(93, 69);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(75, 23);
            this.button2.TabIndex = 3;
            this.button2.Text = "Cancel";
            this.button2.UseVisualStyleBackColor = true;
            this.button2.Click += new System.EventHandler(this.button2_Click);
            // 
            // formModifyGold
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(180, 110);
            this.ControlBox = false;
            this.Controls.Add(this.button2);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.lblgold);
            this.Controls.Add(this.gold);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Name = "formModifyGold";
            this.Text = "Set Gold Amount";
            this.Load += new System.EventHandler(this.formModifyGold_Load);
            ((System.ComponentModel.ISupportInitialize)(this.gold)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.NumericUpDown gold;
        private System.Windows.Forms.Label lblgold;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
    }
}