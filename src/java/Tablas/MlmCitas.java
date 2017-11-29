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
public class MlmCitas extends TableMaster {

    public MlmCitas() {
            super("vta_citas", "CI_ID", "", "");
        this.Fields.put("CI_ID", 0);
        this.Fields.put("CI_NOMBRE", "");
        this.Fields.put("CI_CORREO", "");
        this.Fields.put("CI_TELEFONO", "");
        this.Fields.put("CI_EMPRESA", "");
        this.Fields.put("CI_DIRECCION", "");
        this.Fields.put("CI_TIPO_SERVICIO", "");
        this.setBolGetAutonumeric(true);
    }
}
