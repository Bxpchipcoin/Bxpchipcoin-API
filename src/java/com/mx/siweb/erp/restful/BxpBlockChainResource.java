/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import struts.LoginAction;
import com.mx.siweb.erp.restful.MobilserviceResource;
import com.mx.siweb.erp.restful.EvalSesion;
import com.siweb.utilerias.json.JSONArray;
import com.siweb.utilerias.json.JSONObject;
import apiSiweb.ContextoApt.VariableSession;
import apiSiweb.Operaciones.Conexion;
import apiSiweb.Utilerias.Fechas;
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
@Path("BxpBlockChain")
public class BxpBlockChainResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(BxpBlockChainResource.class.getName());

    /**
     * Creates a new instance of MobilserviceResource
     */
    public BxpBlockChainResource() {

    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.MobilserviceResource
     *
    * @param strCodigo Es el codigo de la sesion
    * @param strCliente Es el id del cliente
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String BxpBlockChain(
            @DefaultValue("") @QueryParam("code") String strCodigo,
            @DefaultValue("") @QueryParam("User") String strCliente) {
        //Objeto json para almacenar los objetos
        JSONArray jsonChild = new JSONArray();

        //Objeto para validar la seguridad
        LoginAction action = new LoginAction();
        Fechas fecha = new Fechas();
        VariableSession varSesiones = new VariableSession(servletRequest);

        try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();

            

            //Validamos si tenemos acceso
            EvalSesion eval = new EvalSesion();
            log.debug("Validando sesion");
            log.debug("strCodigo"+strCodigo);
            if (eval.evaluaSesion(strCodigo, oConn)) {
                varSesiones.SetVars(eval.getIntIdUser(), 0, 0, 0, "", "", 0, "", 0);
                log.debug("Seccion Valida");
                varSesiones.getVars();
                
                 //Consulta
                String strSqlRM = "SELECT MMC_FECHA, MMC_ABONO, MMC_CARGO, "
                   + "MMC_NOTAS,CT_ID, MMC_TRANSACCION, MMC_NUMERO_CUENTA,"
                   + "(SELECT w.CW_NOMBRE_CUENTA FROM mlm_chipcoin_wallet w where w.CW_NUMERO_CUENTA = MMC_NUMERO_CUENTA ) as NOMBRE_CUENTA  FROM mlm_mov_comis"
                   + " where MMC_BXP = 1 AND (MMC_ABONO> 0 OR MMC_CARGO>0) AND CT_ID = " + strCliente;
                ResultSet rs = oConn.runQuery(strSqlRM, true);
                while (rs.next()) {
                    System.out.println("TRANSACCION "+rs.getString("MMC_TRANSACCION"));
                    JSONObject objJsonCte = new JSONObject();
                    objJsonCte.put("Fecha", fecha.FormateaDDMMAAAA(rs.getString("MMC_FECHA"), "/") );
                    objJsonCte.put("Cuenta", rs.getString("MMC_NUMERO_CUENTA"));
                    if(rs.getString("NOMBRE_CUENTA") != null){
                       objJsonCte.put("NombreCuenta", rs.getString("NOMBRE_CUENTA"));
                    }else{
                       objJsonCte.put("NombreCuenta", "");
                    }
                    objJsonCte.put("Abono", rs.getDouble("MMC_CARGO"));
                    objJsonCte.put("Cargo", rs.getDouble("MMC_ABONO"));
                    objJsonCte.put("Transaccion", rs.getString("MMC_TRANSACCION"));
                    objJsonCte.put("Destino", rs.getInt("CT_ID"));
                    objJsonCte.put("Impuesto", 0);
                    objJsonCte.put("Paridad", 0);
                    
                    jsonChild.put(objJsonCte);
                }
                log.debug("strSqlRM"+strSqlRM);
                rs.close();
                
            }
            oConn.close();
        } catch (Exception ex) {
            log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        return jsonChild.toString();
    }

}