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
 * @author ZeusSIWEB
 */
public class ChipPrecioMoneda {

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
    public double getNuevoValor(String strFecha) {
        double dblNvoValor = 0;
        try {

            double dblDiff = 0;
            double dblTodDif = 0;
            double dblTodDiff = 0;
            double dblValAnterior = 0;
            double dnlTotMonedas = 0;
            String strSql = "select getChipCalcPrecioVentas(" + strFecha + ") as tot_ventas,"
                    + "getChipCalcPrecioCompras(" + strFecha + ") as tot_compras, getChipCalcPrecioVentas(" + strFecha + ") - getChipCalcPrecioCompras(" + strFecha + ") as diff"
                    + ",(SELECT gd_gana_iniciador FROM `sii_ganancias_diarias` where gd_fecha< " + strFecha + " order by gd_Fecha desc limit 0,1) as valorAcnterior,"
                    + "getChipCalcPrecioMonedas(" + strFecha + ") as tot_monedas";
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                dblDiff = rs.getDouble("diff");
                dblValAnterior = rs.getDouble("valorAcnterior");
                dnlTotMonedas = rs.getDouble("tot_monedas");

                if (dblDiff > 0) {
                    dblTodDif = dblDiff * 0.035;
                } else if (dblDiff < 0) {
                    dblTodDif = dblDiff * 0.070;
                }

                dblTodDiff = dblTodDif / dnlTotMonedas;
                dblNvoValor = dblValAnterior + dblTodDiff;
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        asignaNuevoValor(dblNvoValor, strFecha);
        return dblNvoValor;

    }

    public void asignaNuevoValor(double dblNvoValor, String strFecha) {

        if (dblNvoValor != 0) {
            boolean boolExixte = false;
            // Obtenemos datos del precio del chipcoin
            try {
                double dblEjecutivo = 0.0;
                double dblMaster = 0.0;
                double dblElite = 0.0;
                String strFechaValorAnt = "select count(*) as cuantos from sii_ganancias_diarias where GD_FECHA = '" + strFecha + "'";
                ResultSet rs = oConn.runQuery(strFechaValorAnt, true);
                while (rs.next()) {
                    if (rs.getInt("cuantos") > 0) {
                        boolExixte = true;
                    }

                    if (boolExixte) {
                        String strSqlUdt = "UPDATE sii_ganancias_diarias "
                                + " set GD_GANA_INICIADOR = " + dblNvoValor + " where GD_FECHA = " + "'" + strFecha + "'";
                        oConn.runQueryLMD(strSqlUdt);
                    } else {
                        String strInsert = "insert into sii_ganancias_diarias(GD_FECHA,GD_GANA_INICIADOR,GD_GANA_EJECUTIVA,GD_GANA_MASTER,GD_GANA_ELITE)values('"
                                + strFecha + "'," + dblNvoValor + "," + dblEjecutivo + "," + dblMaster + "," + dblElite + ")";
                        oConn.runQueryLMD(strInsert);
                    }
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
            }
        }

    }
    // </editor-fold>
}
