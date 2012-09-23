namespace BladeCraft.Forms.ActionForms
{
    partial class formFade
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
            this.fadeSpeed = new System.Windows.Forms.NumericUpDown();
            this.a = new System.Windows.Forms.NumericUpDown();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.g = new System.Windows.Forms.NumericUpDown();
            this.label4 = new System.Windows.Forms.Label();
            this.b = new System.Windows.Forms.NumericUpDown();
            this.label5 = new System.Windows.Forms.Label();
            this.r = new System.Windows.Forms.NumericUpDown();
            this.fadeOut = new System.Windows.Forms.CheckBox();
            this.btnOK = new System.Windows.Forms.Button();
            this.btnCancel = new System.Windows.Forms.Button();
            this.wait = new System.Windows.Forms.CheckBox();
            ((System.ComponentModel.ISupportInitialize)(this.fadeSpeed)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.a)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.g)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.b)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.r)).BeginInit();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 9);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(65, 13);
            this.label1.TabIndex = 0;
            this.label1.Text = "Fade Speed";
            // 
            // fadeSpeed
            // 
            this.fadeSpeed.Location = new System.Drawing.Point(83, 7);
            this.fadeSpeed.Name = "fadeSpeed";
            this.fadeSpeed.Size = new System.Drawing.Size(56, 20);
            this.fadeSpeed.TabIndex = 1;
            // 
            // a
            // 
            this.a.Location = new System.Drawing.Point(34, 37);
            this.a.Maximum = new decimal(new int[] {
            255,
            0,
            0,
            0});
            this.a.Name = "a";
            this.a.Size = new System.Drawing.Size(39, 20);
            this.a.TabIndex = 2;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(12, 39);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(17, 13);
            this.label2.TabIndex = 3;
            this.label2.Text = "A:";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(146, 39);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(18, 13);
            this.label3.TabIndex = 5;
            this.label3.Text = "G:";
            // 
            // g
            // 
            this.g.Location = new System.Drawing.Point(168, 37);
            this.g.Maximum = new decimal(new int[] {
            255,
            0,
            0,
            0});
            this.g.Name = "g";
            this.g.Size = new System.Drawing.Size(39, 20);
            this.g.TabIndex = 4;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(213, 39);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(17, 13);
            this.label4.TabIndex = 7;
            this.label4.Text = "B:";
            // 
            // b
            // 
            this.b.Location = new System.Drawing.Point(235, 37);
            this.b.Maximum = new decimal(new int[] {
            255,
            0,
            0,
            0});
            this.b.Name = "b";
            this.b.Size = new System.Drawing.Size(39, 20);
            this.b.TabIndex = 6;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(79, 39);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(18, 13);
            this.label5.TabIndex = 9;
            this.label5.Text = "R:";
            // 
            // r
            // 
            this.r.Location = new System.Drawing.Point(101, 37);
            this.r.Maximum = new decimal(new int[] {
            255,
            0,
            0,
            0});
            this.r.Name = "r";
            this.r.Size = new System.Drawing.Size(39, 20);
            this.r.TabIndex = 8;
            // 
            // fadeOut
            // 
            this.fadeOut.AutoSize = true;
            this.fadeOut.Location = new System.Drawing.Point(15, 71);
            this.fadeOut.Name = "fadeOut";
            this.fadeOut.Size = new System.Drawing.Size(70, 17);
            this.fadeOut.TabIndex = 10;
            this.fadeOut.Text = "Fade Out";
            this.fadeOut.UseVisualStyleBackColor = true;
            // 
            // btnOK
            // 
            this.btnOK.Location = new System.Drawing.Point(116, 110);
            this.btnOK.Name = "btnOK";
            this.btnOK.Size = new System.Drawing.Size(75, 23);
            this.btnOK.TabIndex = 11;
            this.btnOK.Text = "OK";
            this.btnOK.UseVisualStyleBackColor = true;
            this.btnOK.Click += new System.EventHandler(this.btnOK_Click);
            // 
            // btnCancel
            // 
            this.btnCancel.Location = new System.Drawing.Point(197, 110);
            this.btnCancel.Name = "btnCancel";
            this.btnCancel.Size = new System.Drawing.Size(75, 23);
            this.btnCancel.TabIndex = 12;
            this.btnCancel.Text = "CANCEL";
            this.btnCancel.UseVisualStyleBackColor = true;
            this.btnCancel.Click += new System.EventHandler(this.btnCancel_Click);
            // 
            // wait
            // 
            this.wait.AutoSize = true;
            this.wait.Location = new System.Drawing.Point(92, 71);
            this.wait.Name = "wait";
            this.wait.Size = new System.Drawing.Size(54, 17);
            this.wait.TabIndex = 13;
            this.wait.Text = "Wait?";
            this.wait.UseVisualStyleBackColor = true;
            // 
            // formFade
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(284, 145);
            this.ControlBox = false;
            this.Controls.Add(this.wait);
            this.Controls.Add(this.btnCancel);
            this.Controls.Add(this.btnOK);
            this.Controls.Add(this.fadeOut);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.r);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.b);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.g);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.a);
            this.Controls.Add(this.fadeSpeed);
            this.Controls.Add(this.label1);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Name = "formFade";
            this.Text = "Fade Control";
            this.Load += new System.EventHandler(this.formFade_Load);
            ((System.ComponentModel.ISupportInitialize)(this.fadeSpeed)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.a)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.g)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.b)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.r)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.NumericUpDown fadeSpeed;
        private System.Windows.Forms.NumericUpDown a;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.NumericUpDown g;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.NumericUpDown b;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.NumericUpDown r;
        private System.Windows.Forms.CheckBox fadeOut;
        private System.Windows.Forms.Button btnOK;
        private System.Windows.Forms.Button btnCancel;
        private System.Windows.Forms.CheckBox wait;
    }
}