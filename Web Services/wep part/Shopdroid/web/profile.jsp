<%@page import="org.apache.http.message.BasicNameValuePair"%>
<%@page import="org.apache.http.NameValuePair"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%@page import="DB.Connect.*"%>
<%
            String userid = "",yoj, fname = "", lname = null, intake = "",  email, rdate, mobile;


           
 JSONObject json=new JSONObject();
            try {

                String username1 = request.getParameter("username");
                System.out.println("username="+username1);
                DB.Connect.openConnection();
                DB.Connect.rs = DB.Connect.stat.executeQuery("SELECT * FROM tbluser where userid='" + username1 + "' ");

                if (DB.Connect.rs.next()) {
                    userid = DB.Connect.rs.getString("userid");
                    fname = DB.Connect.rs.getString("name");
                    lname = DB.Connect.rs.getString("usertype");
                    yoj = DB.Connect.rs.getString("yoj");
                    mobile = DB.Connect.rs.getString("mobile_no");
                    email = DB.Connect.rs.getString("emailid");
                    
                    rdate = DB.Connect.rs.getString("rdate");
                
                    intake = DB.Connect.rs.getString("intake");

//{"firstName":"John", "lastName":"Doe"}
/*
                    json.put("msg", 
                            "Userid: "+userid
                                    +"\nName: "+fname
                                   + "\nEmployed At: "+lname
                                    + "\nYear of Joining Company: " + yoj
                                    +"\nIntake: "+intake+
                                    "\nMobile No. "+mobile+
                                    "\nEmail ID: "+email+
                                    "\nRegistration Date: "+rdate);
        */
                      json.put("msg", 
                          
                                    "\nName:                "+fname
                                   +"\nUsertype:    "+lname
                                   +"\nJoining Date:    " + yoj
                                   +"\nMobile No.:       "+mobile+
                                   "\nEmail ID:           "+email
                                   );
                
               
                    json.put("image",  DB.Connect.rs.getString("image"));
                	
				



              }
            } catch (Exception e) {
                e.printStackTrace();
            }
             System.out.println("username="+json);
            // response.getWriter().print("\nexpense="+expense+"\namount="+amount+"\ndoe="+doe+"\ndescription="+description);
            response.getWriter().print(json);

%>