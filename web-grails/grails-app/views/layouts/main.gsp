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
          <h1>Javabot</h1>
        </div>

      </div>

      <div id="left_side">
        <span id="ChannelBox"><h3>Channels</h3>
          <div class="boxWrapper">
            <ul class="plain" id="logged_channels">
              <g:each var="chan" in="${Channel.list()}">
                <li><g:link controller="logController">${chan.name}</g:link></li>
              </g:each>
            </ul>
          </div></span>
      </div>

      <div id="right_side">
        <span id="info">
          <h3>Info</h3>

          <div id="boxWrapper">
            <ul class="plain">
              <li><a id="homepage" href="#">[link text here]</a></li>
              <li><a id="factoid_link"><span id="factoid">[factoid]</span></a><span id="factoid_count">[count]</span></li>
              <li><a id="activity_link"><span id="stats">[stats]</span></a></li>
              <li><a id="karma_link"><span id="karma">[karma]</span></a></li>
              <li><a id="changes_link"><span id="changes">[changes]</span></a></li>
            </ul>
          </div>
        </span>

        <span id="credits">
          <h3>Credits</h3>
          <ul>
            <li>cheeser</li>
            <li>joed</li>
            <li>kinabalu</li>
            <li>lunk</li>
            <li>ojacobson</li>
            <li>r0bby</li>
            <li>ernimril</li>
          </ul>
        </span>
      </div>

      <div id="content">
        <div class='featurebox_center'>
          <g:layoutBody/>
        </div>
      </div>
    </div>

  </body>
</html>