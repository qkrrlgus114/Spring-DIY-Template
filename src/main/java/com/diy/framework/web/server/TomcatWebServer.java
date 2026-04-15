package com.diy.framework.web.server;

import com.diy.framework.web.server.bean.BeanFactory;
import com.diy.framework.web.server.servlet.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.CodeSource;


public class TomcatWebServer {

    private final Tomcat tomcat = new Tomcat();
    private final int port = 8080;

    public void start(BeanFactory beanFactory) {
        Context context = setServerContext();
        registerDispatcherServlet(context, beanFactory);
        startDaemonAwaitThread();
        startServerInternal();
    }

    public void startServerInternal() {
        try {
            tomcat.setPort(port);
            tomcat.start();

            final Thread awaitThread = new Thread(() -> tomcat.getServer().await());
            awaitThread.start();
        } catch (LifecycleException e) {
            throw new RuntimeException("톰켓 서버 실행 중 예외가 발생했습니다.", e);
        }
    }

    private Context setServerContext() {
        final String resourcesPath = Paths.get("src", "main", "resources").toString();
        final String absoluteResourcesPath = new File(resourcesPath).getAbsolutePath();

        final Context context = this.tomcat.addWebapp("/", absoluteResourcesPath);

        setServerResources(context);
        return context;
    }

    private void registerDispatcherServlet(Context context, BeanFactory beanFactory) {
        Wrapper wrapper = Tomcat.addServlet(context, "dispatcher", new DispatcherServlet(beanFactory));
        wrapper.setLoadOnStartup(1);
        context.addServletMappingDecoded("/", "dispatcher");
        // DefaultWebXmlListener가 "/" → "default" 매핑을 나중에 걸어버리므로
        // Context 시작 완료 후 다시 dispatcher 쪽으로 덮어쓴다.
        context.addLifecycleListener(event -> {
            if (Lifecycle.AFTER_START_EVENT.equals(event.getType())) {
                context.addServletMappingDecoded("/", "dispatcher");
            }
        });
    }

    private void setServerResources(final Context context) {
        final String classPath = getClassPath();

        final StandardRoot resources = new StandardRoot(context);
        resources.addPostResources(new DirResourceSet(resources, "/WEB-INF/classes", classPath, "/"));

        context.setResources(resources);
    }

    private String getClassPath() {
        try {
            final CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();

            return new File(codeSource.getLocation().toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void startDaemonAwaitThread() {
        final Thread awaitThread = new Thread(() -> TomcatWebServer.this.tomcat.getServer().await());
        awaitThread.setContextClassLoader(getClass().getClassLoader());
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
