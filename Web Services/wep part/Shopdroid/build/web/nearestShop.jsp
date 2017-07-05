<%@page import="JSON.JSONArray"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="org.json.simple.JSONObject"%>
<%
    
    String username = request.getParameter("userid");

	  String TAG_SHOP_NAME = "shopname";
	     String TAG_ID = "username";
	     String TAG_NAME = "state";
	    String TAG_ADD ="city";
 

    JSON.JSONArray jArray=new JSONArray();
String latitude,longitude;

    try {
      Connection con=  DB.Connect.openConnection();
      
String sql="select * from tbllocation where userid='" + username + "'";
System.out.println(sql);
Statement  stat=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
       ResultSet rs = stat.executeQuery(sql);
        if (rs.next()) {
            latitude = rs.getString("latitude");
            longitude = rs.getString("longitude");
           
            System.out.println("latitude i" + latitude+","+longitude);
           String sql_shop="select username,shop_name,address,state,city,pincode,mobile_no, "
                            + "(ACOS( SIN("+latitude+"*PI()/180)*SIN(latitude*PI()/180) + COS("+latitude+"*PI()/180)"
                            + "*COS(latitude*PI()/180)*COS(longitude*PI()/180-"+longitude+"*PI()/180) ) * 6371000) "
                            + "AS distance from shop_registration order by distance asc";
              System.out.println(sql_shop);
               Statement  stat1=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
               ResultSet rs1=stat1.executeQuery(sql_shop);
               while(rs1.next()){
                   if(rs1.getDouble("distance")<50000){
                     System.out.println("distance="+rs1.getString("distance"));
               JSONObject jobj1=new JSONObject();
               jobj1.put(TAG_SHOP_NAME, rs1.getString("shop_name"));
               jobj1.put(TAG_ID, rs1.getString("username"));
               jobj1.put(TAG_NAME, rs1.getString("state"));
               jobj1.put(TAG_ADD, rs1.getString("city"));
               int dis=(int)rs1.getDouble("distance")/1000;
               jobj1.put("distance", dis);
                       jArray.put(jobj1);
                   }
                  
               }
        } 

        DB.Connect.closeConnection();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
 response.getWriter().print(jArray.toString());


%>