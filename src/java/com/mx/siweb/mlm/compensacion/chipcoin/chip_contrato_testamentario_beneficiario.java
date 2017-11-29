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
public class chip_contrato_testamentario_beneficiario extends TableMaster {

    public chip_contrato_testamentario_beneficiario() {
        super("chip_contrato_testamentario_beneficiario", "CCTB_ID", "", "");
        this.Fields.put("CCTB_ID", 0);
        this.Fields.put("CCT_ID", 0);
        this.Fields.put("CCTB_NOMBRE", "");
        this.Fields.put("CCTB_CLAVE", "");
        this.Fields.put("CCTB_MONTO", 0.0);
        this.Fields.put("CCTB_CORREO", "");
        this.Fields.put("CCTB_FB", "");
        this.Fields.put("CCTB_CELULAR", "");
        this.Fields.put("CCTBN_ID", 0);
        this.Fields.put("CCTB_RECIBE_CARTERAS", 0);
        this.setBolGetAutonumeric(true);

    }

}
