package DB;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class Connect {

    public static Connection conn = null;
    public static Statement stat=null,stat1 = null,stat2=null,stat1123=null;
    public static ResultSet rs1,rs2,rs1123;
    static PreparedStatement ps = null;
    public static ResultSet rs = null;
    static String rdate = "";
    static String rtime = "";

    public static Connection openConnection() {
        try {
            System.out.println("02");
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/shopdroid?user='root'&password=");
            //conn = DriverManager.getConnection("jdbc:mysql://localhost/OMVTS_Db?user='omvts_ruchika'&password='omvts_ruchika1234'");
       

            stat = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             stat1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             stat2 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             stat1123 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           
            System.out.println("Connection done");
            rdate = getDate();
            rtime = getTime();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return conn;
    }

    public static boolean validateLogin(String username, String password) {
        System.out.println(username + ":" + password);
        boolean b = false;
        try {
            if (!(username.isEmpty() || password.isEmpty())) {
                openConnection();
                String sql="select * from tbluser where (emailid='" + username + "') && (password='" + password + "')";
                System.out.println();
                rs = stat.executeQuery(sql);
                if (rs.next()) {
                    b = true;
                    System.out.println("user validated");
                }
                closeConnection();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("user validated" + b);

        return b;
    }

 

    public static void closeConnection() {
        try {
            stat.close();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

 

    public static String getField(String tblname, String field, String matching_field, String value, String operator) {
        openConnection();
        String i = "";
        try {
            rs1123 = stat1123.executeQuery("select " + field + " from " + tblname + " where " + matching_field + "" + operator + "'" + value + "'");
            if (rs1123.next()) {
                i = rs1123.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }
    public static String getFieldInside(String tblname, String field, String matching_field, String value, String operator) {
        
        String i = "";
        try {
            rs1123 = stat1123.executeQuery("select " + field + " from " + tblname + " where " + matching_field + "" + operator + "'" + value + "'");
            if (rs1123.next()) {
                i = rs1123.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
        return i;
    }
    public static String getUserName(String tblname, String field1, String matching_field, String value, String operator) {
        //openConnection();
        String i = "";
        try {
            System.out.print("select " + field1 +"  from " + tblname + " where " + matching_field + "" + operator + "'" + value + "'");
            rs1123 = stat1123.executeQuery("select " + field1 + " from " + tblname + " where " + matching_field + "" + operator + "'" + value + "'");
            if (rs1123.next()) {
                i = rs1123.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //closeConnection();
        return i;
    }

    public static String getUserPassword(String id) {
        openConnection();
        String i = "";
        try {
            rs = stat.executeQuery("select password from tbluser where userid='" + id + "'");
            if (rs.next()) {
                i = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }

    public static void changePass(String id, String pass) {

        conn = openConnection();
        try {
            String sql = "update tbluser set password='" + pass + "' where userid='" + id + "' ";
            ps = conn.prepareStatement(sql);//to update
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public static int updateField(String tblname, String field, String value, String matching_field, String matching_value) {
        int i = 0;
        conn = openConnection();
        try {
            String sql = "update " + tblname + " set " + field + "='" + value + "' where " + matching_field + "='" + matching_value + "' ";
            System.out.print("sql=" + sql);
            ps = conn.prepareStatement(sql);//to update
            i = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }

 
      

    public static int saveUsers(String username, String password, String first_name, String last_name, String address
            , String state,  String city, String pincode,String mobile_no) {
        openConnection();
        int i = 0;
        try {
            String sql = "insert into customers (username,password,first_name,last_name,address,state,city,pincode,mobile_no,wallet_amount)values(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, first_name);
            statement.setString(4, last_name);
            statement.setString(5, address);
            statement.setString(6, state);
            statement.setString(7, city);
            
            statement.setString(8, pincode);
            statement.setString(9, mobile_no);
            statement.setString(10, "2000");
          
            i = statement.executeUpdate();
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            i=2;
        }catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }
  
    public static int buyProduct(String product_id, String customer_id, String shop_id) {
        openConnection();
        int i = 0;
        try {
            String sql = "insert into transaction (product_id,customer_id,shop_id,date)values(?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, product_id);
            statement.setString(2, customer_id);
            statement.setString(3, shop_id);
            statement.setString(4, getDate());
       
          
            i = statement.executeUpdate();
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            i=2;
        }catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }
  
    public static int saveNews(String userid, String subject, String news) {
        openConnection();
        int i = 0;
        try {
            String sql = "insert into tblnews (userid,subject,description,rdate)values(?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userid);
            statement.setString(2, subject);
            statement.setString(3, news);
         
            statement.setString(4, getDate());
           
          
            i = statement.executeUpdate();
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            i=2;
        }catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }
  
    
   
    public static int saveEvent(String user_id,String event_name,String event_details, String image,String event_date,
	String status,String venue,String event_time,String latitude,String longitude) {
        openConnection();
        int i = 0;
        try {
            String sql = "insert into tblevents (userid,event_name,event_details,image,event_date,status,venue,event_time,latitude,longitude,rdate)values(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, user_id);
            statement.setString(2, event_name);
            statement.setString(3, event_details);
            statement.setString(4, image);
            statement.setString(5, event_date);
			statement.setString(6, status);
			statement.setString(7, venue);
			statement.setString(8, event_time);
			statement.setString(9, latitude);
			statement.setString(10, longitude);
			statement.setString(11, getDate());

            i = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }
	
  
    public static int saveLocation(String latitude, String longitude,String userid) {
        openConnection();
        int i = 0;
        try {
            String sql = "insert into tbllocation (latitude,longitude,userid)values(?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
         
            statement.setString(1, latitude);
            statement.setString(2, longitude);
            statement.setString(3, userid);
          
            i = statement.executeUpdate();
        } catch (Exception e) {
           //e.printStackTrace();
        }
        closeConnection();
        return i;
    }
      public static int updateLocation(String latitude, String longitude,String userid) {
        openConnection();
        int i = 0;
        try {
            String sql = "update tbllocation set latitude=?,longitude=? where userid=?";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, latitude);
            statement.setString(2, longitude);
            statement.setString(3, userid);

            i = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }
 public static String getFileDateTime() {
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy_MM_dd_hh_mm_ss");

        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
    }
    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd:hh:mm:ss");

        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
    }
  

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd");

        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
    }

    public static String getTime() {
        DateFormat dateFormat1 = new SimpleDateFormat(
                "HH:mm:ss");

        Calendar cal = Calendar.getInstance();

        return dateFormat1.format(cal.getTime());// "11/03/14 12:33:43";
    }
}
