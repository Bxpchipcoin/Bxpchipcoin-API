/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.chipcoin;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author casajosefa
 */
public class MlmTransaccionesTrading extends TableMaster {

    public MlmTransaccionesTrading() {
        super("mlm_chip_trading_transacciones", "TT_ID", "", "");
        this.Fields.put("TT_ID", 0);
        this.Fields.put("TT_FECHA", "");
        this.Fields.put("TT_HORA", "");
        this.Fields.put("TT_SALDO", 0.0);
        this.Fields.put("TT_MONTO", 0.0);
        this.Fields.put("TT_PARIDAD", 0.0);
        this.Fields.put("TT_IMPORTE", 0.0);
        this.Fields.put("TT_CONFIRMA", 0);
        this.Fields.put("TT_TIPO_TRANSACCION", "");
        this.Fields.put("TT_IP", "");
        this.Fields.put("CT_ID", 0);
        this.Fields.put("TT_CUENTA", "");
        this.Fields.put("TT_NOMBRE", "");
        this.Fields.put("TT_ESTADO", ""); 
        this.Fields.put("TT_CARTERA", "");  
        this.setBolGetAutonumeric(true);
    }
}
