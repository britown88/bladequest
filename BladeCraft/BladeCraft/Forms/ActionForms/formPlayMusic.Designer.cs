namespace BladeCraft.Forms.ActionForms
{
    partial class formPlayMusic
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
         this.songName = new System.Windows.Forms.TextBox();
         this.playintro = new System.Windows.Forms.CheckBox();
         this.button1 = new System.Windows.Forms.Button();
         this.button2 = new System.Windows.Forms.Button();
         this.chkLoop = new System.Windows.Forms.CheckBox();
         this.numFade = new System.Windows.Forms.NumericUpDown();
         this.label2 = new System.Windows.Forms.Label();
         this.chkPause = new System.Windows.Forms.CheckBox();
         ((System.ComponentModel.ISupportInitialize)(this.numFade)).BeginInit();
         this.SuspendLayout();
         // 
         // label1
         // 
         this.label1.AutoSize = true;
         this.label1.Location = new System.Drawing.Point(13, 13);
         this.label1.Name = "label1";
         this.label1.Size = new System.Drawing.Size(63, 13);
         this.label1.TabIndex = 0;
         this.label1.Text = "Song Name";
         // 
         // songName
         // 
         this.songName.Location = new System.Drawing.Point(13, 30);
         this.songName.Name = "songName";
         this.songName.Size = new System.Drawing.Size(171, 20);
         this.songName.TabIndex = 1;
         // 
         // playintro
         // 
         this.playintro.AutoSize = true;
         this.playintro.Location = new System.Drawing.Point(13, 80);
         this.playintro.Name = "playintro";
         this.playintro.Size = new System.Drawing.Size(76, 17);
         this.playintro.TabIndex = 2;
         this.playintro.Text = "Play Intro?";
         this.playintro.UseVisualStyleBackColor = true;
         // 
         // button1
         // 
         this.button1.Location = new System.Drawing.Point(12, 126);
         this.button1.Name = "button1";
         this.button1.Size = new System.Drawing.Size(75, 23);
         this.button1.TabIndex = 3;
         this.button1.Text = "OK";
         this.button1.UseVisualStyleBackColor = true;
         this.button1.Click += new System.EventHandler(this.button1_Click);
         // 
         // button2
         // 
         this.button2.Location = new System.Drawing.Point(101, 126);
         this.button2.Name = "button2";
         this.button2.Size = new System.Drawing.Size(75, 23);
         this.button2.TabIndex = 4;
         this.button2.Text = "Cancel";
         this.button2.UseVisualStyleBackColor = true;
         this.button2.Click += new System.EventHandler(this.button2_Click);
         // 
         // chkLoop
         // 
         this.chkLoop.AutoSize = true;
         this.chkLoop.Location = new System.Drawing.Point(13, 57);
         this.chkLoop.Name = "chkLoop";
         this.chkLoop.Size = new System.Drawing.Size(56, 17);
         this.chkLoop.TabIndex = 5;
         this.chkLoop.Text = "Loop?";
         this.chkLoop.UseVisualStyleBackColor = true;
         // 
         // numFade
         // 
         this.numFade.DecimalPlaces = 2;
         this.numFade.Location = new System.Drawing.Point(75, 56);
         this.numFade.Name = "numFade";
         this.numFade.Size = new System.Drawing.Size(46, 20);
         this.numFade.TabIndex = 6;
         // 
         // label2
         // 
         this.label2.AutoSize = true;
         this.label2.Location = new System.Drawing.Point(127, 58);
         this.label2.Name = "label2";
         this.label2.Size = new System.Drawing.Size(57, 13);
         this.label2.TabIndex = 7;
         this.label2.Text = "Fade Time";
         // 
         // chkPause
         // 
         this.chkPause.AutoSize = true;
         this.chkPause.Location = new System.Drawing.Point(13, 103);
         this.chkPause.Name = "chkPause";
         this.chkPause.Size = new System.Drawing.Size(56, 17);
         this.chkPause.TabIndex = 8;
         this.chkPause.Text = "Pause";
         this.chkPause.UseVisualStyleBackColor = true;
         this.chkPause.CheckedChanged += new System.EventHandler(this.chkPause_CheckedChanged);
         // 
         // formPlayMusic
         // 
         this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
         this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
         this.ClientSize = new System.Drawing.Size(188, 158);
         this.ControlBox = false;
         this.Controls.Add(this.chkPause);
         this.Controls.Add(this.label2);
         this.Controls.Add(this.numFade);
         this.Controls.Add(this.chkLoop);
         this.Controls.Add(this.button2);
         this.Controls.Add(this.button1);
         this.Controls.Add(this.playintro);
         this.Controls.Add(this.songName);
         this.Controls.Add(this.label1);
         this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
         this.Name = "formPlayMusic";
         this.Text = "Play Music";
         this.Load += new System.EventHandler(this.formPlayMusic_Load);
         ((System.ComponentModel.ISupportInitialize)(this.numFade)).EndInit();
         this.ResumeLayout(false);
         this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox songName;
        private System.Windows.Forms.CheckBox playintro;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
        private System.Windows.Forms.CheckBox chkLoop;
        private System.Windows.Forms.NumericUpDown numFade;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.CheckBox chkPause;
    }
}