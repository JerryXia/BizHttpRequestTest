using System.Net.Http;

namespace RestHttpClient
{
    public class UriRest
    {
        public static HttpRequest Get(string url)
        {
            return new HttpRequest(HttpMethod.Get, url);
        }

        public static HttpRequest Post(string url)
        {
            return new HttpRequest(HttpMethod.Post, url);
        }

        public static HttpRequest Delete(string url)
        {
            return new HttpRequest(HttpMethod.Delete, url);
        }

        public static HttpRequest Patch(string url)
        {
            return new HttpRequest(new HttpMethod("PATCH"), url);
        }

        public static HttpRequest Put(string url)
        {
            return new HttpRequest(HttpMethod.Put, url);
        }
    }
}
