
<table border="1">
    <thead>
        <tr>
            <th>key</th>
            <th>value</th>
            <th>cnMark</th>
        </tr>
    </thead>
    <tbody>
        <#if pageInfo?exists && (pageInfo.list?size > 0)>
            <#list pageInfo.list as item>
                <tr>
                    <td>${item.key}</td>
                    <td>${item.value}</td>
                    <td>${item.cnMark}</td>
                </tr>
            </#list>
        </#if>
    </tbody>
</table>