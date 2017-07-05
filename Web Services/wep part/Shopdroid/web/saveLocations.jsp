<%
            String latitude = request.getParameter("latitude");
            String longitude = request.getParameter("longitude");
            String userid = request.getParameter("userid");
          
           
          
           int i= DB.Connect.saveLocation( latitude, longitude, userid);

        
            if(i>0){
            response.getWriter().print("Location saved Succesfully");
            }else  response.getWriter().print("Failed To save location");
%>