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
public class VtaAjusteInv extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public VtaAjusteInv() {
      super("vta_ajuste_inv", "AJ_ID", "", "");
      this.Fields.put("AJ_ID", 0);
      this.Fields.put("AJ_FECHA", "");
      this.Fields.put("AJ_HORA", "");
      this.Fields.put("AJ_FECHACREATE", "");
      this.Fields.put("AJ_USER_CREA", 0);
      this.Fields.put("AJ_USER_MODIF", 0);
      this.Fields.put("TJ_ID", 0);
      this.Fields.put("SC_ID", 0);
      this.Fields.put("EMP_ID", 0);
      this.Fields.put("AJ_ESTATUS", "");
      this.Fields.put("AJ_FECHAANULA", "");
      this.Fields.put("AJ_USER_ANULA", 0);
      this.Fields.put("AJ_HORA_ANULA", "");
      this.Fields.put("AJ_COMENTARIOS", "");
      this.Fields.put("AJ_FOLIO", "");
      this.Fields.put("CC_ID", 0);
      this.Fields.put("AJ_TOTAL", 0.0);
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
