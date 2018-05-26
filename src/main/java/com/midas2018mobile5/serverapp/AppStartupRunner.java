package com.midas2018mobile5.serverapp;

import com.midas2018mobile5.serverapp.Model.External.Account.AccountDto;
import com.midas2018mobile5.serverapp.Service.Account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {
    @Autowired
    private AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        AccountDto account = new AccountDto();
        account.userid = "midasadmin0527";
        account.password = "midasadmin1!";
        account.username = "MidasCafe Administrator";
        accountService.addMember(account);
    }
}
