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
public class MlmDocumentosMercancias extends TableMaster {

    public MlmDocumentosMercancias() {
            super("mlm_documentos_mercancias","DM_ID", "", "");
        this.Fields.put("DM_ID",0);
        this.Fields.put("CT_ID",0);
        this.Fields.put("DM_DOC_NAME",""); 
        this.Fields.put("DM_FECHA",""); 
        this.Fields.put("DM_HORA",""); 
        this.Fields.put("DM_NUMERO_CONTRATO","");              
        this.setBolGetAutonumeric(true);
    }
}
