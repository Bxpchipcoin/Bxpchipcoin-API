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
public class MlmEmpresaTransportadora extends TableMaster {

    public MlmEmpresaTransportadora() {
            super("mlm_empresas_transportadoras", "ET_ID", "", "");
        this.Fields.put("ET_ID",0);
        this.Fields.put("CT_ID",0);
        this.Fields.put("LM_ID",0);
        this.Fields.put("EM_NOMBRE_EMPRESA","");
        this.Fields.put("EM_FECHA","");
        this.Fields.put("EM_HORA","");
        this.Fields.put("EM_NUMERO_CONTRATO","");    
        this.setBolGetAutonumeric(true);
    }
}
