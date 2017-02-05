/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.manager;

import com.firebase.client.DataSnapshot;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author mathew
 */
public class MainFXMLDocumentController implements Initializable {
    
    @FXML
    private TextField emailWLbl, emailRLbl, textWLbl, textRLbl;
    
    @FXML
    private ChoiceBox locationChoice;
    
    @FXML
    private ChoiceBox chartType;
    
    @FXML
    private Button emailBtn, textBtn, emailWBtn, emailRBtn, textWBtn, textRBtn, sendR;
    
    @FXML
    private XYChart chart;
    
    String chartChoice, locChoice;
    
    ValueEventListener vEL = null;
    
    Firebase ref = new Firebase("https://rewardsprogram-e57cb.firebaseio.com/");
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emailWBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(emailWBtn.getScene().getWindow());
            if (selectedFile != null) {
                System.out.println(selectedFile.toString());
                    File welcome = new File(selectedFile.toString());
                    emailWLbl.setText(welcome.toString());
                }
            }
        });
        emailRBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(emailWBtn.getScene().getWindow());
            if (selectedFile != null) {
                System.out.println(selectedFile.toString());
                    File welcome = new File(selectedFile.toString());
                    emailRLbl.setText(welcome.toString());
                }
            
            }
        });
        textWBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(emailWBtn.getScene().getWindow());
            if (selectedFile != null) {
                System.out.println(selectedFile.toString());
                    File welcome = new File(selectedFile.toString());
                    textWLbl.setText(welcome.toString());
                }
            
            }
        });
        textRBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
                File selectedFile = fileChooser.showOpenDialog(emailWBtn.getScene().getWindow());
            if (selectedFile != null) {
                System.out.println(selectedFile.toString());
                    File welcome = new File(selectedFile.toString());
                    textRLbl.setText(welcome.toString());
                }
            
            }
        });
        sendR.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(textRLbl.getText().isEmpty() || emailRLbl.getText().isEmpty()){
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Please pick a text and email file before sending!");
                    alert.showAndWait();;
                }
                else{
                    System.out.println("files there " + textRLbl.getText() + " " + emailRLbl.getText());
                    sendRewards(textRLbl.getText(), emailRLbl.getText());
                }
            }
        });
        Platform.runLater(() -> {
                locationChoice.getSelectionModel().select("ALL");
                chartType.getSelectionModel().select("Day");
                emailWLbl.setText("welcomeEmailWithPictures.txt");
                textWLbl.setText("welcomeText.txt");
            });
    }    

    @FXML
    public void load(){
        
        emailBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("emailFXML.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Email Message");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        }
    
    });
        textBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("textFXML.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Text Message");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        }
    
    });
        locationChoice.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                locChoice = locationChoice.getValue().toString();
                System.out.println(locChoice);
                setChart(locChoice, chartChoice);
            }
        });
        chartType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                chartChoice = chartType.getValue().toString();
                System.out.println(chartChoice);
                setChart(locChoice, chartChoice);
            }
        });

        ref.addValueEventListener(vEL = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            
            String[] tempArray;
            if(snapshot.getValue()!=null){
            tempArray = snapshot.getValue().toString().split(",");
            for(int i = 0; i < tempArray.length; i++){
                String[] subArray = tempArray[i].trim().split("=");
                System.out.println(subArray[0].trim().replace("{", ""));
                //ref.removeValue();
                System.out.println(subArray[1].trim().replace("}", ""));
                String[] microArray = subArray[1].trim().replace("}", "").split(";");
                if(microArray[3].equals("coupon") || microArray[3].equals("none")){
                    System.out.println(microArray[1] + microArray[2]);
                    sqlUpdate(microArray);
                }
                else if(microArray.length == 4){
                        System.out.println(microArray[0] + microArray[1] + "5");
                        sqlInsert(microArray);       
                }
                else
                    System.out.println("Sorry this field does not match the pattern");
            }
            System.out.println("Waiting...");
            ref.removeValue();
            Platform.runLater(() -> {
                setChart(locChoice, chartChoice);
            });
            
            }
        }
       
        @Override
        public void onCancelled(FirebaseError firebaseError) {
            System.out.println("The read failed: " + firebaseError.getMessage());
        }
    });
    }


    @FXML
    public void setChart(String loc, String type){
        chart.getData().clear();
        System.out.println("Setting Chart");
        if(loc!=null && type != null){
        Connection conn;
        Statement stmt;
        conn = DBConnect.connect();
        stmt = DBConnect.stmt;
        ResultSet rs;
        int count=0;
        String sql;
        int numLogs;
        int startDay;
        String dayNum;
        if(type.equals("Week")){
            numLogs = 7;
            startDay = LocalDate.now().minusDays(6).getDayOfMonth();
        }
        else if(type.equals("Month")){
            numLogs = LocalDate.now().lengthOfMonth();
            startDay = 1;
        }
        else{
            numLogs = 1;
            startDay = LocalDate.now().getDayOfMonth();
        }
            
        String monthNum;
        if(LocalDate.now().getMonth().getValue() < 10)
            monthNum = "0" + LocalDate.now().getMonth().getValue();
        else
            monthNum = "" + LocalDate.now().getMonth().getValue();
        		   try{
                               XYChart.Series series = new XYChart.Series();
                               series.setName("Customer Logs");
                            for(int i = startDay; i < startDay + numLogs; i++){
                              if(i < 10)
                                  dayNum = "0" + i;                   
                              else
                                  dayNum = "" + i;
                              if(loc.equals("ALL"))
                                sql = "SELECT * FROM customer_logs WHERE date = '" + LocalDate.now().getYear() + "-" + monthNum + "-" + dayNum + "'";
                              else
                                sql = "SELECT * FROM customer_logs WHERE date = '" + LocalDate.now().getYear() + "-" + monthNum + "-" + i + "' AND location = '" + loc + "'";
                              System.out.println(sql);
                              rs = stmt.executeQuery(sql);
                              while(rs.next()){
                                  count++;
                              }
                              System.out.println(count);
                              series.getData().add(new XYChart.Data(LocalDate.now().getYear() + "-" + monthNum + "-" + dayNum, count));
                              count = 0;
        		      }
                            chart.getData().add(series);
                              }
        		   catch(SQLException se){
        		      //Handle errors for JDBC
        		      se.printStackTrace();
        		   }catch(Exception e){
        		      //Handle errors for Class.forName
        		      e.printStackTrace();
        		   }	
        		   try {
        			conn.close();
        		} catch (SQLException e) {
        			
        			e.printStackTrace();
        		}
        		   try {
        			stmt.close();
        		} catch (SQLException e) {
        			
        			e.printStackTrace();
                        }
    }
        
    }
    

    public void sendRewards(String text, String email){
        System.out.println("Sending Rewards");
        Connection conn;
        Statement stmt;
        conn = DBConnect.connect();
        stmt = DBConnect.stmt;
        ResultSet rs;
        String sql;
            
        String monthNum;
        if(LocalDate.now().getMonth().getValue() < 10)
            monthNum = "0" + LocalDate.now().getMonth().getValue();
        else
            monthNum = "" + LocalDate.now().getMonth().getValue();
        		   try{
                               XYChart.Series series = new XYChart.Series();
                               series.setName("Customer Logs");
                              if(locationChoice.getSelectionModel().getSelectedItem().toString().equals("ALL"))
                                sql = "SELECT * FROM users WHERE points >= 10";
                              else
                                sql = "SELECT * FROM users WHERE points >= 10 and location = '" + locationChoice.getSelectionModel().getSelectedItem().toString() + "'";
                              System.out.println(sql);
                              rs = stmt.executeQuery(sql);
                              while(rs.next()){
                                  sendREmail(rs.getString("email"));
                                  new sendText(rs.getString("phone"), textRLbl.getText());
                                  System.out.println(rs.getString("email") + " " + rs.getString("phone"));
                              }
        		      }
                              
        		   catch(SQLException se){
        		      //Handle errors for JDBC
        		      se.printStackTrace();
        		   }catch(Exception e){
        		      //Handle errors for Class.forName
        		      e.printStackTrace();
        		   }	
        		   try {
        			conn.close();
        		} catch (SQLException e) {
        			
        			e.printStackTrace();
        		}
        		   try {
        			stmt.close();
        		} catch (SQLException e) {
        			
        			e.printStackTrace();
                        }
    
        
    }
    
    public void sqlUpdate(String[] array){
        Connection conn;
        Statement stmt;
        conn = DBConnect.connect();
        stmt = DBConnect.stmt;
        ResultSet rs;
        int coupon;
        if(array[3].equals("none"))
            coupon = 1; 
        else
            coupon = 0;
        String sql;
        String key; 
            	if(array[0].equals(""))
                    key = "phone = '" + array[1] + "'";
                else
                    key = "email = '" + array[0] + "'";
        		   try{
        		      sql = "UPDATE users SET points = points+1 WHERE " + key;
        		      stmt.executeUpdate(sql); 
                              if(key.contains("phone"))
                                  sql = "INSERT INTO customer_logs (location, phone, points, date, coupon) VALUES('" + array[2] + "', '" + array[1] + "', '" + "1', '" + LocalDate.now() + "', '" + coupon + "')";
                              
                              if(key.contains("email"))
                                  sql = "INSERT INTO customer_logs (location, email, points, date, coupon) VALUES('" + array[2] + "', '" + array[0] + "', '" + "1', '" + LocalDate.now() + "', '" + coupon + "')";
                              System.out.println(sql);
        		      stmt.executeUpdate(sql);
                           }
                                
        		   catch(SQLException se){
        		      //Handle errors for JDBC
        		      se.printStackTrace();
        		   }catch(Exception e){
        		      //Handle errors for Class.forName
        		      e.printStackTrace();
        		   }	
        		   try {
        			conn.close();
        		} catch (SQLException e) {
        			
        			e.printStackTrace();
        		}
        		   try {
        			stmt.close();
        		} catch (SQLException e) {
        			
        			e.printStackTrace();
                        }
    }
    
    public void sqlInsert(String[] array){
        Connection conn;
        Statement stmt;
        conn = DBConnect.connect();
        stmt = DBConnect.stmt;
        ResultSet rs;
        String sql;
        		   try{
                              sql = "SELECT name FROM users WHERE phone = '" + array[2] + "'";
                              rs = stmt.executeQuery(sql);
                              if(!rs.next()){
        		      sql = "INSERT INTO users (name, phone, email, location, points) VALUES('" + array[0] + "', '" + array[2] + "', '" + array[1] + "', '"+ array[3] + "', '" + "10')";
                              stmt.executeUpdate(sql); 
                              sql = "INSERT INTO customer_logs (location, email, points, date) VALUES('" + array[3] + "', '" + array[1] + "', " + "'10', '" + LocalDate.now() + "')";
                              System.out.println(sql);
        		        stmt.executeUpdate(sql);
                                sendWEmail(array[1]);
                                new sendText(array[2], textWLbl.getText());
                     
                              }
                              
        		      }

        		   catch(SQLException se){
        		      //Handle errors for JDBC
        		      se.printStackTrace();
        		   }catch(Exception e){
        		      //Handle errors for Class.forName
        		      e.printStackTrace();
        		   }	
        		   try {
        			conn.close();
        		} catch (SQLException e) {
        			
        			e.printStackTrace();
        		}
        		   try {
        			stmt.close();
        		} catch (SQLException e) {
        			
        			e.printStackTrace();
                        }
    }
    private String[] words;
    public void sendWEmail(String recipient){
        // Recipient's email ID needs to be mentioned.
      String to = recipient;
      
        try {
          words = readFile(emailWLbl.getText(), Charset.defaultCharset()).split("`");
        } catch (IOException ex) {
            Logger.getLogger(MainFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
       System.out.println(words[0] + " " + words[1]); 
      // Sender's email ID needs to be mentioned
      String from = "antoninospizzacustomerrewards@gmail.com";
      final String username = "antoninospizzacustomerrewards@gmail.com";//change accordingly
      final String password = "136MwC448";//change accordingly

      Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

      Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
            }
         });

      try {

         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(to));

         // Set Subject: header field
         message.setSubject("Antoninos Rewards");

         // This mail has 2 part, the BODY and the embedded image
         MimeMultipart multipart = new MimeMultipart("related");

         // first part (the html)
         BodyPart messageBodyPart = new MimeBodyPart();
         String htmlText = "</pre><img src=\"cid:image\"><pre style=\"font-family:Times New Roman;font-size:150%\">" + words[0] + "</pre>";
         messageBodyPart.setContent(htmlText, "text/html");
         // add it
         multipart.addBodyPart(messageBodyPart);

         // second part (the image)
         messageBodyPart = new MimeBodyPart();
         DataSource fds = new FileDataSource(new File(words[1].trim()));

         messageBodyPart.setDataHandler(new DataHandler(fds));
         messageBodyPart.setHeader("Content-ID", "<image>");

         // add image to the multipart
         multipart.addBodyPart(messageBodyPart);

         // put everything together
         message.setContent(multipart);
         // Send message
         Transport.send(message);

         System.out.println("Sent message successfully....");

      } catch (MessagingException e) {
         throw new RuntimeException(e);
      }
    }
    
    public void sendREmail(String recipient){
        // Recipient's email ID needs to be mentioned.
      String to = recipient;
      
        try {
          words = readFile(emailRLbl.getText(), Charset.defaultCharset()).split("`");
        } catch (IOException ex) {
            Logger.getLogger(MainFXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
       System.out.println(words[0] + " " + words[1]); 
      // Sender's email ID needs to be mentioned
      String from = "antoninospizzacustomerrewards@gmail.com";
      final String username = "antoninospizzacustomerrewards@gmail.com";//change accordingly
      final String password = "136MwC448";//change accordingly

      Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

      Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
            }
         });

      try {

         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(to));

         // Set Subject: header field
         message.setSubject("Antoninos Rewards");

         // This mail has 2 part, the BODY and the embedded image
         MimeMultipart multipart = new MimeMultipart("related");

         // first part (the html)
         BodyPart messageBodyPart = new MimeBodyPart();
         String htmlText = "</pre><img src=\"cid:image\"><pre style=\"font-family:Times New Roman;font-size:150%\">" + words[0] + "</pre>";
         messageBodyPart.setContent(htmlText, "text/html");
         // add it
         multipart.addBodyPart(messageBodyPart);

         // second part (the image)
         messageBodyPart = new MimeBodyPart();
         DataSource fds = new FileDataSource(new File(words[1].trim()));

         messageBodyPart.setDataHandler(new DataHandler(fds));
         messageBodyPart.setHeader("Content-ID", "<image>");

         // add image to the multipart
         multipart.addBodyPart(messageBodyPart);

         // put everything together
         message.setContent(multipart);
         // Send message
         Transport.send(message);

         System.out.println("Sent message successfully....");

      } catch (MessagingException e) {
         throw new RuntimeException(e);
      }
    }
        
    static String readFile(String path, Charset encoding) 
        throws IOException 
        {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            return new String(encoded, encoding);
        }
}
