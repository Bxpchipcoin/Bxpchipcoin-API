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
public class MlmCementerio extends TableMaster {

    public MlmCementerio () {
        super("mlm_Cementerio ", "CM_ID", "", "");
        this.Fields.put("CM_ID", 0);
        this.Fields.put("CM_FECHA", "");
        this.Fields.put("CM_MEMBRESIA", "");
        this.Fields.put("CM_TOTAL", 0);    
        this.Fields.put("CT_ID", 0);
        this.setBolGetAutonumeric(true);
    }
}