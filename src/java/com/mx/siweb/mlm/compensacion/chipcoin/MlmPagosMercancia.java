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
public class MlmPagosMercancia extends TableMaster {

    public MlmPagosMercancia() {
            super("mlm_pagos_mercancias", "PM_ID", "", "");
        this.Fields.put("PM_ID",0);
        this.Fields.put("LM_ID",0);
        this.Fields.put("CT_ID",0);
        this.Fields.put("PM_PORCENTAJE","");
        this.Fields.put("PM_FECHA","");
        this.Fields.put("PM_HORA","");
        this.Fields.put("PM_FECHA_LIBERACION_PAGO","");
        this.Fields.put("PM_LIB_PAGO","");               
        this.Fields.put("PM_NUMERO_CONTRATO","");       
        this.setBolGetAutonumeric(true);
    }
}
