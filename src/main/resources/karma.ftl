<tr>
    <th>Nick</th>
    <th>Karma</th>
    <th>Last Donor</th>
    <th class="right">Date</th>
</tr>
<#list getPageItems() as karma>
<tr>
    <td>${karma.name}</td>
    <td>${karma.value}</td>
    <td>${karma.userName}</td>
    <td class="right">${ format(karma.updated) }</td>
</tr>
</#list>