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
public class chip_contrato_testamentario_bienes extends TableMaster {

    public chip_contrato_testamentario_bienes() {
        super("chip_contrato_testamentario_bienes", "CCTBN_ID", "", "");
        this.Fields.put("CCTBN_ID", 0);
        this.Fields.put("CCT_ID", 0);
        this.Fields.put("CCTBN_BIEN", "");
        this.Fields.put("CCTBN_CONDICIONES", "");
        this.setBolGetAutonumeric(true);
    }

}
