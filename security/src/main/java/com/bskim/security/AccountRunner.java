package com.bskim.security;

import com.bskim.security.domain.Account;
import com.bskim.security.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * AccountRunner
 */
@Component
public class AccountRunner implements ApplicationRunner {

    @Autowired
    AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {   //편의상...
        Account bosung = accountService.createAccount("bosung", "1234");
    }



}