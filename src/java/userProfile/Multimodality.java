/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userProfile;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Gabriel
 */
public class Multimodality implements UserProfile{

    @Override
    public String userPreference(String capabilities, String preferences) {
        JSONObject cap, pref;
        JSONParser parser = new JSONParser();
        Set chaves;
        Iterator i;
        String chave, valorAtual, valorNovo;
        
        try {
            cap = (JSONObject) parser.parse(capabilities);
            cap = (JSONObject) cap.get("virtual");
            pref = (JSONObject) parser.parse(preferences);
            
            // Verificar se o par chave/valor da preferencia é possivel.
            // Exemplo:   "orientation_preferred": "landscape" -> ok
            //            "orientation_preferred": "diagonal" -> falha
            
            // Voz
            chaves = pref.keySet();
            i = chaves.iterator();
            while (i.hasNext()){
                chave = i.next().toString();
                valorAtual = (String) cap.get(chave);
                if (valorAtual != null){
                    // Verificar aqui se a opção desejada é válida
                    valorNovo = (String) pref.get(chave);
                    
                    switch(chave){
                        case "orientation_preferred":
                            System.out.println("entrou");
                            // Só pode ser portrait ou landscape
                            if (valorNovo.equalsIgnoreCase("portrait") || valorNovo.equalsIgnoreCase("landscape")){
                                cap.put(chave, valorNovo);
                            }
                            break;
                        case "orientation":
                            // Se o valor da capacidade for false, não pode ser alterado para true.
                            // Caso contrário é valido.
                            if (valorAtual.equalsIgnoreCase("true")){
                                if (valorNovo.equalsIgnoreCase("false")){
                                    cap.put(chave, valorNovo);
                                }
                            }
                            break;
                        case "thumbs_only":
                            // True ou False, independente da situação
                            if (valorNovo.equalsIgnoreCase("false") || valorNovo.equalsIgnoreCase("true")){
                                cap.put(chave, valorNovo);
                            }
                            break;
                        case "geolocation":
                            // Se o valor da capacidade for false, não pode ser alterado para true.
                            // Caso contrário é valido.
                            if (valorAtual.equalsIgnoreCase("true")){
                                if (valorNovo.equalsIgnoreCase("false")){
                                    cap.put(chave, valorNovo);
                                }
                            }
                            break;
                        case "webSpeechAPI":
                            // Se o valor da capacidade for false, não pode ser alterado para true.
                            // Caso contrário é valido.
                            if (valorAtual.equalsIgnoreCase("true")){
                                if (valorNovo.equalsIgnoreCase("false")){
                                    cap.put(chave, valorNovo);
                                }
                            }
                            break;
                    }
                }
            }
            return cap.toJSONString();
            
        } catch (ParseException ex) {
            Logger.getLogger(Multimodality.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ("{}");
    }
    
}
