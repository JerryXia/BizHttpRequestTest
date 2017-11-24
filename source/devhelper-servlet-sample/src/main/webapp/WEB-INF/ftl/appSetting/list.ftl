<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
</head>
<body>

<table border="1">
    <thead>
        <tr>
            <th>key</th>
            <th>value</th>
            <th>cnMark</th>
        </tr>
    </thead>
    <tbody>
        <#list appSettings as appSetting>
            <tr>
                <td>${appSetting.key}</td>
                <td>${appSetting.value}</td>
                <td>${appSetting.cnMark}</td>
            </tr>
        </#list>
    </tbody>
</table>


</body>
</html>