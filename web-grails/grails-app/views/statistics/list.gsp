<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Statistics for ${stats.table ? stats.table : "N/A"} from ${stats.table ? stats.start : "N/A"}
      to ${stats.table ? stats.end : "N/A"}</title>
  </head>
  <body>
    <g:if test="${flash.message}">
      <div class="message">${flash.message}</div>
    </g:if>
    <g:form action="list" method="POST" onChange="submit();">
      Statistics for:  <g:select name="table" from="${channels}" value="${stats.table}"/>
    </g:form>
    <table cellspacing="0" cellpadding="2" class="dataview" width="100%">
      <thead>
        <tr id="logHeader">
          <th class="lef">Number</th>
          <th>Nick</th>
          <th>Count (${stats.count})</th>
          <th>Percent</th>
        </tr>
      </thead>
      <tbody>
        <g:each in="${stats.users}" status="i" var="user">
          <tr>
            <td>${i + 1}</td>
            <td>${user.nick}</td>
            <td>${user.count}</td>
            <td>${user.percent}</td>
          </tr>
        </g:each>
      </tbody>
    </table>
  </body>
</html>
