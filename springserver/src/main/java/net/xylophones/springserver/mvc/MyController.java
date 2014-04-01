package net.xylophones.springserver.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

@Controller
public class MyController {

    @RequestMapping("/test")
    public String get(Model model, View view) {
        return null;
    }

}
