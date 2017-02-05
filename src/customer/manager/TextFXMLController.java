/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author mathew
 */
public class TextFXMLController implements Initializable {

    @FXML
    private Button saveBtn, chooseBtn;
        
    @FXML
    private TextArea text;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chooseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(chooseBtn.getScene().getWindow());
            if (selectedFile != null) {
                System.out.println(selectedFile.toString());
                    File welcome = new File(selectedFile.toString());
                try {
                    text.setText(readFile(welcome.toString(), Charset.defaultCharset()));
                } catch (IOException ex) {
                    Logger.getLogger(EmailFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
    
            }
        });

    saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              FileChooser fileChooser = new FileChooser();
  
              //Set extension filter
              FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
              fileChooser.getExtensionFilters().add(extFilter);
              
              //Show save file dialog
              File file = fileChooser.showSaveDialog(chooseBtn.getScene().getWindow());
              
              if(file != null){
                  SaveFile(text.getText(), file);
              }
    
            }
        });

    }
    
    static String readFile(String path, Charset encoding) 
        throws IOException 
        {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
        }
    
    private void SaveFile(String content, File file){
        try {
            FileWriter fileWriter = null;
             
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(EmailFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }   
    
}
