package com.test.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.felix.scr.annotations.sling.SlingFilter;
import org.apache.felix.scr.annotations.sling.SlingFilterScope;
import org.apache.sling.api.SlingHttpServletResponse;

@SlingFilter(label = "Cross-Origin Request Filter",
metatype = false,
scope = SlingFilterScope.REQUEST,
order = -100)

public class CORSFilter implements Filter {

	

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		if (response instanceof SlingHttpServletResponse) {
            SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
            slingResponse.setHeader("Access-Control-Allow-Origin", "*");
            slingResponse.setHeader("Access-Control-Allow-Credentials", "true");
            slingResponse.setHeader("Access-Control-Allow-Headers", "*");
        }
		   
		
		chain.doFilter(request, response);
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

	public void destroy() {
		
	}
	
	
}