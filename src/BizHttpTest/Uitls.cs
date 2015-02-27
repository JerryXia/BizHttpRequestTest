using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BizHttpTest
{
    public static class Uitls
    {
        public static Dictionary<TKey, TValue> ToDictionary<TKey, TValue>(this List<KeyValuePair<TKey, TValue>> kvPairs)
        {
            var dict = new Dictionary<TKey, TValue>();
            foreach (var item in kvPairs)
            {
                if (dict.ContainsKey(item.Key) == false)
                {
                    dict.Add(item.Key, item.Value);
                }
            }
            return dict;
        }

        public static Dictionary<TKey, TValue1> ToDictionary<TKey, TValue, TValue1>(this List<KeyValuePair<TKey, TValue>> kvPairs)
        {
            var dict1 = new Dictionary<TKey, TValue1>();
            foreach (var item in kvPairs)
            {
                if (dict1.ContainsKey(item.Key) == false)
                {
                    dict1.Add(item.Key, (TValue1)Convert.ChangeType(item.Value, typeof(TValue)));
                }
            }
            return dict1;
        }


    }
}
