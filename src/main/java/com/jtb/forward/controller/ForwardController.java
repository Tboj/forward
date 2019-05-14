package com.jtb.forward.controller;

import com.jtb.forward.service.SendTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther: jtb
 * @date: 2019/1/7 16:17
 * @description:
 */
@RestController
public class ForwardController {

    @Autowired
    private SendTask sendTask;

    @RequestMapping(value = "forward")
    public ModelAndView forward(int post, int destPort) {
        Map<String, Object> result = new HashMap<>();
        try {
            System.out.println("forward start");
            SendTask.port = post;
            SendTask.destPort = destPort;
            SendTask.msg = destPort + ":A";
            String resultStr = sendTask.sendAndRec();
            result.put("status", true);
            result.put("result", resultStr);
        } catch (IOException | InterruptedException e) {
            result.put("status", false);
        }
        return new ModelAndView(new MappingJackson2JsonView(), result);
    }

}
