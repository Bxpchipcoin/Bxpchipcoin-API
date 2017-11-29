/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author Siweb
 */
public class EcommPedidoDeta extends TableMaster {

    public EcommPedidoDeta() {
        super("ecomm_pedido_deta", "CARRDETA_ID", "", "");
        this.Fields.put("CARRDETA_ID", 0);
        this.Fields.put("CARR_ID", 0);
        this.Fields.put("PR_ID", 0);
        this.Fields.put("CARRDETA_CANTIDAD", 0);
        this.Fields.put("CARRDETA_PRECIO",0);
        this.Fields.put("CARRDETA_DESCUENTO",0);
        this.Fields.put("CARRDETA_IMPORTE",0);
    }
}
