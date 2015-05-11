package io.mikael.rawmime;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
import com.google.common.io.ByteStreams;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

public class ReceiveServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(ReceiveServlet.class.getName());

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        try {
            final Properties props = new Properties();
            final Session session = Session.getDefaultInstance(props, null);
            final byte[] bytes = ByteStreams.toByteArray(req.getInputStream());
            final String data = new String(bytes, StandardCharsets.ISO_8859_1);
            final MimeMessage message = new MimeMessage(session, new ByteArrayInputStream(bytes));
            final Entity ent = new Entity("Message");
            ent.setProperty("subject", message.getSubject());
            if (message.getFrom().length > 0) {
                ent.setProperty("sender", message.getFrom()[0].toString());
            }
            ent.setProperty("receivedAt", new Date());
            ent.setProperty("data", new Text(data));
            ds.put(ent);
            log.warning(data);
        } catch (Exception e) {
            log.warning(e.toString());
        }
    }

}
