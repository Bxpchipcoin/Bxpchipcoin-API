package com.mx.siweb.mlm.compensacion.chipcoin;

import apiSiweb.Operaciones.Conexion;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase realiza las operaciones de calculo de conversiones emtre monedas
 *
 * @author aleph_79
 */
public class MonedaTrading {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private Conexion oConn;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(MonedasTrading.class.getName());
    private boolean boolConversionAutomatica;
    private int intMonedaBase;
    private double monedasChip = 0.0;

    public boolean isBoolConversionAutomatica() {
        return boolConversionAutomatica;
    }

    public void setBoolConversionAutomatica(boolean boolConversionAutomatica) {
        this.boolConversionAutomatica = boolConversionAutomatica;
    }

    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public MonedaTrading(Conexion oConn) {
        this.oConn = oConn;
        this.boolConversionAutomatica = true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Metodos">
    /**
     *
     * @param intTipoConversion Es el tipo de paridad cambiar 1, contado, 2.., 4
     * diario oficial
     * @param intMonedaBase Es la moneda base de la conversion(origen)
     * @param intMonedaFinal Es la moneda destino
     * @return Regresa el factor de conversion
     */
    public double GetFactorConversionParidad(int intTipoConversion, int intMonedaBase, int intMonedaFinal) {
        return GetFactorConversionParidad("", intTipoConversion, intMonedaBase, intMonedaFinal);
    }

    /**
     *
     * @param strFecha Es la fecha de la operacion
     * @param intTipoConversion Es el tipo de paridad cambiar 1, contado, 2.., 4
     * diario oficial
     * @param intMonedaBase Es la moneda base de la conversion(origen)
     * @param intMonedaFinal Es la moneda destino
     * @return Regresa el factor de conversion
     */
    public double GetFactorConversionParidad(String strFecha, int intTipoConversion, int intMonedaBase, int intMonedaFinal) {
        double dblFactor = 0;
        int intMonedaFinalD = 2;
        double dblFactorDolar = 0;
        double dblFactorMoneda = 0;
        //Construimos consulta
        StringBuilder strSql = new StringBuilder("select TC_PARIDAD,TC_MONEDA1,TC_MONEDA2,TC_FECHA from vta_tasacambio where ");
        strSql.append("((TC_MONEDA1 = ").append(intMonedaBase).append(" and TC_MONEDA2=").append(intMonedaFinalD).append(") or ");
        strSql.append("(TC_MONEDA2 = ").append(intMonedaBase).append(" and TC_MONEDA1=").append(intMonedaFinalD);
        strSql.append(")) " + " and TTC_ID = ").append(intTipoConversion);

        //si la fecha no esta vacia
        if (strFecha != null) {
            if (!strFecha.isEmpty()) {
                strSql.append(" and TC_FECHA <= ").append(strFecha);
            }
        }
        strSql.append(" order by TC_FECHA DESC limit 0,1");
        try {
            ResultSet rs = this.oConn.runQuery(strSql.toString(), true);
            while (rs.next()) {
                //Factor para conversión de izquierda a derecha, Moneda extranjera moneda nacional
                dblFactorDolar = rs.getDouble("TC_PARIDAD");
                log.debug(rs.getString("TC_FECHA") + " " + dblFactor);
                //Validamos si es una conversion a la inversa
                //es decir que de moneda nacional a moneda extranjera
                //derecha a izquierda , dividimos 1 entre el factor
                if (rs.getInt("TC_MONEDA1") != intMonedaBase && this.boolConversionAutomatica) {
                    dblFactorDolar = 1 / dblFactorDolar;
                }
                this.intMonedaBase = rs.getInt("TC_MONEDA1");
                break;
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        System.out.println("dblFactorDolar" + dblFactorDolar);

        if (dblFactorDolar != 0 && intMonedaFinal != 2) {

            strSql = new StringBuilder("select TC_PARIDAD,TC_MONEDA1,TC_MONEDA2 from vta_tasacambio where ");
            strSql.append("((TC_MONEDA1 = ").append(intMonedaFinalD).append(" and TC_MONEDA2=").append(intMonedaFinal).append(") or ");
            strSql.append("(TC_MONEDA2 = ").append(intMonedaFinal).append(" and TC_MONEDA1=").append(intMonedaFinalD);
            strSql.append(")) order by TC_FECHA DESC limit 0,1");
            try {
                ResultSet rs = this.oConn.runQuery(strSql.toString(), true);
                while (rs.next()) {
                    //Factor para conversión de izquierda a derecha, Moneda extranjera moneda nacional
                    dblFactorMoneda = rs.getDouble("TC_PARIDAD");
                    //Validamos si es una conversion a la inversa
                    //es decir que de moneda nacional a moneda extranjera
                    //derecha a izquierda , dividimos 1 entre el factor
                    if (rs.getInt("TC_MONEDA1") != intMonedaBase && this.boolConversionAutomatica) {
                        dblFactor = 1 / dblFactor;
                    }
                    this.intMonedaBase = rs.getInt("TC_MONEDA1");
                    break;
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
            }

            dblFactor = dblFactorDolar / dblFactorMoneda;

        }

        return dblFactor;
    }

    public double GetFactorConversionParidadDolar(String strFecha, int intTipoConversion, int intMonedaFinal) {
        int intMonedaBase = 2;
        double dblFactorDolar = 0;
        StringBuilder strSqld = new StringBuilder("select TC_PARIDAD,TC_MONEDA1,TC_MONEDA2,TC_FECHA from vta_tasacambio where ");
        strSqld.append("((TC_MONEDA1 = ").append(intMonedaBase).append(" and TC_MONEDA2=").append(intMonedaFinal).append(") or ");
        strSqld.append("(TC_MONEDA2 = ").append(intMonedaBase).append(" and TC_MONEDA1=").append(intMonedaFinal);
        strSqld.append(")) " + " and TTC_ID = ").append(intTipoConversion);

        //si la fecha no esta vacia
        if (strFecha != null) {
            if (!strFecha.isEmpty()) {
                strSqld.append(" and TC_FECHA <= ").append(strFecha);
            }
        }
        strSqld.append(" order by TC_FECHA DESC limit 0,1");
        try {
            ResultSet rs = this.oConn.runQuery(strSqld.toString(), true);
            while (rs.next()) {
                //Factor para conversión de izquierda a derecha, Moneda extranjera moneda nacional
                dblFactorDolar = rs.getDouble("TC_PARIDAD");
                log.debug(rs.getString("TC_FECHA") + " " + dblFactorDolar);
                //Validamos si es una conversion a la inversa
                //es decir que de moneda nacional a moneda extranjera
                //derecha a izquierda , dividimos 1 entre el factor
                if (rs.getInt("TC_MONEDA1") != intMonedaBase && this.boolConversionAutomatica) {
                    dblFactorDolar = 1 / dblFactorDolar;
                }
                this.intMonedaBase = rs.getInt("TC_MONEDA1");
                break;
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return dblFactorDolar;

    }

    public double GetFactorConversionChipcoin() {
        double dblFactorChipcoin = 0;
        String strSqlMoneda = "SELECT GD_GANA_INICIADOR FROM sii_ganancias_diarias ORDER BY GD_FECHA DESC LIMIT 0,1";
        try {
            ResultSet rs = oConn.runQuery(strSqlMoneda, true);
            while (rs.next()) {
                monedasChip = rs.getDouble("GD_GANA_INICIADOR");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }

        dblFactorChipcoin = monedasChip;

        return dblFactorChipcoin;
    }
}
