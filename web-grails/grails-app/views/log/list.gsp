<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Channel Logs</title>
    </head>
    <body>
        <div class="body">
            <h1>Channel Logs</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="channel" title="Channel" />
                        
                   	        <g:sortableColumn property="message" title="Message" />
                        
                   	        <g:sortableColumn property="nick" title="Nick" />
                        
                   	        <g:sortableColumn property="updated" title="Updated" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${logInstanceList}" status="i" var="logInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${logInstance.id}">${fieldValue(bean:logInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:logInstance, field:'channel')}</td>
                        
                            <td>${fieldValue(bean:logInstance, field:'message')}</td>
                        
                            <td>${fieldValue(bean:logInstance, field:'nick')}</td>
                        
                            <td>${fieldValue(bean:logInstance, field:'updated')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${logInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
