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
public class Display implements Capabilities{

    @Override
    public String adaptRole(String dbResult) throws Exception {
        // Variáveis retornadas do WURFL em JSON
        int resolution_width;
        int resolution_height;
        int columns;
        int rows;
        int physical_screen_width;
        int physical_screen_height;
        String dual_orientation;

        // Conversao do JSON de entrada para as variaveis respectivas
        JSONObject capabilities;
        JSONParser parser = new JSONParser();
        capabilities = (JSONObject) parser.parse(dbResult);
        //JSONObject capabilities = (JSONObject) my_obj.get("capabilities");
        System.out.println("\t" + capabilities);
        
        resolution_width = Integer.parseInt((String) capabilities.get("resolution_width"));
        resolution_height = Integer.parseInt((String) capabilities.get("resolution_height"));
        columns = Integer.parseInt((String) capabilities.get("columns"));
        rows = Integer.parseInt((String) capabilities.get("rows"));
        physical_screen_width = Integer.parseInt((String) capabilities.get("physical_screen_width"));
        physical_screen_height = Integer.parseInt((String) capabilities.get("physical_screen_height"));
        dual_orientation = (String) capabilities.get("dual_orientation");
        
        // Criar um novo JSON e adicionar as informações à ele.
        JSONObject virtual = new JSONObject();
        
        if (physical_screen_width < physical_screen_height){
            virtual.put("orientation_preferred", "portrait");
            virtual.put("thumbs_only", "true");
        } else {
            virtual.put("orientation_preferred", "landscape");
            virtual.put("thumbs_only", "false");
        }
        
        // Cálculo da dimensão em polegadas da diagonal do dispositivo
        double diagonal = Math.sqrt(physical_screen_width*physical_screen_width + physical_screen_height*physical_screen_height);
        diagonal *= 0.039370;
        
        if (diagonal < 4){
            virtual.put("average_size", "small_dispositive");
        } else if (diagonal > 5.5) {
            virtual.put("average_size", "large_dispositive");
        } else {
            virtual.put("average_size", "medium_dispositive");
        }
        
        // Adicionar esse novo JSON ao JSON de entrada e retorná-lo
        capabilities.put("virtual", virtual);
        return capabilities.toJSONString();
    }

    @Override
    public String dbSearch(String userAgent) throws Exception {
        String urlInicio, urlCapacidades, urlFim, urlPath;
        String capacidades = 
                  "resolution_width%0D%0A"          // Largura da tela em pixels
                + "resolution_height%0D%0A"         // Altura da tela em pixels
                + "columns%0D%0A"                   // Numero de colunas apresentadas
                + "rows%0D%0A"                      // Numero de linhas apresentadas
                + "physical_screen_width%0D%0A"     // Largura da tela em milimetros
                + "physical_screen_height%0D%0A"    // Altura da tela em milimetros
                + "dual_orientation";               // Se pode ter duas orientacoes

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
