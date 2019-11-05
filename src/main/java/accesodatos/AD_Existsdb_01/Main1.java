/*
Proxecto java conectandose a eXists en modo remoto (por defecto)

un recurso e un documento xml  ou un binario
unha colecion e un conxunto de colecions e recursos ( unha coleccion ven a ser o equivalente a  unha carpeta)

eXists organiza os documentos XML ( ou tamen binarios )en colecions dentro da base de datos situados a partir da raiz /db


podes usar a aplicacion “open java admin client” ( do menu de eXists ) para ver as colecions existentes ( sen contrasinal por defecto ) . inicialmente so verás as coleccions "system" e "apps" que non debes tocar . podes crear outras colecions e subir documentos XML a ditas colecions para facer probas .

Despois deberás facer o mesmo dende java :


1) crear unha aplicacion
2) instalar as librerias como se indicou na instalacion
3) conectarse a eXists  :
dentro da clase :

public static String driver = "org.exist.xmldb.DatabaseImpl";

dentro do main
Class<?> cl = Class.forName(driver);
Database database = (Database)cl.newInstance();
DatabaseManager.registerDatabase(database);
String coleccion="/db";
Collection col;
String uri="xmldb:exist://localhost:8080/exist/xmlrpc";
col = DatabaseManager.getCollection(uri+coleccion,"admin”,"");  (observese que non hai contrasinal , se ha houbese haberia que pola (normalmente ‘admin’ ou 'oracle')



4)listar colecions (listChildCollections()),
listar recursos dunha coleción (listResources() ),
crear coleción :
	CollectionManagementService mgtService= (CollectionManagementService) col.getService("CollectionManagementService","1.0");
	mgtService.createCollection("nome da colecion a crear");

borrar colecions :
	CollectionManagementService mgtService= (CollectionManagementService) col.getService("CollectionManagementService","1.0");
	col.removeCollection("NOVA_COLECCION");

subir  recurso(documento) a unha colecion: createResource  nota : subir os documentos empleado.xm, proba.xml e proba2.xml na colecion cousas

    File arquivo=new File ("ruta absoluta do arquivo a subir/nomedoarquivo.extension");
    	col.createResource(arquivo.getName(),"XMLResource");
	novoRecurso.setContent(arquivo);
	col.storeResource(novoRecurso);

borrar recurso : removeResource

	Resource recursoaborrar = col.getResource("nomedorecurso.extension");
	col.removeResource(recursoaborrar);


5)amosar contido dos recurso dunha colecion :  (sendo -col- a coleción )
servicio= (XPathQueryService) col.getService("XPathQueryService", "1.0");
seleciona a partir de que nodo onde queremos que busque: e unha consulta de tipo xPath como por exemplo :
 resultado= servicio.query(“/EMPLEADOS/EMP_ROW[DEPT_NO=10]");
(Probar despois de que funcione con esta nova consulta : resultado= servicio.query( "/PLAY/fm"  para ver a diferenza co seguinte apartado)
construir un iterador para recorrer os contidos dos recursos devoltos:
 i = resultado.getIterator();
recorrer o iterador :
	preguntar por fin de recursos:  i.hasMoreResources()
 	ler cada item dos recursos: r=  i.nextResource();
	imprimir items lidos: (String) r.getContent();


5’) amosar contido dun recurso dunha colecion :  (sendo -col- a coleción  e proba.xml o recurso)

        XPathQueryService servicio = (XPathQueryService)    col.getService("XPathQueryService", "1.0");
        ResourceSet resource = servicio.queryResource("proba.xml","/PLAY/fm");
        ResourceIterator i = resource.getIterator();
        Resource r = i.nextResource();
        System.out.println((String) r.getContent());


OPERACIONS de ACTUALIZACION DUN so  DOCUMENTO (recurso) dunha colecion


6)inserir elemento dentro do documento (recurso)  dunha colecion

por exemplo inserir unha informacion no documento empleados.xml da colecion cousas (para elo debes crear previmente a colecion cousas )

XPathQueryService servicio= (XPathQueryService) col.getService("XPathQueryService", "1.0");
String cons="update insert <zona><cod_zona>50</cod_zona><nombre>Madrid-Oeste</nombre><director>Alicia Ramos Martin</director></zona> into /EMPLEADOS/ZONAS";
result= servicio.queryResource("empleados.xml",cons);


7)actualizar elemento dentro do documento (recurso)  dunha colecion (sendo col a colecion)

por exemplo modificar  unha informacion no documento empleados.xml da colecion cousas

   XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
   String cons="update value /EMPLEADOS/EMP_ROW[EMP_NO=7369]/APELLIDO with 'RODROGUEZ'";
   result= servicio.queryResource("empleados.xml",cons);


8) borrar elemento dentro do documento (recurso)  dunha colecion

por exemplo eliminar unha informacion no documento empleados.xml da colecion cousas

XPathQueryService servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
cons ="update delete /empleados/zonas/zona[cod_zona=50]";
result= servicio.queryResource("empleados.xml",cons);


OPERACIONS de ACTUALIZACION de todos os documentos (recursos) dunahcolecion

9)inserir elemento dentro dos documento (recursos)  da colecion

por exemplo inserir unha informacion nos documentos  da colecion cousas

XPathQueryService servicio= (XPathQueryService) col.getService("XPathQueryService", "1.0");
String cons="update insert <zona><cod_zona>50</cod_zona><nombre>Madrid-Oeste</nombre><director>Alicia Ramos Martin</director></zona> into /empleados/zonas";
result= servicio.query(cons);

outra insercion na colecion cousas:
XPathQueryService servicio= (XPathQueryService) col.getService("XPathQueryService", "1.0");
String cons="update insert <autor><cod_autor>1</cod_autor><nombre>luis</nombre><edad>30</edad></autor> into /PLAY";
result= servicio.query(cons);





10)actualizar elemento dentro dos documentos (recursos)  da colecion

por exemplo actualizar  unha informacion nos documentos  da colecion cousas

XPathQueryService servicio= (XPathQueryService) col.getService("XPathQueryService", "1.0");
String cons="update value /EMPLEADOS/EMP_ROW[EMP_NO=7369]/APELLIDO with 'RODROGUEZ'"
result= servicio.query(cons);

11) borrar elemento dentro doa documentos (recursos)  da colecion

por exemplo eliminar  unha informacion nos documentos  da colecion cousas

XPathQueryService servicio= (XPathQueryService) col.getService("XPathQueryService", "1.0");
String cons ="update delete /empleados/zonas/zona[cod_zona=50]"; result3= servicio.query(cons3);
result= servicio.query(cons);

 */
