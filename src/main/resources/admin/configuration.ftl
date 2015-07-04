<#setting number_format="computer">
<div class="alert alert-error">
<#if hasErrors()>
    <div class="errors">
        <#list errors as error>
            <br/>${error}
        </#list>
    </div>
</#if>
</div>

<form action="/admin/saveConfig" method="POST">
    <table class="form">
        <tr>
            <td class="top">${sofia().configWebUrl()}</td>
            <td class="top right">
                <input type="text" id="url" name="url" value="<#if configuration.url??>${configuration.url}</#if>"/>
            </td>
        </tr>
        <tr>
            <td class="right">
            ${sofia().configIrcServer()}
            </td>
            <td class="right">
                <input type="text" id="server" name="server" value="<#if configuration.server??>${configuration.server}</#if>"/>
            </td>
        </tr>
        <tr>
            <td>
            ${sofia().configIrcPort()}
            </td>
            <td class="right">
                <input type="text" id="port" name="port" value="<#if configuration.port??>${configuration.port}</#if>"/>
            </td>
        </tr>
        <tr>
            <td>
            ${sofia().configIrcHistory()}
            </td>
            <td class="right">
                <input type="text" id="historyLength" name="historyLength"
                       value="<#if configuration.historyLength??>${configuration.historyLength}</#if>"/>
            </td>
        </tr>
        <tr>
            <td>
            ${sofia().configIrcTrigger()}
            </td>
            <td class="right">
                <input type="text" id="trigger" name="trigger" value="<#if configuration.trigger??>${configuration.trigger}</#if>"/>
            </td>
        </tr>
        <tr>
            <td>
            ${sofia().configIrcNick()}
            </td>
            <td class="right">
                <input type="text" id="nick" name="nick" value="<#if configuration.nick??>${configuration.nick}</#if>"/>
            </td>
        </tr>
        <tr>
            <td>
            ${sofia().configIrcPassword()}
            </td>
            <td class="right">
                <input type="password" id="password" name="password" value="<#if configuration.password??>${configuration.password}</#if>"/>
            </td>
        </tr>
        <tr>
            <td>
            ${sofia().configThrottleThreshold()}
            </td>
            <td class="right">
                <input type="text" id="throttleThreshold" name="throttleThreshold"
                       value="<#if configuration.throttleThreshold??>${configuration.throttleThreshold}</#if>"/>
            </td>
        </tr>
        <tr>
            <td>
            ${sofia().configMinimumNickservAge()}
            </td>
            <td class="right">
                <input type="text" id="minimumNickServAge" name="minimumNickServAge"
                       value="<#if configuration.minimumNickServAge??>${configuration.minimumNickServAge}</#if>"/>
            </td>
        </tr>
        <tr>
            <td class="right form-submit" colspan="2">
                <input type="submit" value="Save"/>
            </td>
        </tr>
    </table>
</form>

<h2>Operations:</h2>
<div class="columns">
    <table class="form operations">
    <#list operations() as operation>
        <tr>
            <td class="top">${operation}</td>
            <td class="top button">
                <a id="enable${operation.name}" title="Enable" <#if enabled(operation.name) >class="inactive"<#else>class="active"</#if>
                href="/admin/enableOperation/${operation.name}">
                <img src="/assets/images/boomy/add24.png" alt="Enable"></a>
            </td>
            <td class="top right button">
                <a id="disable${operation.name}" title="Disable" <#if enabled(operation.name) >class="active"<#else>class="inactive"</#if>
                href="/admin/disableOperation/${operation.name}">
                <img src="/assets/images/boomy/delete24.png" alt="Disable"></a>
            </td>
        </tr>
    </#list>
    </table>
</div>

