package com.jtb.forward;

import com.jtb.forward.service.ClientTask;
import com.jtb.forward.service.RecRouterTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jtb
 * @date 2019/1/7 16:17
 */
@SpringBootApplication
public class ForwardApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForwardApplication.class, args);

        RecRouterTask recRouterTask = new RecRouterTask();
        new Thread(recRouterTask).start();

        ClientTask clientTask = new ClientTask();
        new Thread(clientTask).start();
    }

}

