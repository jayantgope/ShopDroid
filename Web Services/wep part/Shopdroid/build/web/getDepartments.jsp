<%@page import="JSON.JSONArray"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="org.json.simple.JSONObject"%>
<%

  
    String TAG_CAT = "category";
   

    JSON.JSONArray jArray = new JSONArray();

    try {
        Connection con = DB.Connect.openConnection();

        String sql = "SELECT category FROM `products` group by category";
        System.out.println(sql);
        Statement stat = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stat.executeQuery(sql);
        while (rs.next()) {

            JSONObject jobj1 = new JSONObject();
         
            jobj1.put(TAG_CAT, rs.getString("category"));
           
            jArray.put(jobj1);
        }

        DB.Connect.closeConnection();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    System.out.println(jArray);
    response.getWriter().print(jArray.toString());


%>