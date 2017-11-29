/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import Core.Opalina;
import struts.LoginAction;
import com.mx.siweb.mlm.compensacion.chipcoin.ExecuteShellComand;
import com.mx.siweb.mlm.compensacion.chipcoin.MlmChipcoinWallet;
import com.siweb.utilerias.json.JSONObject;
import apiSiweb.ContextoApt.VariableSession;
import apiSiweb.Operaciones.Conexion;
import apiSiweb.Utilerias.Fechas;
import apiSiweb.Utilerias.UtilXml;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
@Path("BxpAccount")
public class BxpAccountResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(BxpAccountResource.class.getName());

    /**
     * Creates a new instance of MobilserviceResource
     */
    public BxpAccountResource() {

    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.MobilserviceResource
     *
     * @param nombreCta Nombre de la cartera a agregar
     * @param Password Contraseña de la cartera
     * @param Pregunta Id de la pregunta de seguridad
     * @param Respuesta Respuesta de la pregunta de seguridad
     * @param Codigo Código de sesión
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String BxpAccount(
            @DefaultValue("") @QueryParam("AccountName") String AccountName,
            @DefaultValue("") @QueryParam("Password") String Password, 
            @DefaultValue("") @QueryParam("Ask") String Ask, 
            @DefaultValue("") @QueryParam("Answer") String Answer, 
            @DefaultValue("") @QueryParam("Code") String Code) {
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
            EvalSesion eval = new EvalSesion();
            log.debug("Validando secion");
            if (eval.evaluaSesion(Code, oConn)) {
                varSesiones.SetVars(eval.getIntIdUser(), 0, 0, 0, "", "", 0, "", 0);
                log.debug("Seccion Valida");
                varSesiones.getVars();  
                String FILENAME = "c:/SIWEB/tmp/chipcoin/ethereum_" + varSesiones.getIntNoUser() + ".txt";
                BufferedWriter bw = null;
                FileWriter fw = null;
                try {
                    String content = Password + "\n";
                    fw = new FileWriter(FILENAME);
                    bw = new BufferedWriter(fw);
                    bw.write(content);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                        if (fw != null) {
                            fw.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
                //Shell a ejecutar
                ExecuteShellComand obj = new ExecuteShellComand();
                String command = "\"C:/Program Files/Geth/geth\" --datadir \"C:/SIWEB/EthereumChipCoin/Data\" --password " + FILENAME + " account new";
                String output = obj.executeCommand(command);
                String strNumCta = "";
                String strResult = "";
                Fechas fecha = new Fechas();
                UtilXml util = new UtilXml();
                if (output.contains("Address:")) {
                    strNumCta = output.replace("Address: {", "").replace("}", "");
                    //Encriptamos el password
                    String strPassB64 = servletContext.getInitParameter("SecretWord");
                    Opalina opa = new Opalina();
                    String strMyPass = opa.Encripta(Password, strPassB64);
                    //Validamos si ya existe a referencia strAutorizado
                    MlmChipcoinWallet objTabla = new MlmChipcoinWallet();
                    objTabla.setFieldString("CW_NOMBRE_CUENTA", AccountName);
                    objTabla.setFieldString("CW_NUMERO_CUENTA", strNumCta.trim());
                    objTabla.setFieldString("CW_FECHA_REG", fecha.getFechaActual());
                    objTabla.setFieldString("CW_HORA_REG", fecha.getHoraActual());
                    objTabla.setFieldInt("CT_ID", varSesiones.getIntNoUser());
                    objTabla.setFieldInt("CW_BXP", 1);
                    objTabla.setFieldString("CW_PASS", strMyPass);
                    strResult = objTabla.Agrega(oConn);

                    //Borramos el archivo
                    File fBorrar = new File(FILENAME);
                    if (fBorrar.exists()) {
                        fBorrar.delete();
                    }
                } else {
                    strResult = "Error: No se puedo generar la cuenta";
                }
                    objJson.put("Resultado",strResultado);      
            }
            oConn.close();
        } catch (Exception ex) {
           log.error("BXPaAccount " + ex.getMessage() + " " + ex.getLocalizedMessage());
           ex.printStackTrace();
        }
        return objJson.toString();
    }

   
}