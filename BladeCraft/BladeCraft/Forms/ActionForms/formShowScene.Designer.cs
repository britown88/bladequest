namespace BladeCraft.Forms.ActionForms
{
    partial class formShowScene
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
         this.sceneName = new System.Windows.Forms.TextBox();
         this.button1 = new System.Windows.Forms.Button();
         this.button2 = new System.Windows.Forms.Button();
         this.radInput = new System.Windows.Forms.RadioButton();
         this.radTimer = new System.Windows.Forms.RadioButton();
         this.groupBox1 = new System.Windows.Forms.GroupBox();
         this.groupBox2 = new System.Windows.Forms.GroupBox();
         this.label5 = new System.Windows.Forms.Label();
         this.numR = new System.Windows.Forms.NumericUpDown();
         this.label4 = new System.Windows.Forms.Label();
         this.numB = new System.Windows.Forms.NumericUpDown();
         this.label3 = new System.Windows.Forms.Label();
         this.numG = new System.Windows.Forms.NumericUpDown();
         this.numFade = new System.Windows.Forms.NumericUpDown();
         this.label2 = new System.Windows.Forms.Label();
         this.chkWait = new System.Windows.Forms.CheckBox();
         this.numTime = new System.Windows.Forms.NumericUpDown();
         this.label6 = new System.Windows.Forms.Label();
         this.groupBox1.SuspendLayout();
         this.groupBox2.SuspendLayout();
         ((System.ComponentModel.ISupportInitialize)(this.numR)).BeginInit();
         ((System.ComponentModel.ISupportInitialize)(this.numB)).BeginInit();
         ((System.ComponentModel.ISupportInitialize)(this.numG)).BeginInit();
         ((System.ComponentModel.ISupportInitialize)(this.numFade)).BeginInit();
         ((System.ComponentModel.ISupportInitialize)(this.numTime)).BeginInit();
         this.SuspendLayout();
         // 
         // label1
         // 
         this.label1.AutoSize = true;
         this.label1.Location = new System.Drawing.Point(13, 13);
         this.label1.Name = "label1";
         this.label1.Size = new System.Drawing.Size(69, 13);
         this.label1.TabIndex = 0;
         this.label1.Text = "Scene Name";
         // 
         // sceneName
         // 
         this.sceneName.Location = new System.Drawing.Point(13, 30);
         this.sceneName.Name = "sceneName";
         this.sceneName.Size = new System.Drawing.Size(168, 20);
         this.sceneName.TabIndex = 1;
         // 
         // button1
         // 
         this.button1.Location = new System.Drawing.Point(12, 209);
         this.button1.Name = "button1";
         this.button1.Size = new System.Drawing.Size(75, 23);
         this.button1.TabIndex = 2;
         this.button1.Text = "OK";
         this.button1.UseVisualStyleBackColor = true;
         this.button1.Click += new System.EventHandler(this.button1_Click);
         // 
         // button2
         // 
         this.button2.Location = new System.Drawing.Point(115, 209);
         this.button2.Name = "button2";
         this.button2.Size = new System.Drawing.Size(75, 23);
         this.button2.TabIndex = 3;
         this.button2.Text = "Cancel";
         this.button2.UseVisualStyleBackColor = true;
         this.button2.Click += new System.EventHandler(this.button2_Click);
         // 
         // radInput
         // 
         this.radInput.AutoSize = true;
         this.radInput.Location = new System.Drawing.Point(6, 19);
         this.radInput.Name = "radInput";
         this.radInput.Size = new System.Drawing.Size(49, 17);
         this.radInput.TabIndex = 4;
         this.radInput.TabStop = true;
         this.radInput.Text = "Input";
         this.radInput.UseVisualStyleBackColor = true;
         this.radInput.CheckedChanged += new System.EventHandler(this.radInput_CheckedChanged);
         // 
         // radTimer
         // 
         this.radTimer.AutoSize = true;
         this.radTimer.Location = new System.Drawing.Point(61, 19);
         this.radTimer.Name = "radTimer";
         this.radTimer.Size = new System.Drawing.Size(51, 17);
         this.radTimer.TabIndex = 5;
         this.radTimer.TabStop = true;
         this.radTimer.Text = "Timer";
         this.radTimer.UseVisualStyleBackColor = true;
         this.radTimer.CheckedChanged += new System.EventHandler(this.radTimer_CheckedChanged);
         // 
         // groupBox1
         // 
         this.groupBox1.Controls.Add(this.label6);
         this.groupBox1.Controls.Add(this.radInput);
         this.groupBox1.Controls.Add(this.radTimer);
         this.groupBox1.Location = new System.Drawing.Point(13, 57);
         this.groupBox1.Name = "groupBox1";
         this.groupBox1.Size = new System.Drawing.Size(168, 43);
         this.groupBox1.TabIndex = 6;
         this.groupBox1.TabStop = false;
         this.groupBox1.Text = "End Condition";
         // 
         // groupBox2
         // 
         this.groupBox2.Controls.Add(this.label2);
         this.groupBox2.Controls.Add(this.numFade);
         this.groupBox2.Controls.Add(this.numB);
         this.groupBox2.Controls.Add(this.label5);
         this.groupBox2.Controls.Add(this.numG);
         this.groupBox2.Controls.Add(this.numR);
         this.groupBox2.Controls.Add(this.label4);
         this.groupBox2.Controls.Add(this.label3);
         this.groupBox2.Location = new System.Drawing.Point(13, 107);
         this.groupBox2.Name = "groupBox2";
         this.groupBox2.Size = new System.Drawing.Size(168, 96);
         this.groupBox2.TabIndex = 7;
         this.groupBox2.TabStop = false;
         this.groupBox2.Text = "ExitFade";
         // 
         // label5
         // 
         this.label5.AutoSize = true;
         this.label5.Location = new System.Drawing.Point(6, 16);
         this.label5.Name = "label5";
         this.label5.Size = new System.Drawing.Size(18, 13);
         this.label5.TabIndex = 15;
         this.label5.Text = "R:";
         // 
         // numR
         // 
         this.numR.Location = new System.Drawing.Point(30, 14);
         this.numR.Maximum = new decimal(new int[] {
            255,
            0,
            0,
            0});
         this.numR.Name = "numR";
         this.numR.Size = new System.Drawing.Size(39, 20);
         this.numR.TabIndex = 14;
         // 
         // label4
         // 
         this.label4.AutoSize = true;
         this.label4.Location = new System.Drawing.Point(6, 68);
         this.label4.Name = "label4";
         this.label4.Size = new System.Drawing.Size(17, 13);
         this.label4.TabIndex = 13;
         this.label4.Text = "B:";
         // 
         // numB
         // 
         this.numB.Location = new System.Drawing.Point(30, 66);
         this.numB.Maximum = new decimal(new int[] {
            255,
            0,
            0,
            0});
         this.numB.Name = "numB";
         this.numB.Size = new System.Drawing.Size(39, 20);
         this.numB.TabIndex = 12;
         // 
         // label3
         // 
         this.label3.AutoSize = true;
         this.label3.Location = new System.Drawing.Point(6, 42);
         this.label3.Name = "label3";
         this.label3.Size = new System.Drawing.Size(18, 13);
         this.label3.TabIndex = 11;
         this.label3.Text = "G:";
         // 
         // numG
         // 
         this.numG.Location = new System.Drawing.Point(30, 40);
         this.numG.Maximum = new decimal(new int[] {
            255,
            0,
            0,
            0});
         this.numG.Name = "numG";
         this.numG.Size = new System.Drawing.Size(39, 20);
         this.numG.TabIndex = 10;
         // 
         // numFade
         // 
         this.numFade.Location = new System.Drawing.Point(75, 40);
         this.numFade.Name = "numFade";
         this.numFade.Size = new System.Drawing.Size(82, 20);
         this.numFade.TabIndex = 16;
         // 
         // label2
         // 
         this.label2.AutoSize = true;
         this.label2.Location = new System.Drawing.Point(76, 20);
         this.label2.Name = "label2";
         this.label2.Size = new System.Drawing.Size(65, 13);
         this.label2.TabIndex = 17;
         this.label2.Text = "Fade Speed";
         // 
         // chkWait
         // 
         this.chkWait.AutoSize = true;
         this.chkWait.Location = new System.Drawing.Point(127, 7);
         this.chkWait.Name = "chkWait";
         this.chkWait.Size = new System.Drawing.Size(54, 17);
         this.chkWait.TabIndex = 8;
         this.chkWait.Text = "Wait?";
         this.chkWait.UseVisualStyleBackColor = true;
         // 
         // numTime
         // 
         this.numTime.DecimalPlaces = 2;
         this.numTime.Location = new System.Drawing.Point(127, 76);
         this.numTime.Name = "numTime";
         this.numTime.Size = new System.Drawing.Size(48, 20);
         this.numTime.TabIndex = 6;
         // 
         // label6
         // 
         this.label6.AutoSize = true;
         this.label6.Location = new System.Drawing.Point(111, 3);
         this.label6.Name = "label6";
         this.label6.Size = new System.Drawing.Size(30, 13);
         this.label6.TabIndex = 6;
         this.label6.Text = "Time";
         // 
         // formShowScene
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.ClientSize = new System.Drawing.Size(193, 243);
         this.ControlBox = false;
         this.Controls.Add(this.numTime);
         this.Controls.Add(this.chkWait);
         this.Controls.Add(this.groupBox2);
         this.Controls.Add(this.groupBox1);
         this.Controls.Add(this.button2);
         this.Controls.Add(this.button1);
         this.Controls.Add(this.sceneName);
         this.Controls.Add(this.label1);
         this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
         this.Name = "formShowScene";
         this.Text = "Show Scene";
         this.Load += new System.EventHandler(this.formShowScene_Load);
         this.groupBox1.ResumeLayout(false);
         this.groupBox1.PerformLayout();
         this.groupBox2.ResumeLayout(false);
         this.groupBox2.PerformLayout();
         ((System.ComponentModel.ISupportInitialize)(this.numR)).EndInit();
         ((System.ComponentModel.ISupportInitialize)(this.numB)).EndInit();
         ((System.ComponentModel.ISupportInitialize)(this.numG)).EndInit();
         ((System.ComponentModel.ISupportInitialize)(this.numFade)).EndInit();
         ((System.ComponentModel.ISupportInitialize)(this.numTime)).EndInit();
         this.ResumeLayout(false);
         this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox sceneName;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
        private System.Windows.Forms.RadioButton radInput;
        private System.Windows.Forms.RadioButton radTimer;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.NumericUpDown numFade;
        private System.Windows.Forms.NumericUpDown numB;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.NumericUpDown numG;
        private System.Windows.Forms.NumericUpDown numR;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.CheckBox chkWait;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.NumericUpDown numTime;
    }
}