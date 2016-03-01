/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capabilities;

/**
 *
 * @author Gabriel
 */
public interface Capabilities {
    // String ou JSON?
    String adaptRole (String dbResult) throws Exception;
    
    // String, JSON ou void?
    String dbSearch (String userAgent) throws Exception;
}

// midia, canal multimodal -> Perfil do usuario