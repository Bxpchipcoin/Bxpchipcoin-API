/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion.chipcoin;

import com.siweb.utilerias.json.JSONObject;
import apiSiweb.Operaciones.Conexion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.net.ssl.HttpsURLConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Aplica los chipcoins en las carteras
 *
 * @author ZeusSIWEB
 */
public class AplicaChipCoinsCarteras {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final Logger log = LogManager.getLogger(AplicaChipCoinsCarteras.class.getName());
   private final String USER_AGENT = "Mozilla/5.0";
   private Conexion oConn;

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public AplicaChipCoinsCarteras(Conexion oConn) {
      this.oConn = oConn;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void doAplicaChipCoinsCarteras() {
      //Se encarga de aplicar todos los chipcoins de las comisiones cerradas
      //Usamos la tabla de mlm_mov_comis que se genera al cerrar la semana
      try {
         ResultSet rs;
         String strPathBase = "";
         String strNomTmpTrx = "";
         String strNumHexMaster = "";
         String strPassHexMaster = "";
         String strPathExec = "";
         String strSql = "select CHIP_PATH_BASE,CHIP_FILE_TRX_TMP,CHIP_CTA_MASTER_HEX,CHIP_CTA_MASTER_PASS,CHIP_PATH_EXEC"
            + "  from vta_empresas  where EMP_ID = 1 ";
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strPathBase = rs.getString("CHIP_PATH_BASE");
            strNomTmpTrx = rs.getString("CHIP_FILE_TRX_TMP");
            strNumHexMaster = rs.getString("CHIP_CTA_MASTER_HEX");
            strPassHexMaster = rs.getString("CHIP_CTA_MASTER_PASS");
            strPathExec = rs.getString("CHIP_PATH_EXEC");
         }
         rs.close();
         strSql = "select *,(select w.CW_ID from mlm_chipcoin_wallet w where w.CT_ID = mlm_mov_comis.CT_ID limit 0,1) AS IdWallet"
            + "  from mlm_mov_comis where MMC_SINCR = 0 and MMC_ABONO> 0 "
            + " and (select w.CW_ID from mlm_chipcoin_wallet w where w.CT_ID = mlm_mov_comis.CT_ID limit 0,1) is not null limit 0,1 ";
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intMMC_ID = rs.getInt("MMC_ID");
            int intIdCliente = rs.getInt("CT_ID");
            int intIdWallet = rs.getInt("IdWallet");
            double dblMontAbono = rs.getDouble("MMC_ABONO");
            //Obtenemos datos de la cartera
            //Buscamos la primer cuenta del cliente
            String strNumHexDestino = "";
            double dblMontoCartera = 0;
            String strSql2 = "select w.CW_TOT_CHIPCOINS,w.CW_NUMERO_CUENTA from mlm_chipcoin_wallet w where w.CW_ID = " + intIdWallet;
            ResultSet rs2 = oConn.runQuery(strSql2, true);
            while (rs2.next()) {
               dblMontoCartera = rs2.getDouble("CW_TOT_CHIPCOINS");
               strNumHexDestino = rs2.getString("CW_NUMERO_CUENTA");
            }
            rs2.close();

            //Sincronizamos con la red privada Ethereum - ChipCoin
            boolean bolSincroniza = false;
            String strNumTransaccion = "";

            //Generamos el script
            //copiarJs(strPathBase, strNomTmpTrx, strNumHexMaster, strNumHexDestino, strPassHexMaster, dblMontAbono, intIdCliente);
            //Ejecutamos el shell
            /**
             * geth --exec 'loadScript("../ScriptJsChipCoin.js")' attach
             * http://localhost:8099
             */
            //ExecuteShellComand obj = new ExecuteShellComand();
            //String command = strPathExec + " --exec 'loadScript(\"" + strPathBase + strNomTmpTrx + intIdCliente + ".js\")' attach http://localhost:8099";
            //log.debug("command:" + command);
            //String output = obj.executeCommand(command);
            //log.debug("output: " + output);
            String strResp1 = sendPost("http://localhost:8099", "{\"method\": \"personal_unlockAccount\", \"params\": [\"" + strNumHexMaster + "\", \"" + strPassHexMaster + "\", 10 ],\"id\":" + intIdCliente + "}");
            JSONObject jsonObj1 = new JSONObject(strResp1);
            boolean bolResult = jsonObj1.getBoolean("result");
            log.debug("result:" + bolResult);
            if (bolResult) {
               //Conversion del monto a wei
               BigInteger bigNum = EtherUtil.convert((long) dblMontAbono, EtherUtil.Unit.ETHER);

               String strResp2 = sendPost("http://localhost:8099", "{\"jsonrpc\":\"2.0\",\"method\":\"eth_sendTransaction\",\"params\":[{\n"
                  + "  \"from\": \"" + strNumHexMaster + "\",\n"
                  + "  \"to\": \"0x" + strNumHexDestino + "\",\n"
                  + "  \"gas\": \"0x76c0\", \n"
                  + "  \"gasPrice\": \"0x9184e72a000\", \n"
                  + "  \"value\": \"0x" + bigNum.toString(16) + "\", \n"
                  + "  \"data\": \"0xd46e8dd67c5d32be8d46e8dd67c5d32be8058bb8eb970870f072445675058bb8eb970870f072445675\"\n"
                  + "}],\"id\":1}");
               JSONObject jsonObj2 = new JSONObject(strResp2);
               log.debug("strResp2:" + strResp2);
               strNumTransaccion = jsonObj2.getString("result");
               log.debug("result:" + strNumTransaccion);
               if (!strNumTransaccion.isEmpty()) {
                  bolSincroniza = true;
               }

               //Marcamos que ya se sincronizo
               if (bolSincroniza) {
                  String strUpdate = "update mlm_mov_comis set  MMC_SINCR = 1,MMC_TRANSACCION='" + strNumTransaccion + "' "
                     + " where MMC_ID = " + intMMC_ID;
                  oConn.runQueryLMD(strUpdate);
                  //Aumentamos el total de monedas del wallet
                  dblMontoCartera += dblMontAbono;
                  strUpdate = "update mlm_chipcoin_wallet set  CW_TOT_CHIPCOINS  = " + dblMontoCartera + " "
                     + " where CW_ID = " + intIdWallet;
                  oConn.runQueryLMD(strUpdate);
               }
            }

         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      } catch (Exception ex) {
         java.util.logging.Logger.getLogger(AplicaChipCoinsCarteras.class.getName()).log(Level.SEVERE, null, ex);
      }

   }

