<table>
    <form action="/admin/addApi" method="POST">
        <tr>
            <td class="left top">
                ${sofia().javadocApiName()}: <input type="text" name="name"/>
            </td>
            <td class="top">
                ${sofia().javadocUrl()}: <input type="text" name="baseUrl" width="30"/>
                <br/>${sofia().javadocDownloadUrl()}: <input type="text" name="downloadUrl" size="50"/>
            </td>
            <td class="right top">
                <input type="image" src="/assets/images/boomy/add24.png" alt="Add"/>
            </td>
        </tr>
    </form>
    <tr>
        <th>${sofia().javadocApiName()}</th>
        <th>${sofia().javadocUrl()}</th>
        <th class="right">&nbsp;</th>
    </tr>
    <#list apis() as api>
        <tr>
            <td>${api.name}</td>
            <td>${api.baseUrl}</td>
            <td class="right">
                <a href="/admin/reloadApi/${api.id}">
                    <img src="/assets/images/boomy/refresh24.png" alt="Refresh"/>
                </a>
                <a href=""/admin/deleteApi/${api.id}"">
                    <img src="/assets/images/boomy/delete24.png" alt="Delete"/>
                </a>
            </td>
        </tr>
    </#list>

</table>
