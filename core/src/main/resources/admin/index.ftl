<form action="/admin/add" method="POST">
    <table class="form">
        <tr class="topRow">
            <td><label for="irc">${sofia().ircName()}</label></td>
            <td class="right"><input type="text" id="irc" name="ircName"></td>
        </tr>
        <tr class="topRow">
            <td><label for="host">${sofia().hostName()}</label></td>
            <td class="right"><input type="text" id="host" name="hostName"></td>
        </tr>
        <tr>
            <td><label for="userName">${sofia().email()}</label></td>
            <td class="right"><input type="text" id="userName" name="emailAddress"></td>
        </tr>
        <tr>
            <td colspan="2" class="form-submit right">
                <input type="submit" value="${sofia().submit()}">
            </td>
        </tr>
    </table>
</form>
<table>
    <tr>
        <th class="top">${sofia().ircName()}</th>
        <th class="top">${sofia().hostName()}</th>
        <th class="top">${sofia().email()}</th>
        <th class="top">${sofia().addedBy()}</th>
        <th class="top">${sofia().addedOn()}</th>
        <th class="top right">${sofia().action()}</th>
    </tr>
    <#list admins as admin>
        <tr id="admin${admin_index}">
            <td><#if admin.ircName??>${admin.ircName}</#if></td>
            <td><#if admin.hostName??>${admin.hostName}</#if></td>
            <td><#if admin.emailAddress??>${admin.emailAddress}</#if></td>
            <td><#if admin.addedBy??>${admin.addedBy}</#if></td>
            <td class="right">${format(admin.updated)}</td>
            <td class="right top">
                <#if !admin.botOwner >
                    <a href="/admin/delete/${admin.id}"> <img src="/assets/images/boomy/delete24.png" alt="Delete"/></a>
                </#if>
            </td>
        </tr>
    </#list>
</table>