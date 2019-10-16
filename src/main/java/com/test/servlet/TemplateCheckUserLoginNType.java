package com.test.servlet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;

import org.apache.commons.codec.binary.Base64;
import org.apache.felix.scr.annotations.Reference;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

public class TemplateCheckUserLoginNType {
	// ResourceBundle bundle = ResourceBundle.getBundle("config");
	//static ResourceBundle bundleststic = ResourceBundle.getBundle("config_SKU1");
	
	@Reference
	private SlingRepository repo;
	
	//Session session = null;
	Base64 base64 = new Base64();

	public static void main(String[] args) throws JSONException {
		String a = "[\"name\",\"aaddr\",\"city\"]";
		JSONArray fieldslist = new JSONArray(a);
		String a1= new TemplateCheckUserLoginNType().checkfreetrial("nihalgajbhiye2011@gmail.com");
		System.out.println("a1 "+a1);
		
//		String a1=getWordToPdfData();
//		System.out.println(a1);
		
		//new FreeTrialandCart().createExcel(fieldslist, "abcdefghi");
		// "mohit.raj@bizlem.com"
	}
	
	public static String getWordToPdfData(){

		StringBuffer response= new StringBuffer();
		try {

		String url = "http://uk.bluealgo.com:8087/apirest/trialmgmt/trialuser/vivek@bizlem.com/DocTigerFreeTrial";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
//			System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		new InputStreamReader(con.getInputStream()));
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
		}
		in.close();

		//print result
//			System.out.println(response.toString());



		} catch (Exception e) {
		e.printStackTrace();
		}
		return response.toString();

		}
	

	public static String checkfreetrial(String userid) {
		int expireFlag = 0;
		if(userid.equalsIgnoreCase("viki@gmail.com") || userid.equalsIgnoreCase("nilesh@gmail.com") ) {
			System.out.println("userid "+userid);
			expireFlag=1;
			}
		//String addr = bundleststic.getString("Freetrialurl") + userid + "/DocTigerFreeTrial";
		String addr = "http://uk.bluealgo.com:8087/apirest/trialmgmt/trialuser/" + userid + "/DocTigerFreeTrial/";
		
		// String addr =
		// "http://dev.uk.bluealgo.com:8086/apirest/trialmgmt/trialuser/"+userid+"/DocTigerFreeTrial";
		try {
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String text = reader.readLine();
			System.out.println(text);
			JSONObject obj = new JSONObject(text);

			
			
			if(obj.has("expireFlag")) {
				expireFlag = obj.getInt("expireFlag");
				System.out.println(expireFlag);
				}else {
				expireFlag = 1;
				}
			

			conn.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("made it here");
			expireFlag = 1;

		}
		 return expireFlag+"";
		//return "1";

	}

	public static Node getDocTigerAdvNode(String freetrialstatus, String email, String group, Session session1,
			SlingHttpServletResponse response) {

		// freetrialstatus="0";
		PrintWriter out = null;
		// out.println("in getDocTigerAdvNode");

		Node contentNode = null;
		Node appserviceNode = null;
		Node appfreetrialNode = null;
		Node emailNode = null;
		Node DoctigerAdvNode = null;

		Node adminserviceidNode = null;
		String adminserviceid = "";
		try {
			out = response.getWriter();

			// out.println("in method ");
			// out.println("email "+email);

			// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			if (session1.getRootNode().hasNode("content")) {
				contentNode = session1.getRootNode().getNode("content");
			} else {
				contentNode = session1.getRootNode().addNode("content");
			}
			// out.println("contentNode "+contentNode);

			if (freetrialstatus.equalsIgnoreCase("0")) {

				if (contentNode.hasNode("services")) {
					appserviceNode = contentNode.getNode("services");

					// out.println("appserviceNode "+appserviceNode);

					if (appserviceNode.hasNode("freetrial")) {
						appfreetrialNode = appserviceNode.getNode("freetrial");

						// out.println("appfreetrialNode "+appfreetrialNode);

						if (appfreetrialNode.hasNode("users")
								&& appfreetrialNode.getNode("users").hasNode(email.replace("@", "_"))) {
							emailNode = appfreetrialNode.getNode("users").getNode(email.replace("@", "_"));
							// out.println("emailNode "+emailNode);
							if (emailNode.hasNode("DocTigerAdvanced")) {
								DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
							} else {
								DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
							}
							// out.println("DoctigerAdvNode "+DoctigerAdvNode);

						} else {
							// emailNode=appfreetrialNode.getNode("users").addNode(email.replace("@", "_"));
						}
					} else {
						// appfreetrialNode=appserviceNode.addNode("freetrial");
					}
				} else {
					// appserviceNode=contentNode.addNode("services");
				}

			} else {

				// out.println("in else");

				if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
					emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
					if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("doctiger")
							&& emailNode.getNode("services").getNode("doctiger").hasNodes()) {
						NodeIterator itr = emailNode.getNode("services").getNode("doctiger").getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
							if (!adminserviceid.equalsIgnoreCase("DocTigerFreeTrial")) {
								if ((adminserviceid != "") && (!adminserviceid.equals("DocTigerFreeTrial"))) {

									if (contentNode.hasNode("services")) {
										appserviceNode = contentNode.getNode("services");
									} else {
										appserviceNode = contentNode.addNode("services");
									}
									// out.println("appserviceNode "+appserviceNode);

									if (appserviceNode.hasNode(adminserviceid)) {
										appfreetrialNode = appserviceNode.getNode(adminserviceid);
									 
									if (appfreetrialNode.hasNode(group)) {
										emailNode = appfreetrialNode.getNode(group);
									} else {
										emailNode = appfreetrialNode.addNode(group);
									}
									// out.println("emailNode "+emailNode);
									if (emailNode.hasNode("DocTigerAdvanced")) {
										DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
									} else {
										DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
									}
									}
									break;
								}
								
							}
						}
					}
				}
				// out.println("adminserviceid "+adminserviceid);
				
			}

			// session.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 out.println(e.getMessage());
			DoctigerAdvNode = null;
		}

		return DoctigerAdvNode;
	}

	
	
	 public static JSONArray getListOfAssignedGroups(String freetrialstatus, String email,  Session session1,
				SlingHttpServletResponse response) {
		 PrintWriter out = null;
			// out.println("in getDocTigerAdvNode");

			Node contentNode = null;
			Node appserviceNode = null;
			Node appfreetrialNode = null;
			Node emailNode = null;
			Node DoctigerAdvNode = null;
            JSONArray arr =null;
			Node adminserviceidNode = null;
			String adminserviceid = "";
			try {
				arr =new JSONArray();
				out = response.getWriter();

				// out.println("in method ");
				// out.println("email "+email);

				// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				if (session1.getRootNode().hasNode("content")) {
					contentNode = session1.getRootNode().getNode("content");
				} else {
					contentNode = session1.getRootNode().addNode("content");
				}

					if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
						emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
						if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("doctiger")
								&& emailNode.getNode("services").getNode("doctiger").hasNodes()) {
							NodeIterator itr = emailNode.getNode("services").getNode("doctiger").getNodes();
							while (itr.hasNext()) {
								adminserviceidNode=itr.nextNode();
								adminserviceid = adminserviceidNode.getName();
								if (!adminserviceid.equalsIgnoreCase("DocTigerFreeTrial")) {
									if ((adminserviceid != "") && (!adminserviceid.equals("DocTigerFreeTrial"))) {

										if (contentNode.hasNode("services")) {
											appserviceNode = contentNode.getNode("services");
										} else {
											appserviceNode = contentNode.addNode("services");
										}
										// out.println("appserviceNode "+appserviceNode);

										if (appserviceNode.hasNode(adminserviceid)) {
										if(adminserviceidNode.hasNodes()) {
											NodeIterator itr2 = adminserviceidNode.getNodes();
											while(itr2.hasNext()) {
												Node groupNode =itr2.nextNode();
												arr.put(groupNode.getName());
											}
										}
										break;
										}
									}
									
								}
							}
						
					}
					// out.println("adminserviceid "+adminserviceid);
					
				}

				// session.save();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				 out.println(e.getMessage());
				DoctigerAdvNode = null;
			}
			return arr;
		}
	 
	 
	 
	
			}





