<table class="mytable">
    <tr>
        <td class="tableWrapper" colspan="3">
            <table class="myTable">
                <tr>
                    <td class="dateNav top">
                        <h3>
                            <a href="/logs/${encode(channel)}/${yesterday}">&laquo; ${yesterday}</a>
                        </h3>
                    </td>
                    <td class="dateNav top today">
                        <h3>${today}</h3>
                    <#if isAdmin()>
                        <a href="/admin/editChannel/${channel}">(edit channel)</a>
                    </#if>
                    </td>
                    <td class="dateNav top right nextNav">
                        <h3>
                            <a href="/logs/${encode(channel)}/${tomorrow}">${tomorrow} &raquo;</a>
                        </h3>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr id="logHeader">
        <th>Nick</th>
        <th>Message</th>
        <th class="right">Date</th>
    </tr>
<#list logs() as log>
    <tr>
        <#switch log.type>
            <#case "JOIN">
            <#case "PART">
                <td class="${log.type?lower_case}" colspan="2">${log.message}</td>
                <#break>
            <#case "ACTION">
            <#case "KICK">
            <#case "QUIT">
                <td class="${log.type?lower_case}" colspan="2">${log.nick} ${log.message}</td>
                <#break>
            <#default>
                <td class="nick">${log.nick}</td>
                <td>${log.message}</td>
                <#break>
        </#switch>
        <td class="time right">[${format(log.updated)}]</td>
    </tr>
</#list>
</table>
