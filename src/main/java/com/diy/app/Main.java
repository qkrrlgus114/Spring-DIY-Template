package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.server.bean.BeanScanner;
import com.diy.framework.web.server.bean.Component;

import java.nio.charset.StandardCharsets;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * 강의 서버 관련은 app 패키지
 *
 */
public class Main {
    public static void main(String[] args) {
        configureJulConsoleEncoding();
        BeanScanner beanScanner = new BeanScanner("com.diy");
        beanScanner.scanClassesTypeAnnotatedWith(Component.class);

        TomcatWebServer tomcatWebServer = new TomcatWebServer();
        tomcatWebServer.start();

    }

    private static void configureJulConsoleEncoding() {
        System.setProperty("java.util.logging.ConsoleHandler.encoding", StandardCharsets.UTF_8.name());

        final Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            try {
                handler.setEncoding(StandardCharsets.UTF_8.name());
            } catch (Exception ignored) {
                // Keep the default encoding when a handler does not allow updates.
            }
        }
    }
}
