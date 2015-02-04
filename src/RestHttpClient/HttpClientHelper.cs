using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;

namespace RestHttpClient
{
    public class HttpClientHelper
    {
        private const string USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36";

        public static HttpResponse<T> Request<T>(HttpRequest request)
        {
            var responseTask = RequestHelper(request);
            Task.WaitAll(responseTask);
            var response = responseTask.Result;

            return new HttpResponse<T>(response);
        }

        public static Task<HttpResponse<T>> RequestAsync<T>(HttpRequest request)
        {
            var responseTask = RequestHelper(request);
            return Task<HttpResponse<T>>.Factory.StartNew(() =>
            {
                Task.WaitAll(responseTask);
                return new HttpResponse<T>(responseTask.Result);
            });
        }

        private static Task<HttpResponseMessage> RequestHelper(HttpRequest request)
        {
            if (!request.Headers.ContainsKey("user-agent"))
            {
                request.Headers.Add("user-agent", USER_AGENT);
            }

            var client = new HttpClient();
            var msg = new HttpRequestMessage(request.HttpMethod, request.URL);

            foreach (var header in request.Headers)
            {
                msg.Headers.Add(header.Key, header.Value);
            }

            if (request.Body.Any())
            {
                msg.Content = request.Body;
            }

            return client.SendAsync(msg);
        }
    }
}
