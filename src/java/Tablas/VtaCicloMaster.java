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
public class VtaCicloMaster extends TableMaster {

    public VtaCicloMaster() {
        super("vta_ciclico_master", "CC_ID", "", "");
        this.Fields.put("CC_ID", 0);
        this.Fields.put("CC_USUARIO", 0);
        this.Fields.put("EMP_ID", 0);
        this.Fields.put("CC_FECHA", "");
        this.Fields.put("CC_HORA", "");
        this.Fields.put("CC_SUCURSAL", 0);
        this.Fields.put("CC_BODEGA", 0);
        this.Fields.put("CC_STATUS", 0);
        this.Fields.put("CC_FOLIO", "");
        this.Fields.put("CC_FECHA_CONTEO1", "");
        this.Fields.put("CC_FECHA_CONTEO2", "");
        this.setBolGetAutonumeric(true);
    }
}