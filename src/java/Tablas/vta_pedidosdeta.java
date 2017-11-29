package Tablas;

import apiSiweb.Operaciones.TableMaster;

/**
 * Representa la tabla a detalle de los pedidos
 *
 * @author zeus
 */
public class vta_pedidosdeta extends TableMaster {

    /**
     * Constructor
     */
    public vta_pedidosdeta() {
        super("vta_pedidosdeta", "PDD_ID", "", "");
        this.Fields.put("PDD_ID", new Integer(0));
        this.Fields.put("PD_ID", new Integer(0));
        this.Fields.put("PDD_CVE", "");
        this.Fields.put("PDD_DESCRIPCION", "");
        this.Fields.put("PDD_IMPORTE", new Double(0));
        this.Fields.put("PDD_CANTIDAD", new Double(0));
        this.Fields.put("PDD_TASAIVA1", new Double(0));
        this.Fields.put("PDD_TASAIVA2", new Double(0));
        this.Fields.put("PDD_TASAIVA3", new Double(0));
        this.Fields.put("PDD_DESGLOSA1", new Integer(0));
        this.Fields.put("PDD_DESGLOSA2", new Integer(0));
        this.Fields.put("PDD_DESGLOSA3", new Integer(0));
        this.Fields.put("SC_ID", new Integer(0));
        this.Fields.put("PDD_IMPUESTO1", new Double(0));
        this.Fields.put("PDD_IMPUESTO2", new Double(0));
        this.Fields.put("PDD_IMPUESTO3", new Double(0));
        this.Fields.put("PDD_NOSERIE", "");
        this.Fields.put("PDD_ESREGALO", new Integer(0));
        this.Fields.put("PDD_IMPORTEREAL", new Double(0));
        this.Fields.put("PDD_PRECIO", new Double(0));
        this.Fields.put("PDD_EXENTO1", new Integer(0));
        this.Fields.put("PDD_EXENTO2", new Integer(0));
        this.Fields.put("PDD_EXENTO3", new Integer(0));
        this.Fields.put("PR_ID", new Integer(0));
        this.Fields.put("PDD_COSTO", new Double(0));
        this.Fields.put("PDD_GANANCIA", new Double(0));
        this.Fields.put("PDD_DESCUENTO", new Double(0));
        this.Fields.put("PDD_PORDESC", new Double(0));
        this.Fields.put("PDD_PRECFIJO", new Integer(0));
        this.Fields.put("PDD_PRECREAL", new Double(0));
        this.Fields.put("PDD_COMENTARIO", "");
        this.Fields.put("PDD_UNIDAD_MEDIDA", "");
        this.Fields.put("PDD_CANTIDADSURTIDA", new Double(0));
        this.Fields.put("PDD_CANT_FACT", new Double(0));
        this.Fields.put("PDD_APARTADO", new Double(0));
        //MLM
        this.Fields.put("PDD_PUNTOS", new Double(0));
        this.Fields.put("PDD_VNEGOCIO", new Double(0));
        this.Fields.put("PDD_IMP_PUNTOS", new Double(0));
        this.Fields.put("PDD_IMP_VNEGOCIO", new Double(0));
        this.Fields.put("PDD_DESC_PREC", new Integer(0));
        this.Fields.put("PDD_DESC_PUNTOS", new Integer(0));
        this.Fields.put("PDD_DESC_VNEGOCIO", new Integer(0));
        this.Fields.put("PDD_REGALO", new Integer(0));
        this.Fields.put("PDD_ID_PROMO", new Integer(0));
        this.Fields.put("PDD_EXEN_RET_ISR", new Integer(0));
        this.Fields.put("PDD_EXEN_RET_IVA", new Integer(0));
        this.Fields.put("PDD_DESC_ORI", new Double(0));
        //Conceptos
        this.Fields.put("CF_ID", new Integer(0));
        this.Fields.put("PDD_CANT_REQU", new Integer(0));
        this.Fields.put("PDD_BACKORDER", "");
        this.Fields.put("PDD_CANTTRANS", new Integer(0));
        this.Fields.put("PDD_ES_PAQUETE", new Integer(0));
        this.Fields.put("PDD_ES_COMPONENTE", new Integer(0));
        this.Fields.put("PDD_PR_PAQUETE", new Integer(0));
        this.Fields.put("PDD_MULTIPLO", new Integer(0));
        this.Fields.put("PDD_CANTIDAD_IMPRIMIR", new Integer(0));
        this.Fields.put("PDD_ES_VIRTUAL", new Integer(0));
        this.Fields.put("PDD_CANT_MULTIPLOS", new Integer(0));
        this.Fields.put("PDD_PRECIO_MULTIPLO", new Double(0));
        this.Fields.put("PDD_CANT_CORTESIAS", new Integer(0));
        this.Fields.put("PDD_CANTTOT_BACKORDER", new Double(0));
        this.Fields.put("PDD_CANT_PAQUETES", new Integer(0));        
        this.Fields.put("PDD_PRECIOSOLIC", new Double(0));  
        this.Fields.put("PDD_ES_MASTER_KITVIRT", new Integer(0));  
        this.Fields.put("PDD_ES_COMPONENTE_KITVIRT", new Integer(0));  
        this.Fields.put("PDD_COMPONENTES_KITVIRT", "");  
        this.Fields.put("PDD_CANT_RECIB_CONSIG", new Double(0));
        this.setBolGetAutonumeric(false);
    }
}
