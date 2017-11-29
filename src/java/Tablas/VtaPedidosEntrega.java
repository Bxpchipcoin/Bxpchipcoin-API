/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author ZeusSIWEB
 */
public class VtaPedidosEntrega extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public VtaPedidosEntrega() {
      super("vta_pedidos_entrega", "PDE_ID", "", "");
      this.Fields.put("PDE_ID", 0);
      this.Fields.put("PDE_QUIEN_ENTREGO", "");
      this.Fields.put("PDE_FECHA", "");
      this.Fields.put("PDE_HORA", "");
      this.Fields.put("RU_ID", 0);
      this.Fields.put("RUD_ID", 0);
      this.Fields.put("PD_ID", 0);
      this.Fields.put("PDE_USUARIO", 0);
      this.Fields.put("PDE_FECHA_CREATE", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
