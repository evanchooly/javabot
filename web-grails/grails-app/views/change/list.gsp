<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Changes</title>
  </head>
  <body>
    <div class="body">
      <h1>Changes</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <div class="paginateButtons">
        <g:paginate total="${changeInstanceTotal}"/>
      </div>
      <div class="list">
        <table class="dataview">
          <thead>
            <tr>
              <th>Message</th>
              <th>Updated</th>
            </tr>
          </thead>
          <tbody>
            <g:each in="${changeInstanceList}" status="i" var="changeInstance">
              <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                <td>${changeInstance.message}</td>
                <td class="nowrap">${changeInstance.changeDate.format("yyyy-MM-dd hh:mm")}</td>
              </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      <div class="paginateButtons">
        <g:paginate total="${changeInstanceTotal}"/>
      </div>
    </div>
  </body>
</html>
