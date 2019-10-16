package com.test.servlet;

import java.io.IOException;
import java.io.PrintWriter;


import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
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

@SuppressWarnings("serial")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Prefix Test Servlet Minus One"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/TemplateAdminListData" }),
		@Property(name = "sling.servlet.extensions", value = { "addAccount", "account", "success", "ajax", "newBlog",
				"ajaxBlog", "searchBlog", "search", "following", "follower", "userContent", "userPost", "userDraft",
				"userQueue", "home", "menu", "likeBlog", "deleteBlog", "tagSearch", "followerSearch", "edit",
				"viewBlog", "tagPosts", "blogSearch", "deleteBlogId", "deleteAccount", "confirmAccount", "confirmBlog",
				"randomBlog" })

})
public class CheckTestServlet2 extends SlingAllMethodsServlet {


	@Reference
	private SlingRepository repo;

	Session session = null;

	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out=response.getWriter();
		String adminEmailId=request.getParameter("adminEmailId");
		
		try{
			
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			
	    		   Node content=null;
	    		   Node services=null;
	    		   Node freetrial=null;
	    		   Node users=null;
					
						if (session.getRootNode().hasNode("content")) {
							content = session.getRootNode().getNode("content");
							
							if(content.hasNode("services")){
								services = content.getNode("services");
								
								if(services.hasNode("freetrial")){
									freetrial=services.getNode("freetrial");
									
									if(freetrial.hasNode("users")){
										users=freetrial.getNode("users");
										
										if(users.hasNode(adminEmailId)){
										   Node adminEmailIdNode=users.getNode(adminEmailId);
										   
										   Node DocTigerAdvanced=null;
										   Node TemplateLibrary=null;
										   
										   if(adminEmailIdNode.hasNode("DocTigerAdvanced")){
											   DocTigerAdvanced= adminEmailIdNode.getNode("DocTigerAdvanced");
											   
											   if(DocTigerAdvanced.hasNode("TemplateLibrary")){
												   TemplateLibrary=DocTigerAdvanced.getNode("TemplateLibrary");
												   
												   if( TemplateLibrary.hasNodes() ){
													  NodeIterator itr= TemplateLibrary.getNodes();
													  JSONObject mainJsonObj=new JSONObject();
													  
													  JSONArray domainArray=null;
													 
													  
													  while(itr.hasNext()){
														  Node nextNode=itr.nextNode();
															String docximagesarr="";
															JSONArray docximagesArray = new JSONArray();
															JSONArray returndocximagesArray = new JSONArray();

														  String domainname="";
														  String subdomainname="";
														  String type="";
														  
														  if(nextNode.hasProperty("domainname")){
															 domainname= nextNode.getProperty("domainname").getString();
														  }if(nextNode.hasProperty("subdomainname")){
															  subdomainname= nextNode.getProperty("subdomainname").getString();
														  }if(nextNode.hasProperty("type")){
															  type= nextNode.getProperty("type").getString();
														  }
														  
														  
								                            if(nextNode.hasProperty("docximagesarr")) {
								                            	 docximagesarr =nextNode.getProperty("docximagesarr").getString();
								     							//out.println("docximagesarr  "+docximagesarr);

								                            	 if(isJSONValid(docximagesarr)) {
								                            		 docximagesArray=new JSONArray(docximagesarr);
								                            		 
								                            		for(int i=0 ;i<docximagesArray.length(); i++) {
								                            			JSONObject sub= docximagesArray.getJSONObject(i);
								                            			if(sub.has("fileName")) {
								                            				returndocximagesArray.put("https://uk.bluealgo.com:8092/wordToImageFolder/"+sub.getString("fileName"));
								                            			}
								                            		} 
								                            	 }
								                            }		

														  
//														  domainArray.put();
//														  mainJsonObj.put(domainname, domainArray);
														//  out.println("domainname :: "+domainname);
														 
														  if(mainJsonObj.has(domainname)){
//															 String a1= mainJsonObj.getString(domainname);
															  JSONArray alreadydomainArray=mainJsonObj.getJSONArray(domainname);
															  JSONObject objProp1=new JSONObject();
//															  if(domainname.equalsIgnoreCase(a1)){
																  objProp1.put("subdomainname", subdomainname);
																  objProp1.put("type", type);
																  objProp1.put("nodepath", nextNode.getPath());
																  objProp1.put("templatename", nextNode.getName());
																  objProp1.put("imagelinkarr", returndocximagesArray);
																  
																  alreadydomainArray.put(objProp1);
																  mainJsonObj.put(domainname, alreadydomainArray);
//															  }
															  
														  }else{
															  domainArray=new JSONArray();
															  JSONObject objProp1=new JSONObject();
															  objProp1.put("subdomainname", subdomainname);
															  objProp1.put("type", type);
															  objProp1.put("nodepath", nextNode.getPath());
															  objProp1.put("templatename", nextNode.getName());
															  objProp1.put("imagelinkarr", returndocximagesArray);

															  domainArray.put(objProp1);
															  mainJsonObj.put(domainname, domainArray);
														  }
														 
														 // mainJsonObj.put(domainname, domainArray);
														 // out.println(mainJsonObj);
														  
													  } // while close
													  
													  out.println(mainJsonObj);
													  
												   } // hashnode check
												   
											   } // TemplateLibrary close
											   
										   } // DocTigerAdvanced close
										   
										} // adminEmailId check close
										
									} // users close
									
								} // freetrial close
								
								
								
							}// services  close
							
						} // content close
						
			
		} catch (Exception e) {
			e.printStackTrace(out);
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
