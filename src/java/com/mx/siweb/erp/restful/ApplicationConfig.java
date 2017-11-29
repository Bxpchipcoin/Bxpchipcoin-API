/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mx.siweb.erp.restful;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author ZeusGalindo
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

   @Override
   public Set<Class<?>> getClasses() {
      Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
      addRestResourceClasses(resources);
      return resources;
   }

   /**
    * Do not modify addRestResourceClasses() method.
    * It is automatically populated with
    * all resources defined in the project.
    * If required, comment out calling this method in getClasses().
    */
   private void addRestResourceClasses(Set<Class<?>> resources) {
       
      resources.add(com.mx.siweb.erp.restful.BxpWalletResource.class);
      resources.add(com.mx.siweb.erp.restful.BxpUsersResource.class);
      resources.add(com.mx.siweb.erp.restful.BxpAccountResource.class);
      resources.add(com.mx.siweb.erp.restful.BxpAccountListResource.class);
      resources.add(com.mx.siweb.erp.restful.BxpBlockChainResource.class);
      resources.add(com.mx.siweb.erp.restful.BxpBuyResource.class);
      resources.add(com.mx.siweb.erp.restful.BxpTradingResource.class);
      resources.add(com.mx.siweb.erp.restful.ClientesResource.class);
      resources.add(com.mx.siweb.erp.restful.MobilserviceResource.class);
      resources.add(com.mx.siweb.erp.restful.MobilserviceExit.class);
      
   }

}