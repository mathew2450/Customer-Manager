/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.manager;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
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
import javafx.stage.FileChooser.ExtensionFilter;


/**
 * FXML Controller class
 *
 * @author mathew
 */
public class EmailFXMLController implements Initializable {
    @FXML
    private Button chooseTop, saveBtn, textChoice, chooseBot;
    
    @FXML
    private ImageView headerPic, footerPic;
    
    @FXML
    private TextArea emailBody;
    
    File homeDir = new File(System.getProperty("user.dir"));
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chooseBot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(chooseBot.getScene().getWindow());
            if (selectedFile != null) {
                System.out.println(selectedFile.toString());
                Image image1 = new Image(selectedFile.toURI().toString());
                footerPic.setImage(image1);
                headerPic.setAccessibleText(homeDir.toURI().relativize(selectedFile.toURI()).getPath());
                }
    
            }
        });
        chooseTop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(chooseTop.getScene().getWindow());
            if (selectedFile != null) {
                System.out.println(selectedFile.toString());
                Image image1 = new Image(selectedFile.toURI().toString());
                headerPic.setImage(image1);
                headerPic.setAccessibleText(homeDir.toURI().relativize(selectedFile.toURI()).getPath());
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
              File file = fileChooser.showSaveDialog(chooseTop.getScene().getWindow());
              
              if(file != null){
                  SaveFile(emailBody.getText(), file);
              }
    
            }
        });
    textChoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Text Files (*.txt)", "*.txt"),
                    new ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(chooseTop.getScene().getWindow());
            if (selectedFile != null) {
                System.out.println(selectedFile.toString());
                    File welcome = new File(selectedFile.toString());
                try {
                    emailBody.setText(readFile(welcome.toString(), Charset.defaultCharset()));
                } catch (IOException ex) {
                    Logger.getLogger(EmailFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
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
            fileWriter.write(content + "\n`" + headerPic.getAccessibleText() + "\n`" + footerPic.getAccessibleText());
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(EmailFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
}
