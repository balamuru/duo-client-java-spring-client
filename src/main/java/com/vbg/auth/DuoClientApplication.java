package com.vbg.auth;

import com.vbg.auth.service.DuoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class DuoClientApplication {


    @Autowired
    DuoClient duoClient;


    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(DuoClientApplication.class, args);


        //note: this is for application ilustration purposes only. Use the DuoClientApplicationTests instead to test functionality
        DuoClient duoClient = ctx.getBean(DuoClient.class);
        System.out.println("/ping => " +duoClient.ping());
        System.out.println("/check => " +duoClient.check());
        {
            final Map<String, String> authParams = new HashMap<>();
            authParams.put("username", "user1");
            authParams.put("factor", "auto");
            authParams.put("factor", "passcode"); //auto
            authParams.put("passcode", "678044");
            System.out.println("/auth => " +duoClient.auth(authParams));
        }

    }
}
