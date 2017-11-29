/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author Siweb
 */
public class MlmPagosDeta extends TableMaster {

    public MlmPagosDeta() {
        super("mlm_pagos_deta", "PROS_DETA_ID", "", "");
        this.Fields.put("PROS_DETA_ID", 0);
        this.Fields.put("PROS_ID", 0);
        this.Fields.put("PROS_DETA_IMPORTE", 0);
        this.Fields.put("PROS_DETA_FECHA", "");
        this.Fields.put("CT_ID", 0);
        this.setBolGetAutonumeric(true);
    }
}
