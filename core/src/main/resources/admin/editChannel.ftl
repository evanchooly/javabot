<form action="/admin/saveChannel" method="POST">
    <table class="form">
        <tr>
            <td class="top">Name</td>
            <td class="top right">
                <#if channel.id?? >
                    <input type="hidden" name="id" value="${channel.id}"/>
                </#if>

                <input type="text" name="name" value="<#if channel.name?? >${channel.name}</#if>"
                    <#if channel.id?? > disabled </#if>
                />
                <#--<span class="error">#{error 'channel.name' /}</span>-->
            </td>
        </tr>
        <tr>
            <td>Key (optional)</td>
            <td class="right"><input type="text" name="key" value="<#if channel.key?? >${channel.key}</#if>"></td>
        </tr>
        <tr>
            <td>Logged?</td>
            <td class="right">
                <select name="logged">
                    <option value="true" title="yes" <#if channel.logged>selected</#if>>yes</option>
                    <option value="false" title="no" <#if !channel.logged>selected</#if>}>no</option>
                </select>
            </td>
        </tr>
        <tr>
            <td class="form-submit right" colspan="2">
                <input type="submit" value="Save">
            </td>
        </tr>
    </table>
</form>