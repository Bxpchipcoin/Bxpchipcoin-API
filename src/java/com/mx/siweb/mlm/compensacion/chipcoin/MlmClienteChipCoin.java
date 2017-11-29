/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.chipcoin;

import com.mx.siweb.mlm.compensacion.entidades.mlm_cliente;

/**
 * Representa el objeto de chipcoin
 *
 * @author ZeusSIWEB
 */
public class MlmClienteChipCoin extends mlm_cliente {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private double dblTotGananciaDiferenciales;
   private double dblTotGananciaRed;
   private double dblTotGananciaIgualacion;
   private double dblTotVentasComis;
   private double dblTotChipCoins;
   private int intPaId;
   private int intTipoImpuesto;

   public double getDblTotVentasComis() {
      return dblTotVentasComis;
   }

   public void setDblTotVentasComis(double dblTotVentasComis) {
      this.dblTotVentasComis = dblTotVentasComis;
   }

   public double getDblTotChipCoins() {
      return dblTotChipCoins;
   }

   public void setDblTotChipCoins(double dblTotChipCoins) {
      this.dblTotChipCoins = dblTotChipCoins;
   }

   public int getIntTipoImpuesto() {
      return intTipoImpuesto;
   }

   public void setIntTipoImpuesto(int intTipoImpuesto) {
      this.intTipoImpuesto = intTipoImpuesto;
   }

   public int getIntPaId() {
      return intPaId;
   }

   public void setIntPaId(int intPaId) {
      this.intPaId = intPaId;
   }

   public double getDblTotGananciaDiferenciales() {
      return dblTotGananciaDiferenciales;
   }

   public void setDblTotGananciaDiferenciales(double dblTotGananciaDiferenciales) {
      this.dblTotGananciaDiferenciales = dblTotGananciaDiferenciales;
   }

   public double getDblTotGananciaRed() {
      return dblTotGananciaRed;
   }

   public void setDblTotGananciaRed(double dblTotGananciaRed) {
      this.dblTotGananciaRed = dblTotGananciaRed;
   }

   public double getDblTotGananciaIgualacion() {
      return dblTotGananciaIgualacion;
   }

   public void setDblTotGananciaIgualacion(double dblTotGananciaIgualacion) {
      this.dblTotGananciaIgualacion = dblTotGananciaIgualacion;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>

}
