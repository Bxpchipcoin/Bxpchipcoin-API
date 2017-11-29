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
public class VtaRutas extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public VtaRutas() {
      super("vta_rutas", "RU_ID", "", "");
      this.Fields.put("RU_ID", 0);
      this.Fields.put("RU_FOLIO", "");
      this.Fields.put("TR_ID", 0);
      this.Fields.put("RU_CODIGO", "");
      this.Fields.put("RU_NOMBRE", "");
      this.Fields.put("RU_ENCARGADO", "");
      this.Fields.put("RU_DESCRIPCION", "");
      this.Fields.put("RU_DIRECCION_PARTIDA", "");
      this.Fields.put("RU_FECHA", "");
      this.Fields.put("RU_HORA", "");
      this.Fields.put("RU_USUARIO_CREATE", 0);
      this.Fields.put("RU_USUARIO_MODIF", 0);
      this.Fields.put("RU_NO_PEDIDOS", 0);
      this.Fields.put("RU_ESTATUS", 0);
      this.bolGetAutonumeric = true;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}