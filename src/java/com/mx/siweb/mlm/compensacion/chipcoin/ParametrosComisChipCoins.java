/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.mlm.compensacion.chipcoin;

import apiSiweb.Operaciones.TableMaster;

/**
 *Representa los parametros para las comisiones mensuales
 * @author ZeusSIWEB
 */
public class ParametrosComisChipCoins  extends TableMaster {

   public ParametrosComisChipCoins() {
      super("mlm_comis_param", "CP_ID", "", "");
      this.Fields.put("CP_ID", new Integer(0));
      this.Fields.put("CP_NOMBRE_NIVEL", "");
      this.Fields.put("CP_PPUNTOS", new Double(0));
      this.Fields.put("CP_PNEGOCIO", new Double(0));
      this.Fields.put("CP_GPPUNTOS", new Double(0));
      this.Fields.put("CP_GPUNTOS", new Double(0));
      this.Fields.put("CP_GNEGOCIO", new Double(0));
      this.Fields.put("CP_HIST1", new Double(0));
      this.Fields.put("CP_HIJOS", new Integer(0));
      this.Fields.put("CP_UNILEVEL1", new Double(0));
      this.Fields.put("CP_UNILEVEL2", new Double(0));
      this.Fields.put("CP_UNILEVEL3", new Double(0));
      this.Fields.put("CP_UNILEVEL4", new Double(0));
      this.Fields.put("CP_UNILEVEL5", new Double(0));
      this.Fields.put("CP_UNILEVEL6", new Double(0));
      this.Fields.put("CP_DIFERENCIAL1", new Double(0));
      this.Fields.put("CP_ORDEN", new Integer(0));
      this.Fields.put("CP_NIVEL", new Integer(0));
   }

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
