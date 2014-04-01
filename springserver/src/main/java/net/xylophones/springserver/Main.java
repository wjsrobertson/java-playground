package net.xylophones.springserver;

import net.xylophones.springserver.server.Service;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import sun.net.www.content.text.Generic;

public class Main {

    public static void main(String[] args) {
        GenericApplicationContext appCtx = new GenericApplicationContext();
        //AnnotationConfigApplicationContext appCtx = new AnnotationConfigApplicationContext();
        //appCtx.scan("net.xylophones.springserver");
        appCtx.register(ApplicationConfig.class);
        appCtx.refresh();

        Service service = appCtx.getBean(Service.class);
        service.start();
    }

}
