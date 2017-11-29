/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import struts.LoginAction;
import com.mx.siweb.erp.restful.MobilserviceResource;
import com.mx.siweb.mlm.compensacion.Periodos;
import com.siweb.utilerias.json.JSONObject;
import apiSiweb.ContextoApt.VariableSession;
import apiSiweb.Operaciones.CIP_Tabla;
import apiSiweb.Operaciones.Conexion;
import apiSiweb.Utilerias.Fechas;
import apiSiweb.Utilerias.Mail;
import java.sql.ResultSet;
import java.sql.SQLException;
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
@Path("BxpUsers")
public class BxpUsersResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(BxpUsersResource.class.getName());

    /**
     * Creates a new instance of MobilserviceResource
     */
    public BxpUsersResource() {

    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.MobilserviceResource
     *
     * @param Nombre Es el nombre
     * @param Password Es la ocntraseña del usuario
     * @param Pregunta Es la pregunta del usuario
     * @param Respuesta Es la respuesta del usuario
     * @param Mail Es el mail del usuario
     * @param Telefono Es el telefono del usuario
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String BxpUsers(
            @DefaultValue("") @QueryParam("Name") String Name, 
            @DefaultValue("") @QueryParam("Password") String Password, 
            @DefaultValue("") @QueryParam("Ask") String Ask, 
            @DefaultValue("") @QueryParam("Answer") String Answer, 
            @DefaultValue("") @QueryParam("Mail") String Mail, 
            @DefaultValue("") @QueryParam("Cellphone") String Cellphone) {
        //Objeto json para almacenar los objetos
        JSONObject objJson = new JSONObject();

        //Objeto para validar la seguridad
        LoginAction action = new LoginAction();
        VariableSession varSesiones = new VariableSession(servletRequest);

        try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();
            String strResultado = "Registro exitoso";
            //Validamos si tenemos acceso

            String strResult = "";
            Fechas fecha = new Fechas();
            Periodos periodo = new Periodos();

            CIP_Tabla objTabla = new CIP_Tabla("", "", "", "", varSesiones);
            objTabla.Init("CLIENTES", true, true, false, oConn);
            objTabla.setBolGetAutonumeric(true);

            objTabla.setFieldInt("EMP_ID", 1);
            objTabla.setFieldInt("SC_ID", 12);
            objTabla.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
            objTabla.setFieldString("CT_RAZONSOCIAL", Name);
            objTabla.setFieldString("CT_PASSWORD", Password);
            objTabla.setFieldInt("CT_UPLINE", 3);
            objTabla.setFieldString("CT_EMAIL1", Mail);
            objTabla.setFieldString("CT_EMAIL2", Mail);
            objTabla.setFieldString("CT_TELEFONO2", Cellphone);
            objTabla.setFieldInt("CHIP_EXT", 1);
            objTabla.setFieldString("CHIP_PREGUNTA", Ask);
            objTabla.setFieldString("CHIP_RESPUESTA", Answer);
            objTabla.setFieldInt("CHIP_BXP", 1);

            strResult = objTabla.Agrega(oConn);

            Mail mail = new Mail();
            if (!Mail.isEmpty() || !Mail.isEmpty()) {
                String strLstMail = "";
                //Validamos si el mail del cliente es valido
                if (mail.isEmail(Mail)) {
                    strLstMail += "," + Mail;
                }
                if (mail.isEmail(Mail)) {
                    strLstMail += "," + Mail;
                }

                String strSqlUsuarios = "SELECT EMAIL FROM usuarios WHERE BOL_MAIL_INGRESOS=1";
                try {
                    ResultSet rs = oConn.runQuery(strSqlUsuarios);

                    while (rs.next()) {
                        if (!rs.getString("EMAIL").equals("")) {
                            strLstMail += "," + rs.getString("EMAIL");
                        }
                    }

                    rs.close();
                } catch (SQLException ex) {
                    //this.strResultLast = "ERROR:" + ex.getMessage();
                    ex.fillInStackTrace();
                }
                //Intentamos mandar el mail
                mail.setBolDepuracion(true);
                mail.getTemplate("MSG_ING", oConn);

                mail.getMensaje();
                String strSqlEmp = "SELECT * FROM vta_cliente"
                        + " where CT_ID=" + objTabla.getValorKey() + "";
                try {
                    ResultSet rs1 = oConn.runQuery(strSqlEmp);
                    mail.setReplaceContent(rs1);
                    rs1.close();
                } catch (SQLException ex) {
                    //this.strResultLast = "ERROR:" + ex.getMessage();
                    ex.fillInStackTrace();
                }
                mail.setDestino(strLstMail);
                boolean bol = mail.sendMail();
                if (bol) {
                    //strResp = "MAIL ENVIADO.";
                } else {
                    //strResp = "FALLO EL ENVIO DEL MAIL.";
                }

            } else {
                //strResp = "ERROR: INGRESE UN MAIL";
            }

            objJson.put("Resultado", strResultado);

            oConn.close();
        } catch (Exception ex) {
            this.log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        return objJson.toString();
    }

}
