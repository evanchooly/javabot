<?xml version="1.0" encoding="utf-8"?>
<!--
@!@!@!@!@ ATTENTION EDITORS OF THIS FILE @!@!@!@!@

If you edit this file, please validate your work using http://validator.w3.org/
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="html"
              indent="no"
              doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
              doctype-system="http://www.w3.org/TR/html4/loose.dtd"/>

  <xsl:param name="lps"><xsl:value-of select="/*/request/@lps"/></xsl:param>
  <xsl:param name="debug"><xsl:value-of select="(/*/info/@debug = 'true') or (/canvas/@debug = 'true')"/></xsl:param>
  <xsl:param name="lzruntime" select="@runtime"/>
  <xsl:param name="appinfo"><xsl:value-of select="/*/console_appinfo/text()"/></xsl:param>
  <xsl:param name="assets"><xsl:value-of select="/*/request/@lps"/>/lps/assets</xsl:param>
  <xsl:param name="console-floating-window"><xsl:value-of select="/*/request/@console-floating-window"/></xsl:param>


  <!-- console will be 70 high, unless the remote console debugger is on, in which case
       it will be 370 -->
  <xsl:param name="consoleheight"><xsl:choose><xsl:when test="/*/request/@console-remote-debug = 'true'">370</xsl:when><xsl:otherwise>70</xsl:otherwise> </xsl:choose></xsl:param>

  <xsl:param name="consolefooter"><xsl:choose><xsl:when test="/*/request/@console-remote-debug = 'true'">console-large</xsl:when><xsl:otherwise>console</xsl:otherwise></xsl:choose></xsl:param>

  <!--  <xsl:param name="appuid"><xsl:value-of select="/*/request/@appuid"/></xsl:param> -->
  <!-- we'll use the app name for now, to make it easier to point a remote debugger at it -->
  <xsl:param name="appuid"><xsl:value-of select="/*/request/@url"/></xsl:param>

  <xsl:param name="opturl" select="/*/request/@opt-url"/>
  <xsl:param name="unopturl" select="/*/request/@unopt-url"/>

  <!--
      In standards mode, your dimensions must have explicit units
  -->
  <xsl:template name="dimension">
    <xsl:param name="value" />
    <xsl:choose>
      <xsl:when test="substring-before($value, '%') = ''">
        <xsl:value-of select="concat($value, 'px')"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="'100%'"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:param name="canvasheight"><xsl:call-template name="dimension"><xsl:with-param name="value" select="/canvas/@height" /></xsl:call-template></xsl:param>
  <xsl:param name="canvaswidth"><xsl:call-template name="dimension"><xsl:with-param name="value" select="/canvas/@width" /></xsl:call-template></xsl:param>

  <!--
      embed.js strikes again: We should be able to have the div's
      around the application auto size to their content, and the
      application should be the size of the canvas.  But in some
      browsers (Firefox at least), dynamically creating an object does
      not cause the DOM to update its size calculations, so we
      explicitly set the size of the div's surrounding the application
      to 100%.
  -->
  <xsl:param name="containerheight">
  <xsl:choose>
    <xsl:when test="$debug = 'true' and /canvas/@runtime = 'dhtml'">auto</xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$canvasheight"/>
    </xsl:otherwise>
  </xsl:choose>
  </xsl:param>

  <xsl:template match="/">
    <html>
      <head>
        <link rel="SHORTCUT ICON" href="http://www.laszlosystems.com/favicon.ico"/>
        <link rel="stylesheet" href="{$lps}/lps/includes/console.css" type="text/css"/>
        <title>
          <xsl:choose>
            <xsl:when test="canvas">
              <xsl:value-of select="canvas/@title"/>
            </xsl:when>
            <xsl:otherwise>
              Compilation Errors
            </xsl:otherwise>
          </xsl:choose>
        </title>
        <script src="{/canvas/request/@lps}/lps/includes/embed-compressed.js" type="text/javascript"/>
      <xsl:choose>
        <xsl:when test="/canvas/@runtime = 'dhtml'">
        <script type="text/javascript">
            lzOptions = { ServerRoot: '<xsl:value-of select="/canvas/request/@lps"/>', dhtmlKeyboardControl: false };
        </script>  
        </xsl:when>
      </xsl:choose>
      </head>
      <body>
        <xsl:if test="/canvas/warnings">
          <div id="warnings">
            <a href="#warnings">Review</a>
            <xsl:text> </xsl:text>
            <xsl:value-of select="count(/canvas/warnings/error)"/>
            <xsl:text> compilation warning</xsl:text>
            <xsl:if test="count(/canvas/warnings/error) > 1">s</xsl:if>.
          </div>
        </xsl:if>
        <xsl:apply-templates select="canvas|errors"/>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="canvas">
    <xsl:param name="url" select="request/@url"/>
    <xsl:param name="query_args" select="request/@query_args"/>
    <div id="magilla" style="height: {$containerheight}">
    <xsl:choose>
      <!-- In the case of an lzt=html request, ResponderHTML uses string
           concatenation to create the <OBJECT>, <object>, and <embed>
           elements. See the comment in ResponderHTML for an explanation. -->
      <xsl:when test="@pocketpc">
        <OBJECT classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
                width="{@width}" height="{@height}" id="lzx">
          <PARAM NAME="movie" VALUE="{$url}?lzt=swf"/>
        </OBJECT>
      </xsl:when>
      <xsl:otherwise>
        <div id="application" style="background-color: {/canvas/@bgcolor}; height: {$containerheight}">

          <xsl:choose>
            <xsl:when test="@runtime = 'dhtml'">
              <xsl:choose>
                <xsl:when test="$debug = 'true'" >
                  <div id="dhtml-debugger-application">
                    <iframe id="dhtml-application"
                            src="{/canvas/request/@url}?lzt=html{/canvas/request/@query_args}"
                            style="width: {$canvaswidth}; height: {$canvasheight}" />
                    <!-- bootstrap debugger window -->
                  </div>
                  <div id="dhtml-debugger">
                    <div title="OpenLaszlo Debugger" id="dhtml-debugger-label">
                      <span></span>OpenLaszlo Debugger
                    </div>
                    <div id="dhtml-debugger-output">
                      <!-- Opera barfs if there is no src property -->
                      <iframe name="LaszloDebugger" id="LaszloDebugger"
                              src='{/canvas/request/@lps}/lps/includes/laszlo-debugger.html'>
                      </iframe>
                    </div>
                    <form id="dhtml-debugger-input"
                          action="#"
                          onsubmit="$modules.lz.Debug.doEval(document.getElementById('LaszloDebuggerInput').value); return false">
                        <div>
                          <input id="LaszloDebuggerInput" type="text" />
                          <input type="button" value="eval" onclick="$modules.lz.Debug.doEval(document.getElementById('LaszloDebuggerInput').value); return false"/>
                          <input type="button" value="clear" onclick="$modules.lz.Debug.clear(); return false"/>
                        </div>
                      </form>
                  </div>
                </xsl:when>

                <xsl:otherwise>
                  <!-- just the application -->
                  <iframe id="dhtml-application"
                          src="{/canvas/request/@url}?lzt=html{/canvas/request/@query_args}"
                          style="width: {$canvaswidth}; height: {$canvasheight}" />
                </xsl:otherwise>
              </xsl:choose>

            </xsl:when>
            <xsl:otherwise>
              <iframe id="dhtml-application"
                      src="{/canvas/request/@url}?lzt=html{/canvas/request/@query_args}"
                      style="width: {$canvaswidth}; height: {$canvasheight}" />
            </xsl:otherwise>
          </xsl:choose>

          <noscript>
            Please enable JavaScript in order to use this application.
          </noscript>
        </div>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:call-template name="footer"/>
