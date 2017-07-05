<%@page import="org.json.simple.JSONObject"%>
<%
    	
		
		
            String product_id = request.getParameter("product_id");
            String customer_id = request.getParameter("userid");
            String shop_id = request.getParameter("shop");
         
           
            DB.Connect.openConnection();
            JSONObject jSONObject = new JSONObject();
          //  DB.Connect.saveUsers(name, employee, intake, yoj, email, status, usertype, password, mobile)
            int i = DB.Connect.buyProduct(product_id, customer_id, shop_id);

          
          
             if(i>0){
                jSONObject.put("msg", "Product Purchased successfully!!");
          
              response.getWriter().print(jSONObject);
            }else{
             jSONObject.put("msg", "Failed To purchase product");
              
                response.getWriter().print(jSONObject);
            }
%>