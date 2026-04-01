package com.diy.framework.web.server;

import com.diy.framework.web.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.loader.WebappLoader;
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

    public TomcatWebServer() {
    }

    public void start() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        setServerContext(dispatcherServlet);
        startServerInternal();
        startDaemonAwaitThread();
    }

    public void startServerInternal() {
        try {
            tomcat.setPort(port);
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException("톰켓 서버 실행 중 예외가 발생했습니다.", e);
        }
    }

    private void setServerContext(DispatcherServlet dispatcherServlet) {
        final String resourcesPath = Paths.get("src", "main", "resources").toString();
        final String absoluteResourcesPath = new File(resourcesPath).getAbsolutePath();

        final Context context = this.tomcat.addWebapp("/", absoluteResourcesPath);

        // 임베디드 환경에서는 부모 클래스로더에 위임하여 클래스 중복 로딩 방지
        WebappLoader loader = new WebappLoader(this.getClass().getClassLoader());
        loader.setDelegate(true);
        context.setLoader(loader);

        // addWebapp의 기본 서블릿 초기화 후 "/" 매핑을 dispatcher로 교체
        context.addLifecycleListener(event -> {
            if (Lifecycle.CONFIGURE_START_EVENT.equals(event.getType())) {
                context.removeServletMapping("/");
                Tomcat.addServlet(context, "dispatcher", dispatcherServlet);
                context.addServletMappingDecoded("/", "dispatcher");
            }
        });

        setServerResources(context);
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
