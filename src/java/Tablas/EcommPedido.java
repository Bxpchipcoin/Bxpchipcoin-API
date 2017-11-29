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
public class EcommPedido extends TableMaster {

    public EcommPedido() {
        super("ecomm_pedido", "CARR_ID", "", "");
        this.Fields.put("CARR_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("CARR_FECHAINICIAL", "");
        this.Fields.put("CARR_FECHAHORA", "");
        this.Fields.put("CARR_STATUS", "");
        this.Fields.put("CARR_TOTAL", 0);
    }
    
}
