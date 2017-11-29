/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.chipcoin;

import apiSiweb.Operaciones.TableMaster;

/**
 *
 * @author ZeusSIWEB
 */
public class MlmChipcoinWallet extends TableMaster {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public MlmChipcoinWallet() {
      super("mlm_chipcoin_wallet", "CW_ID", "", "");
      this.Fields.put("CW_ID", 0);
      this.Fields.put("CW_TOT_CHIPCOINS", 0.0);
      this.Fields.put("CW_NOMBRE_CUENTA", "");
      this.Fields.put("CW_NUMERO_CUENTA", "");
      this.Fields.put("CT_ID", 0);
      this.Fields.put("CW_FECHA_REG", "");
      this.Fields.put("CW_HORA_REG", "");
      this.Fields.put("CW_PASS", "");
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
