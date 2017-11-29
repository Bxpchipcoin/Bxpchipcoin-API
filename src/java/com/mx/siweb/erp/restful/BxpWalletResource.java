/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import Tablas.MlmMovComis;
import struts.LoginAction;
import com.mx.siweb.erp.restful.EvalSesion;
import com.mx.siweb.erp.restful.MobilserviceResource;
import com.mx.siweb.mlm.compensacion.chipcoin.AplicaChipCoinsCarteras;
import com.mx.siweb.mlm.compensacion.chipcoin.EtherUtil;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import apiSiweb.ContextoApt.VariableSession;
import apiSiweb.Operaciones.Conexion;
import apiSiweb.Utilerias.Fechas;
import java.math.BigInteger;
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
@Path("BxpWallet")
public class BxpWalletResource {

   @Context
   private UriInfo context;
   @Context
   private HttpServletRequest servletRequest;
   @Context
   private javax.servlet.ServletContext servletContext;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(BxpWalletResource.class.getName());

   /**
    * Creates a new instance of MobilserviceResource
    */
   public BxpWalletResource() {

   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.MobilserviceResource
    *
    * @param cuenta_origen Cuenta origen que enviará la transacción
    * @param cuenta_destino Cuenta destino que recibirá la transacción
    * @param Password Contraseña de la cuenta origen para realizar la transacción
    * @param Monto Monto que se enviará en la transacción
    * @param Codigo Código de sesión
    * @return an instance of java.lang.String
    */
   @GET
   @Produces("application/json")
   public String BxpWallet(
        @DefaultValue("") @QueryParam("cuenta_origen") String strNumCtaOri, 
        @DefaultValue("") @QueryParam("cuenta_destino") String strNumCtaDest, 
        @DefaultValue("") @QueryParam("Password") String strPass, 
        @DefaultValue("") @QueryParam("Monto") String strMonto, 
        @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
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
         if (eval.evaluaSesion(strCodigo, oConn)) {
            varSesiones.SetVars(eval.getIntIdUser(), 0, 0, 0, "", "", 0, "", 0);
            log.debug("Seccion Valida");
            varSesiones.getVars();
            Fechas fecha = new Fechas();

            float fMonto = (float) (Double.parseDouble(strMonto));
            //Query
            //datos de origen
            int intIdOri = 0;
            //datos de destino
            int intIdDest = 0;
            String strDatosCtaOri = "select CW_ID, CW_TOT_CHIPCOINS, "
               + " CW_NOMBRE_CUENTA, CT_ID, CW_PASS "
               + " from mlm_chipcoin_wallet WHERE CW_NUMERO_CUENTA =" + "'" + strNumCtaOri + "'";
            ResultSet rs = oConn.runQuery(strDatosCtaOri, true);
            while (rs.next()) {
               intIdOri = rs.getInt("CT_ID");
            }
            rs.close();
            // Obtenemos datos de la cuenta destino
            String strDatosCtaDest = "select CW_ID, CW_TOT_CHIPCOINS, "
               + " CW_NOMBRE_CUENTA, CT_ID, CW_PASS "
               + " from mlm_chipcoin_wallet WHERE CW_NUMERO_CUENTA = " + "'" + strNumCtaDest + "'";
            rs = oConn.runQuery(strDatosCtaDest, true);
            while (rs.next()) {
               intIdDest = rs.getInt("CT_ID");
            }
            rs.close();
            double dblPorcentaje = 0;
            String strCuenta = "";
            // Obtenemos datos de la cuenta destino
            String strDatos = "select s.SC_CUENTA,s.SC_PORCENTAJE, c.SC_ID "
               + " from vta_sucursal s, vta_cliente c  WHERE  SC_BANDERA ='MX' AND c.SC_ID = s.SC_ID  AND c.CT_ID = " + varSesiones.getintIdCliente() + " LIMIT 0,1";
            rs = oConn.runQuery(strDatos, true);
            while (rs.next()) {
               dblPorcentaje = rs.getDouble("SC_PORCENTAJE");
               strCuenta = rs.getString("SC_CUENTA");
            }
            rs.close();
            int intIdDestP = 0;
            // Obtenemos datos de la cuenta destino
            String strDatosCtaDestP = "select CW_ID, CW_TOT_CHIPCOINS, "
               + " CW_NOMBRE_CUENTA, CT_ID, CW_PASS "
               + " from mlm_chipcoin_wallet WHERE CW_NUMERO_CUENTA = " + "'" + strCuenta + "'";
            rs = oConn.runQuery(strDatosCtaDestP, true);
            while (rs.next()) {
               intIdDestP = rs.getInt("CT_ID");
            }
            rs.close();

            //Validamos si fue exitoso la transaccion por ethereum
            //Sincronizamos con la red privada Ethereum - ChipCoin
            AplicaChipCoinsCarteras aplica = new AplicaChipCoinsCarteras(oConn);
            boolean bolSincroniza = false;

            String strNumTransaccion = "";
            String strErrorJson = "";
            //Ejecutamos instrucciones RPC
            System.out.println(   strNumCtaOri +  "\" , \"" + strPass + "\", 10 ],\"id\":" + intIdOri + "}");
            String strResp1 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strNumCtaOri + "\", \"" + strPass + "\", 10 ],\"id\":" + intIdOri + "}");
            System.out.println("strResp1:" + strResp1);
            JSONObject jsonObj1 = new JSONObject(strResp1);
            boolean bolResult = false;
            try {
               bolResult = jsonObj1.getBoolean("result");
            } catch (JSONException ex) {
               strErrorJson = jsonObj1.getJSONObject("error").toString();
            }
            System.out.println("result:" + bolResult);

            if (bolResult) {
               //Conversion del monto a wei
               BigInteger bigNum = EtherUtil.convert((long) fMonto, EtherUtil.Unit.ETHER);
               String strResp2 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                  + "  \"from\": \"0x" + strNumCtaOri + "\",\n"
                  + "  \"to\": \"0x" + strNumCtaDest + "\",\n"
                  + "  \"gas\": \"0x76c0\", \n"
                  + "  \"gasPrice\": \"0x9184e72a000\", \n"
                  + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                  + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                  + "}],\"id\":1}");
               JSONObject jsonObj2 = new JSONObject(strResp2);
               System.out.println("strResp2:" + strResp2);
               strNumTransaccion = jsonObj2.getString("result");
               System.out.println("result:" + strNumTransaccion);
               if (!strNumTransaccion.isEmpty()) {
                  bolSincroniza = true;
               }

               //Conversion del monto a wei
               if (!strCuenta.isEmpty() && dblPorcentaje > 0) {
                  double dblValorPorcentaje = (dblPorcentaje) / 100;
                  double dblTotP = (dblValorPorcentaje * fMonto);

                  boolean bolSincronizaa = false;
                  BigInteger bigNum2 = EtherUtil.convert((long) dblTotP, EtherUtil.Unit.ETHER);
                  String strResp4 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                     + "  \"from\": \"0x" + strNumCtaOri + "\",\n"
                     + "  \"to\": \"0x" + strCuenta + "\",\n"
                     + "  \"gas\": \"0x76c0\", \n"
                     + "  \"gasPrice\": \"0x9184e72a000\", \n"
                     + "  \"value\": \"0x" + bigNum2.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                     + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                     + "}],\"id\":1}");
                  JSONObject jsonObj4 = new JSONObject(strResp4);
                  System.out.println("strResp2:" + strResp4);
                  strNumTransaccion = jsonObj4.getString("result");
                  System.out.println("result:" + strNumTransaccion);
                  if (!strNumTransaccion.isEmpty()) {
                     bolSincronizaa = true;
                  }
                  if (bolSincronizaa) {

                     //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                     String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                        + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblTotP + " where CW_NUMERO_CUENTA = " + "'" + strNumCtaOri + "'";
                     oConn.runQueryLMD(strSqlUdtR);
                     String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                        + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblTotP + " where CW_NUMERO_CUENTA = " + "'" + strCuenta + "'";
                     oConn.runQueryLMD(strSqlUdtRD);

                     String strSqlUdtRC = "UPDATE vta_cliente "
                        + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS -" + dblTotP + " where CT_ID = " + "'" + intIdOri + "'";
                     oConn.runQueryLMD(strSqlUdtRC);
                     String strSqlUdtRDC = "UPDATE vta_cliente "
                        + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS + " + dblTotP + " where CT_ID = " + "'" + intIdDestP + "'";
                     oConn.runQueryLMD(strSqlUdtRDC);

                     //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                     MlmMovComis movComi = new MlmMovComis();
                     movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                     movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                     movComi.setFieldInt("CT_ID", intIdOri);
                     movComi.setFieldInt("MMC_SINCR", 1);
                     movComi.setFieldInt("MMC_BXP", 1);
                     movComi.setFieldDouble("MMC_CARGO", dblTotP);
                     movComi.setFieldDouble("MMC_ABONO", 0.0);
                     movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                     movComi.setFieldString("MMC_NUMERO_CUENTA", strNumCtaOri);
                     movComi.Agrega(oConn);
                     //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                     movComi = new MlmMovComis();
                     movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                     movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                     movComi.setFieldInt("CT_ID", intIdDestP);
                     movComi.setFieldInt("MMC_SINCR", 1);
                     movComi.setFieldInt("MMC_BXP", 1);
                     movComi.setFieldDouble("MMC_CARGO", 0.0);
                     movComi.setFieldDouble("MMC_ABONO", dblTotP);
                     movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                     movComi.setFieldString("MMC_NUMERO_CUENTA", strCuenta);
                     movComi.Agrega(oConn);
                  } else {
                     strResultado = "ERROR:Al sincronizar con el blockchain " + strResp2;
                  }
               }

               //Marcamos que ya se sincronizo
               if (bolSincroniza) {
                  //Guardamos en la base                            
                  //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                  String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                     + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + fMonto + " where CW_NUMERO_CUENTA = " + "'" + strNumCtaOri + "'";
                  oConn.runQueryLMD(strSqlUdtR);
                  String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                     + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + fMonto + " where CW_NUMERO_CUENTA = " + "'" + strNumCtaDest + "'";
                  oConn.runQueryLMD(strSqlUdtRD);

                  String strSqlUdtRC = "UPDATE vta_cliente "
                     + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + fMonto + " where CT_ID = " + "'" + intIdOri + "'";
                  oConn.runQueryLMD(strSqlUdtRC);
                  String strSqlUdtRDC = "UPDATE vta_cliente "
                     + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + fMonto + " where CT_ID = " + "'" + intIdDest + "'";
                  oConn.runQueryLMD(strSqlUdtRDC);
                  //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                  MlmMovComis movComi = new MlmMovComis();
                  movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                  movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                  movComi.setFieldInt("CT_ID", intIdOri);
                  movComi.setFieldInt("MMC_SINCR", 1);
                  movComi.setFieldInt("MMC_BXP", 1);
                  movComi.setFieldDouble("MMC_CARGO", fMonto);
                  movComi.setFieldDouble("MMC_ABONO", 0.0);
                  movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                  movComi.setFieldString("MMC_NUMERO_CUENTA", strNumCtaOri);
                  movComi.Agrega(oConn);
                  //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                  movComi = new MlmMovComis();
                  movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                  movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                  movComi.setFieldInt("CT_ID", intIdDest);
                  movComi.setFieldInt("MMC_SINCR", 1);
                  movComi.setFieldInt("MMC_BXP", 1);
                  movComi.setFieldDouble("MMC_CARGO", 0.0);
                  movComi.setFieldDouble("MMC_ABONO", fMonto);
                  movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                  movComi.setFieldString("MMC_NUMERO_CUENTA", strNumCtaDest);
                  movComi.Agrega(oConn);

               } else {
                  strResultado = "ERROR:Al sincronizar con el blockchain " + strResp2;
               }
            } else if (!strErrorJson.isEmpty()) {
               strResultado = "ERROR:" + strErrorJson + " favor de verificar el password de la cuenta";
            } else {
               strResultado = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
            }

            objJson.put("Resultado", strResultado);
         }
         oConn.close();
      } catch (Exception ex) {
         log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
      }
      return objJson.toString();
   }

}