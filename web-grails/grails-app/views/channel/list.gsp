

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Channel List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Channel</g:link></span>
        </div>
        <div class="body">
            <h1>Channel List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>

                   	        <g:sortableColumn property="id" title="Id" />

                   	        <g:sortableColumn property="key" title="Key" />

                   	        <g:sortableColumn property="logged" title="Logged" />

                   	        <g:sortableColumn property="name" title="Name" />

                   	        <g:sortableColumn property="updated" title="Updated" />

                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${channelInstanceList}" status="i" var="channelInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td><g:link action="show" id="${channelInstance.id}">${fieldValue(bean:channelInstance, field:'id')}</g:link></td>

                            <td>${fieldValue(bean:channelInstance, field:'key')}</td>

                            <td>${fieldValue(bean:channelInstance, field:'logged')}</td>

                            <td>${fieldValue(bean:channelInstance, field:'name')}</td>

                            <td>${fieldValue(bean:channelInstance, field:'updated')}</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${channelInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
