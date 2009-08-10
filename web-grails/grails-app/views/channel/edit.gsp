

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Channel</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Channel List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Channel</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Channel</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${channelInstance}">
            <div class="errors">
                <g:renderErrors bean="${channelInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${channelInstance?.id}" />
                <input type="hidden" name="version" value="${channelInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="key">Key:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:channelInstance,field:'key','errors')}">
                                    <input type="text" id="key" name="key" value="${fieldValue(bean:channelInstance,field:'key')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="logged">Logged:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:channelInstance,field:'logged','errors')}">
                                    <g:checkBox name="logged" value="${channelInstance?.logged}" ></g:checkBox>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name">Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:channelInstance,field:'name','errors')}">
                                    <input type="text" id="name" name="name" value="${fieldValue(bean:channelInstance,field:'name')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="updated">Updated:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:channelInstance,field:'updated','errors')}">
                                    <g:datePicker name="updated" value="${channelInstance?.updated}" ></g:datePicker>
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
