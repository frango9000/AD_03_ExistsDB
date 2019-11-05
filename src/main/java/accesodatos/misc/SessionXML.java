package accesodatos.misc;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

public class SessionXML {


    private static SessionXML sessionXML = null;
    private String driver = "org.exist.xmldb.DatabaseImpl";
    private String user = "admin";
    private String pass = "castelao";

    private SessionXML() {
    }

    public static SessionXML getSessionXML() {
        if (sessionXML == null)
            sessionXML = new SessionXML();
        return sessionXML;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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
