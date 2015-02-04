using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace BizHttpTest.Controls
{
    public partial class PostParamsControl : UserControl
    {
        private Control _parent;

        public PostParamsControl()
        {
            InitializeComponent();
        }

        public PostParamsControl(Control parent)
        {
            InitializeComponent();
            _parent = parent;
        }

        private void btn_delete_Click(object sender, EventArgs e)
        {
            this.Parent.Controls.Remove(this);
        }
    }
}
