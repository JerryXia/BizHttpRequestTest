<html>
<head>
<title>${title}</title>
</head>
<body>
<h1>${title}</h1>
<p>first request: ${ requestFirst }</p>
<p>appName: ${ appName }</p>
<p>now time: <span id="now"></span></p>
<p><a href="/requestcapture/" target="_blank">requestcapture</a></p>
<iframe src="/requestcapture/" style="border:0px;width:100%;height:500px;"></iframe>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
var now = function(){
    $.post('/now.json', {}, function(res){
        $('#now').html(res.t);
    });
};
setTimeout(now, 1234);
</script>
</body>
</html>