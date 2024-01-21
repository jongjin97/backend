package com.example.back.controller;

import com.example.back.config.auth.PrincipalDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    @PostMapping
    public boolean checkToken(@AuthenticationPrincipal PrincipalDetail principalDetail){
        if(principalDetail == null)
            return false;
        return true;
    }
}
