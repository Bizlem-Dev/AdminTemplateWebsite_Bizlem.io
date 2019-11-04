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
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import org.apache.commons.codec.binary.Base64;
//import com.sun.jersey.core.util.Base64;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/useglobaltemplateNew" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class UseGlobalTemplate extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	//static ResourceBundle bundleststic = ResourceBundle.getBundle("config_SKU1");

	// @Reference
	// private ParseSlingData parseSlingData;
	// ParseSlingData parseSlingData= new ParseSlingDataImpl();
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String email = request.getRemoteUser();
		out.println("email  :: "+email);
	}

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		Session session = null;
		Workspace ws = null;

		PrintWriter out = response.getWriter();

		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			ws = session.getWorkspace();

			 BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
			 ByteArrayOutputStream buf = new ByteArrayOutputStream();
			 int result = bis.read();
			 while (result != -1) {
			 buf.write((byte) result);
			 result = bis.read();
			 }
			 String res = buf.toString("UTF-8");
			 JSONObject obj = new JSONObject(res);
			// out.println(obj);
			JSONObject js = new JSONObject();
			// {"subdomainname":"vlaue", "type":"freetrial", //shoppingcart
			// "nodepath":"/content/services/freetrial/users/templateadmin_gmail.com/DocTigerAdvanced/TemplateLibrary/MailTestAdmin",
			// "templatename":"abc"
			// }

			String email = "";
			String type = "";
			String templatename = "";
			String nodepath = "";
			String usertype = "";
			Node DoctigerAdvancedNode = null;
			Node GlobalDTANode = null;
			Node GlobalTemplateNode = null;
			Node GlobalDataSource = null;

			email = request.getRemoteUser();
