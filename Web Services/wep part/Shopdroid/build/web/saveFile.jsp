<%@page import="JSON.JSONObject"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStream"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.io.File" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
 

        <%
            boolean b = false;
            System.out.println("here");
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            String filename = "", realname = "", userid = "";
            System.out.println("here");

            String context = config.getServletContext().getRealPath("/");
            String filePath = context + "upload";

            int i = 0;
            File f = null;
            if (!isMultipart) {
            } else {
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                List items = null;
                try {
                    items = upload.parseRequest(request);
                } catch (FileUploadException e) {
                    e.printStackTrace();
                }
                Iterator itr = items.iterator();
                while (itr.hasNext()) {
                    FileItem item = (FileItem) itr.next();
                    if (item.isFormField()) {
                        String name = item.getFieldName();
                        String value = item.getString();
                        if (name.equalsIgnoreCase("userid")) {
                            userid = value;
                        }


                        System.out.println("Parameters " + name + " value " + value);


                    } else {
                        try {
                            String itemName = item.getName();
                            // File root = File.listRoots()[0];
                            //  System.out.println("fILE pATH=" + root.getAbsolutePath());
                            System.out.println("fILE pATH=" + filePath + itemName);
                            String strFile = DB.Connect.getFileDateTime();

                            f = new File(filePath + "\\" + strFile + ".jpg");
                            realname = strFile + ".jpg";
                            f.setWritable(true);
                            f.setReadable(true);
                            item.write(f);

                            b = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }

            }
            JSONObject jObj = new JSONObject();
            if (b == true) {
                i = DB.Connect.saveImage(userid, realname);
            }

            if (i > 0) {
                jObj.put("msg", "Image Uploaded Successfully!!");
                jObj.put("image", realname);
                response.getWriter().print(jObj);
            } else {
                jObj.put("msg", "Failed to update location");
                response.getWriter().print(jObj);
            }

%>
  


