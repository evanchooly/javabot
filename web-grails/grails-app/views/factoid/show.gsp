<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Show Factoid</title>
  </head>
  <body>
    <div class="body">
        <h1>Show Factoid</h1>
        <g:if test="${flash.message}">
          <div class="message">${flash.message}</div>
        </g:if>
        <div class="dialog">
          <table class="dataview">
            <tbody>
              <tr class="prop">
                <td valign="top" class="name">Id:</td>
                <td valign="top" class="value">${fieldValue(bean: factoidInstance, field: 'id')}</td>
              </tr>

              <tr class="prop">
                <td valign="top" class="name">Name:</td>
                <td valign="top" class="value">${fieldValue(bean: factoidInstance, field: 'name')}</td>
              </tr>

              <tr class="prop">
                <td valign="top" class="name">Value:</td>
                <td valign="top" class="value">${fieldValue(bean: factoidInstance, field: 'value')}</td>
              </tr>

              <tr class="prop">
                <td valign="top" class="name">User Name:</td>
                <td valign="top" class="value">${fieldValue(bean: factoidInstance, field: 'userName')}</td>
              </tr>

              <tr class="prop">
                <td valign="top" class="name">Updated:</td>
                <td valign="top" class="value">${fieldValue(bean: factoidInstance, field: 'updated')}</td>
              </tr>

              <tr class="prop">
                <td valign="top" class="name">Last Used:</td>
                <td valign="top" class="value">${fieldValue(bean: factoidInstance, field: 'lastUsed')}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
  </body>
</html>
