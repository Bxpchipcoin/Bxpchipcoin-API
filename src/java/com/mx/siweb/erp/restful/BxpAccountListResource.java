/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import struts.LoginAction;
import com.mx.siweb.erp.restful.EvalSesion;
import com.mx.siweb.erp.restful.MobilserviceResource;
import com.siweb.utilerias.json.JSONArray;
import com.siweb.utilerias.json.JSONObject;
import apiSiweb.ContextoApt.VariableSession;
import apiSiweb.Operaciones.Conexion;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.apache.logging.log4j.LogManager;

/**
 * REST Web Service
 *
 * @author Siweb
 */
@Path("BxpAccountList")
public class BxpAccountListResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(BxpAccountListResource.class.getName());

    /**
     * Creates a new instance of MobilserviceResource
     */
    public BxpAccountListResource() {

    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.MobilserviceResource
     *
     * @param idCliente Id del cliente al cuál pertenecen las  carteras
     * @param Codigo Código de sesión
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String BxpAccountList(
            @DefaultValue("") @QueryParam("UserId") String UserId, 
            @DefaultValue("") @QueryParam("Code") String Code) {
        //Objeto json para almacenar los objetos
        JSONArray jsonChild = new JSONArray();

        //Objeto para validar la seguridad
        LoginAction action = new LoginAction();
        VariableSession varSesiones = new VariableSession(servletRequest);

        try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();

            String strqr = "xhtjyju";
            //Validamos si tenemos acceso
            EvalSesion eval = new EvalSesion();
            log.debug("Validando secion");
            if (eval.evaluaSesion(Code, oConn)) {
                varSesiones.SetVars(eval.getIntIdUser(), 0, 0, 0, "", "", 0, "", 0);
                log.debug("Seccion Valida");
                varSesiones.getVars();

                String consulta = "SELECT * FROM mlm_chipcoin_wallet where CW_BXP = 1 AND CT_ID = " + varSesiones.getIntNoUser();
                ResultSet rs = oConn.runQuery(consulta);
                while (rs.next()) {
                    JSONObject objJsonCte = new JSONObject();
                    objJsonCte.put("Id", varSesiones.getIntNoUser());
                    objJsonCte.put("Cuenta", rs.getString("CW_NUMERO_CUENTA"));
                    objJsonCte.put("Nombre", rs.getString("CW_NOMBRE_CUENTA"));
                    objJsonCte.put("Saldo", rs.getString("CW_TOT_CHIPCOINS"));
                    objJsonCte.put("Qr", strqr);

                    jsonChild.put(objJsonCte);

                }
                rs.close();

            }
            oConn.close();
        } catch (Exception ex) {
            log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        return jsonChild.toString();
    }

}
