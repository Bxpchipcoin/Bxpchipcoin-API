/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import Tablas.MlmMovComis;
import Tablas.MlmPagos;
import struts.LoginAction;
import com.mx.siweb.erp.restful.EvalSesion;
import com.mx.siweb.erp.restful.MobilserviceResource;
import com.mx.siweb.mlm.compensacion.Periodos;
import com.mx.siweb.mlm.compensacion.chipcoin.AplicaChipCoinsCarteras;
import com.mx.siweb.mlm.compensacion.chipcoin.EtherUtil;
import com.mx.siweb.mlm.compensacion.chipcoin.MlmPagosFondeo;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import apiSiweb.ContextoApt.VariableSession;
import apiSiweb.Operaciones.Conexion;
import apiSiweb.Utilerias.Fechas;
import java.math.BigInteger;
import java.net.InetAddress;
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
@Path("BxpBuy")
public class BxpBuyResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(BxpBuyResource.class.getName());

    /**
     * Creates a new instance of MobilserviceResource
     */
    public BxpBuyResource() {

    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.MobilserviceResource
     *
     * @param Monto Se ingresa el monto a fondear
     * @param NumeroAprobacion Numero d eaprobación para identificar el pago del fondeo
     * @param Paquete Tipo de paquete para fondear chipcoin
     * @param Cliente Cliente que realiza el fondeo 
     * @param Fecha Fecha con la que se realiza el fondeo 
     * @param Codigo Codigo de la sesión
     * @param Sucursal Sucursal en la que se realiza el pago dle fondeo 
     * @param CuentaOrigen cuenta maestra con la que se enviaran los chipcoins a la cuenta del cliente
     * @param CuentaDestino Cuenta del cliente al que se se le fondeara el pago 
     * @param Pass Contraseña de la cuenta origen
     * @param IdOri Id de la cuenta maestra que envia  el fondeo a la cuenta del cliente
     * @param IdDes Id de la cuenta del cliente que recibe el fondeo 
     * @param CuentaOrigenComision Cuenta del cliente el cuál envía una comisión por cada fondeo realizado
     * @param CuentaDestinoComision Cuenta Maestra que recibirá el monto de la ocmisión asignada por cada fondeo realizado
     * @param MyPassSecretComision Contraseña de la cuenta del cliente que envía la comisión por cada fondeo realizado
     * @param IdOriComision Id de la cuenta del cliente que envía la comision por cada fondeo realizado
     * @param IdDestinoComision Id de la cuenta maestra que recibe la comisión por fondeo realizado
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String BxpBuy(
            @DefaultValue("") @QueryParam("Amount") String Amount, 
            @DefaultValue("") @QueryParam("Number") String Number, 
            @DefaultValue("") @QueryParam("Package") String Package, 
            @DefaultValue("") @QueryParam("User") String User, 
            @DefaultValue("") @QueryParam("Date") String Date, 
            @DefaultValue("") @QueryParam("Code") String Code, 
            @DefaultValue("") @QueryParam("Bank") String Bank, 
            @DefaultValue("") @QueryParam("OriginAccount") String OriginAccount, 
            @DefaultValue("") @QueryParam("FinaAccount") String FinaAccount, 
            @DefaultValue("") @QueryParam("Pass") String Pass, 
            @DefaultValue("") @QueryParam("IdOri") String strIdOri, 
            @DefaultValue("") @QueryParam("IdDes") String strIdDestino,
            @DefaultValue("") @QueryParam("ComisionOriginAcount") String ComisionOriginAcount,
            @DefaultValue("") @QueryParam("ComisionFinalAccount") String ComisionFinalAccount,
            @DefaultValue("") @QueryParam("MyPassSecretComision") String strMyPassSecretComision,
            @DefaultValue("") @QueryParam("IdOriComision") String strIdOriComision,
            @DefaultValue("") @QueryParam("IdDestinoComision") String strIdDestinoComision) {
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

                Fechas fecha = new Fechas();
                InetAddress localHost = InetAddress.getLocalHost();
                String strAddr = localHost.getHostAddress();
                String strResultados = "";

                int intIdOri = Integer.parseInt(strIdOri);
                int IdDestino = Integer.parseInt(strIdDestino);
                int intIdOriComision = Integer.parseInt(strIdOriComision);
                int IdDestinoComision = Integer.parseInt(strIdDestinoComision);
                double dblporcentaje = 2.8 / 100;
                double dblImportequita = 0.0;
                double dblImporteTot = 0.0;
                double dblSaldo = 0.0;
                double dblTotSaldo = 0.0;
                double dblImportePago = Double.parseDouble(Amount);
                String strSqli = "select PGF_SALDO  from mlm_pagos_fondeo where CT_ID = " + varSesiones.getIntNoUser() + " ORDER BY PGF_ID DESC LIMIT 0,1";
                ResultSet rs = oConn.runQuery(strSqli, true);
                while (rs.next()) {
                    dblSaldo = rs.getDouble("PGF_SALDO");
                }
                rs.close();
                String strRazonsocial = "";
                String strEstado = "";
                String strDatosCliente = "select * from vta_cliente where CT_ID = " + varSesiones.getIntNoUser();
                rs = oConn.runQuery(strDatosCliente, true);
                while (rs.next()) {
                    strRazonsocial = rs.getString("CT_RAZONSOCIAL");
                    strEstado = rs.getString("CT_ESTADO");
                }
                rs.close();
                dblImportequita = dblImportePago * dblporcentaje;
                dblImporteTot = dblImportePago - dblImportequita;

                MlmPagosFondeo FondeoPago = new MlmPagosFondeo();
                Periodos periodo = new Periodos();

                FondeoPago.setFieldString("PGF_FECHA", fecha.getFechaActual());
                FondeoPago.setFieldString("PGF_HORA", fecha.getHoraActual());
                FondeoPago.setFieldString("PGF_FECHA_DEPOSITO", fecha.FormateaBD(Date, "/"));
                FondeoPago.setFieldString("PGF_SUCURSAL", Bank);
                FondeoPago.setFieldString("PGF_NUM_AUTORIZA", Number);
                FondeoPago.setFieldInt("CT_ID", varSesiones.getIntNoUser());
                FondeoPago.setFieldString("PGF_NOMBRE", strRazonsocial);
                FondeoPago.setFieldString("PGF_IP_ADDRESS", strAddr);
                FondeoPago.setFieldString("PGF_ESTADO", strEstado);
                FondeoPago.setFieldInt("PGF_TIPO_PAGO", 1);
                FondeoPago.setFieldInt("PGF_BXP", 1);
                FondeoPago.setFieldString("PGF_TYPE_FONDEO", "FONDEO MULTINIVEL");
                FondeoPago.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
                FondeoPago.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
                FondeoPago.setFieldDouble("PGF_IMPORTE", dblImporteTot);
                FondeoPago.setFieldDouble("PGF_SALDO", dblSaldo);

                strResultados = FondeoPago.Agrega(oConn);
                System.out.println("strResultados:" + strResultados);
                objJson.put("Resultado", strResultados);
                dblTotSaldo = dblSaldo + dblImportePago;
                String strUpdateF = "update mlm_pagos_fondeo set PGF_SALDO = " + dblTotSaldo + " WHERE CT_ID = " + varSesiones.getIntNoUser();
                oConn.runQueryLMD(strUpdateF);

                // <editor-fold defaultstate="collapsed" desc="Realiza transaccion de fondeo multinivel">
                //Validamos si fue exitoso la transaccion por ethereum
                //Sincronizamos con la red privada Ethereum - ChipCoin
                AplicaChipCoinsCarteras aplica = new AplicaChipCoinsCarteras(oConn);
                boolean bolSincroniza = false;
                String strNumTransaccion = "";
                String strErrorJson = "";
                //Ejecutamos instrucciones RPC
                String strResp28 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + OriginAccount + "\", \"" + Pass + "\", 10 ],\"id\":" + intIdOri + "}");
                System.out.println("strResp2:" + strResp28);
                JSONObject jsonObj1 = new JSONObject(strResp28);
                boolean bolResult = false;
                try {
                    bolResult = jsonObj1.getBoolean("result");
                } catch (JSONException ex) {
                    strErrorJson = jsonObj1.getJSONObject("error").toString();
                }
                System.out.println("result:" + bolResult);
                if (bolResult) {
                    //Conversion del monto a wei
                    BigInteger bigNum = EtherUtil.convert((long) dblImporteTot, EtherUtil.Unit.ETHER);
                    String strResp3 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                            + "  \"from\": \"0x" + OriginAccount + "\",\n"
                            + "  \"to\": \"0x" + FinaAccount + "\",\n"
                            + "  \"gas\": \"0x76c0\", \n"
                            + "  \"gasPrice\": \"0x9184e72a000\", \n"
                            + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                            + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                            + "}],\"id\":1}");
                    JSONObject jsonObj2 = new JSONObject(strResp3);
                    System.out.println("strResp2:" + strResp3);
                    strNumTransaccion = jsonObj2.getString("result");
                    System.out.println("result:" + strNumTransaccion);
                    if (!strNumTransaccion.isEmpty()) {
                        bolSincroniza = true;
                    }
                    if (bolSincroniza) {
                        //Guardamos en la base                            
                        //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                        String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTot + " where CW_NUMERO_CUENTA = " + "'" + OriginAccount + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTot + " where CW_NUMERO_CUENTA = " + "'" + FinaAccount + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteTot + " where CT_ID = " + "'" + intIdOri + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteTot + " where CT_ID = " + "'" + IdDestino + "'";
                        oConn.runQueryLMD(strSqlUdtRDC);
                        //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                        MlmMovComis movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdOri);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldInt("MMC_BXP", 1);
                        movComi.setFieldString("MMC_NOTAS", "FONDEO TRADICIONAL");
                        movComi.setFieldDouble("MMC_CARGO", dblImporteTot);
                        movComi.setFieldDouble("MMC_ABONO", 0.0);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", OriginAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", IdDestino);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldInt("MMC_BXP", 1);
                        movComi.setFieldString("MMC_NOTAS", "FONDEO TRADICIONAL");
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImporteTot);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", FinaAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);

                    } else {
                        strResultados = "ERROR:Al sincronizar con el blockchain " + strResp28;
                    }
                } else if (!strErrorJson.isEmpty()) {
                    strResultados = "ERROR:" + strErrorJson + " favor de verificar el password de la cuenta";
                } else {
                    strResultados = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                }
                // </editor-fold>                
                // <editor-fold defaultstate="collapsed" desc="Realiza comision de transaccion de fondeo multinivel">
                //Validamos si fue exitoso la transaccion por ethereum
                //Sincronizamos con la red privada Ethereum - ChipCoin
                aplica = new AplicaChipCoinsCarteras(oConn);
                boolean bolSincronizaa = false;
                String strNumTransaccioon = "";
                String strErrorJsoon = "";
                //Ejecutamos instrucciones RPC
                String strResp29 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + ComisionOriginAcount + "\", \"" + strMyPassSecretComision + "\", 10 ],\"id\":" + intIdOriComision + "}");
                System.out.println("strResp2:" + strResp29);
                jsonObj1 = new JSONObject(strResp29);
                boolean bolResullt = false;
                try {
                    bolResullt = jsonObj1.getBoolean("result");
                } catch (JSONException ex) {
                    strErrorJsoon = jsonObj1.getJSONObject("error").toString();
                }
                System.out.println("result:" + bolResullt);
                if (bolResullt) {
                    //Conversion del monto a wei
                    BigInteger bigNum = EtherUtil.convert((long) dblImportequita, EtherUtil.Unit.ETHER);
                    String strResp33 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                            + "  \"from\": \"0x" + ComisionOriginAcount + "\",\n"
                            + "  \"to\": \"0x" + ComisionFinalAccount + "\",\n"
                            + "  \"gas\": \"0x76c0\", \n"
                            + "  \"gasPrice\": \"0x9184e72a000\", \n"
                            + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"//El numero en formato wei lo convertimos en hexadecimal
                            + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                            + "}],\"id\":1}");
                    JSONObject jsonObj2 = new JSONObject(strResp33);
                    System.out.println("strResp33:" + strResp33);
                    strNumTransaccioon = jsonObj2.getString("result");
                    System.out.println("result:" + strNumTransaccioon);
                    if (!strNumTransaccioon.isEmpty()) {
                        bolSincronizaa = true;
                    }
                    if (bolSincronizaa) {
                        //Guardamos en la base                            
                        //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                        String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImportequita + " where CW_NUMERO_CUENTA = " + "'" + ComisionOriginAcount + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImportequita + " where CW_NUMERO_CUENTA = " + "'" + ComisionFinalAccount + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImportequita + " where CT_ID = " + "'" + intIdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImportequita + " where CT_ID = " + "'" + IdDestinoComision + "'";
                        oConn.runQueryLMD(strSqlUdtRDC);
                        //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                        MlmMovComis movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", intIdOriComision);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldInt("MMC_BXP", 1);
                        movComi.setFieldString("MMC_NOTAS", "COMISION FONDEO TRADICIONAL");
                        movComi.setFieldDouble("MMC_CARGO", dblImportequita);
                        movComi.setFieldDouble("MMC_ABONO", 0.0);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransaccioon);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionOriginAcount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", IdDestinoComision);
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldInt("MMC_BXP", 1);
                        movComi.setFieldString("MMC_NOTAS", "COMISION TRADICIONAL");
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImportequita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransaccioon);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", ComisionFinalAccount);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);

                    } else {
                        strResultados = "ERROR:Al sincronizar con el blockchain " + strResp28;
                    }
                } else if (!strErrorJson.isEmpty()) {
                    strResultados = "ERROR:" + strErrorJson + " favor de verificar el password de la cuenta";
                } else {
                    strResultados = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
                }
                // </editor-fold>

            }
            oConn.close();
        } catch (Exception ex) {
            log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        return objJson.toString();
    }

}
