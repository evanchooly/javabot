<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Channel Logs for ${params.channel}</title>
  </head>
  <body>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <table cellspacing="0" cellpadding="2" class="dataview" width="100%">
        <tr>
          <g:set var="date" value="${Date.parse('yyyy-MM-dd', params.date)}"/>
          <g:set var="prevDay" value="${params.prevLogDay.format('yyyy-MM-dd')}"/>
          <g:set var="nextDay" value="${params.nextLogDay.format('yyyy-MM-dd')}"/>
          <td align="left">
            <h3><g:link action="list" params="[date:prevDay, channel:params.channel]">${prevDay}</g:link></h3>
          </td>
          <td>
            <h3><span id="channel">${params.date} (${logInstanceCount} entries)</span>
            </h3>
          </td>
          <td align="right">
            <h3><g:link action="list" params="[date:nextDay, channel:params.channel]">${nextDay}</g:link></h3>
          </td>
        </tr>
      </table>
      <table class="dataview">
        <thead>
          <tr id="logHeader">
            <th class="lef">Nick</th>
            <th>Message</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
          <g:each in="${logInstanceList}" status="i" var="logInstance">
            <tr>
              <g:if test="${logInstance.isAction() || logInstance.isServerMessage()}">
                <td class="action" colspan="2">*** ${logInstance.nick} ${logInstance.message}</td>
              </g:if>
              <g:elseif test="${logInstance.isKick()}">
                <td class="kick" colspan="2">*** ${logInstance.nick} ${logInstance.message}</td>
              </g:elseif>
              <g:else>
                <td>&lt;${logInstance.nick}&gt;</td>
                <td>${logInstance.message}</td>
              </g:else>
              <td>[${logInstance.updated.format("HH:mm:ss")}]</td>
            </tr>
          </g:each>
        </tbody>
      </table>
  </body>
</html>
