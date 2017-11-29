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
public class MlmTrading extends TableMaster {

    public MlmTrading() {
        super("mlm_chip_trading", "TR_ID", "", "");
        this.Fields.put("TR_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("TR_CLIENTE", "");
        this.Fields.put("TR_HORA", "");
        this.Fields.put("TR_FECHA", "");
        this.Fields.put("TR_IP_CLIENTE", "");
        this.Fields.put("TR_TIPO_TRANSACCION", "");
        this.Fields.put("TR_IMPORTE", 0.0);
        this.Fields.put("TR_ESTADO", "");
        this.Fields.put("TR_TIPO_MONEDA", "");
        this.Fields.put("MPE_ID", 0);
        this.Fields.put("MSE_ID", 0);
        this.Fields.put("TR_TASA_CAMBIO", 0.0);
        this.Fields.put("TR_SOLICITUD_CONFIRMADO", "");
        this.Fields.put("TR_NUMERO_AUTORIZACION", "");
        this.Fields.put("TR_MONTO", 0.0);
        this.Fields.put("TR_MONTO_MINIMO", 0.0);
        this.Fields.put("TR_MONTO_MAXIMO", 0.0);
        this.Fields.put("TR_MONTO", 0.0);
        this.setBolGetAutonumeric(true);
    }
}
