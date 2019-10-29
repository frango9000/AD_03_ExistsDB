package accesodatos.misc;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

public class SessionXML {


    private String driver = "org.exist.xmldb.DatabaseImpl";
    private String user = "admin";
    private String pass = "castelao";

    private static SessionXML sessionXML = null;

    private SessionXML() {
    }

    public static SessionXML getSessionXML() {
        if (sessionXML == null)
            sessionXML = new SessionXML();
        return sessionXML;
    }

    public Collection getCollection(String colectionName) {
        Class<?> cl = null;
        Collection col = null;
        try {
            cl = Class.forName(driver);

            Database database = (Database) cl.newInstance();
            DatabaseManager.registerDatabase(database);
            String coleccion = "/db" + "/" + colectionName;
            String uri = "xmldb:exist://10.0.9.146:8080/exist/xmlrpc";
            col = DatabaseManager.getCollection(uri + coleccion, user, pass);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | XMLDBException e) {
            e.printStackTrace();
        }
        return col;
    }
}
