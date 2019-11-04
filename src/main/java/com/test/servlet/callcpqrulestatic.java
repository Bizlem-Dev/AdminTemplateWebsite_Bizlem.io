package com.test.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import com.service.impl.BypassSSlCertificate;

@SlingServlet(paths = "/callcpqrulestaticWeb")

public class callcpqrulestatic extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
	
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		       PrintWriter out=response.getWriter();
		       try {
		    	   
		    	   if (request.getRequestPathInfo().getExtension().equals("getcpqdata")) {
		    	   
		    	   BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
					ByteArrayOutputStream buf = new ByteArrayOutputStream();
					int result = bis.read();

					while (result != -1) {
						buf.write((byte) result);
						result = bis.read();
					}
					String res = buf.toString("UTF-8");
					JSONObject resultjsonobject = new JSONObject(res);
					
		    	   String urlstr=bundleststic.getString("carrotrule_outputbuilder");
		    	  // "http://development.bizlem.io:8087/com.carrotruleangular/ExecuteFinalOutput";
		    	   String methodres =callPostJSon(urlstr, resultjsonobject);
		    	   out.println(methodres);
		    	   } // for callcpqdata
		    	   else{
		    		   
		    		   if (request.getRequestPathInfo().getExtension().equals("sendmaildata")) {
		    			   
		    			   String urlstrMail=bundleststic.getString("sendatamaildependency");
		    			   
		    			   BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
							ByteArrayOutputStream buf = new ByteArrayOutputStream();
							int result = bis.read();

							while (result != -1) {
								buf.write((byte) result);
								result = bis.read();
							}
							String res = buf.toString("UTF-8");
							JSONObject resultjsonobjectMail = new JSONObject(res);
							
							  String methodresMail =callPostJSon(urlstrMail, resultjsonobjectMail);
					    	  out.println(methodresMail);
		    			   
		    		   } // sendmaildata
		    		   
		    	   }
			} catch (Exception e) {
				e.printStackTrace(out);
	    	    out.println("");  
			}
	}
	
	
	public static boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
		} catch (JSONException ex) {
			
			try {
				new JSONArray(test);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}
	
	public String callPostJSon(String urlstr, JSONObject Obj) {
		StringBuffer response =null;
			  try {
				  
				  BypassSSlCertificate.ignoreHttps(urlstr);

			   URL url = new URL(urlstr);
			   HttpURLConnection con = (HttpURLConnection) url.openConnection();
			   con.setRequestMethod("POST");

			   con.setRequestProperty("Content-Type", "application/json");
			   con.setRequestProperty("Accept-Charset", "UTF-8");
			  
			   con.setDoOutput(true);
			   DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			   wr.writeBytes(Obj.toString());
			   wr.flush();
			   wr.close();


			   BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			   String inputLine;
			    response = new StringBuffer();

			   while ((inputLine = in.readLine()) != null) {
			    response.append(inputLine);
			   }
			   in.close();

			   System.out.println("post op "+response.toString());
			  }
			  catch (Exception e) {
			return   e.getMessage();
			  }
			  return response.toString();

			 }
}