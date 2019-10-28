package ex13.pyrmont.startup;

import ex13.pyrmont.core.SimpleContextConfig;
import org.apache.catalina.Connector;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Loader;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.http.HttpConnector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.core.StandardWrapper;
import org.apache.catalina.loader.WebappLoader;

/**
 * @author yuchen.wu
 * @date 2019-10-28
 */

public class MyBootstrap3 {

    public static void main(String[] args) {
        //invoke: http://localhost:8080/app1/Primitive or http://localhost:8080/app1/Modern
        System.setProperty("catalina.base", System.getProperty("user.dir"));
        Connector connector = new HttpConnector();

        Wrapper wrapper1 = new StandardWrapper();
        wrapper1.setName("Primitive");
        wrapper1.setServletClass("PrimitiveServlet");
        Wrapper wrapper2 = new StandardWrapper();
        wrapper2.setName("Modern");
        wrapper2.setServletClass("ModernServlet");

        Context context = new StandardContext();
        // StandardContext's start method adds a default mapper
        context.setPath("/app1");
        context.setDocBase("app1");

        context.addChild(wrapper1);

        Context context2 = new StandardContext();
        context2.setPath("/app2");
        context2.setDocBase("app2");
        context2.addChild(wrapper2);

        LifecycleListener listener = new SimpleContextConfig();
        ((Lifecycle) context).addLifecycleListener(listener);
        ((Lifecycle) context2).addLifecycleListener(listener);

        Host host = new StandardHost();
        host.addChild(context);
        host.addChild(context2);
        host.setName("localhost");
        host.setAppBase("webapps");

        Loader loader = new WebappLoader();
        context.setLoader(loader);
        // context.addServletMapping(pattern, name);
        context.addServletMapping("/Primitive", "Primitive");

        Loader loader2 = new WebappLoader();
        context2.setLoader(loader2);
        context2.addServletMapping("/Modern", "Modern");


        connector.setContainer(host);
        try {
            connector.initialize();
            ((Lifecycle) connector).start();
            ((Lifecycle) host).start();

            // make the application wait until we press a key.
            System.in.read();
            ((Lifecycle) host).stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
