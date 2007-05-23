package javabot;

/**
 * @author ricky_clarkson
 */
public class Result {
    private String link;
    private String linkInfo;

    /**
     * @return
     */
    public String getLink() {
        return link;
    }

    /**
     * @return
     */
    public String getLinkInfo() {
        return linkInfo;
    }

    /**
     * @param string
     */
    public void setLink(String string) {
        link = string;
    }

    /**
     * @param string
     */
    public void setLinkInfo(String string) {
        linkInfo = string;
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return link + " (" + linkInfo + ")";
    }
}


