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
public class chip_contrato_testamentario_carteras extends TableMaster {

    public chip_contrato_testamentario_carteras() {
        super("chip_contrato_testamentario_carteras", "CCTC_ID", "", "");
        this.Fields.put("CCTC_ID", 0);
        this.Fields.put("CCT_ID", 0);
        this.Fields.put("CCTC_CUENTA_CARTERA", "");
        this.Fields.put("CW_ID", 0);
        this.setBolGetAutonumeric(true);
    }

}
