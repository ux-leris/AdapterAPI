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
public class Browser implements Capabilities{

    @Override
    public String adaptRole(String dbResult) throws Exception{
        // Variáveis retornadas do WURFL em JSON
        String mobile_browser;
        String mobile_browser_version;

        // Conversao do JSON de entrada para as variaveis respectivas
        JSONObject capabilities;
        JSONParser parser = new JSONParser();
        capabilities = (JSONObject) parser.parse(dbResult);
        //JSONObject capabilities = (JSONObject) my_obj.get("capabilities");
        System.out.println("\t" + capabilities);
        
        mobile_browser = (String) capabilities.get("mobile_browser");
        mobile_browser_version = (String) capabilities.get("mobile_browser_version");
        
        // Olhar as APIs compatíveis em http://mobilehtml5.org/
        // para possíveis regras.
        
        
        // Criar um novo JSON e adicionar as informações à ele.
        JSONObject virtual = new JSONObject();
        
        if (mobile_browser.equals("Firefox Mobile")){
            virtual.put("luminosity", "true");
            virtual.put("proximity", "true");
            virtual.put("vibration", "true");
        } else {
            virtual.put("luminosity", "false");
            virtual.put("proximity", "false");
            virtual.put("vibration", "false");
        }
        
        if (mobile_browser.equals("Opera Mini")){
            virtual.put("geolocation", "false");
        } else {
            virtual.put("geolocation", "true");
        }
        
        if (mobile_browser.equals("IEMobile")){
            virtual.put("cameraAPI", "false");
            virtual.put("orientation", "false");
        } else {
            virtual.put("cameraAPI", "true");
            virtual.put("orientation", "true");
        }
            
        if (mobile_browser.equals("Chrome Mobile")){
            virtual.put("webSpeechAPI", "true");
        } else {
            virtual.put("webSpeechAPI", "false");
        }

        // Adicionar esse novo JSON ao JSON de entrada e retorná-lo
        capabilities.put("virtual", virtual);
        return capabilities.toJSONString();
    }

    @Override
    public String dbSearch(String userAgent) throws Exception {
        String urlInicio, urlCapacidades, urlFim, urlPath;
        String capacidades = 
                  "mobile_browser_version%0D%0A"
                + "mobile_browser";

        // Montagem URL de acesso ao Introspector Servlet WURFL
        urlPath = "http://localhost:8080/AdapterAPI/"; // Caminho do projeto
        urlInicio = "introspector.do?action=Form&form=pippo&ua=" + userAgent;
        urlCapacidades = "&capabilities="+capacidades;
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
