package com.ss.bootcamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ss.bootcamp.service.AuthenticationResponse;
import com.ss.bootcamp.service.UserDetailsServiceImpl;

import static org.springframework.http.ResponseEntity.ok;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;


    @RequestMapping({"/hello"})
    public ResponseEntity<String> hello() {
        return ok("Hello World");
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> auth(@RequestBody UserRequest userRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getUserName(), userRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Username/Password is incorrect", e);
        }
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userRequest.getUserName());
        String token = JwtUtil.generateToken(userDetails);
        ResponseEntity<AuthenticationResponse> re = new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(token), HttpStatus.OK);
        return re;
    }

}
