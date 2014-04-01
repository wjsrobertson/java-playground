package net.xylophones.springserver;

import net.xylophones.springserver.server.Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

@Configuration
@ComponentScan(basePackages = {"net.xylophones.springserver.*"})
@PropertySource("classpath:springserver.properties")
public class ApplicationConfig {

}
