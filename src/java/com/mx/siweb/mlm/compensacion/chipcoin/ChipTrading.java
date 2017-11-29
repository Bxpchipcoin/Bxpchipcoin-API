/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.chipcoin;

import Tablas.MlmMovComis;
import com.mx.siweb.mlm.compensacion.chipcoin.MlmPagosFondeo;
import com.mx.siweb.mlm.compensacion.Periodos;
import apiSiweb.Operaciones.Conexion;
import apiSiweb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.math.BigInteger;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author ZeusSIWEB
 */
public class ChipTrading {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ChipPrecioMoneda.class.getName());
    private Conexion oConn;

    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">

    public String movTrading(int intTypeMoneda, int intIdCte, int intCriptomoneda, double dblParidad, double dblMonto, String strPass, String strCuentaOrigen, String strCuentaDestino, int intIdOri, int intIdDestP, double dblImporte, String strCuentaDestinoComision, int IdDestinoComision, String strCuentaOrigenComision, int intIdOriComision, String strMyPassSecretComision) throws JSONException, Exception {
        System.out.println("intTypeMoneda:" + intTypeMoneda);
        System.out.println("intCriptomoneda:" + intCriptomoneda);
        System.out.println("dblParidad:" + dblParidad);
        System.out.println("dblMonto:" + dblMonto);
        System.out.println("strPass:" + strPass);
        System.out.println("strCuentaOrigen:" + strCuentaOrigen);
        System.out.println("strCuentaDestino:" + strCuentaDestino);
        System.out.println("intIdOri:" + intIdOri);
        System.out.println("intIdDestP:" + intIdDestP);
        System.out.println("dblImporte:" + dblImporte);
        System.out.println("strCuentaDestinoComision:" + strCuentaDestinoComision);
        System.out.println("IdDestinoComision:" + IdDestinoComision);
        System.out.println("strCuentaOrigenComision:" + strCuentaOrigenComision);
        System.out.println("intIdOriComision:" + intIdOriComision);
        System.out.println("strMyPassSecretComision:" + strMyPassSecretComision);

        Periodos periodo = new Periodos();
        Fechas fecha = new Fechas();
        MlmTrading Trading = new MlmTrading();
        InetAddress localHost = InetAddress.getLocalHost();
        String strAddr = localHost.getHostAddress();
        String strResult = "";
        try {
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
                String strResulttComision;
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
                String strResp1 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigenComision + "\", \"" + strMyPassSecretComision + "\", 10 ],\"id\":" + intIdOri + "}");
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
                            + "  \"from\": \"0x" + strCuentaOrigenComision + "\",\n"
                            + "  \"to\": \"0x" + strCuentaDestinoComision + "\",\n"
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
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigenComision + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestinoComision + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteQuita + " where CT_ID = " + "'" + intIdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteQuita + " where CT_ID = " + "'" + IdDestinoComision + "'";
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
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigenComision);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", IdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImporteQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestinoComision);
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
                    String strResp101 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigen + "\", \"" + strPass + "\", 10 ],\"id\":" + intIdOri + "}");
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
                                + "  \"from\": \"0x" + strCuentaOrigen + "\",\n"
                                + "  \"to\": \"0x" + strCuentaDestino + "\",\n"
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
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTotalComision + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigen + "'";
                            oConn.runQueryLMD(strSqlUdtR);
                            String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTotalComision + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestino + "'";
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
                            movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigen);
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
                            movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestino);
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
                String strResulttComision;
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
                String strResp112 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigenComision + "\", \"" + strMyPassSecretComision + "\", 10 ],\"id\":" + intIdOriComision + "}");
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
                            + "  \"from\": \"0x" + strCuentaOrigenComision + "\",\n"
                            + "  \"to\": \"0x" + strCuentaDestinoComision + "\",\n"
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
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigenComision + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestinoComision + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteQuita + " where CT_ID = " + "'" + intIdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteQuita + " where CT_ID = " + "'" + IdDestinoComision + "'";
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
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigenComision);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", IdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImporteQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestinoComision);
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
                    String strResp1 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigen + "\", \"" + strPass + "\", 10 ],\"id\":" + intIdOri + "}");
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
                                + "  \"from\": \"0x" + strCuentaOrigen + "\",\n"
                                + "  \"to\": \"0x" + strCuentaDestino + "\",\n"
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
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigen + "'";
                            oConn.runQueryLMD(strSqlUdtR);
                            String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestino + "'";
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
                            movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigen);
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
                            movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestino);
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
                String strResulttComision;
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
                String strResp113 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigenComision + "\", \"" + strMyPassSecretComision + "\", 10 ],\"id\":" + intIdOriComision + "}");
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
                            + "  \"from\": \"0x" + strCuentaOrigenComision + "\",\n"
                            + "  \"to\": \"0x" + strCuentaDestinoComision + "\",\n"
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
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblmontoQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigenComision + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestinoComision + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteQuita + " where CT_ID = " + "'" + intIdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteQuita + " where CT_ID = " + "'" + IdDestinoComision + "'";
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
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigenComision);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", IdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImporteQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestinoComision);
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
                    String strResp1 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigen + "\", \"" + strPass + "\", 10 ],\"id\":" + intIdOri + "}");
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
                                + "  \"from\": \"0x" + strCuentaOrigen + "\",\n"
                                + "  \"to\": \"0x" + strCuentaDestino + "\",\n"
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
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigen + "'";
                            oConn.runQueryLMD(strSqlUdtR);
                            String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestino + "'";
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
                            movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigen);
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
                            movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestino);
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
                String strResulttComision;
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
                String strResp114 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigenComision + "\", \"" + strMyPassSecretComision + "\", 10 ],\"id\":" + intIdOriComision + "}");
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
                            + "  \"from\": \"0x" + strCuentaOrigenComision + "\",\n"
                            + "  \"to\": \"0x" + strCuentaDestinoComision + "\",\n"
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
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigenComision + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestinoComision + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteQuita + " where CT_ID = " + "'" + intIdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteQuita + " where CT_ID = " + "'" + IdDestinoComision + "'";
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
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigenComision);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", IdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblmontoQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestinoComision);
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
                    String strResp14 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigen + "\", \"" + strPass + "\", 10 ],\"id\":" + intIdOri + "}");
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
                                + "  \"from\": \"0x" + strCuentaOrigen + "\",\n"
                                + "  \"to\": \"0x" + strCuentaDestino + "\",\n"
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
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigen + "'";
                            oConn.runQueryLMD(strSqlUdtR);
                            String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestino + "'";
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
                            movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigen);
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
                            movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestino);
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
                String strResulttComision;
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
                String strResp115 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigenComision + "\", \"" + strMyPassSecretComision + "\", 10 ],\"id\":" + intIdOriComision + "}");
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
                            + "  \"from\": \"0x" + strCuentaOrigenComision + "\",\n"
                            + "  \"to\": \"0x" + strCuentaDestinoComision + "\",\n"
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
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigenComision + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestinoComision + "'";
                        oConn.runQueryLMD(strSqlUdtRD);

                        String strSqlUdtRV = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + dblImporteQuita + " where CT_ID = " + "'" + intIdOriComision + "'";
                        oConn.runQueryLMD(strSqlUdtRV);
                        String strSqlUdtRDC = "UPDATE vta_cliente "
                                + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + dblImporteQuita + " where CT_ID = " + "'" + IdDestinoComision + "'";
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
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigenComision);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", IdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImporteQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestinoComision);
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
                    String strResp16 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigen + "\", \"" + strPass + "\", 10 ],\"id\":" + intIdOri + "}");
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
                                + "  \"from\": \"0x" + strCuentaOrigen + "\",\n"
                                + "  \"to\": \"0x" + strCuentaDestino + "\",\n"
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
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigen + "'";
                            oConn.runQueryLMD(strSqlUdtR);
                            String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                    + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTotal + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestino + "'";
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
                            movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigen);
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
                            movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestino);
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
                String strResulttComision;
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
                String strResp117 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigen + "\", \"" + strPass + "\", 10 ],\"id\":" + intIdOri + "}");
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
                            + "  \"from\": \"0x" + strCuentaOrigen + "\",\n"
                            + "  \"to\": \"0x" + strCuentaDestino + "\",\n"
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
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigen + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteQuita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestino + "'";
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
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigen);
                        movComi.setFieldString("MMC_IP", strAddr);
                        movComi.Agrega(oConn);
                        //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                        movComi = new MlmMovComis();
                        movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                        movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                        movComi.setFieldInt("CT_ID", IdDestinoComision);
                        movComi.setFieldString("MMC_NOTAS", "COMISION POR TRADING");
                        movComi.setFieldInt("MMC_SINCR", 1);
                        movComi.setFieldDouble("MMC_CARGO", 0.0);
                        movComi.setFieldDouble("MMC_ABONO", dblImporteQuita);
                        movComi.setFieldString("MMC_TRANSACCION", strNumTransacciones);
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestino);
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
                // </editor-fold>
                String strResultt = "";
                //Validamos si fue exitoso la transaccion por ethereum
                //Sincronizamos con la red privada Ethereum - ChipCoin
                aplica = new AplicaChipCoinsCarteras(oConn);
                boolean bolSincroniza = false;
                String strNumTransaccion = "";
                String strErrorJson = "";
                //Ejecutamos instrucciones RPC
                String strResp18 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigen + "\", \"" + strPass + "\", 10 ],\"id\":" + intIdOri + "}");
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
                            + "  \"from\": \"0x" + strCuentaOrigen + "\",\n"
                            + "  \"to\": \"0x" + strCuentaDestino + "\",\n"
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
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblMontoTotal + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigen + "'";
                        oConn.runQueryLMD(strSqlUdtR);
                        String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                                + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblMontoTotal + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestino + "'";
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
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigen);
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
                        movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestino);
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
        return strResult;
    }

    public void TransaccionesTrading(int intTypeMoneda, int intIdCte, double dblSaldo, double dblMontoRetira, String strCuenta, double dblParidad, String strCartera, double dblImporte) throws UnknownHostException {
        Fechas fecha = new Fechas();
        MlmTransaccionesTrading TradingTransaccion = new MlmTransaccionesTrading();
        InetAddress localHost = InetAddress.getLocalHost();
        String strAddress = localHost.getHostAddress();
        String strResultado = "";
        try {
            String strRazonsocial = "";
            String strEstado = "";
            String strDatosCliente = "select * from vta_cliente where CT_ID = " + intIdCte + "";
            ResultSet rs = oConn.runQuery(strDatosCliente, true);
            while (rs.next()) {
                strRazonsocial = rs.getString("CT_RAZONSOCIAL");
                strEstado = rs.getString("CT_ESTADO");
                // <editor-fold defaultstate="collapsed" desc="Realizamos transaccion de dolares ">
                if (intTypeMoneda == 7) {
                    TradingTransaccion = new MlmTransaccionesTrading();
                    TradingTransaccion.setFieldInt("CT_ID", intIdCte);
                    TradingTransaccion.setFieldString("TT_NOMBRE", strRazonsocial);
                    TradingTransaccion.setFieldString("TT_FECHA", fecha.getFechaActual());
                    TradingTransaccion.setFieldString("TT_HORA", fecha.getHoraActual());
                    TradingTransaccion.setFieldString("TT_IP", strAddress);
                    TradingTransaccion.setFieldDouble("TT_SALDO", dblSaldo);
                    TradingTransaccion.setFieldString("TT_TIPO_TRANSACCION", " DOLARES");
                    TradingTransaccion.setFieldDouble("TT_IMPORTE", dblMontoRetira);
                    TradingTransaccion.setFieldDouble("TT_MONTO", dblMontoRetira);
                    TradingTransaccion.setFieldString("TT_ESTADO", strEstado);
                    TradingTransaccion.setFieldInt("TT_CONFIRMA", 0);
                    TradingTransaccion.setFieldString("TT_CUENTA", strCuenta);
                    strResultado = TradingTransaccion.Agrega(oConn);

                    String strSqlUdtRC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DOLAR = CHIP_TOT_MONEDAS_DOLAR -" + dblMontoRetira + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRC);
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Realizamos transaccion de bitcoins ">
                if (intTypeMoneda == 8) {
                    TradingTransaccion = new MlmTransaccionesTrading();
                    TradingTransaccion.setFieldInt("CT_ID", intIdCte);
                    TradingTransaccion.setFieldString("TT_NOMBRE", strRazonsocial);
                    TradingTransaccion.setFieldDouble("TT_SALDO", dblSaldo);
                    TradingTransaccion.setFieldDouble("TT_PARIDAD", dblParidad);
                    TradingTransaccion.setFieldString("TT_FECHA", fecha.getFechaActual());
                    TradingTransaccion.setFieldString("TT_HORA", fecha.getHoraActual());
                    TradingTransaccion.setFieldString("TT_IP", strAddress);
                    TradingTransaccion.setFieldString("TT_TIPO_TRANSACCION", "BITCOINS");
                    TradingTransaccion.setFieldDouble("TT_IMPORTE", dblImporte);
                    TradingTransaccion.setFieldDouble("TT_MONTO", dblMontoRetira);
                    TradingTransaccion.setFieldString("TT_ESTADO", strEstado);
                    TradingTransaccion.setFieldInt("TT_CONFIRMA", 0);
                    TradingTransaccion.setFieldString("TT_CUENTA", strCuenta);
                    TradingTransaccion.setFieldString("TT_CUENTA", strCartera);
                    strResultado = TradingTransaccion.Agrega(oConn);

                    String strSqlUdtRC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_BTC = CHIP_TOT_MONEDAS_BTC -" + dblMontoRetira + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRC);
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Realizamos transaccion de Etherums">
                if (intTypeMoneda == 9) {
                    TradingTransaccion = new MlmTransaccionesTrading();
                    TradingTransaccion.setFieldInt("CT_ID", intIdCte);
                    TradingTransaccion.setFieldString("TT_NOMBRE", strRazonsocial);
                    TradingTransaccion.setFieldDouble("TT_SALDO", dblSaldo);
                    TradingTransaccion.setFieldDouble("TT_PARIDAD", dblParidad);
                    TradingTransaccion.setFieldString("TT_FECHA", fecha.getFechaActual());
                    TradingTransaccion.setFieldString("TT_HORA", fecha.getHoraActual());
                    TradingTransaccion.setFieldString("TT_IP", strAddress);
                    TradingTransaccion.setFieldString("TT_TIPO_TRANSACCION", "ETHERUMS");
                    TradingTransaccion.setFieldDouble("TT_IMPORTE", dblImporte);
                    TradingTransaccion.setFieldDouble("TT_MONTO", dblMontoRetira);
                    TradingTransaccion.setFieldString("TT_ESTADO", strEstado);
                    TradingTransaccion.setFieldInt("TT_CONFIRMA", 0);
                    TradingTransaccion.setFieldString("TT_CUENTA", strCuenta);
                    TradingTransaccion.setFieldString("TT_CUENTA", strCartera);

                    strResultado = TradingTransaccion.Agrega(oConn);

                    String strSqlUdtRC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_ETHERUM = CHIP_TOT_MONEDAS_ETHERUM -" + dblMontoRetira + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRC);
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Realizamos transaccion de Dash">
                if (intTypeMoneda == 10) {
                    TradingTransaccion = new MlmTransaccionesTrading();
                    TradingTransaccion.setFieldInt("CT_ID", intIdCte);
                    TradingTransaccion.setFieldString("TT_NOMBRE", strRazonsocial);
                    TradingTransaccion.setFieldDouble("TT_SALDO", dblSaldo);
                    TradingTransaccion.setFieldDouble("TT_PARIDAD", dblParidad);
                    TradingTransaccion.setFieldString("TT_FECHA", fecha.getFechaActual());
                    TradingTransaccion.setFieldString("TT_HORA", fecha.getHoraActual());
                    TradingTransaccion.setFieldString("TT_IP", strAddress);
                    TradingTransaccion.setFieldString("TT_TIPO_TRANSACCION", "DASH");
                    TradingTransaccion.setFieldDouble("TT_IMPORTE", dblImporte);
                    TradingTransaccion.setFieldDouble("TT_MONTO", dblMontoRetira);
                    TradingTransaccion.setFieldString("TT_ESTADO", strEstado);
                    TradingTransaccion.setFieldInt("TT_CONFIRMA", 0);
                    TradingTransaccion.setFieldString("TT_CUENTA", strCuenta);
                    TradingTransaccion.setFieldString("TT_CUENTA", strCartera);
                    strResultado = TradingTransaccion.Agrega(oConn);

                    String strSqlUdtRC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_DASH = CHIP_TOT_MONEDAS_DASH -" + dblMontoRetira + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRC);
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Realizamos transaccion de Ripple">
                if (intTypeMoneda == 11) {
                    TradingTransaccion = new MlmTransaccionesTrading();
                    TradingTransaccion.setFieldInt("CT_ID", intIdCte);
                    TradingTransaccion.setFieldString("TT_NOMBRE", strRazonsocial);
                    TradingTransaccion.setFieldDouble("TT_SALDO", dblSaldo);
                    TradingTransaccion.setFieldDouble("TT_PARIDAD", dblParidad);
                    TradingTransaccion.setFieldString("TT_FECHA", fecha.getFechaActual());
                    TradingTransaccion.setFieldString("TT_HORA", fecha.getHoraActual());
                    TradingTransaccion.setFieldString("TT_IP", strAddress);
                    TradingTransaccion.setFieldString("TT_TIPO_TRANSACCION", "RIPPLE");
                    TradingTransaccion.setFieldDouble("TT_IMPORTE", dblImporte);
                    TradingTransaccion.setFieldDouble("TT_MONTO", dblMontoRetira);
                    TradingTransaccion.setFieldString("TT_ESTADO", strEstado);
                    TradingTransaccion.setFieldInt("TT_CONFIRMA", 0);
                    TradingTransaccion.setFieldString("TT_CUENTA", strCuenta);
                    TradingTransaccion.setFieldString("TT_CUENTA", strCartera);
                    strResultado = TradingTransaccion.Agrega(oConn);

                    String strSqlUdtRC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS_RIPPLE = CHIP_TOT_MONEDAS_RIPPLE -" + dblMontoRetira + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRC);
                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Realizamos transaccion de Chipcoin">
                if (intTypeMoneda == 12) {
                    TradingTransaccion = new MlmTransaccionesTrading();
                    TradingTransaccion.setFieldInt("CT_ID", intIdCte);
                    TradingTransaccion.setFieldString("TT_NOMBRE", strRazonsocial);
                    TradingTransaccion.setFieldDouble("TT_SALDO", dblSaldo);
                    TradingTransaccion.setFieldDouble("TT_PARIDAD", dblParidad);
                    TradingTransaccion.setFieldString("TT_FECHA", fecha.getFechaActual());
                    TradingTransaccion.setFieldString("TT_HORA", fecha.getHoraActual());
                    TradingTransaccion.setFieldString("TT_IP", strAddress);
                    TradingTransaccion.setFieldString("TT_TIPO_TRANSACCION", "RIPPLE");
                    TradingTransaccion.setFieldDouble("TT_IMPORTE", dblImporte);
                    TradingTransaccion.setFieldDouble("TT_MONTO", dblMontoRetira);
                    TradingTransaccion.setFieldString("TT_ESTADO", strEstado);
                    TradingTransaccion.setFieldInt("TT_CONFIRMA", 0);
                    TradingTransaccion.setFieldString("TT_CUENTA", strCuenta);
                    strResultado = TradingTransaccion.Agrega(oConn);

                    String strSqlUdtRC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS -" + dblMontoRetira + " where CT_ID = " + "'" + intIdCte + "'";
                    oConn.runQueryLMD(strSqlUdtRC);
                }
                // </editor-fold>
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

    }

    public String FondeoTradingMultinivel(int intIdCte, String strFechaDeposito, String strAutorizado, String strSucursal, int intIdOri, int IdDestino, double dblImportePago, String strCuentaOrigen, String strCuentaDestino, String strPass, String strCuentaOrigenComision, String strMyPassSecretComision, int intIdOriComision, String strCuentaDestinoComision, int IdDestinoComision) throws Exception {
        Fechas fecha = new Fechas();
        InetAddress localHost = InetAddress.getLocalHost();
        String strAddr = localHost.getHostAddress();
        String strResultados = "";

        double dblporcentaje = 2.8 / 100;
        double dblImportequita = 0.0;
        double dblImporteTot = 0.0;
        double dblSaldo = 0.0;
        double dblTotSaldo = 0.0;
        String strSqli = "select PGF_SALDO  from mlm_pagos_fondeo where CT_ID = " + intIdCte + " ORDER BY PGF_ID DESC LIMIT 0,1";
        ResultSet rs = oConn.runQuery(strSqli, true);
        while (rs.next()) {
            dblSaldo = rs.getDouble("PGF_SALDO");
        }
        rs.close();
        String strRazonsocial = "";
        String strEstado = "";
        String strDatosCliente = "select * from vta_cliente where CT_ID = " + intIdCte;
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
        FondeoPago.setFieldString("PGF_FECHA_DEPOSITO", fecha.FormateaBD(strFechaDeposito, "/"));
        FondeoPago.setFieldString("PGF_SUCURSAL", strSucursal);
        FondeoPago.setFieldString("PGF_NUM_AUTORIZA", strAutorizado);
        FondeoPago.setFieldInt("CT_ID", intIdCte);
        FondeoPago.setFieldString("PGF_NOMBRE", strRazonsocial);
        FondeoPago.setFieldString("PGF_IP_ADDRESS", strAddr);
        FondeoPago.setFieldString("PGF_ESTADO", strEstado);
        FondeoPago.setFieldInt("PGF_TIPO_PAGO", 1);
        FondeoPago.setFieldString("PGF_TYPE_FONDEO", "FONDEO MULTINIVEL");
        FondeoPago.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
        FondeoPago.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
        FondeoPago.setFieldDouble("PGF_IMPORTE", dblImporteTot);
        FondeoPago.setFieldDouble("PGF_SALDO", dblSaldo);

        strResultados = FondeoPago.Agrega(oConn);

        dblTotSaldo = dblSaldo + dblImportePago;
        String strUpdateF = "update mlm_pagos_fondeo set PGF_SALDO = " + dblTotSaldo + " WHERE CT_ID = " + intIdCte;
        oConn.runQueryLMD(strUpdateF);

        // <editor-fold defaultstate="collapsed" desc="Realiza transaccion de fondeo multinivel">
        //Validamos si fue exitoso la transaccion por ethereum
        //Sincronizamos con la red privada Ethereum - ChipCoin
        AplicaChipCoinsCarteras aplica = new AplicaChipCoinsCarteras(oConn);
        boolean bolSincroniza = false;
        String strNumTransaccion = "";
        String strErrorJson = "";
        //Ejecutamos instrucciones RPC
        String strResp28 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigen + "\", \"" + strPass + "\", 10 ],\"id\":" + intIdOri + "}");
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
                    + "  \"from\": \"0x" + strCuentaOrigen + "\",\n"
                    + "  \"to\": \"0x" + strCuentaDestino + "\",\n"
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
                        + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImporteTot + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigen + "'";
                oConn.runQueryLMD(strSqlUdtR);
                String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                        + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImporteTot + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestino + "'";
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
                movComi.setFieldString("MMC_NOTAS", "FONDEO TRADICIONAL");
                movComi.setFieldDouble("MMC_CARGO", dblImporteTot);
                movComi.setFieldDouble("MMC_ABONO", 0.0);
                movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigen);
                movComi.setFieldString("MMC_IP", strAddr);
                movComi.Agrega(oConn);
                //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                movComi = new MlmMovComis();
                movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                movComi.setFieldInt("CT_ID", IdDestino);
                movComi.setFieldInt("MMC_SINCR", 1);
                movComi.setFieldString("MMC_NOTAS", "FONDEO TRADICIONAL");
                movComi.setFieldDouble("MMC_CARGO", 0.0);
                movComi.setFieldDouble("MMC_ABONO", dblImporteTot);
                movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestino);
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
        String strResp29 = aplica.sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"0x" + strCuentaOrigenComision + "\", \"" + strMyPassSecretComision + "\", 10 ],\"id\":" + intIdOriComision + "}");
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
                    + "  \"from\": \"0x" + strCuentaOrigenComision + "\",\n"
                    + "  \"to\": \"0x" + strCuentaDestinoComision + "\",\n"
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
                        + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + dblImportequita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaOrigenComision + "'";
                oConn.runQueryLMD(strSqlUdtR);
                String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                        + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + dblImportequita + " where CW_NUMERO_CUENTA = " + "'" + strCuentaDestinoComision + "'";
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
                movComi.setFieldString("MMC_NOTAS", "COMISION FONDEO TRADICIONAL");
                movComi.setFieldDouble("MMC_CARGO", dblImportequita);
                movComi.setFieldDouble("MMC_ABONO", 0.0);
                movComi.setFieldString("MMC_TRANSACCION", strNumTransaccioon);
                movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaOrigenComision);
                movComi.setFieldString("MMC_IP", strAddr);
                movComi.Agrega(oConn);
                //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                movComi = new MlmMovComis();
                movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                movComi.setFieldInt("CT_ID", IdDestinoComision);
                movComi.setFieldInt("MMC_SINCR", 1);
                movComi.setFieldString("MMC_NOTAS", "COMISION TRADICIONAL");
                movComi.setFieldDouble("MMC_CARGO", 0.0);
                movComi.setFieldDouble("MMC_ABONO", dblImportequita);
                movComi.setFieldString("MMC_TRANSACCION", strNumTransaccioon);
                movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaDestinoComision);
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
        return strResultados;

    }

    public String FondeoTradingTradicional(int intIdCte, String strTipoPago, double dblImportePago, String strAprobacion, String strFechaDeposito, String strSucursal) throws SQLException, UnknownHostException {
        Fechas fecha = new Fechas();
        String strResulta = "";
        InetAddress localHost = InetAddress.getLocalHost();
        String strAddress = localHost.getHostAddress();
        try {
            double dblporcentaje = 2.8 / 100;
            double dblImportequita = 0.0;
            double dblImporteTot = 0.0;
            String strRazonsocial = "";
            String strEstado = "";
            String strDatosCliente = "select * from vta_cliente where CT_ID = " + intIdCte;
            ResultSet rs = oConn.runQuery(strDatosCliente, true);
            while (rs.next()) {
                strRazonsocial = rs.getString("CT_RAZONSOCIAL");
                strEstado = rs.getString("CT_ESTADO");
            }
            rs.close();
            double dblSaldo = 0.0;
            double dblTotSaldo = 0.0;
            String strSql1 = "select PGF_SALDO  from mlm_pagos_fondeo where CT_ID = " + intIdCte + " ORDER BY PGF_ID DESC LIMIT 0,1";
            rs = oConn.runQuery(strSql1, true);
            while (rs.next()) {
                dblSaldo = rs.getDouble("PGF_SALDO");
            }
            rs.close();

            dblImportequita = dblImportePago * dblporcentaje;

            dblImporteTot = dblImportePago - dblImportequita;

            MlmPagosFondeo FondeoPago = new MlmPagosFondeo();
            Periodos periodo = new Periodos();

            FondeoPago.setFieldString("PGF_FECHA", fecha.getFechaActual());
            FondeoPago.setFieldString("PGF_HORA", fecha.getHoraActual());
            FondeoPago.setFieldString("PGF_FECHA_DEPOSITO", fecha.FormateaBD(strFechaDeposito, "/"));
            FondeoPago.setFieldString("PGF_SUCURSAL", strSucursal);
            FondeoPago.setFieldString("PGF_NUM_AUTORIZA", strAprobacion);
            FondeoPago.setFieldInt("CT_ID", intIdCte);
            FondeoPago.setFieldString("PGF_NOMBRE", strRazonsocial);
            FondeoPago.setFieldString("PGF_IP_ADDRESS", strAddress);
            FondeoPago.setFieldString("PGF_ESTADO", strEstado);
            FondeoPago.setFieldString("PGF_TIPO_PAGO", strTipoPago);
            FondeoPago.setFieldString("PGF_TYPE_FONDEO", "FONDEO TRADICIONAL");
            FondeoPago.setFieldInt("MPE_ID", periodo.getPeriodoActual(oConn));
            FondeoPago.setFieldInt("MSE_ID", periodo.getPeriodoSemanalActual(oConn));
            FondeoPago.setFieldDouble("PGF_IMPORTE", dblImporteTot);
            FondeoPago.setFieldDouble("PGF_SALDO", dblSaldo);

            strResulta = FondeoPago.Agrega(oConn);
            dblTotSaldo = dblSaldo + dblImporteTot;
            String strUpdateF = "update mlm_pagos_fondeo set PGF_SALDO = " + dblTotSaldo + " WHERE CT_ID = " + intIdCte;
            oConn.runQueryLMD(strUpdateF);
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return strResulta;
    }

    // </editor-fold>
}
