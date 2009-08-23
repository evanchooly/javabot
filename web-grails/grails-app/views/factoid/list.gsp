<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Factoids</title>
  </head>
  <body>
    <div class="body">
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <div class="paginateButtons">
        <g:paginate total="${factoidInstanceTotal}"/>
      </div>
      <div class="list">
        <table class="dataview">
          <thead>
            <tr>
              <g:sortableColumn class="lef" property="name" title="Name"/>
              <g:sortableColumn property="value" title="Value"/>
              <g:sortableColumn property="userName" title="Added By"/>
              <g:sortableColumn property="updated" title="Updated"/>
              %{--<g:sortableColumn property="lastUsed" title="Last Used"/>--}%
            </tr>
          </thead>
          <tbody>
            <g:each in="${factoidInstanceList}" status="i" var="factoidInstance">
              <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                <td><g:link action="show" id="${factoidInstance.id}">${factoidInstance.name}</g:link></td>
                <td>${factoidInstance.value}</td>
                <td>${factoidInstance.userName}</td>
                <td>${factoidInstance.updated}</td>
                %{--<td>${factoidInstance.lastUsed}</td>--}%
              </tr>
            </g:each>
          </tbody>
        </table>
      </div>
      <div class="paginateButtons">
        <g:paginate total="${factoidInstanceTotal}"/>
      </div>
    </div>
  </body>
</html>
