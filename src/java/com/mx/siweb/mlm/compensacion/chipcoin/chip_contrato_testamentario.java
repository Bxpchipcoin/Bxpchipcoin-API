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
public class chip_contrato_testamentario extends TableMaster {

    public chip_contrato_testamentario() {
        super("chip_contrato_testamentario", "CCT_ID", "", "");
        this.Fields.put("CCT_ID", 0);
        this.Fields.put("CCT_UUID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("CCT_NOMBRE", "");
        this.Fields.put("CCT_FECHA", "");
        this.Fields.put("CCT_ESTATUS", 0);
        this.Fields.put("CCT_DIAS_INACTIVIDAD", 0);
        this.Fields.put("CCT_FECHA_FINALIZACION", "");
        this.Fields.put("CCT_ULTIMO_AVISO", "");
        this.Fields.put("CCT_FECHA_LIBERACION", "");
        this.Fields.put("CCT_LIBERADO", 0);
        this.setBolGetAutonumeric(true);
    }

}
