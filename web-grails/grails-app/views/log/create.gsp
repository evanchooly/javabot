

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Log</title>         
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">Log List</g:link></span>
        </div>
        <div class="body">
            <h1>Create Log</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${logInstance}">
            <div class="errors">
                <g:renderErrors bean="${logInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="channel">Channel:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:logInstance,field:'channel','errors')}">
                                    <input type="text" id="channel" name="channel" value="${fieldValue(bean:logInstance,field:'channel')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="message">Message:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:logInstance,field:'message','errors')}">
                                    <input type="text" id="message" name="message" value="${fieldValue(bean:logInstance,field:'message')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="nick">Nick:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:logInstance,field:'nick','errors')}">
                                    <input type="text" id="nick" name="nick" value="${fieldValue(bean:logInstance,field:'nick')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="updated">Updated:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:logInstance,field:'updated','errors')}">
                                    <g:datePicker name="updated" value="${logInstance?.updated}" ></g:datePicker>
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
