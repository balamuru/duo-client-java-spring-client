package com.vbg.auth;

import com.vbg.auth.service.DuoClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DuoClientApplicationTests {

    @Autowired
    DuoClient duoClient;
	@Test
	public void testDUO() throws Exception {
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
