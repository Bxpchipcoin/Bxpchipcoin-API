package Tablas;

import apiSiweb.Operaciones.TableMaster;

public class VtaCteBroker extends TableMaster {

    public VtaCteBroker() {
        super("vta_cte_broker", "CTE_BRK_ID", "", "");
        this.Fields.put("CTE_BRK_ID", 0);
        this.Fields.put("CT_ID", 0);
        this.Fields.put("BRK_ID", 0);
        this.Fields.put("CTE_BRK_CUENTA", "");
        this.Fields.put("CTE_BRK_SALDO", 0);
    }
}
