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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author mathew
 */
public class MainFXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private ChoiceBox locationChoice;
    
    @FXML
    private ChoiceBox chartType;
    
    @FXML
    private XYChart chart;
    
    String chartChoice, locChoice;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    public void load(){
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
        // Get a reference to our posts
        Firebase ref = new Firebase("https://rewardsprogram-e57cb.firebaseio.com/");
        // Attach an listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
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
                              series.getData().add(new XYChart.Data(LocalDate.now().toString(), count));
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
    
    public void sqlUpdate(String[] array){
        Connection conn;
        Statement stmt;
        conn = DBConnect.connect();
        stmt = DBConnect.stmt;
        ResultSet rs;
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
                                  sql = "INSERT INTO customer_logs (location, phone, points, date, coupon) VALUES('" + array[2] + "', '" + array[1] + "', '" + "1', '" + LocalDate.now() + "', '" + array[3] + "')";
                              
                              if(key.contains("email"))
                                  sql = "INSERT INTO customer_logs (location, email, points, date, coupon) VALUES('" + array[2] + "', '" + array[0] + "', '" + "1', '" + LocalDate.now() + "', '" + array[3] + "')";
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
        		      sql = "INSERT INTO users (name, phone, email, location, points) VALUES('" + array[0] + "', '" + array[2] + "', '" + array[1] + "', '"+ array[3] + "', '" + "'10')";
        		      stmt.executeUpdate(sql); 
                              sql = "INSERT INTO customer_logs (location, email, points, date) VALUES('" + array[3] + "', '" + array[1] + "', '" + "10', '" + LocalDate.now() + "')";
                              System.out.println(sql);
        		      stmt.executeUpdate(sql);
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
}