//			out.println("email  :: "+email);
			//nodepath = request.getParameter("nodepath");
			//type = request.getParameter("type");
			 nodepath = obj.getString("nodepath").trim();
			 templatename = obj.getString("templatename").trim();
			 type = obj.getString("type").trim();
			 //out.println(type);
			//out.println("email  " + email + " nodepath " + nodepath + " templatename " + templatename);
			// get GlobalDTANOde ===========================================================
			try {
				//out.println("*" + bundleststic.getString("Globaltemplatenodepath") + "*");
				//GlobalDTANode = session.getNode(bundleststic.getString("Globaltemplatenodepath"));
				GlobalDTANode = session.getNode("/content/services/freetrial/users/templateadmin_gmail.com/DocTigerAdvanced");
				//out.println("GlobalDTANode  " + GlobalDTANode);
			} catch (Exception e) {
				out.println("error " + e.getMessage());
			}
			// ===============================================================================

			if (GlobalDTANode != null) {

				//FreeTrialandCartNew cart = new FreeTrialandCartNew();
				String freetrialstatus = TemplateCheckUserLoginNType.checkfreetrial(email);
				//out.println("freetrialstatus  " + freetrialstatus);
				if (freetrialstatus.equalsIgnoreCase("0")) {
					usertype = "freetrial";
				} else {
					usertype = "shoppingcart";
				}

				if (email.equalsIgnoreCase("anonymous")) {

					js.put("status", "error");
					js.put("message", "Please Login");
					out.println(js);

				} else if ( type.equalsIgnoreCase("shoppingcart") && usertype.equalsIgnoreCase("freetrial")) {
					js.put("status", "error");
					js.put("message", "Template unavailable");
					out.println(js);
					
				} else if (freetrialstatus.equalsIgnoreCase("0")) {
					DoctigerAdvancedNode = TemplateCheckUserLoginNType.getDocTigerAdvNode(freetrialstatus, email, "", session, response);
					//out.println("DoctigerAdvancedNode  " + DoctigerAdvancedNode);
					if (DoctigerAdvancedNode != null) {
						copyDatasourcenode(DoctigerAdvancedNode, ws, GlobalDTANode, nodepath, session, out);
						copyTemplateNode(DoctigerAdvancedNode, ws, GlobalDTANode, nodepath, session, out);
					}
					js.put("Status", "success");
					js.put("message", "Template added to local Library");
					out.println(js);
				} else if (freetrialstatus.equalsIgnoreCase("1")) {
					JSONArray arr = TemplateCheckUserLoginNType.getListOfAssignedGroups(freetrialstatus, email, session, response);
				//	out.println("arr " + arr);
					for (int i = 0; i < arr.length(); i++) {
						String group = arr.getString(i);
						DoctigerAdvancedNode = TemplateCheckUserLoginNType.getDocTigerAdvNode(freetrialstatus, email, group, session,
								response);
					//	out.println("DoctigerAdvancedNode  " + i + " " + DoctigerAdvancedNode);

						if (DoctigerAdvancedNode != null) {
							copyDatasourcenode(DoctigerAdvancedNode, ws, GlobalDTANode, nodepath, session, out);
							copyTemplateNode(DoctigerAdvancedNode, ws, GlobalDTANode, nodepath, session, out);
						}

					}
					js.put("Status", "success");
					js.put("message", "Template added to local Library");
					out.println(js);
				}
			}
    session.save();
		} catch (Exception e) {
			try {
				JSONObject js = new JSONObject();
				js.put("Status", "error");
				js.put("message", e.getMessage());
				out.println(js);
			} catch (Exception e1) {
			}
		}
	}

	public void copyDatasourcenode(Node DocTigerAdvancedNode, Workspace ws, Node GlobalDTANode,
			String globalTemplateNodepath, Session session, PrintWriter out) {
		try {
			Node globalTemplateNode = null;
			Node DatasourceLibrary = null;
			Node DatasourcenameNode = null;
			Node GDatasourceLibrary = null;
			Node GDatasourcenameNode = null;

			globalTemplateNode = session.getNode(globalTemplateNodepath);
			//out.println("globalTemplateNode " + globalTemplateNode);

			String Datasourcename = "";
			if (globalTemplateNode.hasProperty("datasourcename")) {
				Datasourcename = globalTemplateNode.getProperty("datasourcename").getString();
			}
			if (GlobalDTANode.hasNode("DatasourceLibrary")) {
				GDatasourceLibrary = GlobalDTANode.getNode("DatasourceLibrary");
			}

			//out.println("GDatasourceLibrary " + GDatasourceLibrary);

			if (DocTigerAdvancedNode.hasNode("DatasourceLibrary")) {
				DatasourceLibrary = DocTigerAdvancedNode.getNode("DatasourceLibrary");
			} else {
				DatasourceLibrary = DocTigerAdvancedNode.addNode("DatasourceLibrary");
			}
			//out.println("DatasourceLibrary " + DatasourceLibrary);

			if (GDatasourceLibrary.hasNode(Datasourcename)) {
				GDatasourcenameNode = GDatasourceLibrary.getNode(Datasourcename);
				//out.println("GDatasourcenameNode " + GDatasourcenameNode);
				//out.println("GDatasourcenameNode.getPath() " + GDatasourcenameNode.getPath());
				//out.println("DatasourceLibrary.getPath()+\"/\"+Datasourcename " + DatasourceLibrary.getPath() + "/"
					//	+ Datasourcename);
				if (!DatasourceLibrary.hasNode(Datasourcename)) {
					ws.copy(ws.getName(), GDatasourcenameNode.getPath(),
							DatasourceLibrary.getPath() + "/" + Datasourcename);
				}
			}

		} catch (Exception e) {
			//out.println("error in copy datasourcenode " + e.getMessage());

		}
	}

	public void copyTemplateNode(Node DocTigerAdvancedNode, Workspace ws, Node GlobalDTANode,
			String globalTemplateNodepath, Session session, PrintWriter out1) {
		Node globalTemplateNode = null;
		Node TemplateLibrary = null;
		Node TemplateNode = null;
		Node GTemplateLibrary = null;

		try {

			globalTemplateNode = session.getNode(globalTemplateNodepath);
			//out.println("globalTemplateNode " + globalTemplateNode);

			String TemplateName = globalTemplateNode.getName();
			//out.println("TemplateName " + TemplateName);

			if (DocTigerAdvancedNode.hasNode("TemplateLibrary")) {
				TemplateLibrary = DocTigerAdvancedNode.getNode("TemplateLibrary");
			} else {
				TemplateLibrary = DocTigerAdvancedNode.addNode("TemplateLibrary");
			}
			//out.println("TemplateLibrary " + TemplateLibrary);
			//out.println("globalTemplateNode.getPath() " + globalTemplateNode.getPath());
			//out.println(
			//		"TemplateLibrary.getPath()+\"/\"+TemplateName " + TemplateLibrary.getPath() + "/" + TemplateName);
           if(!TemplateLibrary.hasNode(TemplateName)) {
			ws.copy(ws.getName(), globalTemplateNode.getPath(), TemplateLibrary.getPath() + "/" + TemplateName);
            }
		} catch (Exception e) {
			//out.println("error in copy Template " + e.getMessage());

		}

	}
}
