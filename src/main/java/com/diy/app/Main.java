package com.diy.app;

import com.diy.framework.web.server.TomcatWebServer;
import com.diy.framework.web.server.bean.BeanFactory;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * 강의 서버 관련은 app 패키지
 *
 */
public class Main {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        configureJulConsoleEncoding();

        BeanFactory beanFactory = new BeanFactory();
        try {
            beanFactory.start();
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }

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
