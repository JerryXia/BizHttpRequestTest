namespace BizHttpTest
{
    partial class MainForm
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.tpControl = new System.Windows.Forms.TabControl();
            this.tp1 = new System.Windows.Forms.TabPage();
            this.tp2 = new System.Windows.Forms.TabPage();
            this.groupBox_normal = new System.Windows.Forms.GroupBox();
            this.btn_addtocoll = new System.Windows.Forms.Button();
            this.btn_preview = new System.Windows.Forms.Button();
            this.btn_send = new System.Windows.Forms.Button();
            this.panel_postdata = new System.Windows.Forms.Panel();
            this.chk_headers = new System.Windows.Forms.CheckBox();
            this.chk_params = new System.Windows.Forms.CheckBox();
            this.comboBox1 = new System.Windows.Forms.ComboBox();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.listBox_history = new BizHttpTest.Controls.TipListBox();
            this.listBox_collection = new BizHttpTest.Controls.TipListBox();
            this.tpControl.SuspendLayout();
            this.tp1.SuspendLayout();
            this.tp2.SuspendLayout();
            this.groupBox_normal.SuspendLayout();
            this.SuspendLayout();
            // 
            // tpControl
            // 
            this.tpControl.Controls.Add(this.tp1);
            this.tpControl.Controls.Add(this.tp2);
            this.tpControl.Location = new System.Drawing.Point(12, 9);
            this.tpControl.Name = "tpControl";
            this.tpControl.SelectedIndex = 0;
            this.tpControl.Size = new System.Drawing.Size(200, 600);
            this.tpControl.TabIndex = 0;
            // 
            // tp1
            // 
            this.tp1.Controls.Add(this.listBox_history);
            this.tp1.Location = new System.Drawing.Point(4, 22);
            this.tp1.Name = "tp1";
            this.tp1.Padding = new System.Windows.Forms.Padding(3);
            this.tp1.Size = new System.Drawing.Size(192, 574);
            this.tp1.TabIndex = 1;
            this.tp1.Text = "历史";
            this.tp1.UseVisualStyleBackColor = true;
            // 
            // tp2
            // 
            this.tp2.Controls.Add(this.listBox_collection);
            this.tp2.Location = new System.Drawing.Point(4, 22);
            this.tp2.Name = "tp2";
            this.tp2.Padding = new System.Windows.Forms.Padding(3);
            this.tp2.Size = new System.Drawing.Size(192, 574);
            this.tp2.TabIndex = 2;
            this.tp2.Text = "收藏";
            this.tp2.UseVisualStyleBackColor = true;
            // 
            // groupBox_normal
            // 
            this.groupBox_normal.Controls.Add(this.btn_addtocoll);
            this.groupBox_normal.Controls.Add(this.btn_preview);
            this.groupBox_normal.Controls.Add(this.btn_send);
            this.groupBox_normal.Controls.Add(this.panel_postdata);
            this.groupBox_normal.Controls.Add(this.chk_headers);
            this.groupBox_normal.Controls.Add(this.chk_params);
            this.groupBox_normal.Controls.Add(this.comboBox1);
            this.groupBox_normal.Controls.Add(this.textBox1);
            this.groupBox_normal.Location = new System.Drawing.Point(240, 31);
            this.groupBox_normal.Name = "groupBox_normal";
            this.groupBox_normal.Size = new System.Drawing.Size(941, 578);
            this.groupBox_normal.TabIndex = 1;
            this.groupBox_normal.TabStop = false;
            this.groupBox_normal.Text = "Normal";
            // 
            // btn_addtocoll
            // 
            this.btn_addtocoll.Location = new System.Drawing.Point(250, 411);
            this.btn_addtocoll.Name = "btn_addtocoll";
            this.btn_addtocoll.Size = new System.Drawing.Size(132, 23);
            this.btn_addtocoll.TabIndex = 7;
            this.btn_addtocoll.Text = "Add to collection";
            this.btn_addtocoll.UseVisualStyleBackColor = true;
            // 
            // btn_preview
            // 
            this.btn_preview.Location = new System.Drawing.Point(143, 411);
            this.btn_preview.Name = "btn_preview";
            this.btn_preview.Size = new System.Drawing.Size(75, 23);
            this.btn_preview.TabIndex = 6;
            this.btn_preview.Text = "Preview";
            this.btn_preview.UseVisualStyleBackColor = true;
            // 
            // btn_send
            // 
            this.btn_send.Location = new System.Drawing.Point(34, 411);
            this.btn_send.Name = "btn_send";
            this.btn_send.Size = new System.Drawing.Size(75, 23);
            this.btn_send.TabIndex = 5;
            this.btn_send.Text = "Send";
            this.btn_send.UseVisualStyleBackColor = true;
            // 
            // panel_postdata
            // 
            this.panel_postdata.AutoScroll = true;
            this.panel_postdata.Location = new System.Drawing.Point(16, 70);
            this.panel_postdata.Name = "panel_postdata";
            this.panel_postdata.Size = new System.Drawing.Size(809, 324);
            this.panel_postdata.TabIndex = 4;
            // 
            // chk_headers
            // 
            this.chk_headers.AutoSize = true;
            this.chk_headers.Location = new System.Drawing.Point(662, 25);
            this.chk_headers.Name = "chk_headers";
            this.chk_headers.Size = new System.Drawing.Size(66, 16);
            this.chk_headers.TabIndex = 3;
            this.chk_headers.Text = "Headers";
            this.chk_headers.UseVisualStyleBackColor = true;
            // 
            // chk_params
            // 
            this.chk_params.AutoSize = true;
            this.chk_params.Location = new System.Drawing.Point(558, 23);
            this.chk_params.Name = "chk_params";
            this.chk_params.Size = new System.Drawing.Size(84, 16);
            this.chk_params.TabIndex = 2;
            this.chk_params.Text = "URL params";
            this.chk_params.UseVisualStyleBackColor = true;
            // 
            // comboBox1
            // 
            this.comboBox1.FormattingEnabled = true;
            this.comboBox1.Location = new System.Drawing.Point(458, 21);
            this.comboBox1.Name = "comboBox1";
            this.comboBox1.Size = new System.Drawing.Size(82, 20);
            this.comboBox1.TabIndex = 1;
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(16, 20);
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(436, 21);
            this.textBox1.TabIndex = 0;
            // 
            // listBox_history
            // 
            this.listBox_history.FormattingEnabled = true;
            this.listBox_history.ItemHeight = 12;
            this.listBox_history.Location = new System.Drawing.Point(16, 6);
            this.listBox_history.Name = "listBox_history";
            this.listBox_history.Size = new System.Drawing.Size(154, 556);
            this.listBox_history.TabIndex = 0;
            this.listBox_history.SelectedIndexChanged += new System.EventHandler(this.listBox_history_SelectedIndexChanged);
            // 
            // listBox_collection
            // 
            this.listBox_collection.FormattingEnabled = true;
            this.listBox_collection.ItemHeight = 12;
            this.listBox_collection.Location = new System.Drawing.Point(15, 6);
            this.listBox_collection.Name = "listBox_collection";
            this.listBox_collection.Size = new System.Drawing.Size(154, 544);
            this.listBox_collection.TabIndex = 1;
            this.listBox_collection.SelectedIndexChanged += new System.EventHandler(this.listBox_collection_SelectedIndexChanged);
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1193, 621);
            this.Controls.Add(this.groupBox_normal);
            this.Controls.Add(this.tpControl);
            this.Name = "MainForm";
            this.Text = "Ac业务功能测试";
            this.Load += new System.EventHandler(this.MainForm_Load);
            this.tpControl.ResumeLayout(false);
            this.tp1.ResumeLayout(false);
            this.tp2.ResumeLayout(false);
            this.groupBox_normal.ResumeLayout(false);
            this.groupBox_normal.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TabControl tpControl;
        private System.Windows.Forms.TabPage tp1;
        private System.Windows.Forms.TabPage tp2;
        private BizHttpTest.Controls.TipListBox listBox_history;
        private BizHttpTest.Controls.TipListBox listBox_collection;
        private System.Windows.Forms.GroupBox groupBox_normal;
        private System.Windows.Forms.ComboBox comboBox1;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.CheckBox chk_params;
        private System.Windows.Forms.CheckBox chk_headers;
        private System.Windows.Forms.Panel panel_postdata;
        private System.Windows.Forms.Button btn_addtocoll;
        private System.Windows.Forms.Button btn_preview;
        private System.Windows.Forms.Button btn_send;
    }
}