   /**
    * Copia el archivo template para la transferencia de la cartera
    */
   private void copiarJs(String strPathBase, String strNomTmpTrx,
      String strNumHexMaster, String strNumHexDestino, String strPassHexMaster, double dblMonto, int intIdCliente) {
      //Copiamos el script
      BufferedWriter bw = null;
      FileWriter fw = null;
      try {
         //Escritura

         fw = new FileWriter(strPathBase + strNomTmpTrx + intIdCliente + ".js");
         bw = new BufferedWriter(fw);
         //Lectura
         File f = new File(strPathBase + "template_transfer.js");
         BufferedReader b = new BufferedReader(new FileReader(f));
         String readLine = "";
         while ((readLine = b.readLine()) != null) {
            readLine = readLine.replace("{01}", strNumHexMaster);
            readLine = readLine.replace("{02}", strNumHexDestino);
            readLine = readLine.replace("{03}", strPassHexMaster);
            readLine = readLine.replace("{04}", dblMonto + "");
            //log.debug(readLine);
            bw.write(readLine + "\n");
         }
      } catch (IOException e) {
         log.error(e.getMessage());
      } finally {
         try {
            if (bw != null) {
               bw.close();
            }
            if (fw != null) {
               fw.close();
            }
         } catch (IOException ex) {
            log.error(ex.getMessage());
         }
      }
   }

   // HTTP POST request
   /**
    * Hace una peticion por el protocolo RPC
    *
    * @param url Es la url
    * @param urlParameters Son los parametros de la llamada generalmente en json
    * @return Es la respuesta de la llamada generalmente en json
    * @throws Exception
    */
   public String sendPost(String url, String urlParameters) throws Exception {

      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      //add reuqest header
      con.setRequestMethod("POST");
      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      // Send post request
      con.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      wr.writeBytes(urlParameters);
      wr.flush();
      wr.close();
      int responseCode = con.getResponseCode();
      log.debug("\nSending 'POST' request to URL : " + url);
      log.debug("Post parameters : " + urlParameters);
      log.debug("Response Code : " + responseCode);

      BufferedReader in = new BufferedReader(
         new InputStreamReader(con.getInputStream()));
      String inputLine;
      StringBuilder response = new StringBuilder();

      while ((inputLine = in.readLine()) != null) {
         response.append(inputLine);
      }
      in.close();

      //print result
      return response.toString();

   }
   // </editor-fold>

}
