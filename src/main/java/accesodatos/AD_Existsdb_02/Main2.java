/*
para facer este exercicio debemos ter una colecion denominada 'cousas' que conteña somente os documentos proba.xml e proba2.xml e empleados.xml

1) inserir una nova <persona>  chamada 'alexia' no documento proba.xml
2) inserir una nova <persona>  chamada 'sara' en todos os documentos da coleción 'cousas'

3) modificar o <APELLIDO> do empregado que ten por <EMP_NO> o valor 7521  para que pase a apelidarse 'BIEITEZ'
4) eliminar o empregado de que ten por <EMP_NO> o valor 7698

5) modificar a <persona> chamada 'pedro'  para que pase a chamarse 'xoan' no documento proba2.xml
6) modificar a <persona> chamada 'luis'  para que pase a chamarse 'xulio' no documento proba.xml
7) modificar a <persona> chamada 'xoan'  para que pase a chamarse 'sara' en todos oo documentos da coleción 'cousas'

8) modificar o <nome>  'ana' de do <p id="2">  para que pase a chamarse 'xulia'  no documento proba.xml

 */
package accesodatos.AD_Existsdb_02;

import accesodatos.AD_Existsdb_01.Main1;
import accesodatos.misc.SessionXML;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

public class Main2 {

    public static void main(String[] args) {
        Main1.main(new String[]{});
        Collection testcol = SessionXML.getSessionXML().getCollection("test");
        XPathQueryService service = null;
        try {
            service = (XPathQueryService) testcol.getService("XPathQueryService", "1.0");

            //1
            String insert1 = "update insert <PERSONA>alexia</PERSONA> into /PLAY/PERSONAE";
            service.queryResource("proba.xml", insert1);

            //2
            service.query(insert1.replace("alexia", "sara"));

            //3
            String update3 = "update value /EMPLEADOS/EMP_ROW[EMP_NO=7521]/APELLIDO with 'BIEITEZ'";
            service.query(update3);

            //4
            String delete4 = "update delete /EMPLEADOS/EMP_ROW[EMP_NO=7698]";
            service.query(delete4);

            //5
            String update5 = "update value /PLAY/PERSONAE/PERSONA[ . ='pedro'] with 'xoan'";
            service.queryResource("proba2.xml", update5);

            //6
            String update6 = "update value /PLAY/PERSONAE/PERSONA[ . ='luis'] with 'xulio'";
            service.queryResource("proba.xml", update6);

            //7
            String update7 = "update value /PLAY/PERSONAE/PERSONA[ . ='xoan'] with 'sara'";
            service.query(update7);
//
//            //8 TODO
//            String update8 = "update value /PLAY/PERSONA";
//            service.queryResource("proba.xml", update8);

        } catch (XMLDBException e) {
            e.printStackTrace();
        }
    }
}
