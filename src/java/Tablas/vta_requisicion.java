/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author siwebmx5
 */
public class vta_requisicion extends TableMaster {

    public vta_requisicion() {
        super("vta_requisicion", "REQ_ID", "", "");
        this.Fields.put("REQ_ID", 0);
        this.Fields.put("REQ_FOLIO", "");
        this.Fields.put("REQ_FECHA_SOLICITUD", "");
        this.Fields.put("REQ_USUARIO_SOLICITO", 0);
        this.Fields.put("REQ_HORA", "");
        this.Fields.put("REQ_ID_CLIENTE", 0);
        this.Fields.put("REQ_NOTAS", "");
        this.Fields.put("REQ_AUTORIZADO", 0);
        this.Fields.put("REQ_STATUS", 0);
        this.Fields.put("REQ_FECHA_AUTORIZADA", "");
        this.Fields.put("REQ_USUARIO_AUTORIZA", 0);
        this.Fields.put("REQ_COM_ID", 0);
        this.Fields.put("REQ_PD_ID", 0);
        this.Fields.put("EMP_ID", 0);
        this.Fields.put("SM_ID", 0);
        this.Fields.put("SC_ID", 0);
        this.Fields.put("REQ_FECHA_REQUERIDA", "");
    }
}