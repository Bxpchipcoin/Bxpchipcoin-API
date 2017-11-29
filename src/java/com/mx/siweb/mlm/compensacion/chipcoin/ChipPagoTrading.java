/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.chipcoin;

import Tablas.MlmMovComis;
import com.mx.siweb.estadocuenta.TasaCambio;
import com.mx.siweb.estadocuenta.lstTasaCambio;
import apiSiweb.Operaciones.Conexion;
import apiSiweb.Utilerias.Fechas;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author ZeusSIWEB
 */
public class ChipPagoTrading {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private Conexion oConn;
    protected String strResultLast;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ChipPagoTrading.class.getName());

    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }

    public ChipPagoTrading(Conexion oConn) {
        this.oConn = oConn;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">

    public void EvalPagoFondeoMultinivel() throws UnknownHostException {
        Fechas fecha = new Fechas();
        InetAddress localHost = InetAddress.getLocalHost();
        String strAddr = localHost.getHostAddress();
        //Obtenemos las monedas y paridades
        lstTasaCambio lstTasas = new lstTasaCambio();
        getTasasCambio(lstTasas);
        try {
            //Iniciamos transaccion
            this.oConn.runQueryLMD("BEGIN");

            //Procesamos pagos confirmados y que no han sido procesados
            int intIdCte = 0;
            int intIdPago = 0;
            double dblPuntosChipCoins = 0.0;
            String strSql = "select PGF_ID,PGF_DESCUENTO,mlm_pagos_fondeo.CT_ID,PGF_IMPORTE,PGF_FECHA_DEPOSITO"
                    + ",(SELECT g.GD_GANA_INICIADOR  FROM sii_ganancias_diarias g where g.GD_FECHA<= PGF_FECHA_DEPOSITO order by g.GD_FECHA desc limit 0 ,1 ) as TASA_CAMBIO"
                    + "  from mlm_pagos_fondeo,vta_cliente where mlm_pagos_fondeo.CT_ID = vta_cliente.CT_ID "
                    + " and PGF_CONFIRMADO = 1 "
                    + "  "//and PROS_YA_PROCESO = 0           
                    + " order by mlm_pagos_fondeo.CT_ID";
            ResultSet rs = oConn.runQuery(strSql);
            while (rs.next()) {
                intIdCte = rs.getInt("CT_ID");
                intIdPago = rs.getInt("PGF_ID");
                double dblTasaCambio = rs.getDouble("TASA_CAMBIO");
                dblPuntosChipCoins = rs.getDouble("PGF_IMPORTE") / dblTasaCambio;

                String strUpdate = "update vta_cliente set "
                        + " CHIP_CHIPCOINS =  CHIP_CHIPCOINS + " + dblPuntosChipCoins + " "
                        + " WHERE CT_ID = " + intIdCte;
                this.oConn.runQueryLMD(strUpdate);
                //MOVIMIENTO DE ABONO MLM_MOV_COMIS
                MlmMovComis movComi = new MlmMovComis();
                movComi.setFieldString("MMC_FECHA", fecha.getFechaActual());
                movComi.setFieldString("MMC_HORA", fecha.getHoraActual());
                movComi.setFieldInt("CT_ID", intIdCte);
                movComi.setFieldInt("MMC_SINCR", 1);
                movComi.setFieldInt("PGF_ID",intIdPago);
                movComi.setFieldDouble("MMC_CARGO", 0.0);
                movComi.setFieldString("MMC_NOTAS", "FONDEO MULTINIVEL");
                movComi.setFieldDouble("MMC_ABONO", dblPuntosChipCoins);
                movComi.setFieldString("MMC_TRANSACCION", "");
                movComi.setFieldString("MMC_NUMERO_CUENTA", "");
                movComi.setFieldString("MMC_IP", strAddr);
                movComi.Agrega(oConn);

            }
            rs.close();

            //Terminamos transaccion
            if (this.strResultLast.equals("OK")) {
                this.oConn.runQueryLMD("COMMIT");
            } else {
                this.oConn.runQueryLMD("ROLLBACK");
            }

        } catch (SQLException ex) {
            log.error(ex.getMessage() + " " + ex.getSQLState());
        }
    }

    public void EvalPagoFondeoTradicional() {
        try {
            //Iniciamos transaccion
            this.oConn.runQueryLMD("BEGIN");

            //Procesamos pagos confirmados y que no han sido procesados
            String strSql = "select PGF_ID,PGF_DESCUENTO,mlm_pagos_fondeo.CT_ID,PGF_IMPORTE,PGF_FECHA_DEPOSITO"
                    + "  from mlm_pagos_fondeo,vta_cliente where mlm_pagos_fondeo.CT_ID = vta_cliente.CT_ID "
                    + " and PGF_CONFIRMADO = 1 "
                    + "  "//and PROS_YA_PROCESO = 0           
                    + " order by mlm_pagos_fondeo.CT_ID";
            ResultSet rs = oConn.runQuery(strSql);
            while (rs.next()) {
                double dblPuntosDolar = rs.getDouble("PGF_IMPORTE");
                String strUpdate = "update vta_cliente set "
                        + " CHIP_TOT_MONEDAS_DOLAR =  CHIP_TOT_MONEDAS_DOLAR + " + dblPuntosDolar + " "
                        + " WHERE CT_ID = " + rs.getInt("CT_ID");
                this.oConn.runQueryLMD(strUpdate);

            }
            rs.close();
            //Terminamos transaccion
            if (this.strResultLast.equals("OK")) {
                this.oConn.runQueryLMD("COMMIT");
            } else {
                this.oConn.runQueryLMD("ROLLBACK");
            }

        } catch (SQLException ex) {
            log.error(ex.getMessage() + " " + ex.getSQLState());
        }

    }

    private void getTasasCambio(lstTasaCambio lstTasas) {
        try {
            String strSql = "SELECT TKT_MONEDA FROM vta_tickets group by TKT_MONEDA";
            ResultSet rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (rs.getInt("TKT_MONEDA") != 2) {
                    //Generamos objeto de conversion
                    TasaCambio tCambio = new TasaCambio();
                    tCambio.setMoneda1(2);
                    tCambio.setMoneda2(rs.getInt("TKT_MONEDA"));
                    //Buscamos la tasa de cambio de esta moneda
                    String strSqlT = "select * from vta_tasacambio where "
                            + " (TC_MONEDA1 = 2 and TC_MONEDA2 = " + tCambio.getMoneda2() + ") || "
                            + " (TC_MONEDA2 = 2 and TC_MONEDA1 = " + tCambio.getMoneda2() + ") "
                            + " order by TC_FECHA DESC LIMIT 0,1";
                    System.out.println("strSqlT:" + strSqlT);
                    ResultSet rs2 = this.oConn.runQuery(strSqlT, true);
                    while (rs2.next()) {
                        tCambio.setValor(rs2.getDouble("TC_PARIDAD"));
                    }
//               if(rs2.getStatement() != null )rs2.getStatement().close(); 
                    rs2.close();
                    lstTasas.Agrega(tCambio);
                }
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            log.error(ex.getMessage() + " " + ex.getSQLState());
        }
    }

    // </editor-fold>
}
