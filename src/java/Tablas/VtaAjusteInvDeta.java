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
public class VtaAjusteInvDeta extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public VtaAjusteInvDeta() {
      super("vta_ajuste_inv_deta", "AJD_ID", "", "");
      this.Fields.put("AJD_ID", 0);
      this.Fields.put("AJD_CODIGO", "");
      this.Fields.put("AJD_DESCRIPCION", "");
      this.Fields.put("AJD_CANTIDAD", 0.0);
      this.Fields.put("AJD_SUMA", 0);
      this.Fields.put("PR_ID", 0);
      this.Fields.put("AJ_ID", 0);
      this.Fields.put("AJD_UMEDIDA", "");
      this.Fields.put("AJD_SERIES", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
