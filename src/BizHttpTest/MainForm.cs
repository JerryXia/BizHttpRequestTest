using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace BizHttpTest
{
    using RestHttpClient;

    public partial class MainForm : Form
    {
        //创建一个委托
        public delegate void RunRequest(HttpRequest httpRequest);
        //定义一个委托变量
        public RunRequest exexRequestBackup;


        private List<string> HttpMethods = new List<string>()
        {
            "GET", "POST"
        };
        private List<KeyValuePair<string, string>> RequestParams = new List<KeyValuePair<string, string>>();
        private List<KeyValuePair<string, string>> DefaultHeaders = new List<KeyValuePair<string, string>>();



        public MainForm()
        {
            InitializeComponent();

            InitVars();
        }

        private void InitVars()
        {
            exexRequestBackup = RunHttpRequestBackup;
        }


        private void MainForm_Load(object sender, EventArgs e)
        {
            //listBox_history.BeginUpdate();
            //listBox_history.Items.Clear();
            //listBox_history.Items.Add("123123");
            //listBox_history.EndUpdate();


            //listBox_collection.BeginUpdate();
            //listBox_collection.Items.Clear();
            //listBox_collection.Items.Add("123123");
            //listBox_collection.EndUpdate();

            /*
            for (int i = 0; i < 20; i ++ )
            {
                var test = new BizHttpTest.Controls.PostParamsControl(this.panel_postdata);
                test.Name = i.ToString();
                test.Location = new System.Drawing.Point(10, i * 40 );
                this.panel_postdata.Controls.Add(test);
            }
            */

            InitControlsData();
            InitControls();
        }


        private void InitControlsData()
        {
            DefaultHeaders.Add(new KeyValuePair<string, string>("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"));
            DefaultHeaders.Add(new KeyValuePair<string, string>("Accept-Encoding", "gzip, deflate, sdch"));
            DefaultHeaders.Add(new KeyValuePair<string, string>("Accept-Language", "zh-CN,zh;q=0.8"));
            DefaultHeaders.Add(new KeyValuePair<string, string>("Connection", "keep-alive"));
            DefaultHeaders.Add(new KeyValuePair<string, string>("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36"));
        }

        private void InitControls()
        {
            combox_httpMethod.Items.Clear();
            combox_httpMethod.Items.AddRange(HttpMethods.ToArray());
            combox_httpMethod.SelectedIndex = 0;

            listGetParams.Visible = chk_params.Checked;
            listHttpHeaders.Visible = chk_headers.Checked;
            if (chk_params.Checked == false && chk_headers.Checked == true)
            {
                listHttpHeaders.Location = new Point(listGetParams.Location.X, listGetParams.Location.Y);
            }
            if (chk_params.Checked == true && chk_headers.Checked == true)
            {
                listHttpHeaders.Location = new Point(listHttpHeaders.Location.X, listGetParams.Location.Y + listGetParams.Height + 20);
            }

            listGetParams.Items.Clear();
            foreach (var item in RequestParams)
            {
                listGetParams.Items.Add(new ListViewItem(new string[] { item.Key, item.Value }));
            }

            listHttpHeaders.Items.Clear();
            foreach (var item in DefaultHeaders)
            {
                listHttpHeaders.Items.Add(new ListViewItem(new string[] { item.Key, item.Value }));
            }
        }


        private void listBox_history_SelectedIndexChanged(object sender, EventArgs e)
        {
            int selectIndex = listBox_history.SelectedIndex;
        }

        private void listBox_collection_SelectedIndexChanged(object sender, EventArgs e)
        {
            int selectIndex = listBox_collection.SelectedIndex;
        }

        private void chk_params_CheckedChanged(object sender, EventArgs e)
        {
            InitControls();
        }
        private void chk_headers_CheckedChanged(object sender, EventArgs e)
        {
            InitControls();
        }

        private void btn_ParamsAdd_Click(object sender, EventArgs e)
        {
            string key = txt_ParamsKey.Text.Trim();
            string value = txt_ParamsValue.Text.Trim();
            if (string.IsNullOrEmpty(key))
            {
                return;
            }

            this.txt_ParamsKey.Text = string.Empty;
            this.txt_ParamsValue.Text = string.Empty;

            RequestParams.Add(new KeyValuePair<string, string>(key, value));
            InitControls();
        }
        private void btn_ParamsClear_Click(object sender, EventArgs e)
        {
            RequestParams.Clear();
            InitControls();
        }

        private void btn_HeadersAdd_Click(object sender, EventArgs e)
        {
            string key = txt_HeaderKey.Text.Trim();
            string value = txt_HeadersValue.Text.Trim();
            if (string.IsNullOrEmpty(key))
            {
                return;
            }

            this.txt_HeaderKey.Text = string.Empty;
            this.txt_HeadersValue.Text = string.Empty;

            DefaultHeaders.Add(new KeyValuePair<string, string>(key, value));
            InitControls();
        }
        private void btn_HeadersClear_Click(object sender, EventArgs e)
        {
            DefaultHeaders.Clear();
            InitControls();
        }

        private void listGetParams_ContextMenu_Select_Click(object sender, EventArgs e)
        {
            if (this.listGetParams.SelectedIndices.Count == 0)//无选中信息
                return;

            int n = listGetParams.SelectedItems[0].Index;//获取当前listView选取的行
            var selectItem = RequestParams[n];
            txt_ParamsKey.Text = selectItem.Key;
            txt_ParamsValue.Text = selectItem.Value;
        }
        private void listGetParams_ContextMenu_Delete_Click(object sender, EventArgs e)
        {
            if (this.listGetParams.SelectedIndices.Count == 0)//无选中信息
                return;

            int n = listGetParams.SelectedItems[0].Index;//获取当前listView选取的行
            RequestParams.RemoveAt(n);

            InitControls();
        }

        private void listHttpHeaders_ContextMenu_Select_Click(object sender, EventArgs e)
        {
            if (this.listHttpHeaders.SelectedIndices.Count == 0)//无选中信息
                return;

            int n = listHttpHeaders.SelectedItems[0].Index;//获取当前listView选取的行
            var selectItem = DefaultHeaders[n];
            txt_HeaderKey.Text = selectItem.Key;
            txt_HeadersValue.Text = selectItem.Value;
        }
        private void listHttpHeaders_ContextMenu_Delete_Click(object sender, EventArgs e)
        {
            if (this.listHttpHeaders.SelectedIndices.Count == 0)//无选中信息
                return;

            int n = listHttpHeaders.SelectedItems[0].Index;//获取当前listView选取的行
            DefaultHeaders.RemoveAt(n);

            InitControls();
        }


        private void btn_send_Click(object sender, EventArgs e)
        {
            string url = this.txt_RequestUrl.Text.Trim();
            if(!Uri.IsWellFormedUriString(url, UriKind.RelativeOrAbsolute))
            {
                this.lbl_resultTips.Text = "Url格式不正确";
            }
            string httpMethod = HttpMethods[this.combox_httpMethod.SelectedIndex];

            var task = new Task(() =>
            {
                var httpRequest = BuildRquest(url, httpMethod)
                    .Headers(DefaultHeaders.ToDictionary())
                    .Fields(RequestParams.ToDictionary<string, string, object>());
                this.BeginInvoke(exexRequestBackup, httpRequest);
            });
            task.Start();
        }

        HttpRequest BuildRquest(string url, string method)
        {
            switch (method)
            {
                case "GET":
                    return UriRest.Get(url);
                case "POST":
                    return UriRest.Get(url);
                default:
                    throw new ArgumentOutOfRangeException("Method参数异常");
            }
        }


        private void RunHttpRequestBackup(HttpRequest httpRequest)
        {
            var httpResponse = httpRequest.AsString();

            var sb = new System.Text.StringBuilder();
            sb.AppendFormat("{0}{1}", httpResponse.Code, System.Environment.NewLine);
            sb.AppendFormat("{0}{1}", httpResponse.Headers, System.Environment.NewLine);
            sb.AppendFormat("{0}{1}", httpResponse.Body, System.Environment.NewLine);

            this.txt_response.Text = sb.ToString();
        }


        private void btn_addtocoll_Click(object sender, EventArgs e)
        {

        }

    }
}
