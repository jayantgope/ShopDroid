<%@page import="JSON.JSONArray"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="org.json.simple.JSONObject"%>
<%

    String username = request.getParameter("shop_id");

    String TAG_NAME = "product_name";
    String TAG_CAT = "category";
    String TAG_PRICE = "price";
    String TAG_STOCK = "stock";

    JSON.JSONArray jArray = new JSONArray();

    try {
        Connection con = DB.Connect.openConnection();

        String sql = "select * from products where shop_id='" + username + "'";
        System.out.println(sql);
        Statement stat = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stat.executeQuery(sql);
        while (rs.next()) {

            JSONObject jobj1 = new JSONObject();
            jobj1.put(TAG_NAME, rs.getString("product_name"));
            jobj1.put(TAG_CAT, rs.getString("category"));
            jobj1.put(TAG_PRICE, rs.getString("unit_cost"));
            jobj1.put(TAG_STOCK, rs.getString("quantity"));
            jobj1.put("shop", rs.getString("shop_id"));
 jobj1.put("product_id", rs.getString("product_id"));
            jArray.put(jobj1);
        }

        DB.Connect.closeConnection();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    System.out.println(jArray);
    response.getWriter().print(jArray.toString());


%>