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
public class MlmVolumenes extends TableMaster {

    public MlmVolumenes() {
        super("mlm_volumenes", "VOL_ID", "", "");
        this.Fields.put("VOL_ID", 0);
        this.Fields.put("VOL_NOMBRE", "");
        this.Fields.put("VOL_PAIS", "");
        this.Fields.put("VOL_TELEFONO", "");
        this.Fields.put("VOL_CORREO", "");
        this.Fields.put("VOL_MENSUAL", 0.0);
        this.Fields.put("VOL_OCUPACION", "");
        this.Fields.put("VOL_INGRESOS", 0.0);
        this.Fields.put("VOL_PROPIETARIO", "");
        this.Fields.put("VOL_PROPIETARIO_REAL", "");
        this.Fields.put("VOL_VISITA", "");
        this.Fields.put("VOL_POLITICA", "");
        this.setBolGetAutonumeric(true);
    }
}
