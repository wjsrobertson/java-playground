package net.xylophones.springserver.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Service {

    @Autowired
    @Value("${test}")
    private String test;

    public void start() {
        System.out.println("started: " + test);
    }

}
