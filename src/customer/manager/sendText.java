/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.manager;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 *
 * @author mathew
 */
public class sendText {
    private final String ACCOUNT_SID = "AC04695bfbf38821be336024943ab332ed";
    private final String AUTH_TOKEN = "6934c4c7b7f114eb2c5aac75596b1de4";
    
    sendText(String recipient, String textWLbl) throws IOException{
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        //System.out.println(MainFXMLDocumentController.readFile(textWLbl, Charset.defaultCharset()));
        String body = MainFXMLDocumentController.readFile(textWLbl, Charset.defaultCharset());
        System.out.println(textWLbl);
        Message message = Message.creator(new PhoneNumber(recipient),
        new PhoneNumber("+19042042199"), 
        body).create();

    System.out.println(message.getSid());
  
    }
        
}

