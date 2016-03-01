/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;
import capabilities.Capabilities;
import capabilities.Browser;
import capabilities.DevicesSO;
import capabilities.Display;
import userProfile.UserProfile;
import userProfile.Multimodality;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 * REST Web Service
 *
 * @author Gabriel
 */
@Path("adapt")
public class Adapter {

    @Context
    private UriInfo context;

    public Adapter() {
    }

    // Retorna o conjunto de regras completo, considerando as preferencias do usuario
    @GET
    @Path("/userProfile")
    @Consumes("text/plain")
    @Produces("application/json")
    public Response roleGeral(@QueryParam("useragent") String useragent, @QueryParam("upref") String upref) {
        useragent = useragent.replaceAll(" ", "%20");
        Capabilities cap;
        JSONObject saida = new JSONObject();
        JSONObject virtual = new JSONObject();
        JSONObject entrada;
        JSONObject virtualAux;
        JSONParser parser = new JSONParser();
        try{
            cap = new DevicesSO();
            entrada = (JSONObject) parser.parse(cap.adaptRole(cap.dbSearch(useragent)));
            virtualAux = (JSONObject) entrada.remove("virtual");
            virtual.putAll(virtualAux);
            saida.putAll(entrada);
            
            cap = new Browser();
            entrada = (JSONObject) parser.parse(cap.adaptRole(cap.dbSearch(useragent)));
            virtualAux = (JSONObject) entrada.remove("virtual");
            virtual.putAll(virtualAux);
            saida.putAll(entrada);
            
            cap = new Display();
            entrada = (JSONObject) parser.parse(cap.adaptRole(cap.dbSearch(useragent)));
            virtualAux = (JSONObject) entrada.remove("virtual");
            virtual.putAll(virtualAux);
            saida.putAll(entrada);
            
            saida.put("virtual", virtual);
            
            // Alterar capacidades virtuais incluindo o perfil do usuário
            UserProfile user = new Multimodality();
            virtualAux = (JSONObject) parser.parse(user.userPreference(saida.toJSONString(), upref));
            saida.put("virtual", virtualAux);
            
            System.out.println(saida.toJSONString());
            return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(saida.toJSONString())
                .build();
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    // Retorna o conjunto de regras completo
    @GET
    @Path("/capabilities")
    @Consumes("text/plain")
    @Produces("application/json")
    public Response roleGeral(@QueryParam("useragent") String useragent) {
        useragent = useragent.replaceAll(" ", "%20");
        Capabilities cap;
        JSONObject saida = new JSONObject();
        JSONObject virtual = new JSONObject();
        JSONObject entrada;
        JSONObject virtualAux;
        JSONParser parser = new JSONParser();
        try{
            cap = new DevicesSO();
            entrada = (JSONObject) parser.parse(cap.adaptRole(cap.dbSearch(useragent)));
            virtualAux = (JSONObject) entrada.remove("virtual");
            virtual.putAll(virtualAux);
            saida.putAll(entrada);
            
            cap = new Browser();
            entrada = (JSONObject) parser.parse(cap.adaptRole(cap.dbSearch(useragent)));
            virtualAux = (JSONObject) entrada.remove("virtual");
            virtual.putAll(virtualAux);
            saida.putAll(entrada);
            
            cap = new Display();
            entrada = (JSONObject) parser.parse(cap.adaptRole(cap.dbSearch(useragent)));
            virtualAux = (JSONObject) entrada.remove("virtual");
            virtual.putAll(virtualAux);
            saida.putAll(entrada);
            
            saida.put("virtual", virtual);
           
            return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(saida.toJSONString())
                .build();
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    // Regras relativas ao dispositivo/SO
    @GET
    @Path("/role/devicesso")
    @Consumes("text/plain")
    @Produces("application/json")
    public Response roleDevicesSO(@QueryParam("useragent") String useragent){
        useragent = useragent.replaceAll(" ", "%20");
        Capabilities cap = new DevicesSO();
        
        try{
            return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(cap.adaptRole(cap.dbSearch(useragent)))
                .build();
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    // Regras relativas ao Browser
    @GET
    @Path("/role/browser")
    @Consumes("text/plain")
    @Produces("application/json")
    public Response roleBrowser(@QueryParam("useragent") String useragent){
        useragent = useragent.replaceAll(" ", "%20");
        Capabilities cap = new Browser();
        
        try{
            return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(cap.adaptRole(cap.dbSearch(useragent)))
                .build();
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    // Regras relativas ao Display
    @GET
    @Path("/role/display")
    @Consumes("text/plain")
    @Produces("application/json")
    public Response roleDisplay(@QueryParam("useragent") String useragent){
        useragent = useragent.replaceAll(" ", "%20");
        Capabilities cap = new Display();
        
        try{
            return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(cap.adaptRole(cap.dbSearch(useragent)))
                .build();
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Funções para consulta do resultado puro da consulta ao banco
    @GET
    @Path("/consultdb")
    @Consumes("text/plain")
    @Produces("application/json")
    public Response consultaGeral(@QueryParam("useragent") String useragent){
        useragent = useragent.replaceAll(" ", "%20");
        Capabilities cap;
        JSONObject saida = new JSONObject();
        JSONObject entrada;
        JSONParser parser = new JSONParser();
        try{
            cap = new DevicesSO();
            entrada = (JSONObject) parser.parse(cap.dbSearch(useragent));
            saida.putAll(entrada);
            
            cap = new Browser();
            entrada = (JSONObject) parser.parse(cap.dbSearch(useragent));
            saida.putAll(entrada);
            
            cap = new Display();
            entrada = (JSONObject) parser.parse(cap.dbSearch(useragent));
            saida.putAll(entrada);
            
            return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(saida.toJSONString())
                .build();
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @GET
    @Path("/consultdb/devicesso")
    @Consumes("text/plain")
    @Produces("application/json")
    public Response consultaDevicesSO(@QueryParam("useragent") String useragent){
        useragent = useragent.replaceAll(" ", "%20");
        Capabilities cap = new DevicesSO();
        
        try{
            return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(cap.dbSearch(useragent))
                .build();
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @GET
    @Path("/consultdb/browser")
    @Consumes("text/plain")
    @Produces("application/json")
    public Response consultaBrowser(@QueryParam("useragent") String useragent){
        useragent = useragent.replaceAll(" ", "%20");
        Capabilities cap = new Browser();
        
        try{
            return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(cap.dbSearch(useragent))
                .build();
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @GET
    @Path("/consultdb/display")
    @Consumes("text/plain")
    @Produces("application/json")
    public Response consultaDisplay(@QueryParam("useragent") String useragent){
        useragent = useragent.replaceAll(" ", "%20");
        Capabilities cap = new Display();
        
        try{
            return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(cap.dbSearch(useragent))
                .build();
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
