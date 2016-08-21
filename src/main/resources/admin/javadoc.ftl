<table>
    <form action="/admin/addApi" method="POST">
        <tr>
            <td class="top left bare" colspan="4">
                <table class="bare">
                    <tr>
                        <td class="left">${sofia().javadocApiName()}</td>
                        <td><input type="text" name="name"/></td>
                    </tr>
                    <tr>
                        <td class="left">${sofia().mavenDependency()}</td>
                        <td>
                            <textarea name="dependency" rows="7" cols="50"></textarea>
                        </td>
                    </tr>
                </table>
            </td>
            <td class="right top">
                <input type="image" src="/assets/images/boomy/add24.png" alt="Add"/>
            </td>
        </tr>
    </form>
    <tr>
        <th>${sofia().javadocApiName()}</th>
        <th>${sofia().mavenGroup()}</th>
        <th>${sofia().mavenArtifact()}</th>
        <th>${sofia().mavenVersion()}</th>
        <th class="right">&nbsp;</th>
    </tr>
    <#list apis() as api>
        <tr>
            <td>${api.name}</td>
            <td>${api.groupId}</td>
            <td>${api.artifactId}</td>
            <td>${api.version}</td>
            <td class="right">
                <a href="/admin/reloadApi/${api.id}">
                    <img src="/assets/images/boomy/refresh24.png" alt="Refresh"/>
                </a>
                <a href="/admin/deleteApi/${api.id}">
                    <img src="/assets/images/boomy/delete24.png" alt="Delete"/>
                </a>
            </td>
        </tr>
    </#list>

</table>
