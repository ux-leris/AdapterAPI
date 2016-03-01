/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capabilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Gabriel
 */
public class DevicesSO implements Capabilities {

    @Override
    public String adaptRole(String dbResult) throws Exception {

        // Variáveis retornadas do WURFL em JSON
        String device_os;
        String device_os_version;
        String model_name;
        String brand_name;
        String is_wireless_device;
        String is_tablet;
        String pointing_method;

        // Conversao do JSON de entrada para as variaveis respectivas
        JSONObject capabilities;
        JSONParser parser = new JSONParser();
        capabilities = (JSONObject) parser.parse(dbResult);
        //JSONObject capabilities = (JSONObject) my_obj.get("capabilities");
        System.out.println("\t" + capabilities);
        
        device_os = (String) capabilities.get("device_os");
        device_os_version = (String) capabilities.get("device_os_version");
        model_name = (String) capabilities.get("model_name");
        brand_name = (String) capabilities.get("brand_name");
        is_wireless_device = (String) capabilities.get("is_wireless_device");
        is_tablet = (String) capabilities.get("is_tablet");
        pointing_method = (String) capabilities.get("pointing_method");
        
        // Criar um novo JSON e adicionar as informações à ele.
        JSONObject virtual = new JSONObject();
        
        // Adicionar esse novo JSON ao JSON de entrada e retorná-lo
        capabilities.put("virtual", virtual);
        return capabilities.toJSONString();
    }

    @Override
    public String dbSearch(String userAgent) throws Exception {
        String urlInicio, urlCapacidades, urlFim, urlPath;
        String capacidades = 
                  "device_os%0D%0A"             // Sistema Operacional
                + "device_os_version%0D%0A"     // Versão do sistema operacional
                + "model_name%0D%0A"            // Modelo do dispositivo
                + "brand_name%0D%0A"            // Marca do dispositivo
                + "is_wireless_device%0D%0A"    // Se é um dispositivo movel
                + "is_tablet%0D%0A"             // Se é um tablet
                + "pointing_method";            // Tipo de metodo de entrada

        // Montagem URL de acesso ao Introspector Servlet WURFL
        urlPath = "http://localhost:8080/AdapterAPI/"; // Caminho do projeto
        urlInicio = "introspector.do?action=Form&form=pippo&ua=" + userAgent;
        urlCapacidades = "&capabilities=" + capacidades;
        urlFim = "&wurflEngineTarget=performance&wurflUserAgentPriority=OverrideSideloadedBrowserUserAgent";

        // Conexão com o Servlet
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(
                urlPath + urlInicio + urlCapacidades + urlFim);
        getRequest.addHeader("accept", "application/json");

        HttpResponse response = httpClient.execute(getRequest);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

        String buffer;
        String dados = "";
        //System.out.println("Output from Server .... \n");
        while ((buffer = br.readLine()) != null) {
            dados += buffer;
        }
        //System.out.println("Saída:\n\t" + dados);

        httpClient.getConnectionManager().shutdown();
        
        JSONObject my_obj;
        JSONParser parser = new JSONParser();
        my_obj = (JSONObject) parser.parse(dados);
        JSONObject capabilities = (JSONObject) my_obj.get("capabilities");
        
        return capabilities.toJSONString();
    }
    
}
