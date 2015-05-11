package io.mikael.rawmime;

import com.google.appengine.api.datastore.*;
import com.google.common.html.HtmlEscapers;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MailBoxServlet extends HttpServlet {
    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        res.setCharacterEncoding("UTF-8");
        final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        final Query query = new Query("Message").addSort("__key__", Query.SortDirection.DESCENDING);
        final List<Entity> messages = ds.prepare(query).asList(FetchOptions.Builder.withLimit(25));
        for (final Entity m : messages) {
            res.getWriter().printf("<a href=\"m?id=%d\">%s</a><br/>%n",
                    m.getKey().getId(),
                    HtmlEscapers.htmlEscaper().escape(m.getProperty("subject").toString()));
        }
    }
}
