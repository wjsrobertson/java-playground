package net.xylophones.fotilo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.OutputStream;

@Controller
public class VideoStreamController {

    @RequestMapping
    public void streamVideo(@RequestParam String cameraId, OutputStream outputStream) {

    }

}
