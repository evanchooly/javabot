package javabot.dao;

import javabot.dao.model.factoids;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2002-2007 The OpenNMS Group, Inc. All rights
// reserved.
// OpenNMS(R) is a derivative work, containing both original code, included
// code and modified
// code that was published under the GNU General Public License. Copyrights
// for modified
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Modifications:
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//       
// For more information contact:
// OpenNMS Licensing <license@opennms.org>
// http://www.opennms.org/
// http://www.opennms.com/
//
// Author: joed

// Date  : Apr 12, 2007
public class HtmlRoutines {

    private static final Log log = LogFactory.getLog(HtmlRoutines.class);


    public void dumpHTML(List<factoids> factoids,String htmlFile) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(htmlFile));
            writer.println("<html><body><table border=\"1\"><tr><th>id</th><th>factoid</th><th>value</th><th>user</th></tr>\n");
            for (factoids factoid : factoids) {
                StringBuilder html = new StringBuilder("<tr>");
                appendCell(html, Long.toString(factoid.getId()));
                appendCell(html, factoid.getName());
                appendCell(html, htmlize(factoid.getValue()));
                appendCell(html, factoid.getUserName());
                html.append("</tr>\n");
                writer.println(html);
            }
            writer.println("</table></body></html>");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void appendCell(StringBuilder html, String value) {
        html.append("<td>");
        html.append(value);
        html.append("</td>");
    }

    protected String htmlize(String value) {
        String newValue = value.replaceAll("<", "&lt;");
        newValue = newValue.replaceAll(">", "&gt;");
        int startHttp = newValue.indexOf("http://");
        while (startHttp != -1) {
            StringBuilder builder = new StringBuilder(newValue.substring(0, startHttp));
            int endHttp = newValue.indexOf(" ", startHttp);
            if (endHttp == -1) {
                endHttp = newValue.length();
            }
            String link = newValue.substring(startHttp, endHttp);
            builder.append("<a href=\"");
            builder.append(link);
            builder.append("\" target=\"_blank\">");
            builder.append(link);
            builder.append("</a>");
            builder.append(newValue.substring(endHttp));
            newValue = builder.toString();
            startHttp = newValue.indexOf("http://", link.length() + startHttp + 30);
        }
        return newValue;
    }

}
