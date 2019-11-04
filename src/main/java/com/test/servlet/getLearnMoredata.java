package com.test.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.impl.FreeTrialandCart;

//import com.sun.jersey.core.util.Base64;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/getLearnMoredata" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class getLearnMoredata extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	// static ResourceBundle bundleststic = ResourceBundle.getBundle("config_SKU1");

	// @Reference
	// private ParseSlingData parseSlingData;
	// ParseSlingData parseSlingData= new ParseSlingDataImpl();
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String TemplateName = request.getParameter("templatename");
           String email="templateadmin@gmail.com";
           String group="nogroup";
           Node DocTigerAdvance=null;
           Node TemplateLibrary=null;
		//out.println("email  :: "+email);
		try {

			Session session = null;
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			// Node content = session.getRootNode().getNode("content");

			FreeTrialandCart cart = new FreeTrialandCart();
			String freetrialstatus = cart.checkfreetrial(email);
           // out.println("freetrialstatus  "+freetrialstatus);
			DocTigerAdvance = cart.getDocTigerAdvNode(freetrialstatus, email, group, session, response);
			//out.println("DocTigerAdvance  "+DocTigerAdvance);
			if (DocTigerAdvance != null) {
				
				if (DocTigerAdvance.hasNode("TemplateLibrary")) {
					TemplateLibrary = DocTigerAdvance.getNode("TemplateLibrary");
					
					if(TemplateLibrary.hasNode(TemplateName)){
						
						Node Tempname = TemplateLibrary.getNode(TemplateName);
						//out.println("Tempname "+Tempname);
						String type="";
						if(Tempname.hasProperty("type")) {
							type=Tempname.getProperty("type").getPath();
						}

						
						
						String docximagesarr="";
						JSONArray docximagesArray = new JSONArray();
						JSONArray returndocximagesArray = new JSONArray();

							JSONObject json = new JSONObject();
							//out.println(Tempname.hasProperty("docximagesarr"));

                            if(Tempname.hasProperty("docximagesarr")) {
                            	 docximagesarr =Tempname.getProperty("docximagesarr").getString();
     							//out.println("docximagesarr  "+docximagesarr);

                            	 if(isJSONValid(docximagesarr)) {
                            		 docximagesArray=new JSONArray(docximagesarr);
                            		 
                            		for(int i=0 ;i<docximagesArray.length(); i++) {
                            			JSONObject sub= docximagesArray.getJSONObject(i);
                            			if(sub.has("fileName")) {
                            				returndocximagesArray.put("https://bluealgo.com:8092/wordToImageFolder/"+sub.getString("fileName"));
                            			}
                            		} 
                            	 }
                            }		
 							json.put("imagelinkarr", returndocximagesArray);
							json.put("status", "successs");
							json.put("type", type);
							json.put("nodepath", Tempname.getPath());

                                out.println(json);
					}
				}
				else {
					JSONObject json = new JSONObject();
					json.put("imagelinkarr", new JSONArray());
					json.put("status", "error");
					out.println(json);
				}
			}else {
				JSONObject json = new JSONObject();
				json.put("imagelinkarr", new JSONArray());
				json.put("status", "error");
				out.println(json);
			}
	
       }catch(Exception e){


	}
}

	public static boolean isNullString(String p_text) {
		if (p_text != null && p_text.trim().length() > 0 && !"null".equalsIgnoreCase(p_text.trim())) {
			return false;
		} else {
			return true;
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


