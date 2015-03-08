package eu.pick.initializer;


import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class AppInitializer implements WebApplicationInitializer {

  //  private static final String CONFIG_LOCATION = "eu.pick.config";
    private static final String MAPPING_URL = "/";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	
        WebApplicationContext context = getContext();
        servletContext.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(MAPPING_URL);
        
        
     // Register Spring security filter
     		FilterRegistration.Dynamic springSecurityFilterChain = 
     				servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
     		springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");
     		
     // Register Spring Social filter so that we can disconnect from providers
     		FilterRegistration.Dynamic hiddenHttpMethodFilter = 
     				servletContext.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class);
     		hiddenHttpMethodFilter.addMappingForUrlPatterns(null, false, "/*");
        
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        
        context.register(eu.pick.config.AppConfig.class);
        
      //  context.setConfigLocation(CONFIG_LOCATION);
        context.setDisplayName("SoPickMeUp");
               return context;
    }

}