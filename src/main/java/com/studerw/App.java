package com.studerw;

import com.studerw.activiti.WebConfigurer;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.slf4j.Logger;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletException;

import java.util.Arrays;

import static org.slf4j.LoggerFactory.getLogger;

public class App {
    private static final Logger logger = getLogger(App.class);

    ProcessEngine processEngine = null;

    public static void main( String[] args ) {

        try {
            logger.info("Staring application with args: {}", Arrays.toString(args));
            new App().initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws Exception{
        initUndertow();
    }

    private void initUndertow() throws ServletException {
        logger.info("Initializing Undertow...");

        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(App.class.getClassLoader())
                .setContextPath("/myapp")
                .setDeploymentName("myapp.war")
                .addListener(new ListenerInfo(WebConfigurer.class))
                .addServlets(
                        Servlets.servlet("MyServlet", MessageServlet.class)
                                .addInitParam("message", "MyServlet")
                                .addMapping("/myservlet"));

        DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
        manager.deploy();
        PathHandler path = Handlers.path(Handlers.redirect("/myapp"))
                .addPrefixPath("/myapp", manager.start());

        Undertow server = Undertow.builder().addHttpListener(8080, "localhost")
                .setHandler(path)
                .build();
        server.start();
        logger.info("Undertow init complete: Servlet Context at: http://localhost:8080/myapp");
    }

//    private void initActiviti(){
//        logger.info("Initializing Activiti...");
//
//        processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
//                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_CREATE_DROP)
//                .setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000")
//                .setAsyncExecutorEnabled(true)
//                .setAsyncExecutorActivate(false)
//                .buildProcessEngine();
//
//        logger.debug("Activiti init complete.");
//
//    }

    /**
     * {@link ServletContainerInitializer} to initialize {@link ServletContextInitializer
     * ServletContextInitializers}.
     */
    /*
    private static class Initializer implements ServletContainerInitializer {

        private final ServletContextInitializer[] initializers;

        public Initializer(ServletContextInitializer[] initializers) {
            this.initializers = initializers;
        }

        @Override
        public void onStartup(Set<Class<?>> classes, ServletContext servletContext)
                throws ServletException {
            for (ServletContextInitializer initializer : this.initializers) {
                initializer.onStartup(servletContext);
            }
        }

    }
    */
}
