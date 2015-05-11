package io.mikael.rawmime;

import com.google.appengine.api.datastore.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class MessageServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(MessageServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain");
        final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        try {
            final Entity msg = ds.get(KeyFactory.createKey("Message", Long.parseLong(req.getParameter("id"))));
            final Text data = (Text) msg.getProperty("data");
            res.getWriter().print(data.getValue());
        } catch (NumberFormatException | EntityNotFoundException e) {
            log.severe(e.toString());
        }
    }
}
