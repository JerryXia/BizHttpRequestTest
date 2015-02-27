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
            this.listBox_history = new BizHttpTest.Controls.TipListBox();
            this.tp2 = new System.Windows.Forms.TabPage();
            this.listBox_collection = new BizHttpTest.Controls.TipListBox();
            this.groupBox_normal = new System.Windows.Forms.GroupBox();
            this.txt_response = new System.Windows.Forms.TextBox();
            this.btn_HeadersClear = new System.Windows.Forms.Button();
            this.btn_HeadersAdd = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.txt_HeadersValue = new System.Windows.Forms.TextBox();
            this.txt_HeaderKey = new System.Windows.Forms.TextBox();
            this.listGetParams = new System.Windows.Forms.ListView();
            this.columnHeader_Key = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.columnHeader_Value = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.listHttpHeaders = new System.Windows.Forms.ListView();
            this.columnHeader_Key1 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.columnHeader_Value1 = ((System.Windows.Forms.ColumnHeader)(new System.Windows.Forms.ColumnHeader()));
            this.btn_ParamsClear = new System.Windows.Forms.Button();
            this.btn_ParamsAdd = new System.Windows.Forms.Button();
            this.lbl_ParamsValue = new System.Windows.Forms.Label();
            this.lbl_ParamsKey = new System.Windows.Forms.Label();
            this.txt_ParamsValue = new System.Windows.Forms.TextBox();
            this.txt_ParamsKey = new System.Windows.Forms.TextBox();
            this.btn_addtocoll = new System.Windows.Forms.Button();
            this.btn_send = new System.Windows.Forms.Button();
            this.chk_headers = new System.Windows.Forms.CheckBox();
            this.chk_params = new System.Windows.Forms.CheckBox();
            this.combox_httpMethod = new System.Windows.Forms.ComboBox();
            this.txt_RequestUrl = new System.Windows.Forms.TextBox();
            this.lbl_resultTips = new System.Windows.Forms.Label();
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
            // groupBox_normal
            // 
            this.groupBox_normal.Controls.Add(this.lbl_resultTips);
            this.groupBox_normal.Controls.Add(this.txt_response);
            this.groupBox_normal.Controls.Add(this.btn_HeadersClear);
            this.groupBox_normal.Controls.Add(this.btn_HeadersAdd);
            this.groupBox_normal.Controls.Add(this.label1);
            this.groupBox_normal.Controls.Add(this.label2);
            this.groupBox_normal.Controls.Add(this.txt_HeadersValue);
            this.groupBox_normal.Controls.Add(this.txt_HeaderKey);
            this.groupBox_normal.Controls.Add(this.listGetParams);
            this.groupBox_normal.Controls.Add(this.listHttpHeaders);
            this.groupBox_normal.Controls.Add(this.btn_ParamsClear);
            this.groupBox_normal.Controls.Add(this.btn_ParamsAdd);
            this.groupBox_normal.Controls.Add(this.lbl_ParamsValue);
            this.groupBox_normal.Controls.Add(this.lbl_ParamsKey);
            this.groupBox_normal.Controls.Add(this.txt_ParamsValue);
            this.groupBox_normal.Controls.Add(this.txt_ParamsKey);
            this.groupBox_normal.Controls.Add(this.btn_addtocoll);
            this.groupBox_normal.Controls.Add(this.btn_send);
            this.groupBox_normal.Controls.Add(this.chk_headers);
            this.groupBox_normal.Controls.Add(this.chk_params);
            this.groupBox_normal.Controls.Add(this.combox_httpMethod);
            this.groupBox_normal.Controls.Add(this.txt_RequestUrl);
            this.groupBox_normal.Location = new System.Drawing.Point(240, 31);
            this.groupBox_normal.Name = "groupBox_normal";
            this.groupBox_normal.Size = new System.Drawing.Size(941, 578);
            this.groupBox_normal.TabIndex = 1;
            this.groupBox_normal.TabStop = false;
            this.groupBox_normal.Text = "主体";
            // 
            // txt_response
            // 
            this.txt_response.Location = new System.Drawing.Point(16, 399);
            this.txt_response.Multiline = true;
            this.txt_response.Name = "txt_response";
            this.txt_response.Size = new System.Drawing.Size(820, 173);
            this.txt_response.TabIndex = 21;
            // 
            // btn_HeadersClear
            // 
            this.btn_HeadersClear.Location = new System.Drawing.Point(585, 70);
            this.btn_HeadersClear.Name = "btn_HeadersClear";
            this.btn_HeadersClear.Size = new System.Drawing.Size(44, 23);
            this.btn_HeadersClear.TabIndex = 20;
            this.btn_HeadersClear.Text = "Clear";
            this.btn_HeadersClear.UseVisualStyleBackColor = true;
            this.btn_HeadersClear.Click += new System.EventHandler(this.btn_HeadersClear_Click);
            // 
            // btn_HeadersAdd
            // 
            this.btn_HeadersAdd.Location = new System.Drawing.Point(542, 70);
            this.btn_HeadersAdd.Name = "btn_HeadersAdd";
            this.btn_HeadersAdd.Size = new System.Drawing.Size(40, 23);
            this.btn_HeadersAdd.TabIndex = 19;
            this.btn_HeadersAdd.Text = "Add";
            this.btn_HeadersAdd.UseVisualStyleBackColor = true;
            this.btn_HeadersAdd.Click += new System.EventHandler(this.btn_HeadersAdd_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(286, 75);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(53, 12);
            this.label1.TabIndex = 18;
            this.label1.Text = "参数值：";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(106, 75);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(53, 12);
            this.label2.TabIndex = 17;
            this.label2.Text = "参数名：";
            // 
            // txt_HeadersValue
            // 
            this.txt_HeadersValue.Location = new System.Drawing.Point(340, 72);
            this.txt_HeadersValue.Name = "txt_HeadersValue";
            this.txt_HeadersValue.Size = new System.Drawing.Size(195, 21);
            this.txt_HeadersValue.TabIndex = 16;
            // 
            // txt_HeaderKey
            // 
            this.txt_HeaderKey.Location = new System.Drawing.Point(160, 72);
            this.txt_HeaderKey.Name = "txt_HeaderKey";
            this.txt_HeaderKey.Size = new System.Drawing.Size(120, 21);
            this.txt_HeaderKey.TabIndex = 15;
            // 
            // listGetParams
            // 
            this.listGetParams.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader_Key,
            this.columnHeader_Value});
            this.listGetParams.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.listGetParams.Location = new System.Drawing.Point(16, 100);
            this.listGetParams.Name = "listGetParams";
            this.listGetParams.Size = new System.Drawing.Size(820, 120);
            this.listGetParams.TabIndex = 4;
            this.listGetParams.UseCompatibleStateImageBehavior = false;
            this.listGetParams.View = System.Windows.Forms.View.Details;
            // 
            // columnHeader_Key
            // 
            this.columnHeader_Key.Text = "键";
            this.columnHeader_Key.Width = 212;
            // 
            // columnHeader_Value
            // 
            this.columnHeader_Value.Text = "值";
            this.columnHeader_Value.Width = 520;
            // 
            // listHttpHeaders
            // 
            this.listHttpHeaders.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnHeader_Key1,
            this.columnHeader_Value1});
            this.listHttpHeaders.Font = new System.Drawing.Font("宋体", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(134)));
            this.listHttpHeaders.Location = new System.Drawing.Point(16, 234);
            this.listHttpHeaders.Name = "listHttpHeaders";
            this.listHttpHeaders.Size = new System.Drawing.Size(820, 120);
            this.listHttpHeaders.TabIndex = 14;
            this.listHttpHeaders.UseCompatibleStateImageBehavior = false;
            this.listHttpHeaders.View = System.Windows.Forms.View.Details;
            // 
            // columnHeader_Key1
            // 
            this.columnHeader_Key1.Text = "Header参数名";
            this.columnHeader_Key1.Width = 150;
            // 
            // columnHeader_Value1
            // 
            this.columnHeader_Value1.Text = "Header参数值";
            this.columnHeader_Value1.Width = 650;
            // 
            // btn_ParamsClear
            // 
            this.btn_ParamsClear.Location = new System.Drawing.Point(585, 43);
            this.btn_ParamsClear.Name = "btn_ParamsClear";
            this.btn_ParamsClear.Size = new System.Drawing.Size(44, 23);
            this.btn_ParamsClear.TabIndex = 13;
            this.btn_ParamsClear.Text = "Clear";
            this.btn_ParamsClear.UseVisualStyleBackColor = true;
            this.btn_ParamsClear.Click += new System.EventHandler(this.btn_ParamsClear_Click);
            // 
            // btn_ParamsAdd
            // 
            this.btn_ParamsAdd.Location = new System.Drawing.Point(542, 43);
            this.btn_ParamsAdd.Name = "btn_ParamsAdd";
            this.btn_ParamsAdd.Size = new System.Drawing.Size(40, 23);
            this.btn_ParamsAdd.TabIndex = 12;
            this.btn_ParamsAdd.Text = "Add";
            this.btn_ParamsAdd.UseVisualStyleBackColor = true;
            this.btn_ParamsAdd.Click += new System.EventHandler(this.btn_ParamsAdd_Click);
            // 
            // lbl_ParamsValue
            // 
            this.lbl_ParamsValue.AutoSize = true;
            this.lbl_ParamsValue.Location = new System.Drawing.Point(286, 48);
            this.lbl_ParamsValue.Name = "lbl_ParamsValue";
            this.lbl_ParamsValue.Size = new System.Drawing.Size(29, 12);
            this.lbl_ParamsValue.TabIndex = 11;
            this.lbl_ParamsValue.Text = "值：";
            // 
            // lbl_ParamsKey
            // 
            this.lbl_ParamsKey.AutoSize = true;
            this.lbl_ParamsKey.Location = new System.Drawing.Point(106, 48);
            this.lbl_ParamsKey.Name = "lbl_ParamsKey";
            this.lbl_ParamsKey.Size = new System.Drawing.Size(29, 12);
            this.lbl_ParamsKey.TabIndex = 10;
            this.lbl_ParamsKey.Text = "键：";
            // 
            // txt_ParamsValue
            // 
            this.txt_ParamsValue.Location = new System.Drawing.Point(340, 45);
            this.txt_ParamsValue.Name = "txt_ParamsValue";
            this.txt_ParamsValue.Size = new System.Drawing.Size(195, 21);
            this.txt_ParamsValue.TabIndex = 9;
            // 
            // txt_ParamsKey
            // 
            this.txt_ParamsKey.Location = new System.Drawing.Point(160, 45);
            this.txt_ParamsKey.Name = "txt_ParamsKey";
            this.txt_ParamsKey.Size = new System.Drawing.Size(120, 21);
            this.txt_ParamsKey.TabIndex = 8;
            // 
            // btn_addtocoll
            // 
            this.btn_addtocoll.Location = new System.Drawing.Point(99, 370);
            this.btn_addtocoll.Name = "btn_addtocoll";
            this.btn_addtocoll.Size = new System.Drawing.Size(100, 23);
            this.btn_addtocoll.TabIndex = 7;
            this.btn_addtocoll.Text = "添加到收藏";
            this.btn_addtocoll.UseVisualStyleBackColor = true;
            this.btn_addtocoll.Click += new System.EventHandler(this.btn_addtocoll_Click);
            // 
            // btn_send
            // 
            this.btn_send.Location = new System.Drawing.Point(16, 370);
            this.btn_send.Name = "btn_send";
            this.btn_send.Size = new System.Drawing.Size(66, 23);
            this.btn_send.TabIndex = 5;
            this.btn_send.Text = "发送";
            this.btn_send.UseVisualStyleBackColor = true;
            this.btn_send.Click += new System.EventHandler(this.btn_send_Click);
            // 
            // chk_headers
            // 
            this.chk_headers.AutoSize = true;
            this.chk_headers.Checked = true;
            this.chk_headers.CheckState = System.Windows.Forms.CheckState.Checked;
            this.chk_headers.Location = new System.Drawing.Point(16, 74);
            this.chk_headers.Name = "chk_headers";
            this.chk_headers.Size = new System.Drawing.Size(66, 16);
            this.chk_headers.TabIndex = 3;
            this.chk_headers.Text = "Headers";
            this.chk_headers.UseVisualStyleBackColor = true;
            this.chk_headers.CheckedChanged += new System.EventHandler(this.chk_headers_CheckedChanged);
            // 
            // chk_params
            // 
            this.chk_params.AutoSize = true;
            this.chk_params.Checked = true;
            this.chk_params.CheckState = System.Windows.Forms.CheckState.Checked;
            this.chk_params.Location = new System.Drawing.Point(16, 47);
            this.chk_params.Name = "chk_params";
            this.chk_params.Size = new System.Drawing.Size(84, 16);
            this.chk_params.TabIndex = 2;
            this.chk_params.Text = "URL params";
            this.chk_params.UseVisualStyleBackColor = true;
            this.chk_params.CheckedChanged += new System.EventHandler(this.chk_params_CheckedChanged);
            // 
            // combox_httpMethod
            // 
            this.combox_httpMethod.FormattingEnabled = true;
            this.combox_httpMethod.Location = new System.Drawing.Point(458, 21);
            this.combox_httpMethod.Name = "combox_httpMethod";
            this.combox_httpMethod.Size = new System.Drawing.Size(82, 20);
            this.combox_httpMethod.TabIndex = 1;
            // 
            // txt_RequestUrl
            // 
            this.txt_RequestUrl.Location = new System.Drawing.Point(16, 20);
            this.txt_RequestUrl.Name = "txt_RequestUrl";
            this.txt_RequestUrl.Size = new System.Drawing.Size(436, 21);
            this.txt_RequestUrl.TabIndex = 0;
            // 
            // lbl_resultTips
            // 
            this.lbl_resultTips.AutoSize = true;
            this.lbl_resultTips.ForeColor = System.Drawing.Color.Red;
            this.lbl_resultTips.Location = new System.Drawing.Point(215, 375);
            this.lbl_resultTips.Name = "lbl_resultTips";
            this.lbl_resultTips.Size = new System.Drawing.Size(35, 12);
            this.lbl_resultTips.TabIndex = 22;
            this.lbl_resultTips.Text = "Tips:";
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1193, 621);
            this.Controls.Add(this.groupBox_normal);
            this.Controls.Add(this.tpControl);
            this.Name = "MainForm";
            this.Text = "系统Http请求工具";
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
        private System.Windows.Forms.ComboBox combox_httpMethod;
        private System.Windows.Forms.TextBox txt_RequestUrl;
        private System.Windows.Forms.CheckBox chk_params;
        private System.Windows.Forms.CheckBox chk_headers;
        private System.Windows.Forms.Button btn_addtocoll;
        private System.Windows.Forms.Button btn_send;
        private System.Windows.Forms.Button btn_ParamsClear;
        private System.Windows.Forms.Button btn_ParamsAdd;
        private System.Windows.Forms.Label lbl_ParamsValue;
        private System.Windows.Forms.Label lbl_ParamsKey;
        private System.Windows.Forms.TextBox txt_ParamsValue;
        private System.Windows.Forms.TextBox txt_ParamsKey;
        private System.Windows.Forms.ListView listGetParams;
        private System.Windows.Forms.ColumnHeader columnHeader_Key;
        private System.Windows.Forms.ColumnHeader columnHeader_Value;
        private System.Windows.Forms.MenuItem menuItem_Select;
        private System.Windows.Forms.MenuItem menuItem_Delete;
        private System.Windows.Forms.ListView listHttpHeaders;
        private System.Windows.Forms.ColumnHeader columnHeader_Key1;
        private System.Windows.Forms.ColumnHeader columnHeader_Value1;
        private System.Windows.Forms.MenuItem menuItem_Select1;
        private System.Windows.Forms.MenuItem menuItem_Delete1;
        private System.Windows.Forms.Button btn_HeadersClear;
        private System.Windows.Forms.Button btn_HeadersAdd;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.TextBox txt_HeadersValue;
        private System.Windows.Forms.TextBox txt_HeaderKey;
        private System.Windows.Forms.TextBox txt_response;
        private System.Windows.Forms.Label lbl_resultTips;
    }
}

