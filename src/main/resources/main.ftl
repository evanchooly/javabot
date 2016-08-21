<!DOCTYPE html>

<html>
    <head>
        <title>javabot</title>
        <script src="/webjars/jquery/1.11.0/jquery.min.js"></script>
        <script src="/webjars/bootstrap/2.2.1/js/bootstrap.min.js"></script>
        <script type="text/javascript" src='/assets/js/javabot.js'></script>
        <link rel="stylesheet/less" type="text/css" media="screen" href='/assets/style/main.less'>
        <script type='text/javascript' src='/webjars/less/1.3.1/less.min.js'></script>
    </head>
    <body >
    <div id="page_wrapper">
        <div id="header_wrapper">
            <div id="header">
                <h1>Javabot</h1>
            </div>
        </div>

        <div id="left_side">
            <div>
                <h3>Info</h3>

                <div id="boxWrapper">
                    <ul class="plain">
                        <li><a href="/">Home Page</a></li>
                        <li><a href="/factoids">Factoids</a>: ${factoidCount} </li>
                        <!--<li><a wicket:id="activity_link"><span wicket:id="stats">[stats]</span></a></li>-->
                        <li><a href="/karma">Karma Ranking</a></li>
                        <li><a href="/changes">Changelog</a></li>
                    </ul>
                </div>
            </div>
            <#if !loggedIn()>
                <div>
                    <h3><a href="/auth/login">Login</a></h3>
                </div>
            </#if>
            <#if isAdmin()>
            <div>
                <h3>Admin</h3>
                <ul>
                    <li><a href="/admin">Admins</a></li>
                    <li><a href="/admin/config">Configuration</a></li>
                    <li><a href="/admin/javadoc">Javadoc</a></li>
                </ul>
            </div>
            </#if>

            <h3>
                <table>
                    <tr>
                        <td>Channels</td>
                    <#if isAdmin()>
                        <td>
                            <a id="newChannel" href="/admin/newChannel">+</a>
                        </td>
                    </#if>
                    </tr>
                </table>
            </h3>

            <div class="boxWrapper">
                <table class="plain">
                <#list getChannels() as channel>
                    <tr>
                        <td>
                            <a id="${channel.name}" href="/logs/${encode(channel.name)}/today"
                                <#if channel.name == getCurrentChannel()>
                                    class='current'
                                </#if>
                            >${channel.name}</a>
                        </td>
                    </tr>
                </#list>
                </table>
            </div>

            <h3>APIs</h3>

            <div class="boxWrapper">
                <table class="plain">
                <#list getAPIs() as api>
                    <tr>
                        <td>
                            <a href="/javadoc/${api.name}/index.html" target="_blank">${api.name}</a>
                        </td>
                    </tr>
                </#list>
                </table>
            </div>

            <div>
                <h3>Credits</h3>
                <ul>
                    <li>cheeser</li>
                    <li>ernimril</li>
                    <li>joed</li>
                    <li>kinabalu</li>
                    <li>lunk</li>
                    <li>ojacobson</li>
                    <li>r0bby</li>
                    <li>ThaDon</li>
                    <li>ricky_clarkson</li>
                    <li>topriddy</li>
                </ul>
            </div>
        </div>
        <div id="content">
            <div class='featurebox_center'>
            <#include getChildView() >
            </div>
        </div>

        <br style="clear:both;border:none"/>
    </div>
    </body>
</html>
