using System;
using System.Collections.Generic;

namespace BizHttpTest.Controls
{
    public class TipListBox : System.Windows.Forms.ListBox
    {
        System.Windows.Forms.ToolTip tip;

        public TipListBox()
        {
            tip = new System.Windows.Forms.ToolTip();
        }

        private void SetTipMessage(string strTip)
        {
            tip.SetToolTip(this, strTip);
        }

        /// <summary>
        /// 重写鼠标点击事件
        /// </summary>
        /// <param name="e"></param>
        protected override void OnMouseClick(System.Windows.Forms.MouseEventArgs e)
        {
            base.OnMouseClick(e);
            int idx = IndexFromPoint(e.Location);//获取鼠标所在的项索引
            if (idx == -1)//鼠标所在位置没有 项
            {
                SetTipMessage("");//设置提示信息为空
                return;
            }
            string txt = GetItemText(this.Items[idx]);//获取项文本
            SetTipMessage(txt);//设置提示信息
        }
    }
}
