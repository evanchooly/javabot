<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Karma</title>
  </head>
  <body>
    <div class="body">
      <h1>Karmas</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <div class="paginateButtons">
        <g:paginate total="${karmaInstanceTotal}"/>
      </div>
      <div class="list">
        <table class="dataview">
          <thead>
            <tr>
              <g:sortableColumn class="lef" property="name" title="Nick"/>
              <g:sortableColumn property="value" title="Karma"/>
              <g:sortableColumn property="userName" title="Last Donor"/>
              <g:sortableColumn property="updated" title="Date"/>
            </tr>
          </thead>
          <tbody>
            <g:each in="${karmaInstanceList}" status="i" var="karmaInstance">
              <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                <td>${karmaInstance.name}</td>
                <td>${karmaInstance.value}</td>
                <td>${karmaInstance.userName}</td>
                <td>${karmaInstance.updated.format("yyyy-MM-dd")}</td>
              </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      <div class="paginateButtons">
        <g:paginate total="${karmaInstanceTotal}"/>
      </div>
    </div>
  </body>
</html>
