/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author Fernando
 */
public class MlmBoletos extends TableMaster {

    public MlmBoletos() {
        super("mlm_registro_boletos", "BL_ID", "", "");
        this.Fields.put("BL_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("BL_FOLIO","");
        this.Fields.put("BL_NOMBRE","");       
        this.Fields.put("BL_FECHA","");
        this.Fields.put("BL_HORA","");
        this.Fields.put("BL_ES_SOCIO", 0);
        this.Fields.put("BL_DOCUMENTO",""); 
        this.Fields.put("BL_ABUCHEE","");
        this.Fields.put("BL_TELEFONO","");  
        this.Fields.put("BL_CORREO","");
        this.setBolGetAutonumeric(true);
    }
}