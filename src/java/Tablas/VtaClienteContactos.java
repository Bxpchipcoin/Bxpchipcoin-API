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
public class VtaClienteContactos extends TableMaster {
    public VtaClienteContactos() {
        super("vta_cliente_contactos", "CCO_ID", "", "");
        this.Fields.put("CCO_ID", 0);
        this.Fields.put("CCO_NOMBRE", "");
        this.Fields.put("CCO_APPATERNO", "");
        this.Fields.put("CCO_APMATERNO", "");
        this.Fields.put("CCO_TITULO", "");
        this.Fields.put("CCO_AREA", "");
        this.Fields.put("CCO_CORREO", "");
        this.Fields.put("CCO_TELEFONO", "");
        this.Fields.put("CCO_EXTENCION", "");
        this.Fields.put("CCO_ALTERNO", "");
        this.Fields.put("CT_ID", 0);
        this.Fields.put("CCO_MAILMES", "");
        this.Fields.put("CCO_CORREO2", "");
        this.Fields.put("CCO_FECHA_NACIMIENTO", "");
    }
}