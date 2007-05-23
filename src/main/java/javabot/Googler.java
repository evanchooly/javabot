package javabot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.rickyclarkson.java.util.TypeSafeList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Adapted from mod_googler.cpp by mr_gray in #java on Freenode
 */
public class Googler {
    private static Log log = LogFactory.getLog(Googler.class);
    private static final String TOKEN = "<p class=g><a href=";
    private static final String SPELLING_TOKEN = "<font color=cc0000 class=p>Did you mean: </font><a href=";
    
    private List<Result> searchResults=new ArrayList<Result>();

    private Socket socket;
    private String correctSpelling;
    private boolean foundResults;
    private int currentIndex;
    private static final String GOOGLE_QUERY_URL = "&btnG=Google+Search HTTP/1.1\r\n"
        + "Accept: text/html\r\n"
        + "Referer: http://www.google.com/index.html\r\n"
        + "Accept-Language: en-us\r\n"
        + "User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; "
        + "Avant Browser [avantbrowser.com]; "
        + ".NET CLR 1.1.4322)\r\nHost: www.google.com\r\n"
        + "Connection: close\r\n\r\n";

    /**
     *
     */
    public Googler() {
        try {
            socket = new Socket("www.google.com", 80);
        } catch(Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * @return
     */
    public boolean connect() {
        return socket != null && socket.isConnected();
    }

    /**
     * @param query
     */
    public void search(String query) {
        if(socket != null) {
            resetVars();
            String httpPost = "GET /search?hl=en&ie=UTF-8&oe=UTF-8&q="
                + urlEscape(query) + GOOGLE_QUERY_URL;
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.print(httpPost);
                writer.flush();
            } catch(Exception exception) {
                throw new RuntimeException(exception);
            }
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            } catch(IOException ie) {
                log.error(ie.getMessage(), ie);
            }
            StringBuffer resultBuffer = new StringBuffer();
            String line = null;
            do {
                try {
                    line = bufferedReader.readLine();
                } catch(IOException exception) {
                    throw new RuntimeException(exception);
                }
                if(line != null) {
                    resultBuffer.append(line + "\r\n");
                }
            } while(line != null);
            try {
                socket.close();
            } catch(Exception exception) {
                log.error(exception.getMessage(), exception);
            }
            String result = resultBuffer.toString();
            if(result.indexOf(noResultsToken(query)) != -1) {
                return;
            }
            int findIndex = result.indexOf(SPELLING_TOKEN);
            if(findIndex != -1) {
                findIndex += SPELLING_TOKEN.length();
                int endChar = result.indexOf('>', findIndex);
                if(endChar != -1) {
                    findIndex += (endChar - findIndex);
                    endChar = result.indexOf("</a>", findIndex);
                    if(endChar != -1) {
                        StringBuffer revisedSpelling = new StringBuffer();
                        boolean inHtml = false;
                        for(int a = findIndex; a < endChar; a++) {
                            if(result.charAt(a) == '<') {
                                inHtml = true;
                            }
                            if(result.charAt(a) == '>') {
                                inHtml = false;
                                continue;
                            }
                            if(!inHtml) {
                                revisedSpelling.append(result.charAt(a));
                            }
                        }
                        correctSpelling = revisedSpelling.toString();
                    }
                }
            }
            int linkPosition = result.indexOf(TOKEN);
            while(linkPosition != -1) {
                linkPosition += TOKEN.length();
                int linkEndPos1 = result.indexOf('>', linkPosition + 1);
                int linkEndPos2 = result.indexOf(' ', linkPosition + 1);
                if(linkEndPos1 != -1 || linkEndPos2 != -1) {
                    int end = Math.min(linkEndPos1, linkEndPos2);
                    Result result2 = new Result();
                    result2.setLink(result.substring(linkPosition, linkPosition
                        + (end - linkPosition)));
                    int end2 = result.indexOf("</a>", end);
                    if(end2 != -1) {
                        if(end == linkEndPos1) {
                            result2.setLinkInfo(htmlEscape(result.substring(
                                end + 1, end + ((end2 - end)))));
                            searchResults.add(result2);
                            foundResults = true;
                        } else {
                            int end3 = result.indexOf('>', end);
                            if(end3 != -1) {
                                result2.setLinkInfo(htmlEscape(result
                                    .substring(end3 + 1, end3
                                    + ((end2 - end3)))));
                                searchResults.add(result2);
                                foundResults = true;
                            }
                        }
                    }
                }
                linkPosition = result.indexOf(TOKEN, linkPosition);
            }
        }
    }

    /**
     * @param url
     *
     * @return
     */
    public String urlEscape(String url) {
        return url.replaceAll(" ", "+");
    }

    /**
     * @param html
     *
     * @return
     */
    public String htmlEscape(String html) {
        StringBuffer answer = new StringBuffer();
        boolean inTag = false;
        for(int a = 0; a < html.length(); a++) {
            char currentChar = html.charAt(a);
            if(currentChar == '<') {
                inTag = true;
            }
            if(currentChar == '>') {
                inTag = false;
                continue;
            }
            if(!inTag) {
                answer.append(currentChar);
            }
        }
        return answer.toString();
    }

    /**
     * @see java.lang.Object#finalize()
     */
    public void finalize() {
        close();
    }

    /**
     *
     */
    public void resetVars() {
        correctSpelling = null;
        searchResults = new ArrayList<Result>();
        currentIndex = 0;
        foundResults = false;
    }

    /**
     * @return
     */
    public Result getNextResult() {
        if(searchResults.size() > 0) {
            if(currentIndex >= searchResults.size()) {
                currentIndex = 0;
            }
            return (Result)searchResults.get(currentIndex++);
        }
        return null;
    }

    /**
     * @return
     */
    public Result getCurrentResult() {
        return (Result)searchResults.get(currentIndex);
    }

    /**
     * @return
     */
    public int getNumberOfResults() {
        return searchResults.size();
    }

    /**
     *
     */
    public void close() {
        try {
            socket.close();
        } catch(IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static final String noResultsToken(String searchText) {
        return "<br><br>Your search - <b>" + searchText + "</b> - "
            + "did not match any documents.  <br>No pages were "
            + "found containing";
    }
}
