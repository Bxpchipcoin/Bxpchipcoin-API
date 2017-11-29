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
public class VtaMakPromocion extends TableMaster {

    public VtaMakPromocion() {
        super("vta_mak_promocion", "PRM_ID", "", "");
        this.Fields.put("PRM_ID", 0);
        this.Fields.put("PRM_CODIGO", "");
        this.Fields.put("PRM_CODBAR", "");
        this.Fields.put("PRM_DESCRIPCION", "");
        this.Fields.put("EMP_ID", 0);
        this.Fields.put("SC_ID", 0);
        this.Fields.put("PRM_ZONA", 0);
        this.Fields.put("PRM_TIPO_CL1", 0);
        this.Fields.put("PRM_TIPO_CL2", 0);
        this.Fields.put("PRM_TIPO_CL3", 0);
        this.Fields.put("PRM_TIPO_CL4", 0);
        this.Fields.put("PRM_ACTIVO", 0);
        this.Fields.put("PRM_FECHA_REG", "");
        this.Fields.put("PRM_FECHA_INICIO", "");
        this.Fields.put("PRM_HORA_INICIO", "");
        this.Fields.put("PRM_FECHA_FIN", "");
        this.Fields.put("PRM_HORA_FIN", "");
        this.Fields.put("PRM_AGOTA_EXIS", 0);
        this.bolGetAutonumeric = true;
    }
}