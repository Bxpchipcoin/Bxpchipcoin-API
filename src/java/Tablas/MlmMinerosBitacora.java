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
public class MlmMinerosBitacora extends TableMaster {

    public MlmMinerosBitacora() {
        super("mlm_bitacora_mineros", "BM_ID", "", "");
        this.Fields.put("BM_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("BM_MONEDAS", "");
        this.Fields.put("BM_IP", "");
        this.Fields.put("BM_FECHA", "");
        this.Fields.put("BM_HORA", "");        
        this.setBolGetAutonumeric(true);
    }
}
