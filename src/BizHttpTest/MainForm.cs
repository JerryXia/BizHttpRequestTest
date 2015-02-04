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
    public partial class MainForm : Form
    {
        public MainForm()
        {
            InitializeComponent();
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

            for (int i = 0; i < 20; i ++ )
            {
                var test = new BizHttpTest.Controls.PostParamsControl(this.panel_postdata);
                test.Name = i.ToString();
                test.Location = new System.Drawing.Point(10, i * 40 );
                this.panel_postdata.Controls.Add(test);
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


        

    }
}
