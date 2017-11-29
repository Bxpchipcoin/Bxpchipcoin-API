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
public class MlmPagosFondeo extends TableMaster {

    public MlmPagosFondeo() {
        super("mlm_pagos_fondeo", "PGF_ID", "", "");
        this.Fields.put("PGF_ID", 0);
        this.Fields.put("PGF_FECHA", "");
        this.Fields.put("PGF_HORA", "");
        this.Fields.put("PGF_NOMBRE", "");
        this.Fields.put("PGF_FICHA", "");
        this.Fields.put("PGF_IMPORTE", 0.0);
        this.Fields.put("PGF_SALDO", 0.0);
        this.Fields.put("LIS_NOMARCHIVO", "");
        this.Fields.put("PGF_CONFIRMADO", 0);
        this.Fields.put("LIS_PATHIMGFORM", "");
        this.Fields.put("MSE_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("MPE_ID", 0);
        this.Fields.put("PGF_TIPO_PAGO", 0);
        this.Fields.put("PGF_FECHA_DEPOSITO", "");
        this.Fields.put("PGF_SUCURSAL", "");
        this.Fields.put("PGF_NUM_AUTORIZA", "");
        this.Fields.put("PGF_ESTADO", "");
        this.setBolGetAutonumeric(true);
    }
}
