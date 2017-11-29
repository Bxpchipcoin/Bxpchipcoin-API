/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import Tablas.MlmMovComis;
import struts.LoginAction;
import com.mx.siweb.erp.restful.MobilserviceResource;
import com.mx.siweb.mlm.compensacion.Periodos;
import com.mx.siweb.mlm.compensacion.chipcoin.AplicaChipCoinsCarteras;
import com.mx.siweb.mlm.compensacion.chipcoin.EtherUtil;
import com.mx.siweb.mlm.compensacion.chipcoin.MlmTrading;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import apiSiweb.ContextoApt.VariableSession;
import apiSiweb.Operaciones.Conexion;
import apiSiweb.Utilerias.Fechas;
import java.math.BigInteger;
import java.net.InetAddress;
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
@Path("BxpTrading")
public class BxpTradingResource {

   @Context
   private UriInfo context;
   @Context
   private HttpServletRequest servletRequest;
   @Context
   private javax.servlet.ServletContext servletContext;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(BxpTradingResource.class.getName());

   /**
    * Creates a new instance of MobilserviceResource
    */
   public BxpTradingResource() {

   }

   /**
    * Retrieves representation of an instance of
    * com.mx.siweb.erp.restful.MobilserviceResource
    *
    * @param TypeMoneda Tipo de moneda que realizará el trading
    * @param IdCte Id del cliente que realizará trading
    * @param Criptomoneda Moneda por la que se realizará el cambio en trading
    * @param Paridad Paridad entre las monedas a hacer trading
    * @param Monto Monto con el que se realizará trading
    * @param cuenta_origen Cuenta que enviará el monto en caso de hacer cambio por chipcoins
    * @param cuenta_destino Cuenta que recibira el monto en caso de hacer cambio por chipcoins
    * @param Password_or Contraseña d la cuenta origen
    * @param Importe Importe total que se cobrará restando la comisión por trading
    * @param IdOri Id de la cuenta origen que enviará en monto de trading
    * @param IdDestP Id de la cuenta destino que recibirá el monto de trading
    * @param CuentaDestinoComision Cuenta destino que recibirá la comisión por trading
    * @param IdDestinoComision Id de la cuenta destino que recibira la comisión   por trading
    * @param CuentaOrigenComision Cuenta origen que enviará la comisión por trading
    * @param intIdOriComision Id de la cuenta origen que enviará la comisión por trading 
    * @param MyPassSecretComision Contraseña d ela cuenta origen que enviará la comisión por trading
    * @param Codigo Código de sesión
    * @return an instance of java.lang.String
    */
   @GET
   @Produces("application/json")
   public String BxpTrading(
        @DefaultValue("") @QueryParam("TypeCoin") String TypeCoin,
        @DefaultValue("") @QueryParam("UserId") String UserId,
        @DefaultValue("") @QueryParam("coin") String coin, 
        @DefaultValue("") @QueryParam("Parity") String Parity,
        @DefaultValue("") @QueryParam("Amount") String Amount,
        @DefaultValue("") @QueryParam("originAccount") String originAccount, 
        @DefaultValue("") @QueryParam("FinalAccount") String FinalAccount, 
        @DefaultValue("") @QueryParam("Password_or") String Password_or, 
        @DefaultValue("") @QueryParam("Import") String Import,
        @DefaultValue("") @QueryParam("IdOri") String IdOri,
        @DefaultValue("") @QueryParam("IdDestP") String IdDestP,
        @DefaultValue("") @QueryParam("ComisionFinalAccount") String ComisionFinalAccount,
        @DefaultValue("") @QueryParam("ComisionFinalId") String ComisionFinalId,
        @DefaultValue("") @QueryParam("ComisionOriginAccount") String ComisionOriginAccount,
        @DefaultValue("") @QueryParam("IdOriComision") String IdOriComision, 
        @DefaultValue("") @QueryParam("MyPassSecretComision") String MyPassSecretComision,
        @DefaultValue("") @QueryParam("code") String strCodigo) throws Exception {
      //Objeto json para almacenar los objetos
      JSONObject objJson = new JSONObject();
      
      //parseamos  las variables correspondintes a enteros
      int intTypeMoneda = Integer.parseInt(TypeCoin);
      int intIdCte = Integer.parseInt(UserId);
      int intCriptomoneda = Integer.parseInt(coin);
      double dblParidad = Double.parseDouble(Parity);
      double dblMonto = Double.parseDouble(Amount);
      double dblImporte = Double.parseDouble(Import);
      int intIdOri = Integer.parseInt(IdOri);
      int intIdDestP = Integer.parseInt(IdDestP);
      int intIdDestinoComision = Integer.parseInt(ComisionFinalId);
      int intIdOriComision = Integer.parseInt(IdOriComision);
      String strResulttComision = "";
      //Objeto para validar la seguridad
      LoginAction action = new LoginAction();
      VariableSession varSesiones = new VariableSession(servletRequest);

      Periodos periodo = new Periodos();
        Fechas fecha = new Fechas();
        MlmTrading Trading = new MlmTrading();
        InetAddress localHost = InetAddress.getLocalHost();
        String strAddr = localHost.getHostAddress();
        String strResult = "";
        try {
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();
            String strRazonsocial = "";
            String strEstado = "";
            String strDatosCliente = "select * from vta_cliente where CT_ID = " + intIdCte;
            ResultSet rs = oConn.runQuery(strDatosCliente, true);
            while (rs.next()) {
                strRazonsocial = rs.getString("CT_RAZONSOCIAL");
                strEstado = rs.getString("CT_ESTADO");
            }
            rs.close();
            // <editor-fold defaultstate="collapsed" desc="Realizamos trading de dolares acualquier criptomoneda">
            if (intTypeMoneda == 1) {

                //Transferencia de porcentaje Comision
                AplicaChipCoinsCarteras aplica = new AplicaChipCoinsCarteras(oConn);
                boolean bolSincronizaa = false;               
                double dblPorceComision = 0.9 / 100;
                double dblmontoQuita = dblMonto * dblPorceComision;
                double dblMontoTotalComision = dblMonto - dblmontoQuita;

                double dblImporteQuita = dblImporte * dblPorceComision;
                double dblImporteTotalComision = dblImporte - dblImporteQuita;
                // <editor-fold defaultstate="collapsed" desc="Cobro de comision por movimiento trading">
                //Ejecutamos instrucciones RPC

                String strNumTransacciones = "";
                String strErrorJson = "";
                //Ejecutamos instrucciones RPC
                String strResp1 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + ComisionOriginAccount + "\", \"" + MyPassSecretComision + "\", 10 ],\"id\":" + intIdOri + "}");
                System.out.println("strResp1:" + strResp1);
                JSONObject jsonObj1 = new JSONObject(strResp1);
                boolean bolResultt = false;
                try {
                    bolResultt = jsonObj1.getBoolean("result");
                } catch (JSONException ex) {
                    strErrorJson = jsonObj1.getJSONObject("error").toString();
                }
                System.out.println("result:" + bolResultt);
                if (bolResultt) {
                    System.out.println("ENTRAAAAA");
                    //Conversion del monto a wei
                    BigInteger bigNum = EtherUtil.convert((long) dblImporteQuita, EtherUtil.Unit.ETHER);
                    String strResp222 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                            + "  \"from\": \"0x" + ComisionOriginAccount + "\",\n"
                            + "  \"to\": \"0x" + ComisionFinalAccount + "\",\n"
                            + "  \"gas\": \"0x76c0\", \n"
                            + "  \"gasPrice\": \"0x9184e72a000\", \n"
                            + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                            + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                            + "}],\"id\":1}");
                    JSONObject jsonObj22 = new JSONObject(strResp222);
                    System.out.println("strResp222:" + strResp222);
                    strNumTransacciones = jsonObj22.getString("result");
                    System.out.println("result:" + strNumTransacciones);
                    if (!strNumTransacciones.isEmpty()) {
                        bolSincronizaa = true;
                    }
                    if (bolSincronizaa) {
                        //Guardamos en la base                            
                        //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                        String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + ComisionOriginAccount + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + ComisionFinalAccount + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteQuita + " where CT_ID = " + "'" + intIdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteQuita + " where CT_ID = " + "'" + intIdDestinoComision + "'";
                        oConn.runQueryLMD(strSqlUdtRDC);
                        //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                        MlmMovComis movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdOriComision);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldDouble("MMC_CARGO", dblImporteQuita);
                        movComi.setFieldDouble("MMC_ABONO", 0.0);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionOriginAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImporteQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionFinalAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);

                    } else {
                        strResulttComision = "ERROR:Al sincronizar con el blockchain " + strResp222;
                    }
                } else if (!strErrorJson.isEmpty()) {
                    strResulttComision = "ERROR:" + strErrorJson + " favor de verificar el password de la cuenta";
                } else {
                    strResulttComision = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                }
                objJson.put("Resultado", strResulttComision);

