package com.example.travelapp.Security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.AssertJUnit;

import static org.testng.AssertJUnit.*;

@SpringBootTest
public class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void testGenerateJWT() {
        String token = jwtTokenUtil.generateJWT("testUser");
        AssertJUnit.assertNotNull(token);
        System.out.println("Generated token: " + token);

        String extractedUsername = jwtTokenUtil.extractUsername(token);
        assertEquals("testUser", extractedUsername);

        boolean isValid = jwtTokenUtil.validateToken(token, "testUser");
        AssertJUnit.assertTrue(isValid);
    }
}
