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
public class MlmFraccArancelaria extends TableMaster {

    public MlmFraccArancelaria() {
            super("mlm_fraccion_arancelaria", "FRA_ID", "", "");
        this.Fields.put("FRA_ID", 0);
        this.Fields.put("FRA_FECHA", "");
        this.Fields.put("FRA_HORA", "");
        this.Fields.put("FRA_FRACCION", "");
        this.Fields.put("FRA_EXPORTACION", "");
        this.Fields.put("FRA_IMPORTACION", "");
        this.Fields.put("FRA_PAIS_ORIGEN", 0);
        this.setBolGetAutonumeric(true);
    }
}
