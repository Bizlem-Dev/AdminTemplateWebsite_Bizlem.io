package com.test.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import com.service.impl.SOAPCall;

@SlingServlet(paths = "/getcountrynameWeb")

public class getcountrynameserv extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	
	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		       PrintWriter out=response.getWriter();
		       SOAPCall soapcall = new SOAPCall();
		       String country = null;
		       try {
		    	   
		    	   String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		    	 //  out.println("ipAddress 1 "+ipAddress);
		           if (ipAddress == null) {  
		             ipAddress = request.getRemoteAddr();  
		       }
		    	   //out.println("ipAddress 2 "+ipAddress);

		    		String url ="http://www.geoplugin.net/json.gp?ip="+ipAddress;
		    		String res = soapcall.callGet(url);
		    		
		    		JSONObject obj = new JSONObject(res);
		    		if(obj.has("geoplugin_countryName")) {country = obj.getString("geoplugin_countryName");}
		    				    	    out.print(country.trim());  
		    	  
			} catch (Exception e) {
				e.printStackTrace(out);
	    	    out.print("");  
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
}