package accesodatos.AD_Existsdb_01;

import accesodatos.misc.SessionXML;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XPathQueryService;

/**
 * Hello world!
 */
public class Main1 {

    public static void main(String[] args) {
        SessionXML sessionXML = SessionXML.getSessionXML();
        try {
            //3 Conectarse a exists (root col)
            Collection col = sessionXML.getCollection("");

            CollectionManagementService mgtService = (CollectionManagementService) col.getService("CollectionManagementService", "1.0");

            //pruba crear y eliminar collection
            mgtService.createCollection("testtodelete");
            mgtService.removeCollection("testtodelete");

            //limpiamos la collection test para recrearla y usarla
            mgtService.removeCollection("test");

            //4 listamos las collections existentes
            String[] cols = col.listChildCollections();
            System.out.println(Arrays.toString(cols));

            //creamos la collection q vamos a utilizar si no existe
            String newCollection = "test";
            if (!Arrays.asList(cols).contains(newCollection)) {
                mgtService.createCollection("test");
                System.out.println("Collection " + newCollection + " created.");
            } else
                System.out.println("Collection " + newCollection + "already exists.");

            //verificamos que la collection se creo correctamente
            cols = col.listChildCollections();
            System.out.println(Arrays.toString(cols));

            //conexion a colection test
            Collection testcol = sessionXML.getCollection("test");

            File empleadosxml = new File(Main1.class.getResource("/xml/empleados.xml").toURI());
            File proba1xml = new File(Main1.class.getResource("/xml/proba.xml").toURI());
            File proba2xml = new File(Main1.class.getResource("/xml/proba2.xml").toURI());

            //recurso correspondiente a empleados.xml
            Resource empleadosx = testcol.createResource(empleadosxml.getName(), "XMLResource");
            empleadosx.setContent(empleadosxml);//establecemos el contenidp
            testcol.storeResource(empleadosx);//lo almacenamos en la db

            //listamos los recursos para verificar el recurso añadido antes
            cols = testcol.listResources();
            System.out.println(Arrays.toString(cols));

            //pruba de creacion y eliminacion de un recurso proba.xml
            Resource proba0x = testcol.createResource(proba1xml.getName(), "XMLResource");
            proba0x.setContent(proba1xml);
            testcol.storeResource(proba0x);
            //verificamos que se añadio correctamente
            cols = testcol.listResources();
            System.out.println(Arrays.toString(cols));
            //buscamos el recurso en la db y lo eliminamos
            Resource aborrar = testcol.getResource(proba1xml.getName());
            testcol.removeResource(aborrar);

            //creacion de recurso proba2.xml
            Resource proba2x = testcol.createResource(proba2xml.getName(), "XMLResource");
            proba2x.setContent(proba2xml);
            testcol.storeResource(proba2x);

            //nuevamente verificamos que las acciones previas se realizaron correctamente
            cols = testcol.listResources();
            System.out.println(Arrays.toString(cols));

            //reinsertamos el ultimo recurso eliminado
            testcol.storeResource(proba0x);

            //mostrar el contenido de los recursos de la collection filtrando con un query
            XPathQueryService service = (XPathQueryService) testcol.getService("XPathQueryService", "1.0");
            ResourceSet rs = service.query("/EMPLEADOS/EMP_ROW[DEPT_NO=10]");

            //imprimimos el contenido del resultado del query anterior
            ResourceIterator iterator = rs.getIterator();
            while (iterator.hasMoreResources()) {
                Resource resource = iterator.nextResource();
                System.out.println((String) resource.getContent());
            }

            //5 query de un recurso determinado
            ResourceSet rs2 = service.queryResource("proba.xml", "/PLAY/fm");
            ResourceIterator iterator2 = rs2.getIterator();
            while (iterator2.hasMoreResources()) {
                Resource resource = iterator2.nextResource();
                System.out.println((String) resource.getContent());
            }

            //6
            String insert = "update insert <zona><cod_zona>50</cod_zona><nombre>Madrid-Oeste</nombre><director>Alicia Ramos Martin</director></zona> into /EMPLEADOS/ZONAS";
            service.queryResource("empleados.xml", insert);

            //7
            String update = "update value /EMPLEADOS/EMP_ROW[EMP_NO=7369]/APELLIDO with 'RODROGUEZ'";
            service.queryResource("empleados.xml", update);

            //8
            String delete = "update delete /empleados/zonas/zona[cod_zona=50]";
            service.queryResource("empleados.xml", delete);

            //9
            service.query(insert);

            //9
            String insert2 = "update insert <autor><cod_autor>1</cod_autor><nombre>luis</nombre><edad>30</edad></autor> into /PLAY";
            service.query(insert2);

            //10
            String update2 = "update value /EMPLEADOS/EMP_ROW[EMP_NO=7369]/APELLIDO with 'RODROGUEZ'";
            service.query(update2);

            //11
            String delete2 = "update delete /empleados/zonas/zona[cod_zona=50]";
            service.query(delete2);
        } catch (XMLDBException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
