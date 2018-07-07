package com.example.demo.rest;

import com.example.demo.configuration.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    /**
     * @param principal
     * @return
     */
    @GetMapping("/a")
    public String a(Principal principal) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();
        Object credentials = auth.getCredentials();
        UserDetails details = (UserDetails) auth.getDetails();

        log.debug("Username: {}. Credentials: {}. Details: {}", username, credentials, details);

        return "You're authorized " + principal.getName() + "!";
    }
}
