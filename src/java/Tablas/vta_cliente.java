package Tablas;
// Generated 14/02/2010 11:10:21 AM by Hibernate Tools 3.2.1.GA

import apiSiweb.Operaciones.TableMaster;

/**
 * Representa un Cliente
 */
public class vta_cliente extends TableMaster {

   public vta_cliente() {
      super("vta_cliente", "CT_ID", "", "");
      this.Fields.put("CT_ID", 0);
      this.Fields.put("CT_RAZONSOCIAL", "");
      this.Fields.put("CT_RFC", "");
      this.Fields.put("CT_CALLE", "");
      this.Fields.put("CT_COLONIA", "");
      this.Fields.put("CT_LOCALIDAD", "");
      this.Fields.put("CT_TXTIVA", "");
      this.Fields.put("CT_TXTIVAAGUA", "");
      this.Fields.put("CT_MUNICIPIO", "");
      this.Fields.put("CT_ESTADO", "");
      this.Fields.put("CT_CP", "");
      this.Fields.put("CT_TELEFONO1", "");
      this.Fields.put("CT_TELEFONO2", "");
      this.Fields.put("CT_CONTACTO1", "");
      this.Fields.put("CT_CONTACTO2", "");
      this.Fields.put("CT_FOLIO", 0);
      this.Fields.put("CT_NUMCEROS", 0);
      this.Fields.put("CT_SALDO", 0);
      this.Fields.put("CT_EMAIL1", "");
      this.Fields.put("CT_EMAIL2", "");
      this.Fields.put("CT_NUMERO", "");
      this.Fields.put("CT_NUMINT", "");
      this.Fields.put("CT_LPRECIOS", 0);
      this.Fields.put("CT_DIASCREDITO", 0);
      this.Fields.put("CT_MONTOCRED", 0);
      this.Fields.put("CT_FECHAREG", "");
      this.Fields.put("CT_IDIOMA", 0);
      this.Fields.put("SC_ID", 0);
      this.Fields.put("CT_DESCUENTO", 0);
      this.Fields.put("CT_VENDEDOR", 0);
      this.Fields.put("CT_CONTAVTA", "");
      this.Fields.put("CT_CONTAPAG", "");
      this.Fields.put("CT_CONTANC", "");
      this.Fields.put("CT_NOTAS", "");
      this.Fields.put("CT_EXITOSOS", 0);
      this.Fields.put("CT_CATEGORIA1", 0);
      this.Fields.put("CT_CATEGORIA2", 0);
      this.Fields.put("CT_CATEGORIA3", 0);
      this.Fields.put("CT_CATEGORIA4", 0);
      this.Fields.put("CT_CATEGORIA5", 0);
      this.Fields.put("CT_TIPOPERS", 0);
      this.Fields.put("CT_USOIMBUEBLE", "");
      this.Fields.put("CT_TIPOFAC", 0);
      this.Fields.put("CT_TIT_CONT1", "");
      this.Fields.put("CT_TIT_CONT2", "");
      this.Fields.put("CT_CONT_AP1", "");
      this.Fields.put("CT_CONT_AP2", "");
      this.Fields.put("CT_CONT_AM1", "");
      this.Fields.put("CT_CONT_AM2", "");
      this.Fields.put("EMP_ID", 0);
      this.Fields.put("CT_CONTACTE", "");
      this.Fields.put("CT_CUENTAVTACRED", "");
      this.Fields.put("CT_UPLINE", 0);
      this.Fields.put("CT_CONTACTO", "");
      this.Fields.put("CT_FECHAULTIMOCONTACTO", "");
      this.Fields.put("CT_ARMADOINI", 0);
      this.Fields.put("CT_ARMADOFIN", 0);
      this.Fields.put("CT_ARMADONUM", 0);
      this.Fields.put("CT_ARMADODEEP", 0);
      this.Fields.put("CT_SPONZOR", 0);
      this.Fields.put("CT_LADO", "");
      this.Fields.put("CT_CTABANCO1", "");
      this.Fields.put("CT_CTABANCO2", "");
      this.Fields.put("CT_CTATARJETA", "");
      this.Fields.put("CT_NUMPREDIAL", "");
      this.Fields.put("PA_ID", "");
      this.Fields.put("CT_ACTIVO", 0);
      this.Fields.put("CT_RAZONCOMERCIAL", "");
      this.Fields.put("CT_CATEGORIA6", 0);
      this.Fields.put("CT_CATEGORIA7", 0);
      this.Fields.put("CT_CATEGORIA8", 0);
      this.Fields.put("CT_CATEGORIA9", 0);
      this.Fields.put("CT_CATEGORIA10", 0);
      this.Fields.put("MON_ID", 0);
      this.Fields.put("TI_ID", 0);
      this.Fields.put("TTC_ID", 0);
      this.Fields.put("CT_RBANCARIA1", "");
      this.Fields.put("CT_RBANCARIA2", "");
      this.Fields.put("CT_RBANCARIA3", "");
      this.Fields.put("CT_BANCO1", 0);
      this.Fields.put("CT_BANCO2", 0);
      this.Fields.put("CT_BANCO3", 0);
      this.Fields.put("CT_METODODEPAGO", "");
      this.Fields.put("CT_FORMADEPAGO", "");
      this.Fields.put("CT_FECHA_NAC", "");
      this.Fields.put("CT_NOMBRE", "");
      this.Fields.put("CT_APATERNO", "");
      this.Fields.put("CT_AMATERNO", "");
      this.Fields.put("CT_PPUNTOS", 0);
      this.Fields.put("CT_PNEGOCIO", 0);
      this.Fields.put("CT_GPUNTOS", 0);
      this.Fields.put("CT_GNEGOCIO", 0);
      this.Fields.put("CT_COMISION", 0);
      this.Fields.put("CT_NIVELRED", 0);
      this.Fields.put("MPE_ID", 0);
      this.Fields.put("CT_CONTEO_HIJOS", 0);
      this.Fields.put("CT_CONTEO_HIJOS_ACTIVOS", 0);
      this.Fields.put("CT_CONTEO_INGRESOS", 0);
      this.Fields.put("CT_RLEGAL", "");
      this.Fields.put("CT_FIADOR", "");
      this.Fields.put("CT_F1DIRECCION", "");
      this.Fields.put("CT_F1IFE", "");
      this.Fields.put("CT_FIADOR2", "");
      this.Fields.put("CT_F2DIRECCION", "");
      this.Fields.put("CT_F2IFE", "");
      this.Fields.put("CT_FIADOR3", "");
      this.Fields.put("CT_F3DIRECCION", "");
      this.Fields.put("CT_F3IFE", "");
      this.Fields.put("CT_CTA_BANCO1", "");
      this.Fields.put("CT_CTA_BANCO2", "");
      this.Fields.put("CT_CTA_SUCURSAL1", "");
      this.Fields.put("CT_CTA_SUCURSAL2", "");
      this.Fields.put("CT_CTA_CLABE1", "");
      this.Fields.put("CT_CTA_CLABE2", "");
      this.Fields.put("CT_CONTACTE_COMPL", "");
      this.Fields.put("CT_CTA_ANTICIPO", "");
      this.Fields.put("CT_CTACTE_COMPL_ANTI", "");
      this.Fields.put("CT_CONTA_RET_ISR", "");
      this.Fields.put("CT_CONTA_RET_IVA", "");
      this.Fields.put("TI2_ID", 0);
      this.Fields.put("TI3_ID", 0);
      this.Fields.put("CT_BANCO_CTA1", "");
      this.Fields.put("CT_BANCO_CTA2", "");
      this.Fields.put("CT_CLABE1", "");
      this.Fields.put("CT_CLABE2", "");
      this.Fields.put("CT_CRED_ELECTOR", "");
      this.setBolGetAutonumeric(true);
   }
}
