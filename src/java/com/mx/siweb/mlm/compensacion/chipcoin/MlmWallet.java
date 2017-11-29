/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.chipcoin;

import Tablas.MlmMovComis;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import apiSiweb.Operaciones.Conexion;
import apiSiweb.Utilerias.Fechas;
import java.math.BigInteger;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author casajosefa
 */
public class MlmWallet {

    /**
     * Realiza la transaccion de monedas de una cuenta a otra
     *
     * @param intIdCliente Es el ID del cliente que realiza la transaccion
     * @param strNumCtaOri Es la cuenta de origen
     * @param strPass Es la contraseña de la cuenta origen
     * @param strNumCtaDest Es la cuenta destino de la transaccion
     * @param fMonto Es el monto que va tranferir
     * @param oConn Es la variable de conexion
     * @return Nos regresa un "OK" indicando que la transaccion fue correcta o
     * nos regresa el error
     * @throws java.sql.SQLException
     */
    public String transaccionWallet(int intIdCliente, String strNumCtaOri, String strPass, String strNumCtaDest, float fMonto, Conexion oConn)
            throws SQLException, Exception {
        Fechas fecha = new Fechas();
        oConn.open();
        InetAddress localHost = InetAddress.getLocalHost();
        String strResult = "OK";

        float flCuentaCliente = (float) (fMonto * .90);
        float flCuentaMaestra = (float) (fMonto * .10);

        //String strAddr = request.getRemoteAddr();
        String strAddr = localHost.getHostAddress();
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
                + " from vta_sucursal s, vta_cliente c  WHERE  SC_BANDERA ='MX' AND c.SC_ID = s.SC_ID  AND c.CT_ID = " + intIdCliente + " LIMIT 0,1";
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
        String strCuentaMaestra = "0x77a389210202b4d5776a447ab2a370d63279c3d1";
        //double dblPorcentajeMaestra = 0.1;

        int intIdDestPM = 0;

// Obtenemos datos de la cuenta destino
        String strDatosCtaDestPM = "select CW_ID, CW_TOT_CHIPCOINS, "
                + " CW_NOMBRE_CUENTA, CT_ID, CW_PASS "
                + " from mlm_chipcoin_wallet WHERE CW_NUMERO_CUENTA = " + "'" + strCuentaMaestra + "'";
        rs = oConn.runQuery(strDatosCtaDestPM, true);
        while (rs.next()) {
            intIdDestPM = rs.getInt("CT_ID");
        }
        rs.close();

//Validamos si fue exitoso la transaccion por ethereum
//Sincronizamos con la red privada Ethereum - ChipCoin
        AplicaChipCoinsCarteras aplica = new AplicaChipCoinsCarteras(oConn);
        boolean bolSincroniza = false;

        String strNumTransaccion = "";
        String strErrorJson = "";

//Ejecutamos instrucciones RPC
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
            //TRANSACCION EN CUENTA CLIENTE
            BigInteger bigNum = EtherUtil.convert((long) flCuentaCliente, EtherUtil.Unit.ETHER);
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
            //Marcamos que ya se sincronizo
            if (bolSincroniza) {
                //Guardamos en la base                            
                //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                        + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + flCuentaCliente + " where CW_NUMERO_CUENTA = " + "'" + strNumCtaOri + "'";
                oConn.runQueryLMD(strSqlUdtR);
                String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                        + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + flCuentaCliente + " where CW_NUMERO_CUENTA = " + "'" + strNumCtaDest + "'";
                oConn.runQueryLMD(strSqlUdtRD);

                String strSqlUdtRC = "UPDATE vta_cliente "
                        + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS - " + flCuentaCliente + " where CT_ID = " + "'" + intIdOri + "'";
                oConn.runQueryLMD(strSqlUdtRC);
                String strSqlUdtRDC = "UPDATE vta_cliente "
                        + " set CHIP_TOT_MONEDAS =  CHIP_TOT_MONEDAS + " + flCuentaCliente + " where CT_ID = " + "'" + intIdDest + "'";
                oConn.runQueryLMD(strSqlUdtRDC);
                //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                MlmMovComis movComi = new MlmMovComis();
                movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                movComi.setFieldInt("CT_ID", intIdOri);
                movComi.setFieldInt("MMC_SINCR", 1);
                movComi.setFieldDouble("MMC_CARGO", flCuentaCliente);
                movComi.setFieldDouble("MMC_ABONO", 0.0);
                movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                movComi.setFieldString("MMC_NUMERO_CUENTA", strNumCtaOri);
                movComi.setFieldString("MMC_IP", strAddr);
                movComi.Agrega(oConn);
                //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                movComi = new MlmMovComis();
                movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                movComi.setFieldInt("CT_ID", intIdDest);
                movComi.setFieldInt("MMC_SINCR", 1);
                movComi.setFieldDouble("MMC_CARGO", 0.0);
                movComi.setFieldDouble("MMC_ABONO", flCuentaCliente);
                movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                movComi.setFieldString("MMC_NUMERO_CUENTA", strNumCtaDest);
                movComi.setFieldString("MMC_IP", strAddr);
                movComi.Agrega(oConn);
            } else {
                strResult = "ERROR:Al sincronizar con el blockchain " + strResp2;
            }

//10% a cartera maestra
            if (!strCuentaMaestra.isEmpty() && flCuentaMaestra > 0) {

                boolean bolSincronizaaa = false;
                BigInteger bigNum2 = EtherUtil.convert((long) flCuentaMaestra, EtherUtil.Unit.ETHER);
                String strResp4 = aplica.sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                        + "  \"from\": \"0x" + strNumCtaOri + "\",\n"
                        + "  \"to\": \"0x" + strCuentaMaestra + "\",\n"
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
                    bolSincronizaaa = true;
                }

                if (bolSincronizaaa) {

                    //UPDATE A CARTERAS MLM_CHIPCOIN_WALLET
                    String strSqlUdtR = "UPDATE mlm_chipcoin_wallet "
                            + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS - " + flCuentaMaestra + " where CW_NUMERO_CUENTA = " + "'" + strNumCtaOri + "'";
                    oConn.runQueryLMD(strSqlUdtR);
                    String strSqlUdtRD = "UPDATE mlm_chipcoin_wallet "
                            + " set CW_TOT_CHIPCOINS = CW_TOT_CHIPCOINS + " + flCuentaMaestra + " where CW_NUMERO_CUENTA = " + "'" + strCuentaMaestra + "'";
                    oConn.runQueryLMD(strSqlUdtRD);

                    String strSqlUdtRC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS -" + flCuentaMaestra + " where CT_ID = " + "'" + intIdOri + "'";
                    oConn.runQueryLMD(strSqlUdtRC);
                    String strSqlUdtRDC = "UPDATE vta_cliente "
                            + " set CHIP_TOT_MONEDAS = CHIP_TOT_MONEDAS + " + flCuentaMaestra + " where CT_ID = " + "'" + intIdDestPM + "'";
                    oConn.runQueryLMD(strSqlUdtRDC);

                    //MOVIMIENTO DE CARGO MLM_MOV_COMIS
                    MlmMovComis movComi = new MlmMovComis();
                    movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                    movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                    movComi.setFieldInt("CT_ID", intIdOri);
                    movComi.setFieldInt("MMC_SINCR", 1);
                    movComi.setFieldDouble("MMC_CARGO", flCuentaMaestra);
                    movComi.setFieldDouble("MMC_ABONO", 0.0);
                    movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                    movComi.setFieldString("MMC_NUMERO_CUENTA", strNumCtaOri);
                    movComi.setFieldString("MMC_IP", strAddr);
                    movComi.Agrega(oConn);
                    //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                    movComi = new MlmMovComis();
                    movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                    movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                    movComi.setFieldInt("CT_ID", intIdDestP);
                    movComi.setFieldInt("MMC_SINCR", 1);
                    movComi.setFieldDouble("MMC_CARGO", 0.0);
                    movComi.setFieldDouble("MMC_ABONO", flCuentaMaestra);
                    movComi.setFieldString("MMC_TRANSACCION", strNumTransaccion);
                    movComi.setFieldString("MMC_NUMERO_CUENTA", strCuentaMaestra);
                    movComi.setFieldString("MMC_IP", strAddr);
                    movComi.Agrega(oConn);
                } else {
                    strResult = "ERROR:Al sincronizar con el blockchain " + strResp2;
                }
            }

        } else if (!strErrorJson.isEmpty()) {
            strResult = "ERROR:" + strErrorJson + " favor de verificar el password de la cuenta";
        } else {
            strResult = "ERROR:El password de la cuenta origen no es valido favor de verificar ";
        }
        return strResult;
    }
}