</div>
    <xsl:if test="not(//param[@name='showTaskBar'])">
    <xsl:choose>
    <xsl:when test="@runtime = 'dhtml'">
        <script type="text/javascript">
        Lz.dhtmlEmbed({url: '<xsl:value-of select="$lps"/>/lps/admin/dev-console.lzx.js?lzappuid=<xsl:value-of select="$appuid"/>&amp;lzt=dhtml&amp;appinfo=<xsl:value-of select="$appinfo"/>', bgcolor: '#9494ad', width: '100%', height: '<xsl:value-of select="$consoleheight"/>', appenddivid: 'console'}, true);
        </script>
    </xsl:when>
    </xsl:choose>
    </xsl:if>
  </xsl:template>

  <xsl:template name="footer">
    <xsl:param name="url" select="request/@url"/>
    <xsl:param name="query_args" select="request/@query_args"/>
    <xsl:if test="not(//param[@name='showTaskBar'])">
      <div id="footer" style="position: relative">
        <xsl:call-template name="tasks"/>
        <xsl:apply-templates select="info"/>
      </div>
    </xsl:if>
    <xsl:apply-templates select="warnings"/>

  </xsl:template>

  <xsl:template name="tasks">
    <xsl:param name="url" select="request/@url"/>
    <xsl:param name="query_args" select="request/@query_args"/>

    <div id="{$consolefooter}">
      <!-- an embedded SOLO console app to replace the HTML console -->
      <xsl:choose>
        <xsl:when test="@runtime = 'dhtml'">
        </xsl:when>
        <xsl:otherwise>
            <script type="text/javascript">
                Lz.swfEmbed({url: '<xsl:value-of select="$lps"/>/lps/admin/dev-console.lzx.swf?lzappuid=<xsl:value-of select="$appuid"/>&amp;lzt=swf&amp;appinfo=<xsl:value-of select="$appinfo"/>', bgcolor: '#9494ad', width: '100%', height: '<xsl:value-of select="$consoleheight"/>', appenddivid: 'console'});
            </script>
        </xsl:otherwise>
      </xsl:choose>

      <!-- pop up console debugger window -->
      <!--
          <script>
          CWin = window.open('<xsl:value-of select="$lps"/>/lps/admin/dev-console-popup.html?lzappuid=<xsl:value-of select="$appuid"/>&amp;lzt=swf&amp;appinfo=<xsl:value-of select="$appinfo"/>', "_blank", "toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=0,resizable=1,copyhistory=0,width=1250,height=400");
          </script>
      -->
    </div>
  </xsl:template>

  <xsl:template match="info">
    <xsl:param name="url" select="../request/@url"/>
    <xsl:param name="query_args" select="../request/@query_args"/>
    <xsl:param name="size" select="@size"/>
    <xsl:param name="gzsize" select="@gzsize"/>
    <xsl:param name="lfcsize" select="@lfcsize"/>
    <xsl:param name="gzlfcsize" select="@gzlfcsize"/>
    <xsl:param name="totalsize" select="$size + $lfcsize"/>
    <xsl:param name="gztotalsize" select="$gzsize + $gzlfcsize"/>
    <xsl:param name="runtime" select="@runtime"/>
    <!-- megabytes * 10 -->
    <xsl:param name="mb10" select="round($size * 10 div 1024 div 1024)"/>

    <div class="info">
      <b>Runtime Target: </b> <xsl:value-of select="$runtime"/><br/>
      <xsl:choose>
        <xsl:when test="@runtime = 'dhtml'">
            <b>Total Script Size: </b>
            <b><xsl:value-of select="round($gztotalsize div 1024)"/>K</b>
            (<xsl:value-of select="round($totalsize div 1024)"/>K uncompressed)
            <b> = </b>
            <b><xsl:value-of select="round($gzsize div 1024)"/>K</b>
            (<xsl:value-of select="round($size div 1024)"/>K uncompressed) application
            <b> + </b>
            <b><xsl:value-of select="round($gzlfcsize div 1024)"/>K</b>
            (<xsl:value-of select="round($lfcsize div 1024)"/>K uncompressed) LFC
        </xsl:when>
        <xsl:otherwise>
            <b>Application Size: </b>
            <!-- print "ddK" or "d.dMB" -->
            <xsl:choose>
                <xsl:when test="$mb10 >= 9">
                <xsl:value-of select="floor($mb10 div 10)"/>
                <xsl:text>.</xsl:text>
                <xsl:value-of select="$mb10 mod 10"/>MB
                </xsl:when>
                <xsl:otherwise>
                <xsl:value-of select="round($size div 1024)"/>K
                </xsl:otherwise>
            </xsl:choose>
            <!-- "(ddd,ddd bytes)" -->
            (<xsl:call-template name="decimal">
            <xsl:with-param name="value" select="$size"/>
            </xsl:call-template> bytes)
            <a target="_blank"
                href="{$url}?lzt=info{$query_args}">Size profile</a>
        </xsl:otherwise>
      </xsl:choose>
    </div>
  </xsl:template>

  <!-- prints a decimal with interpolated commas -->
  <xsl:template name="decimal">
    <xsl:param name="value"/>
    <xsl:param name="remainder" select="$value mod 1000"/>
    <xsl:if test="$value &gt; 1000">
      <xsl:call-template name="decimal">
        <xsl:with-param name="value" select="floor($value div 1000)"/>
      </xsl:call-template>
      <xsl:text>,</xsl:text>
      <xsl:if test="$remainder &lt; 100">0</xsl:if>
      <xsl:if test="$remainder &lt; 10">0</xsl:if>
    </xsl:if>
    <xsl:value-of select="$remainder"/>
  </xsl:template>

  <xsl:template match="canvas/warnings">
    <div id="warnings">
      <h2>Compilation Warnings</h2>
      <pre class="warning">
        <xsl:for-each select="error">
          <xsl:if test="position() > 1">
            <br/>
          </xsl:if>
          <xsl:apply-templates select="."/>
        </xsl:for-each>
      </pre>
    </div>
  </xsl:template>

  <xsl:template match="error">
    <xsl:choose>
      <xsl:when test="starts-with(text(), '.tmp_')">
        <xsl:value-of select="substring-after(substring-after(text(), '_'), '_')"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="text()"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="/errors">
    <div style="border-top: 1px solid; border-bottom: 1px solid; background-color: #fcc; padding: 1pt; padding-left: 2pt">
      The application could not be compiled due to the following errors:
    </div>
    <h1>Compilation Errors</h1>
    <code class="error">
      <xsl:apply-templates select="error"/>
    </code>
    <xsl:if test="error/error">
      <div id="warnings">
        <h2>Compilation Warnings</h2>
        <pre class="warning">
          <xsl:for-each select="error/error">
            <xsl:if test="position() > 1">
              <br/>
            </xsl:if>
            <xsl:apply-templates select="."/>
          </xsl:for-each>
        </pre>
      </div>
    </xsl:if>

    <xsl:call-template name="footer"/>
  </xsl:template>

</xsl:stylesheet>

<!-- * X_LZ_COPYRIGHT_BEGIN ***************************************************
* Copyright 2001-2006 Laszlo Systems, Inc.  All Rights Reserved.              *
* Use is subject to license terms.                                            *
* X_LZ_COPYRIGHT_END ****************************************************** -->

