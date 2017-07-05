<%@page import="org.json.simple.JSONObject"%>
<%
    String password = request.getParameter("password");
    String username = request.getParameter("username");

    String first_name = null;

    JSONObject jObj = new JSONObject();

    String userid = null;

    try {
        DB.Connect.openConnection();
String sql="select username,first_name from customers where username='" + username + "' and password='" + password + "'";
System.out.println(sql);
        DB.Connect.rs = DB.Connect.stat.executeQuery(sql);
        if (DB.Connect.rs.next()) {
            userid = DB.Connect.rs.getString(1);
            first_name = DB.Connect.rs.getString(2);
            jObj.put("userid", userid);
            jObj.put("login", true);

            jObj.put("firstName", first_name);
               response.getWriter().print(jObj);
            System.out.println("userid in session now" + DB.Connect.rs.getString(1));
        } else {
            jObj.put("userid", "");
            jObj.put("login", false);

            jObj.put("firstName", "");
            response.getWriter().print(jObj);
        }

        DB.Connect.closeConnection();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
 


%>