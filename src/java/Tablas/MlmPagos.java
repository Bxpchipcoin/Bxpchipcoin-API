/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author casajosefa
 */
public class MlmPagos extends TableMaster {

    public MlmPagos() {
        super("mlm_pagos", "PROS_ID", "", "");
        this.Fields.put("PROS_ID", 0);
        this.Fields.put("PROS_FECHA", "");
        this.Fields.put("PROS_HORA", "");
        this.Fields.put("PROS_NOMBRE", "");
        this.Fields.put("PROS_FICHA", "");
        this.Fields.put("PROS_IMPORTE", 0.0);
        this.Fields.put("LIS_NOMARCHIVO", "");
        this.Fields.put("PROS_CONFIRMADO", 0);
        this.Fields.put("LIS_PATHIMGFORM", "");
        this.Fields.put("MSE_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("MPE_ID", 0);
        this.Fields.put("PROS_ES_KIT", 0);
        this.Fields.put("PROS_FECHA_DEPOSITO", "");
        this.Fields.put("PROS_SUCURSAL", "");
        this.Fields.put("PROS_NUM_AUTORIZA", "");
        this.Fields.put("PROS_ESTADO", "");
        this.setBolGetAutonumeric(true);
    }
}
