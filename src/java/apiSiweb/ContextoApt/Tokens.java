/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package apiSiweb.ContextoApt;

import java.util.UUID;

/**
 *Genera tokens
 * @author ZeusSIWEB
 */
public class Tokens {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    *Regresa un nuevo token de la base
    * @return Es el nuevo token
    */
   public static String getNewToken(){
      String uuid = UUID.randomUUID().toString();
      return uuid;
   }
   // </editor-fold>
}
