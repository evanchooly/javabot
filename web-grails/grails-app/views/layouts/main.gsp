<html>
  <head>
    <title><g:layoutTitle default="javabot"/></title>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'main.css')}"/>
    <link rel="shortcut icon" href="${createLinkTo(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
    <g:layoutHead/>
    <g:javascript library="application"/>
  </head>
  <body>
    <div id="spinner" class="spinner" style="display:none;">
      <img src="${createLinkTo(dir: 'images', file: 'spinner.gif')}" alt="Spinner"/>
    </div>
    <div id="page_wrapper">
      <div id="header_wrapper">
        <div id="header">
          <h1>Javabot: <g:layoutTitle default="Home"/></h1>
        </div>

      </div>

      <div id="left_side">
        <g:set var="list" value="${Channel.listPublic()}"/>
        <span id="ChannelBox"><h3>Channels (${list.total})</h3>
          <div class="boxWrapper">
            <ul class="plain" id="logged_channels">
              <g:each var="chan" in="${list.results}">
                <g:set var="name" value="${chan.name}"/>
                <li><g:link controller="log" params="[channel:name]">${name}</g:link></li>
              </g:each>
            </ul>
          </div>
        </span>
      </div>

      <div id="right_side">
        <span id="info">
          <h3>Info</h3>

          <div id="boxWrapper">
            <ul class="plain">
              <li><g:link url="/">Home Page</g:link></li>
              <li><g:link class="create" controller="factoid" action="list">Factoids</g:link>: ${Factoid.count()}</li>
              <li><g:link class="create" controller="statistics" action="list">Stats</g:link></li>
              <li><g:link class="create" controller="karma" action="list">Karma</g:link></li>
              <li><g:link class="create" controller="change" action="list">Changes</g:link></li>
            </ul>
          </div>
        </span>

        <span id="credits">
          <h3>Credits</h3>
          <ul>
            <li>cheeser</li>
            <li>ernimril</li>
            <li>Fanook</li>
            <li>joed</li>
            <li>kinabalu</li>
            <li>lunk</li>
            <li>ojacobson</li>
            <li>ricky_clarkson</li>
            <li>r0bby</li>
          </ul>
        </span>
      </div>

      <div id="content">
        <g:layoutBody/>
      </div>
    </div>
  </body>
</html>