/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.chipcoin;

import apiSiweb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Adolfo
 */
public class SaldoMonedas {

    //Propiedades
    private Conexion oConn;
    private double monedasDolar = 0.0;
    private double monedasBTC = 0.0;
    private double monedasEtherum = 0.0;
    private double monedasDash = 0.0;
    private double monedasRipple = 0.0;
    private double monedasChipcoin = 0.0;
    private double id = 0;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger();

    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }

    /**
     * @return the monedasDolar
     */
    public double getMonedasDolar() {
        return monedasDolar;
    }

    /**
     * @param monedasDolar the monedasDolar to set
     */
    public void setMonedasDolar(double monedasDolar) {
        this.monedasDolar = monedasDolar;
    }

    /**
     * @return the monedasBTC
     */
    public double getMonedasBTC() {
        return monedasBTC;
    }

    /**
     * @param monedasBTC the monedasBTC to set
     */
    public void setMonedasBTC(double monedasBTC) {
        this.monedasBTC = monedasBTC;
    }

    /**
     * @return the monedasEtherum
     */
    public double getMonedasEtherum() {
        return monedasEtherum;
    }

    /**
     * @param monedasEtherum the monedasEtherum to set
     */
    public void setMonedasEtherum(double monedasEtherum) {
        this.monedasEtherum = monedasEtherum;
    }

    /**
     * @return the monedasDash
     */
    public double getMonedasDash() {
        return monedasDash;
    }

    /**
     * @param monedasDash the monedasDash to set
     */
    public void setMonedasDash(double monedasDash) {
        this.monedasDash = monedasDash;
    }

    /**
     * @return the monedasRipple
     */
    public double getMonedasRipple() {
        return monedasRipple;
    }

    /**
     * @param monedasRipple the monedasRipple to set
     */
    public void setMonedasRipple(double monedasRipple) {
        this.monedasRipple = monedasRipple;
    }

    /**
     * @return the monedasChipcoin
     */
    public double getMonedasChipcoin() {
        return monedasChipcoin;
    }

    /**
     * @param monedasChipcoin the monedasChipcoin to set
     */
    public void setMonedasChipcoin(double monedasChipcoin) {
        this.monedasChipcoin = monedasChipcoin;
    }

    //Consutructor
    public SaldoMonedas(Conexion oConn, int int1) {
        this.oConn = oConn;
        this.id = int1;
    }

    //Metodos
    public ResultSet moneda(double id) throws SQLException{
        String strSqlMoneda = "select  CHIP_TOT_MONEDAS_DOLAR, CHIP_TOT_MONEDAS_BTC, CHIP_TOT_MONEDAS_ETHERUM, CHIP_TOT_MONEDAS_DASH,CHIP_TOT_MONEDAS_RIPPLE, CHIP_TOT_MONEDAS from vta_cliente WHERE CT_ID = " + id;
        ResultSet rs = oConn.runQuery(strSqlMoneda, true);
        try {
           
            while (rs.next()) {
                setMonedasDolar(rs.getDouble("CHIP_TOT_MONEDAS_DOLAR"));
                setMonedasBTC(rs.getDouble("CHIP_TOT_MONEDAS_BTC"));
                setMonedasEtherum(rs.getDouble("CHIP_TOT_MONEDAS_ETHERUM"));
                setMonedasDash(rs.getDouble("CHIP_TOT_MONEDAS_DASH"));
                setMonedasRipple(rs.getDouble("CHIP_TOT_MONEDAS_RIPPLE"));
                setMonedasChipcoin(rs.getDouble("CHIP_TOT_MONEDAS"));
            }
        } catch (SQLException ex) {
        }
        return rs;
    }

}
