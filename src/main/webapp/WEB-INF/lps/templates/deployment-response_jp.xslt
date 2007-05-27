<?xml version="1.0" encoding="utf-8"?>

<!-- * X_LZ_COPYRIGHT_BEGIN ***************************************************
* Copyright 2001-2006 Laszlo Systems, Inc.  All Rights Reserved.              *
* Use is subject to license terms.                                            *
* X_LZ_COPYRIGHT_END ****************************************************** -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="html"
              indent="yes"
              doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
              doctype-system="http://www.w3.org/TR/html4/loose.dtd"/>
  
  <xsl:template match="/">
<html>
  <head>
    <link rel="SHORTCUT ICON" href="http://www.laszlosystems.com/favicon.ico"/>
    <title>
      Deploying This Application
    </title>
    <style type="text/css">
      pre {border: 1px solid; background-color: #eef}
    </style>
  </head>
  <body>
      <h1>このアプリケーションの配置</h1>
      
      <h2>HTMLラッパーを利用</h2>
      <p>アプリケーションの配置に一番簡単な方法は <code>html</code> リクエストを使うことです。
	ここでは、 <code>embed-compressed.js</code>JavaScriptライブラリを使いアプリケーションを埋め込んだHTMLを作成します。
	（リクエストタイプを指定していない）開発用ページとは違い、デベロッパーコンソールとコンパイラ・ワーニングを表示しません。</p>
      
      <p>HTML配置ページ結果のプレビューは <a
      href="{/canvas/request/@url}?lzt=html{/canvas/request/@query_args}">こちら</a>です。
      (このページはブラウザ内で動的に作成されており、各種ブラウザに対応します。ブラウザからのソースビューとHTML配置ページ用のページソースとは異なります。)</p>
      
      <h2>埋め込み<code>オブジェクト</code>タグによる配置</h2>
      <p>JavaScriptライブラリを使わなくても、アプリケーションをページ内に埋め込むことができます。もしほとんどの対象クライアントがブラウザのJavaScriptを無効にしていることが予想されるなら、この方法がよい選択となります。</p>
      
      <p>埋め込みオブジェクトタグによるHTMLページは<a
      href="{/canvas/request/@url}?lzt=html-object{/canvas/request/@query_args}">こちら</a>です。
      (このページはサーバーによって動的に作成されており、各種ブラウザに対応します。すべてのブラウザ向けに生成されたコードはXHTML1.0に対応していますが、Windows版Internet Explorerのみ特別な扱いを受けます。
Windows版Internet Explorer向けに生成されたコードは、正しいFlashプラグインバージョンがインストールされているか確認する拡張コードが埋め込まれています。そのため、アプリケーションを大きな静的ページの中に埋め込む必要がある場合は、XHTML1.0バージョンを利用してください。)</p>

      <p>静的XHTML1.0仕様のコードを使って配置するには、Laszloアプリケーションを表示させたいHTMLドキュメント内に以下のコードを貼り付けてください。</p>

      <pre>&lt;object type="application/x-shockwave-flash"
        data="<xsl:value-of select='canvas/request/@url' />?lzt=swf<xsl:value-of select='canvas/request/@query_args' />"
        width="<xsl:value-of select='canvas/@width' />" height="<xsl:value-of select='canvas/@height' />">
  &lt;param name="movie" value="<xsl:value-of select='canvas/request/@url' />?lzt=swf<xsl:value-of select='canvas/request/@query_args' />" />
  &lt;param name="quality" value="best" />
  &lt;param name="scale" value="noscale" />
  &lt;param name="salign" value="LT" />
  &lt;param name="menu" value="false" />
&lt;/object></pre>
      
      <h2><code>embed-compressed.js</code> JavaScriptライブラリを利用した配置</h2>
      <p> <code>embed-compressed.js</code>JavaScriptライブラリを利用して配置するには、次の行をLaszloアプリケーションを貼り付けるHTMLドキュメントの<code>&lt;head&gt;</code>セクションに貼り付けます。
</p>
      
      <pre>&lt;script src="<xsl:value-of select="/canvas/request/@lps"/>/lps/includes/embed-compressed.js" type="text/javascript">&lt;/script></pre>
      
      <p>次に次のコードを<code>&lt;body></code>セクション内のLaszloアプリケーションを表示したい場所へ貼り付けます。
</p>
      
      <pre>&lt;script type="text/javascript"&gt;
          Lz.swfEmbed({url: '<xsl:value-of select="/canvas/request/@url"/>?lzt=swf<xsl:value-of select="/canvas/request/@query_args"/>', bgcolor: '<xsl:value-of select="/canvas/@bgcolor"/>', width: '<xsl:value-of select="/canvas/@width"/>', height: '<xsl:value-of select="/canvas/@height"/>', id: '<xsl:value-of select="/canvas/@id"/>'});
&lt;/script></pre>

      <p> <a href="{/canvas/request/@url}?lzt=html{/canvas/request/@query_arg}">こちら</a>をクリックして配置ページの例をご覧下さい。これにはクライアント側でのバージョン確認用とブラウザ履歴統合の追加コードが含まれています。</p>
      
      <p><code>Lz.swfEmbed</code>を呼ぶコードを生成するよう<code>js</code>リクエストタイプを使うこともできます。 </p>
      
      <pre>&lt;script src="<xsl:value-of select="/canvas/request/@url"/>?lzt=js" type="text/javascript"&gt;
&lt;/script></pre>
      
      <p>HTMLページがLZXソースファイルと違う他のディレクトリへ移動された場合には'url'パラメータの値を変更する必要があります。</p>
      
      <h2>クランクされたアプリケーションの配置</h2>
      <p>クランクされたアプリケーションを使うには、
      <code><xsl:value-of select="//@url"/></code> が
      <code><xsl:value-of select="//@opt-url"/></code>と置き変えます。クランク機能についての詳細は<a href="{/canvas/request/@lps}/docs/guide/">LZX開発者用ガイド</a> の  <a
      href="{/canvas/request/@lps}/docs/guide/krank.html">クランクの章</a>をお読み下さい。</p>
      
      <p>クランクバージョンが利用可能な場合に、クランクされていないアプリケーションをコンパイルするには、<code><xsl:value-of
      select="//@url"/></code>を<code><xsl:value-of
      select="//@opt-url"/>?fb=1</code>と置き換えます。</p>
      
      <h2>拡大縮小と大きさ指定</h2>
      <p>アプリケーションの幅と高さを指定するには、<code>scale: 'noscale'</code>を<code>Lz.swfEmbed</code>内に追加します。そうでなければ、アプリケーションは自動的に最大化されます。</p>
<h2>SOLOアプリケーションへパラメータを渡す</h2>
<p>
配置したSOLOアプリケーションへ、パラメータをアプリケーションに渡したい場合はサーバーが生成したHTMLラッパーページの修正が必要となります。</p>
<p>
次の<code>Lz.swfEmbed</code>行はすべてのクエリパラメータをLaszloアプリケーションへ渡しています。</p>
<pre>
Lz.swfEmbed({url: 'main.lzx.lzr=swf7.swf?'+window.location.search.substring(1), bgcolor: '<xsl:value-of select="/canvas/@bgcolor"/>', width: '<xsl:value-of select="/canvas/@width"/>', height: '<xsl:value-of select="/canvas/@height"/>', id: '<xsl:value-of select="/canvas/@id"/>', accessible: '<xsl:value-of select="/canvas/@accessible"/>'});
</pre>
<p>
<code>main.lzx?lzt=swf</code>から<code>main.lzx.swf? </code>への変更点は<code>'+window.location.search.substring(1)'</code>が追加されていることです。</p>

<h3><a href='{canvas/request/@lps}/lps/admin/solo-deploy.jsp?appurl={canvas/request/@relurl}'>SOLO配置ウィザード</a></h3>
OpenLaszloサーバーには、SOLO配置用にアプリケーションのパッケージ化を支援する<a href='{canvas/request/@lps}/lps/admin/solo-deploy.jsp?appurl={canvas/request/@relurl}'>SOLO配置用ウィザード</a>アプリケーションが用意されています。

      <h2>もっと情報が必要なら</h2>
      <ul>
        <li><a href="{/canvas/request/@lps}/docs/deploy/">Laszloアプリケーション配置用システム管理者用ガイド</a></li>
        <li><a href="{/canvas/request/@lps}/docs/guide/">ソフトウェア開発者ガイド</a></li>
        <li><a href="http://forum.openlaszlo.org/">開発者フォーラム</a></li>
      </ul>
  </body>
</html>
  </xsl:template>
</xsl:stylesheet>