                //Termina porcentaje Comision
                // </editor-fold>
                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                Trading.setFieldString("TR_TIPO_TRANSACCION", "DE DOLARES");
                Trading.setFieldDouble("TR_IMPORTE", dblImporteTotalComision);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_CARGO", dblMontoTotalComision);
                strResult = Trading.Agrega(oConn);

                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                if (intCriptomoneda == 6) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A DASH");
                } else if (intCriptomoneda == 7) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A ETHERUMS");
                } else if (intCriptomoneda == 8) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A RIPPLE");
                } else if (intCriptomoneda == 5) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A BITCOINS");
                }
                Trading.setFieldDouble("TR_IMPORTE", dblImporteTotalComision);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_ABONO", dblImporteTotalComision);
                strResult = Trading.Agrega(oConn);

                String strSqlUdtRC = "UPDATE vta_cliente "
                        + " set CHIP_TOT_MONEDAS_DOLAR = CHIP_TOT_MONEDAS_DOLAR -" + dblMontoTotalComision + " where CT_ID = " + "'" + intIdCte + "'";
                oConn.runQueryLMD(strSqlUdtRC);
                if (intCriptomoneda == 5) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_BTC = CHIP_TOT_MONEDAS_BTC + " + dblImporteTotalComision + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 6) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DASH = CHIP_TOT_MONEDAS_DASH + " + dblImporteTotalComision + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 7) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_ETHERUM = CHIP_TOT_MONEDAS_ETHERUM + " + dblImporteTotalComision + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 8) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_RIPPLE = CHIP_TOT_MONEDAS_RIPPLE + " + dblImporteTotalComision + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 9) {
                    String strResultt = "";
                    //Validamos si fue exitoso la transaccion por ethereum
                    //Sincronizamos con la red privada Ethereum - ChipCoin
                    aplica = new AplicaChipCoinsCarteras(oConn);
                    boolean bolSincroniza = false;

                    String strNumTransaccion = "";
                    String strErrorJsonn = "";
                    //Ejecutamos instrucciones RPC
                    String strResp101 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + originAccount + "\", \"" + Password_or + "\", 10 ],\"id\":" + intIdOri + "}");
                    System.out.println("strResp1:" + strResp101);
                    jsonObj1 = new JSONObject(strResp101);
                    boolean bolResult = false;
                    try {
                        bolResult = jsonObj1.getBoolean("result");
                    } catch (JSONException ex) {
                        strErrorJsonn = jsonObj1.getJSONObject("error").toString();
                    }
                    System.out.println("result:" + bolResult);
                    if (bolResult) {
                        //Conversion del monto a wei
                        BigInteger bigNum = EtherUtil.convert((long) dblImporteTotalComision, EtherUtil.Unit.ETHER);
                        String strResp2 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                                + "  \"from\": \"0x" + originAccount + "\",\n"
                                + "  \"to\": \"0x" + FinalAccount + "\",\n"
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
                        if (bolSincroniza) {
                            //Guardamos en la base                            
                            //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                            String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTotalComision + " where CW_NUMERO_CUENTA = " + "'" + originAccount + "'";
                            oConn.runQueryLMD(strSqlUdtR);
                            String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTotalComision + " where CW_NUMERO_CUENTA = " + "'" + FinalAccount + "'";
                            oConn.runQueryLMD(strSqlUdtRD);

                            String strSqlUdtRV = "UPDATE vta_cliente "
                                    + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteTotalComision + " where CT_ID = " + "'" + intIdOri + "'";
                            oConn.runQueryLMD(strSqlUdtRV);
                            String strSqlUdtRDC = "UPDATE vta_cliente "
                                    + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteTotalComision + " where CT_ID = " + "'" + intIdDestP + "'";
                            oConn.runQueryLMD(strSqlUdtRDC);
                            //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                            MlmMovComis movComi = new MlmMovComis();
                            movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                            movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                            movComi.setFieldInt("CT_ID", intIdOri);
                            movComi.setFieldInt("MMC_SINCR", 1);
                            movComi.setFieldString("MMC_NOTAS", "TRADING");
                            movComi.setFieldDouble("MMC_CARGO", dblImporteTotalComision);
                            movComi.setFieldDouble("MMC_ABONO", 0.0);
                            movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                            movComi.setFieldString("MMC_NUMERO_CUENTA", originAccount);
                            movComi.setFieldString("MMC_IP", strAddr);
                            movComi.Agrega(oConn);
                            //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                            movComi = new MlmMovComis();
                            movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                            movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                            movComi.setFieldInt("CT_ID", intIdDestP);
                            movComi.setFieldString("MMC_NOTAS", "TRADING");
                            movComi.setFieldInt("MMC_SINCR", 1);
                            movComi.setFieldDouble("MMC_CARGO", 0.0);
                            movComi.setFieldDouble("MMC_ABONO", dblImporteTotalComision);
                            movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                            movComi.setFieldString("MMC_NUMERO_CUENTA", FinalAccount);
                            movComi.setFieldString("MMC_IP", strAddr);
                            movComi.Agrega(oConn);

                        } else {
                            strResultt = "ERROR:Al sincronizar con el blockchain " + strResp2;
                        }
                    } else if (!strErrorJson.isEmpty()) {
                        strResultt = "ERROR:" + strErrorJson + " favor de verificar el password de la cuenta";
                    } else {
                        strResultt = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                    }
                    
                }

            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Realizamos trading de bitcoins acualquier criptomoneda">
            if (intTypeMoneda == 2) {
                AplicaChipCoinsCarteras aplica = new AplicaChipCoinsCarteras(oConn);
                boolean bolSincronizaa = false;                
                String strNumTransacciones = "";
                String strErrorJsonn = "";
                double dblPorceComision = 0.75 / 100;
                double dblmontoQuita = dblMonto * dblPorceComision;
                double dblMontoTotalComision = dblMonto - dblmontoQuita;

                double dblImporteQuita = dblImporte * dblPorceComision;
                double dblImporteTotal = dblImporte - dblImporteQuita;

                //Termina porcentaje Comision
                // <editor-fold defaultstate="collapsed" desc="Cobro de comision por movimiento trading">
                //Ejecutamos instrucciones RPC
                String strResp112 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + ComisionOriginAccount + "\", \"" + MyPassSecretComision + "\", 10 ],\"id\":" + intIdOriComision + "}");
                System.out.println("strResp112:" + strResp112);
                JSONObject jsonObj1 = new JSONObject(strResp112);
                boolean bolResultt = false;
                try {
                    bolResultt = jsonObj1.getBoolean("result");
                } catch (JSONException ex) {
                    strErrorJsonn = jsonObj1.getJSONObject("error").toString();
                }
                System.out.println("resultt:" + bolResultt);
                if (bolResultt) {
                    //Conversion del monto a wei
                    BigInteger bigNum = EtherUtil.convert((long) dblImporteQuita, EtherUtil.Unit.ETHER);
                    String strResp22 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                            + "  \"from\": \"0x" + ComisionOriginAccount + "\",\n"
                            + "  \"to\": \"0x" + ComisionFinalAccount + "\",\n"
                            + "  \"gas\": \"0x76c0\", \n"
                            + "  \"gasPrice\": \"0x9184e72a000\", \n"
                            + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                            + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                            + "}],\"id\":1}");
                    JSONObject jsonObj2 = new JSONObject(strResp22);
                    System.out.println("strResp2:" + strResp22);
                    strNumTransacciones = jsonObj2.getString("result");
                    System.out.println("result:" + strNumTransacciones);
                    if (!strNumTransacciones.isEmpty()) {
                        bolSincronizaa = true;
                    }
                    if (bolSincronizaa) {
                        //Guardamos en la base                            
                        //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                        String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + ComisionOriginAccount + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + ComisionFinalAccount + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteQuita + " where CT_ID = " + "'" + intIdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteQuita + " where CT_ID = " + "'" + IdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRDC);
                        //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                        MlmMovComis movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdOriComision);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldDouble("MMC_CARGO", dblImporteQuita);
                        movComi.setFieldDouble("MMC_ABONO", 0.0);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionOriginAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImporteQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionFinalAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);

                    } else {
                        strResulttComision = "ERROR:Al sincronizar con el blockchain " + strResp22;
                    }
                } else if (!strErrorJsonn.isEmpty()) {
                    strResulttComision = "ERROR:" + strErrorJsonn + " favor de verificar el password de la cuenta";
                } else {
                    strResulttComision = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                }
                objJson.put("Resultado", strResulttComision);
                
                // </editor-fold>
                //Termina porcentaje Comision
                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                Trading.setFieldString("TR_TIPO_TRANSACCION", "DE BITCOINS");
                Trading.setFieldDouble("TR_IMPORTE", dblImporteTotal);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_CARGO", dblMontoTotalComision);
                strResult = Trading.Agrega(oConn);

                //creamos movimiento con abono
                Trading = new MlmTrading();
                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                if (intCriptomoneda == 6) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A DASH");
                } else if (intCriptomoneda == 7) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A ETHERUMS");
                } else if (intCriptomoneda == 8) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A RIPPLE");
                } else if (intCriptomoneda == 2) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A DOLARES");
                }
                Trading.setFieldDouble("TR_IMPORTE", dblImporteTotal);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_ABONO", dblImporteTotal);
                strResult = Trading.Agrega(oConn);

                //actualizamos saldos de carteras
                String strSqlUdtRC = "UPDATE vta_cliente "
                        + " set CHIP_TOT_MONEDAS_BTC = CHIP_TOT_MONEDAS_BTC -" + dblMontoTotalComision + " where CT_ID = " + "'" + intIdCte + "'";
                oConn.runQueryLMD(strSqlUdtRC);
                if (intCriptomoneda == 2) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DOLAR = CHIP_TOT_MONEDAS_DOLAR + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 6) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DASH = CHIP_TOT_MONEDAS_DASH + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 7) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_ETHERUM = CHIP_TOT_MONEDAS_ETHERUM + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 8) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_RIPPLE = CHIP_TOT_MONEDAS_RIPPLE + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 9) {
                    String strResultt = "";
                    //Validamos si fue exitoso la transaccion por ethereum
                    //Sincronizamos con la red privada Ethereum - ChipCoin
                    aplica = new AplicaChipCoinsCarteras(oConn);
                    boolean bolSincronizza = false;

                    String strNumTransaccion = "";
                    String strErrorJson = "";
                    //Ejecutamos instrucciones RPC
                    String strResp1 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + originAccount + "\", \"" + Password_or + "\", 10 ],\"id\":" + intIdOri + "}");
                    System.out.println("strResp1:" + strResp1);
                    jsonObj1 = new JSONObject(strResp1);
                    boolean bolResult = false;
                    try {
                        bolResult = jsonObj1.getBoolean("result");
                    } catch (JSONException ex) {
                        strErrorJson = jsonObj1.getJSONObject("error").toString();
                    }
                    System.out.println("result:" + bolResult);
                    if (bolResult) {
                        //Conversion del monto a wei
                        BigInteger bigNum = EtherUtil.convert((long) dblImporteTotal, EtherUtil.Unit.ETHER);
                        String strResp212 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                                + "  \"from\": \"0x" + originAccount + "\",\n"
                                + "  \"to\": \"0x" + FinalAccount + "\",\n"
                                + "  \"gas\": \"0x76c0\", \n"
                                + "  \"gasPrice\": \"0x9184e72a000\", \n"
                                + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                                + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                                + "}],\"id\":1}");
                        JSONObject jsonObj2 = new JSONObject(strResp212);
                        System.out.println("strResp2:" + strResp212);
                        strNumTransaccion = jsonObj2.getString("result");
                        System.out.println("result:" + strNumTransaccion);
                        if (!strNumTransaccion.isEmpty()) {
                            bolSincronizza = true;
                        }
                        if (bolSincronizza) {
                            //Guardamos en la base                            
                            //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                            String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + originAccount + "'";
                            oConn.runQueryLMD(strSqlUdtR);
                            String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + FinalAccount + "'";
                            oConn.runQueryLMD(strSqlUdtRD);

                            String strSqlUdtRV = "UPDATE vta_cliente "
                                    + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteTotal + " where CT_ID = " + "'" + intIdOri + "'";
                            oConn.runQueryLMD(strSqlUdtRV);
                            String strSqlUdtRDC = "UPDATE vta_cliente "
                                    + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteTotal + " where CT_ID = " + "'" + intIdDestP + "'";
                            oConn.runQueryLMD(strSqlUdtRDC);
                            //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                            MlmMovComis movComi = new MlmMovComis();
                            movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                            movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                            movComi.setFieldInt("CT_ID", intIdOri);
                            movComi.setFieldString("MMC_NOTAS", "TRADING");
                            movComi.setFieldInt("MMC_SINCR", 1);
                            movComi.setFieldDouble("MMC_CARGO", dblImporteTotal);
                            movComi.setFieldDouble("MMC_ABONO", 0.0);
                            movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                            movComi.setFieldString("MMC_NUMERO_CUENTA", originAccount);
                            movComi.setFieldString("MMC_IP", strAddr);
                            movComi.Agrega(oConn);
                            //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                            movComi = new MlmMovComis();
                            movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                            movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                            movComi.setFieldInt("CT_ID", intIdDestP);
                            movComi.setFieldString("MMC_NOTAS", "TRADING");
                            movComi.setFieldInt("MMC_SINCR", 1);
                            movComi.setFieldDouble("MMC_CARGO", 0.0);
                            movComi.setFieldDouble("MMC_ABONO", dblImporteTotal);
                            movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                            movComi.setFieldString("MMC_NUMERO_CUENTA", FinalAccount);
                            movComi.setFieldString("MMC_IP", strAddr);
                            movComi.Agrega(oConn);

                        } else {
                            strResultt = "ERROR:Al sincronizar con el blockchain " + strResp212;
                        }
                    } else if (!strErrorJson.isEmpty()) {
                        strResultt = "ERROR:" + strErrorJson + " favor de verificar el password de la cuenta";
                    } else {
                        strResultt = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                    }
                }

            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Realizamos trading de etherums acualquier criptomoneda">
            if (intTypeMoneda == 3) {
                AplicaChipCoinsCarteras aplica = new AplicaChipCoinsCarteras(oConn);
                boolean bolSincronizaa = false;
                String strNumTransacciones = "";
                String strErrorJsonn = "";
                double dblPorceComision = 0.75 / 100;
                double dblmontoQuita = dblMonto * dblPorceComision;
                double dblMontoTotalComision = dblMonto - dblmontoQuita;

                double dblImporteQuita = dblImporte * dblPorceComision;
                double dblImporteTotal = dblImporte - dblImporteQuita;

                //Termina porcentaje Comision
                // <editor-fold defaultstate="collapsed" desc="Cobro de comision por movimiento trading">
                //Ejecutamos instrucciones RPC
                String strResp113 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + ComisionOriginAccount + "\", \"" + MyPassSecretComision + "\", 10 ],\"id\":" + intIdOriComision + "}");
                System.out.println("strResp113:" + strResp113);
                JSONObject jsonObj1 = new JSONObject(strResp113);
                boolean bolResultt = false;
                try {
                    bolResultt = jsonObj1.getBoolean("resultt");
                } catch (JSONException ex) {
                    strErrorJsonn = jsonObj1.getJSONObject("error").toString();
                }
                System.out.println("result:" + bolResultt);
                if (bolResultt) {
                    //Conversion del monto a wei
                    BigInteger bigNum = EtherUtil.convert((long) dblImporteQuita, EtherUtil.Unit.ETHER);
                    String strResp23 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                            + "  \"from\": \"0x" + ComisionOriginAccount + "\",\n"
                            + "  \"to\": \"0x" + ComisionFinalAccount + "\",\n"
                            + "  \"gas\": \"0x76c0\", \n"
                            + "  \"gasPrice\": \"0x9184e72a000\", \n"
                            + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                            + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                            + "}],\"id\":1}");
                    JSONObject jsonObj2 = new JSONObject(strResp23);
                    System.out.println("strResp2:" + strResp23);
                    strNumTransacciones = jsonObj2.getString("result");
                    System.out.println("result:" + strNumTransacciones);
                    if (!strNumTransacciones.isEmpty()) {
                        bolSincronizaa = true;
                    }
                    if (bolSincronizaa) {
                        //Guardamos en la base                            
                        //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                        String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblmontoQuita + " where CW_NUMERO_CUENTA = " + "'" + ComisionOriginAccount + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + ComisionFinalAccount + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteQuita + " where CT_ID = " + "'" + intIdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteQuita + " where CT_ID = " + "'" + intIdDestinoComision + "'";
                        oConn.runQueryLMD(strSqlUdtRDC);
                        //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                        MlmMovComis movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdOriComision);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldDouble("MMC_CARGO", dblImporteQuita);
                        movComi.setFieldDouble("MMC_ABONO", 0.0);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionOriginAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImporteQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionFinalAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);

                    } else {
                        strResulttComision = "ERROR:Al sincronizar con el blockchain " + strResp23;
                    }
                } else if (!strErrorJsonn.isEmpty()) {
                    strResulttComision = "ERROR:" + strErrorJsonn + " favor de verificar el password de la cuenta";
                } else {
                    strResulttComision = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                }
                objJson.put("Resultado", strResulttComision);
                // </editor-fold>
                //Termina porcentaje Comision
                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                Trading.setFieldString("TR_TIPO_TRANSACCION", " DE ETHERUMS");
                Trading.setFieldDouble("TR_IMPORTE", dblImporteTotal);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_CARGO", dblMontoTotalComision);
                strResult = Trading.Agrega(oConn);

                //creamos movimiento con abono
                periodo = new Periodos();
                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                if (intCriptomoneda == 5) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A BITCOINS");
                } else if (intCriptomoneda == 6) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A DASH");
                } else if (intCriptomoneda == 8) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A RIPPLE");
                } else if (intCriptomoneda == 2) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A DOLARES");
                }
                Trading.setFieldDouble("TR_IMPORTE", dblImporteTotal);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_ABONO", dblImporteTotal);
                strResult = Trading.Agrega(oConn);

                //actualizamos saldos de carteras
                String strSqlUdtRC = "UPDATE vta_cliente "
                        + " set CHIP_TOT_MONEDAS_ETHERUM = CHIP_TOT_MONEDAS_ETHERUM -" + dblMontoTotalComision + " where CT_ID = " + "'" + intIdCte + "'";
                oConn.runQueryLMD(strSqlUdtRC);
                if (intCriptomoneda == 2) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DOLAR = CHIP_TOT_MONEDAS_DOLAR + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 6) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DASH = CHIP_TOT_MONEDAS_DASH + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 5) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_BTC = CHIP_TOT_MONEDAS_BTC + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 8) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_RIPPLE = CHIP_TOT_MONEDAS_RIPPLE + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 9) {
                    String strResultt = "";
                    //Validamos si fue exitoso la transaccion por ethereum
                    //Sincronizamos con la red privada Ethereum - ChipCoin
                    aplica = new AplicaChipCoinsCarteras(oConn);
                    boolean bolSincroniza = false;
                    String strNumTransaccion = "";
                    String strErrorJson = "";
                    //Ejecutamos instrucciones RPC
                    String strResp1 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + originAccount + "\", \"" + Password_or + "\", 10 ],\"id\":" + intIdOri + "}");
                    System.out.println("strResp1:" + strResp1);
                    jsonObj1 = new JSONObject(strResp1);
                    boolean bolResult = false;
                    try {
                        bolResult = jsonObj1.getBoolean("result");
                    } catch (JSONException ex) {
                        strErrorJson = jsonObj1.getJSONObject("error").toString();
                    }
                    System.out.println("result:" + bolResult);
                    if (bolResult) {
                        //Conversion del monto a wei
                        BigInteger bigNum = EtherUtil.convert((long) dblImporteTotal, EtherUtil.Unit.ETHER);
                        String strResp2 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                                + "  \"from\": \"0x" + originAccount + "\",\n"
                                + "  \"to\": \"0x" + FinalAccount + "\",\n"
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
                        if (bolSincroniza) {
                            //Guardamos en la base                            
                            //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                            String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + originAccount + "'";
                            oConn.runQueryLMD(strSqlUdtR);
                            String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + FinalAccount + "'";
                            oConn.runQueryLMD(strSqlUdtRD);

                            String strSqlUdtRV = "UPDATE vta_cliente "
                                    + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteTotal + " where CT_ID = " + "'" + intIdOri + "'";
                            oConn.runQueryLMD(strSqlUdtRV);
                            String strSqlUdtRDC = "UPDATE vta_cliente "
                                    + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteTotal + " where CT_ID = " + "'" + intIdDestP + "'";
                            oConn.runQueryLMD(strSqlUdtRDC);
                            //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                            MlmMovComis movComi = new MlmMovComis();
                            movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                            movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                            movComi.setFieldInt("CT_ID", intIdOri);
                            movComi.setFieldInt("MMC_SINCR", 1);
                            movComi.setFieldString("MMC_NOTAS", "TRADING");
                            movComi.setFieldDouble("MMC_CARGO", dblImporteTotal);
                            movComi.setFieldDouble("MMC_ABONO", 0.0);
                            movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                            movComi.setFieldString("MMC_NUMERO_CUENTA", originAccount);
                            movComi.setFieldString("MMC_IP", strAddr);
                            movComi.Agrega(oConn);
                            //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                            movComi = new MlmMovComis();
                            movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                            movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                            movComi.setFieldInt("CT_ID", intIdDestP);
                            movComi.setFieldInt("MMC_SINCR", 1);
                            movComi.setFieldString("MMC_NOTAS", "TRADING");
                            movComi.setFieldDouble("MMC_CARGO", 0.0);
                            movComi.setFieldDouble("MMC_ABONO", dblImporteTotal);
                            movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                            movComi.setFieldString("MMC_NUMERO_CUENTA", FinalAccount);
                            movComi.setFieldString("MMC_IP", strAddr);
                            movComi.Agrega(oConn);

                        } else {
                            strResultt = "ERROR:Al sincronizar con el blockchain " + strResp2;
                        }
                    } else if (!strErrorJson.isEmpty()) {
                        strResultt = "ERROR:" + strErrorJson + " favor de verificar el password de la cuenta";
                    } else {
                        strResultt = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                    }

                }

            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Realizamos trading de Dash acualquier criptomoneda">
            if (intTypeMoneda == 4) {

                AplicaChipCoinsCarteras aplica = new AplicaChipCoinsCarteras(oConn);
                boolean bolSincronizaaa = false;
                String strNumTransacciones = "";
                String strErrorJsonn = "";
                double dblPorceComision = 0.75 / 100;
                double dblmontoQuita = dblMonto * dblPorceComision;
                double dblMontoTotalComision = dblMonto - dblmontoQuita;

                double dblImporteQuita = dblImporte * dblPorceComision;
                double dblImporteTotal = dblImporte - dblImporteQuita;

                //Termina porcentaje Comision
                // <editor-fold defaultstate="collapsed" desc="Cobro de comision por movimiento trading">
                //Ejecutamos instrucciones RPC
                String strResp114 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + ComisionOriginAccount + "\", \"" + MyPassSecretComision + "\", 10 ],\"id\":" + intIdOriComision + "}");
                System.out.println("strResp114:" + strResp114);
                JSONObject jsonObj1 = new JSONObject(strResp114);
                boolean bolResultt = false;
                try {
                    bolResultt = jsonObj1.getBoolean("result");
                } catch (JSONException ex) {
                    strErrorJsonn = jsonObj1.getJSONObject("error").toString();
                }
                System.out.println("result:" + bolResultt);
                if (bolResultt) {
                    //Conversion del monto a wei
                    BigInteger bigNum = EtherUtil.convert((long) dblImporteQuita, EtherUtil.Unit.ETHER);
                    String strResp24 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                            + "  \"from\": \"0x" + ComisionOriginAccount + "\",\n"
                            + "  \"to\": \"0x" + ComisionFinalAccount + "\",\n"
                            + "  \"gas\": \"0x76c0\", \n"
                            + "  \"gasPrice\": \"0x9184e72a000\", \n"
                            + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                            + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                            + "}],\"id\":1}");
                    JSONObject jsonObj2 = new JSONObject(strResp24);
                    System.out.println("strResp2:" + strResp24);
                    strNumTransacciones = jsonObj2.getString("result");
                    System.out.println("result:" + strNumTransacciones);
                    if (!strNumTransacciones.isEmpty()) {
                        bolSincronizaaa = true;
                    }
                    if (bolSincronizaaa) {
                        //Guardamos en la base                            
                        //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                        String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + ComisionOriginAccount + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + ComisionFinalAccount + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteQuita + " where CT_ID = " + "'" + intIdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteQuita + " where CT_ID = " + "'" + intIdDestinoComision + "'";
                        oConn.runQueryLMD(strSqlUdtRDC);
                        //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                        MlmMovComis movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdOriComision);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldDouble("MMC_CARGO", dblmontoQuita);
                        movComi.setFieldDouble("MMC_ABONO", 0.0);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionOriginAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblmontoQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionFinalAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);

                    } else {
                        strResulttComision = "ERROR:Al sincronizar con el blockchain " + strResp24;
                    }
                } else if (!strErrorJsonn.isEmpty()) {
                    strResulttComision = "ERROR:" + strErrorJsonn + " favor de verificar el password de la cuenta";
                } else {
                    strResulttComision = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                }
                objJson.put("Resultado", strResulttComision);
                // </editor-fold>
                //Termina porcentaje Comision
                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                Trading.setFieldString("TR_TIPO_TRANSACCION", "DE DASH");
                Trading.setFieldDouble("TR_IMPORTE", dblImporteTotal);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_CARGO", dblMontoTotalComision);
                strResult = Trading.Agrega(oConn);

                //creamos movimiento con abono
                Trading = new MlmTrading();
                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                if (intCriptomoneda == 5) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A BITCOINS");
                } else if (intCriptomoneda == 7) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A ETHERUMS");
                } else if (intCriptomoneda == 8) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A RIPPLE");
                } else if (intCriptomoneda == 2) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A DOLARES");
                }
                Trading.setFieldDouble("TR_IMPORTE", dblImporteTotal);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_ABONO", dblImporteTotal);
                strResult = Trading.Agrega(oConn);

                //actualizamos saldos de carteras
                String strSqlUdtRC = "UPDATE vta_cliente "
                        + " set CHIP_TOT_MONEDAS_DASH = CHIP_TOT_MONEDAS_DASH -" + dblMontoTotalComision + " where CT_ID = " + "'" + intIdCte + "'";
                oConn.runQueryLMD(strSqlUdtRC);
                if (intCriptomoneda == 7) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DOLAR = CHIP_TOT_MONEDAS_DOLAR + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 7) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_ETHERUM = CHIP_TOT_MONEDAS_ETHERUM + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 5) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_BTC = CHIP_TOT_MONEDAS_BTC + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 8) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_RIPPLE = CHIP_TOT_MONEDAS_RIPPLE + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 9) {
                    String strResultt = "";
                    //Validamos si fue exitoso la transaccion por ethereum
                    //Sincronizamos con la red privada Ethereum - ChipCoin
                    aplica = new AplicaChipCoinsCarteras(oConn);
                    boolean bolSincroniza = false;
                    String strNumTransaccion = "";
                    String strErrorJson = "";
                    //Ejecutamos instrucciones RPC
                    String strResp14 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + originAccount + "\", \"" + Password_or + "\", 10 ],\"id\":" + intIdOri + "}");
                    System.out.println("strResp1:" + strResp14);
                    jsonObj1 = new JSONObject(strResp14);
                    boolean bolResult = false;
                    try {
                        bolResult = jsonObj1.getBoolean("result");
                    } catch (JSONException ex) {
                        strErrorJson = jsonObj1.getJSONObject("error").toString();
                    }
                    System.out.println("result:" + bolResult);
                    if (bolResult) {
                        //Conversion del monto a wei
                        BigInteger bigNum = EtherUtil.convert((long) dblImporteTotal, EtherUtil.Unit.ETHER);
                        String strResp24 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                                + "  \"from\": \"0x" + originAccount + "\",\n"
                                + "  \"to\": \"0x" + FinalAccount + "\",\n"
                                + "  \"gas\": \"0x76c0\", \n"
                                + "  \"gasPrice\": \"0x9184e72a000\", \n"
                                + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                                + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                                + "}],\"id\":1}");
                        JSONObject jsonObj2 = new JSONObject(strResp24);
                        System.out.println("strResp2:" + strResp24);
                        strNumTransaccion = jsonObj2.getString("result");
                        System.out.println("result:" + strNumTransaccion);
                        if (!strNumTransaccion.isEmpty()) {
                            bolSincroniza = true;
                        }
                        if (bolSincroniza) {
                            //Guardamos en la base                            
                            //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                            String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + originAccount + "'";
                            oConn.runQueryLMD(strSqlUdtR);
                            String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + FinalAccount + "'";
                            oConn.runQueryLMD(strSqlUdtRD);

                            String strSqlUdtRV = "UPDATE vta_cliente "
                                    + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteTotal + " where CT_ID = " + "'" + intIdOri + "'";
                            oConn.runQueryLMD(strSqlUdtRV);
                            String strSqlUdtRDC = "UPDATE vta_cliente "
                                    + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteTotal + " where CT_ID = " + "'" + intIdDestP + "'";
                            oConn.runQueryLMD(strSqlUdtRDC);
                            //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                            MlmMovComis movComi = new MlmMovComis();
                            movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                            movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                            movComi.setFieldInt("CT_ID", intIdOri);
                            movComi.setFieldInt("MMC_SINCR", 1);
                            movComi.setFieldString("MMC_NOTAS", "TRADING");
                            movComi.setFieldDouble("MMC_CARGO", dblImporteTotal);
                            movComi.setFieldDouble("MMC_ABONO", 0.0);
                            movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                            movComi.setFieldString("MMC_NUMERO_CUENTA", originAccount);
                            movComi.setFieldString("MMC_IP", strAddr);
                            movComi.Agrega(oConn);
                            //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                            movComi = new MlmMovComis();
                            movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                            movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                            movComi.setFieldInt("CT_ID", intIdDestP);
                            movComi.setFieldInt("MMC_SINCR", 1);
                            movComi.setFieldString("MMC_NOTAS", "TRADING");
                            movComi.setFieldDouble("MMC_CARGO", 0.0);
                            movComi.setFieldDouble("MMC_ABONO", dblImporteTotal);
                            movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                            movComi.setFieldString("MMC_NUMERO_CUENTA", FinalAccount);
                            movComi.setFieldString("MMC_IP", strAddr);
                            movComi.Agrega(oConn);

                        } else {
                            strResultt = "ERROR:Al sincronizar con el blockchain " + strResp24;
                        }
                    } else if (!strErrorJson.isEmpty()) {
                        strResultt = "ERROR:" + strErrorJson + " favor de verificar el password de la cuenta";
                    } else {
                        strResultt = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                    }
                }

            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Realizamos trading de Ripple acualquier criptomoneda">
            if (intTypeMoneda == 5) {
                AplicaChipCoinsCarteras aplica = new AplicaChipCoinsCarteras(oConn);
                boolean bolSincronizaa = false;
                String strNumTransacciones = "";
                String strErrorJsonn = "";
                double dblPorceComision = 0.75 / 100;
                double dblmontoQuita = dblMonto * dblPorceComision;
                double dblMontoTotalComision = dblMonto - dblmontoQuita;

                double dblImporteQuita = dblImporte * dblPorceComision;
                double dblImporteTotal = dblImporte - dblImporteQuita;

                //Termina porcentaje Comision
                // <editor-fold defaultstate="collapsed" desc="Cobro de comision por movimiento trading">
                //Ejecutamos instrucciones RPC
                String strResp115 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + ComisionOriginAccount + "\", \"" + MyPassSecretComision + "\", 10 ],\"id\":" + intIdOriComision + "}");
                System.out.println("strResp115:" + strResp115);
                JSONObject jsonObj1 = new JSONObject(strResp115);
                boolean bolResultt = false;
                try {
                    bolResultt = jsonObj1.getBoolean("result");
                } catch (JSONException ex) {
                    strErrorJsonn = jsonObj1.getJSONObject("error").toString();
                }
                System.out.println("result:" + bolResultt);
                if (bolResultt) {
                    //Conversion del monto a wei
                    BigInteger bigNum = EtherUtil.convert((long) dblImporteQuita, EtherUtil.Unit.ETHER);
                    String strResp25 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                            + "  \"from\": \"0x" + ComisionOriginAccount + "\",\n"
                            + "  \"to\": \"0x" + ComisionFinalAccount + "\",\n"
                            + "  \"gas\": \"0x76c0\", \n"
                            + "  \"gasPrice\": \"0x9184e72a000\", \n"
                            + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                            + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                            + "}],\"id\":1}");
                    JSONObject jsonObj2 = new JSONObject(strResp25);
                    System.out.println("strResp2:" + strResp25);
                    strNumTransacciones = jsonObj2.getString("result");
                    System.out.println("result:" + strNumTransacciones);
                    if (!strNumTransacciones.isEmpty()) {
                        bolSincronizaa = true;
                    }
                    if (bolSincronizaa) {
                        //Guardamos en la base                            
                        //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                        String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + ComisionOriginAccount + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + ComisionFinalAccount + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteQuita + " where CT_ID = " + "'" + intIdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteQuita + " where CT_ID = " + "'" + intIdDestinoComision + "'";
                        oConn.runQueryLMD(strSqlUdtRDC);
                        //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                        MlmMovComis movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdOriComision);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldDouble("MMC_CARGO", dblImporteQuita);
                        movComi.setFieldDouble("MMC_ABONO", 0.0);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionOriginAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImporteQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionFinalAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);

                    } else {
                        strResulttComision = "ERROR:Al sincronizar con el blockchain " + strResp25;
                    }
                } else if (!strErrorJsonn.isEmpty()) {
                    strResulttComision = "ERROR:" + strErrorJsonn + " favor de verificar el password de la cuenta";
                } else {
                    strResulttComision = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                }
                objJson.put("Resultado", strResulttComision);
                // </editor-fold>
                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                Trading.setFieldString("TR_TIPO_TRANSACCION", " DE DASH");
                Trading.setFieldDouble("TR_IMPORTE", dblImporteTotal);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_CARGO", dblMontoTotalComision);
                strResult = Trading.Agrega(oConn);

                //creamos movimiento con abono
                Trading = new MlmTrading();
                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                if (intCriptomoneda == 5) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A BITCOINS");
                } else if (intCriptomoneda == 7) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A ETHERUMS");
                } else if (intCriptomoneda == 6) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A DASH");
                } else if (intCriptomoneda == 2) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A DOLARES");
                }
                Trading.setFieldDouble("TR_IMPORTE", dblImporteTotal);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_ABONO", dblImporteTotal);
                strResult = Trading.Agrega(oConn);
                //actualizamos saldos de carteras
                String strSqlUdtRC = "UPDATE vta_cliente "
                        + " set CHIP_TOT_MONEDAS_RIPPLE = CHIP_TOT_MONEDAS_RIPPLE -" + dblMontoTotalComision + " where CT_ID = " + "'" + intIdCte + "'";
                oConn.runQueryLMD(strSqlUdtRC);
                if (intCriptomoneda == 7) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_ETHERUM = CHIP_TOT_MONEDAS_ETHERUM + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 2) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DOLAR = CHIP_TOT_MONEDAS_DOLAR + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 5) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_BTC = CHIP_TOT_MONEDAS_BTC + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 6) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DASH = CHIP_TOT_MONEDAS_DASH + " + dblImporteTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 9) {
                    System.out.println("AQUI no  ENTRA chipcoin");
                    String strResultt = "";
                    //Validamos si fue exitoso la transaccion por ethereum
                    //Sincronizamos con la red privada Ethereum - ChipCoin
                    aplica = new AplicaChipCoinsCarteras(oConn);
                    boolean bolSincroniza = false;
                    String strNumTransaccion = "";
                    String strErrorJson = "";
                    //Ejecutamos instrucciones RPC
                    String strResp16 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + FinalAccount + "\", \"" + Password_or + "\", 10 ],\"id\":" + intIdOri + "}");
                    System.out.println("strResp1:" + strResp16);
                    jsonObj1 = new JSONObject(strResp16);
                    boolean bolResult = false;
                    try {
                        bolResult = jsonObj1.getBoolean("result");
                    } catch (JSONException ex) {
                        strErrorJson = jsonObj1.getJSONObject("error").toString();
                    }
                    System.out.println("result:" + bolResult);
                    if (bolResult) {
                        //Conversion del monto a wei
                        BigInteger bigNum = EtherUtil.convert((long) dblImporteTotal, EtherUtil.Unit.ETHER);
                        String strResp26 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                                + "  \"from\": \"0x" + originAccount + "\",\n"
                                + "  \"to\": \"0x" + FinalAccount + "\",\n"
                                + "  \"gas\": \"0x76c0\", \n"
                                + "  \"gasPrice\": \"0x9184e72a000\", \n"
                                + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                                + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                                + "}],\"id\":1}");
                        JSONObject jsonObj2 = new JSONObject(strResp26);
                        System.out.println("strResp2:" + strResp26);
                        strNumTransaccion = jsonObj2.getString("result");
                        System.out.println("result:" + strNumTransaccion);
                        if (!strNumTransaccion.isEmpty()) {
                            bolSincroniza = true;
                        }
                         System.out.println("AQUI no  ENTRA");
                        if (bolSincroniza) {
                            System.out.println("AQUI ENTRA");
                            //Guardamos en la base                            
                            //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                            String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + originAccount + "'";
                            oConn.runQueryLMD(strSqlUdtR);
                            String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + FinalAccount + "'";
                            oConn.runQueryLMD(strSqlUdtRD);

                            String strSqlUdtRV = "UPDATE vta_cliente "
                                    + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteTotal + " where CT_ID = " + "'" + intIdOri + "'";
                            oConn.runQueryLMD(strSqlUdtRV);
                            String strSqlUdtRDC = "UPDATE vta_cliente "
                                    + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteTotal + " where CT_ID = " + "'" + intIdDestP + "'";
                            oConn.runQueryLMD(strSqlUdtRDC);
                            //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                            MlmMovComis movComi = new MlmMovComis();
                            movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                            movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                            movComi.setFieldInt("CT_ID", intIdOri);
                            movComi.setFieldInt("MMC_SINCR", 1);
                            movComi.setFieldString("MMC_NOTAS", "TRADING");
                            movComi.setFieldDouble("MMC_CARGO", dblImporteTotal);
                            movComi.setFieldDouble("MMC_ABONO", 0.0);
                            movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                            movComi.setFieldString("MMC_NUMERO_CUENTA", originAccount);
                            movComi.setFieldString("MMC_IP", strAddr);
                            movComi.Agrega(oConn);
                            //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                            movComi = new MlmMovComis();
                            movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                            movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                            movComi.setFieldInt("CT_ID", intIdDestP);
                            movComi.setFieldInt("MMC_SINCR", 1);
                            movComi.setFieldString("MMC_NOTAS", "TRADING");
                            movComi.setFieldDouble("MMC_CARGO", 0.0);
                            movComi.setFieldDouble("MMC_ABONO", dblImporteTotal);
                            movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                            movComi.setFieldString("MMC_NUMERO_CUENTA", FinalAccount);
                            movComi.setFieldString("MMC_IP", strAddr);
                            movComi.Agrega(oConn);

                        } else {
                            strResultt = "ERROR:Al sincronizar con el blockchain " + strResp26;
                        }
                    } else if (!strErrorJson.isEmpty()) {
                        strResultt = "ERROR:" + strErrorJson + " favor de verificar el password de la cuenta";
                    } else {
                        strResultt = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                    }
                }

            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Realizamos trading de Chipcoin acualquier criptomoneda">
            if (intTypeMoneda == 6) {
                AplicaChipCoinsCarteras aplica = new AplicaChipCoinsCarteras(oConn);
                boolean bolSincronizaa = false;
                String strNumTransacciones = "";
                String strErrorJsonn = "";
                double dblPorceComision = 0.75 / 100;
                double dblmontoQuita = dblMonto * dblPorceComision;
                double dblMontoTotal = dblMonto - dblmontoQuita;

                double dblImporteQuita = dblImporte * dblPorceComision;
                double dblImporteTotalComision = dblImporte - dblImporteQuita;

                //Termina porcentaje Comision
                // <editor-fold defaultstate="collapsed" desc="Cobro de comision por movimiento trading">
                //Ejecutamos instrucciones RPC
                String strResp117 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + originAccount + "\", \"" + Password_or + "\", 10 ],\"id\":" + intIdOri + "}");
                System.out.println("strResp117:" + strResp117);
                JSONObject jsonObj1 = new JSONObject(strResp117);
                boolean bolResultt = false;
                try {
                    bolResultt = jsonObj1.getBoolean("result");
                } catch (JSONException ex) {
                    strErrorJsonn = jsonObj1.getJSONObject("error").toString();
                }
                System.out.println("result:" + bolResultt);
                if (bolResultt) {
                    //Conversion del monto a wei
                    BigInteger bigNum = EtherUtil.convert((long) dblImporteQuita, EtherUtil.Unit.ETHER);
                    String strResp27 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                            + "  \"from\": \"0x" + originAccount + "\",\n"
                            + "  \"to\": \"0x" + FinalAccount + "\",\n"
                            + "  \"gas\": \"0x76c0\", \n"
                            + "  \"gasPrice\": \"0x9184e72a000\", \n"
                            + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                            + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                            + "}],\"id\":1}");
                    JSONObject jsonObj2 = new JSONObject(strResp27);
                    System.out.println("strResp2:" + strResp27);
                    strNumTransacciones = jsonObj2.getString("result");
                    System.out.println("result:" + strNumTransacciones);
                    if (!strNumTransacciones.isEmpty()) {
                        bolSincronizaa = true;
                    }
                    if (bolSincronizaa) {
                        //Guardamos en la base                            
                        //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                        String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + originAccount + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + FinalAccount + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteQuita + " where CT_ID = " + "'" + intIdOri + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteQuita + " where CT_ID = " + "'" + intIdDestP + "'";
                        oConn.runQueryLMD(strSqlUdtRDC);
                        //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                        MlmMovComis movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdOriComision);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldDouble("MMC_CARGO", dblImporteQuita);
                        movComi.setFieldDouble("MMC_ABONO", 0.0);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", originAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImporteQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", FinalAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);

                    } else {
                        strResulttComision = "ERROR:Al sincronizar con el blockchain " + strResp27;
                    }
                } else if (!strErrorJsonn.isEmpty()) {
                    strResulttComision = "ERROR:" + strErrorJsonn + " favor de verificar el password de la cuenta";
                } else {
                    strResulttComision = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                }
                objJson.put("Resultado", strResulttComision);
                // </editor-fold>
                String strResultt = "";
                //Validamos si fue exitoso la transaccion por ethereum
                //Sincronizamos con la red privada Ethereum - ChipCoin
                aplica = new AplicaChipCoinsCarteras(oConn);
                boolean bolSincroniza = false;
                String strNumTransaccion = "";
                String strErrorJson = "";
                //Ejecutamos instrucciones RPC
                String strResp18 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + originAccount + "\", \"" + Password_or + "\", 10 ],\"id\":" + intIdOri + "}");
                System.out.println("strResp18:" + strResp18);
                jsonObj1 = new JSONObject(strResp18);
                boolean bolResult = false;
                try {
                    bolResult = jsonObj1.getBoolean("result");
                } catch (JSONException ex) {
                    strErrorJson = jsonObj1.getJSONObject("error").toString();
                }
                System.out.println("result:" + bolResult);
                if (bolResult) {
                    //Conversion del monto a wei
                    BigInteger bigNum = EtherUtil.convert((long) dblMontoTotal, EtherUtil.Unit.ETHER);
                    String strResp28 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                            + "  \"from\": \"0x" + originAccount + "\",\n"
                            + "  \"to\": \"0x" + FinalAccount + "\",\n"
                            + "  \"gas\": \"0x76c0\", \n"
                            + "  \"gasPrice\": \"0x9184e72a000\", \n"
                            + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                            + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                            + "}],\"id\":1}");
                    JSONObject jsonObj2 = new JSONObject(strResp28);
                    System.out.println("strResp2:" + strResp28);
                    strNumTransaccion = jsonObj2.getString("result");
                    System.out.println("result:" + strNumTransaccion);
                    if (!strNumTransaccion.isEmpty()) {
                        bolSincroniza = true;
                    }
                    if (bolSincroniza) {
                        //Guardamos en la base                            
                        //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                        String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblMontoTotal + " where CW_NUMERO_CUENTA = " + "'" + originAccount + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblMontoTotal + " where CW_NUMERO_CUENTA = " + "'" + FinalAccount + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblMontoTotal + " where CT_ID = " + "'" + intIdOri + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblMontoTotal + " where CT_ID = " + "'" + intIdDestP + "'";
                        oConn.runQueryLMD(strSqlUdtRDC);
                        //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                        MlmMovComis movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdOri);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldString("MMC_NOTAS", "TRADING");
                        movComi.setFieldDouble("MMC_CARGO", dblMontoTotal);
                        movComi.setFieldDouble("MMC_ABONO", 0.0);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", originAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdDestP);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldString("MMC_NOTAS", "TRADING");
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblMontoTotal);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", FinalAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);

                    } else {
                        strResultt = "ERROR:Al sincronizar con el blockchain " + strResp18;
                    }
                } else if (!strErrorJson.isEmpty()) {
                    strResultt = "ERROR:" + strErrorJson + " favor de verificar el password de la cuenta";
                } else {
                    strResultt = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                }
                 objJson.put("Resultado", strResultt);
                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                Trading.setFieldString("TR_TIPO_TRANSACCION", " DE CHIPCOIN");
                Trading.setFieldDouble("TR_IMPORTE", dblMontoTotal);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_CARGO", dblMontoTotal);
                strResult = Trading.Agrega(oConn);

                //creamos movimiento con abono
                Trading = new MlmTrading();
                Trading.setFieldInt("CT_ID", intIdCte);
                Trading.setFieldString("TR_CLIENTE", strRazonsocial);
                Trading.setFieldString("TR_FECHA", fecha.getFechaActual());
                Trading.setFieldString("TR_HORA", fecha.getHoraActual());
                Trading.setFieldString("TR_IP_CLIENTE", strAddr);
                if (intCriptomoneda == 5) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A BITCOINS");
                } else if (intCriptomoneda == 7) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A ETHERUMS");
                } else if (intCriptomoneda == 6) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A DASH");
                } else if (intCriptomoneda == 8) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A RIPPLE");
                } else if (intCriptomoneda == 2) {
                    Trading.setFieldString("TR_TIPO_TRANSACCION", "A DOLARES");
                }
                Trading.setFieldDouble("TR_IMPORTE", dblMontoTotal);
                Trading.setFieldString("TR_ESTADO", strEstado);
                Trading.setFieldInt("TR_SOLICITUD_CONFIRMADO", 0);
                Trading.setFieldInt("TR_TIPO_MONEDA", intCriptomoneda);
                Trading.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                Trading.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                Trading.setFieldDouble("TR_TASA_CAMBIO", dblParidad);
                Trading.setFieldDouble("TR_ABONO", dblMontoTotal);
                strResult = Trading.Agrega(oConn);

                if (intCriptomoneda == 2) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DOLAR = CHIP_TOT_MONEDAS_DOLAR + " + dblMontoTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 7) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_ETHERUM = CHIP_TOT_MONEDAS_ETHERUM + " + dblMontoTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 5) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_BTC = CHIP_TOT_MONEDAS_BTC + " + dblMontoTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 6) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DASH = CHIP_TOT_MONEDAS_DASH + " + dblMontoTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }
                if (intCriptomoneda == 8) {
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_RIPPLE = CHIP_TOT_MONEDAS_RIPPLE + " + dblMontoTotal + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);
                }

            }
            // </editor-fold>

        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
      return objJson.toString();
   }

}