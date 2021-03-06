package com.vbg.auth;

import com.vbg.auth.service.DuoClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DuoClientApplicationTests {

    @Autowired
    DuoClient duoClient;

    @Test
    public void testPing() throws Exception {
        assertEquals("OK", duoClient.ping().getStat());
    }

    @Test
    public void testCheck() throws Exception {
        assertEquals("OK", duoClient.check().getStat());
    }

    @Test
    public void testAuth() throws Exception {
        final Map<String, String> authParams = new HashMap<>();
        authParams.put("username", "user1");
        authParams.put("factor", "passcode");
        authParams.put("passcode", "678045");
        assertEquals("OK", duoClient.auth(authParams).getStat());
    }
}
