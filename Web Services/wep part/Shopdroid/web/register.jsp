<%@page import="org.json.simple.JSONObject"%>
<%
    	
		
		
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fname = request.getParameter("fname");
            String lname = request.getParameter("lname");
            String address = request.getParameter("address");
         
            String pincode = request.getParameter("pincode");
            
            String contact = request.getParameter("contact");
            String state = request.getParameter("state");
            String city = request.getParameter("city");
            DB.Connect.openConnection();
            JSONObject jSONObject = new JSONObject();
          //  DB.Connect.saveUsers(name, employee, intake, yoj, email, status, usertype, password, mobile)
            int i = DB.Connect.saveUsers(username, password, fname, lname, address, state, city, pincode, contact);

          
            DB.Connect.saveLocation("0", "0", username);
            jSONObject.put("msg", "Your account has been successfully created!!");
            jSONObject.put("userid", username);
            jSONObject.put("firstname", fname);

            DB.Connect.closeConnection();
            if (i == 2) {
                 jSONObject.put("msg", "You are already a registered user!!");
            jSONObject.put("userid", username);
            jSONObject.put("firstname", fname);
                response.getWriter().print(jSONObject);
            } else if(i>0){
                jSONObject.put("msg", "Your account has been successfully created!!");
            jSONObject.put("userid", username);
            jSONObject.put("firstname", fname);
              response.getWriter().print(jSONObject);
            }else{
             jSONObject.put("msg", "Failed To create account plz check all the fields");
                jSONObject.put("userid", "");
                jSONObject.put("firstname", fname);
                response.getWriter().print(jSONObject);
            }
%>