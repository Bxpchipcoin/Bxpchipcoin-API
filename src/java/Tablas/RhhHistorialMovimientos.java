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
public class RhhHistorialMovimientos extends TableMaster {

    public RhhHistorialMovimientos() {
        super("rhh_historial_movimientos", "RHI_ID", "", "");
        this.Fields.put("RHI_ID", 0);
        this.Fields.put("RHI_FECHA", "");
        this.Fields.put("RHI_USUARIO", 0);
        this.Fields.put("RHI_TIPO_CAMBIO", "");
        this.Fields.put("RHI_SALARIO_ANT", 0.0);
        this.Fields.put("RHI_TIPO_SALARIO_ANT", "");
        this.Fields.put("RHI_NUEVO_SALARIO", 0.0);
        this.Fields.put("RHI_TIPO_SALARIO_NVO", "");
        this.Fields.put("RHI_REGISTRO_PATRONAL_ANT", "");
        this.Fields.put("RHI_NOTAS", "");
        this.Fields.put("EMP_NUM", 0);
        this.Fields.put("RHI_REGISTRO_PATRONAL_NVO", "");
    }
}