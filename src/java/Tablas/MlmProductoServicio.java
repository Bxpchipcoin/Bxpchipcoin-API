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
public class MlmProductoServicio extends TableMaster {

    public MlmProductoServicio() {
        super("mlm_producto_servicio", "PS_ID", "", "");
        this.Fields.put("PS_ID", 0);
        this.Fields.put("PS_NOMBRE", "");
        this.Fields.put("PS_CORREO", "");
        this.Fields.put("PS_TELEFONO", "");       
        this.Fields.put("CT_ID", 0);
        this.setBolGetAutonumeric(true);
    }
}
