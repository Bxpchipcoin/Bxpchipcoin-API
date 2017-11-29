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
public class MlmChipMineros extends TableMaster {

    public MlmChipMineros() {
        super("mlm_chip_mineros", "CM_ID", "", "");
        this.Fields.put("CM_ID", 0);
        this.Fields.put("CM_CODIGO", "");
        this.Fields.put("CM_USADO", 0);
        this.Fields.put("CM_FECHA", "");
        this.Fields.put("CM_HORA", "");        
        this.setBolGetAutonumeric(true);
    }
}
