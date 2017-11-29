package com.mx.siweb.mlm.compensacion.chipcoin;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ZeusSIWEB
 */
public class ExecuteShellComand {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    *Ejecuta un comando en el shell
    * @param command Es el comando por ejecutar
    * @return Es el resultado del comando
    */
   public String executeCommand(String command) {

      StringBuilder output = new StringBuilder();

      Process p;
      try {
         p = Runtime.getRuntime().exec(command);
         p.waitFor();
         BufferedReader reader
            = new BufferedReader(new InputStreamReader(p.getInputStream()));

         String line;
         while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
         }

      } catch (Exception e) {
         System.out.println("Error " + e.getMessage());
      }

      return output.toString();

   }
   // </editor-fold>
}
