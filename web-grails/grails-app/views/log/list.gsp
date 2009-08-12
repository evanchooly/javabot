<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Channel Logs for ${params.channel}</title>
  </head>
  <body>
    <div class="body">
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <table cellspacing="0" cellpadding="2" class="dataview" width="100%">
        <tr>
          <g:set var="date" value="${Date.parse('yyyy-MM-dd', params.date)}"/>
          <g:set var="prevDay" value="${date.previous().format('yyyy-MM-dd')}"/>
          <g:set var="nextDay" value="${date.next().format('yyyy-MM-dd')}"/>
          <td align="left">
            <h3><g:link url="prevDay">${prevDay}</g:link></h3>
          </td>
          <td>
            <h3><span
                    wicket:id="channel">${params.channel} ${params.date}</span>
            </h3>
          </td>
          <td align="right">
            <h3><g:link url="nextDay">${nextDay}</g:link></h3>
            </td>
        </tr>
      </table>
      <div class="list">
        <table cellspacing="0" class="mytable" width="100%">
          <thead>
            <tr id="logHeader">
              <th class="lef">Nick</th>
              <th>Message</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${logInstanceList}" status="i" var="logInstance">
              <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                <td><g:link action="show" id="${logInstance.id}">${fieldValue(bean: logInstance, field: 'id')}</g:link></td>

                <td>${fieldValue(bean: logInstance, field: 'channel')}</td>

                <td>${fieldValue(bean: logInstance, field: 'message')}</td>

                <td>${fieldValue(bean: logInstance, field: 'nick')}</td>

                <td>${fieldValue(bean: logInstance, field: 'updated')}</td>

              </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      <div class="paginateButtons">
        <g:paginate total="${logInstanceTotal}"/>
      </div>
    </div>
  </body>
</html>
