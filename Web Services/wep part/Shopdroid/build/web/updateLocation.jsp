<%@page import="JSON.JSONObject"%>
<%
            String latitude = request.getParameter("latitude");
            String longitude = request.getParameter("longitude");
            String userid = request.getParameter("userid");
          int i=0;
         try{
          i= DB.Connect.saveLocation( latitude, longitude, userid);

         }catch(Exception e){}
         if(i>0){

         }else{
          i= DB.Connect.updateLocation( latitude, longitude, userid);

         }

          
          JSONObject jObj=new JSONObject();
            if(i>0){
              jObj.put("msg", "Location Updated Succesfully");
            response.getWriter().print(jObj);
            }else {
              jObj.put("msg", "Failed To update location");
            response.getWriter().print(jObj);
            }
%>