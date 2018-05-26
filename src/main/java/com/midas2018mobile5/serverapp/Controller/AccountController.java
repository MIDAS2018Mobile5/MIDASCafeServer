package com.midas2018mobile5.serverapp.Controller;

import com.midas2018mobile5.serverapp.Model.External.Account.Account;
import com.midas2018mobile5.serverapp.Model.External.Account.AccountAuth;
import com.midas2018mobile5.serverapp.Model.External.Account.AccountDto;
import com.midas2018mobile5.serverapp.Model.Internal.Security.JwtGenerator;
import com.midas2018mobile5.serverapp.Model.Internal.ResponseAuth;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.ResponseError;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.MidasStatus;
import com.midas2018mobile5.serverapp.Service.Account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/api")
public class AccountController {
    private final AccountService accountDAO;
    private final JwtGenerator jwtGenerator;

    @Autowired
    public AccountController(AccountService accountService, JwtGenerator jwtGenerator) {
        this.accountDAO = accountService;
        this.jwtGenerator = jwtGenerator;
    }

    @RequestMapping(value = "account/signup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> signUp(@Valid @RequestBody AccountDto account) {
        if(!isValidID(account.userid)) {
            ResponseError msg = new ResponseError(MidasStatus.BAD_USERNAME);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        if(!isValidPW(account.password)) {
            ResponseError msg = new ResponseError(MidasStatus.BAD_PASSWORD);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        return accountDAO.addMember(account);
    }

    @RequestMapping(value = "account/signin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> validiate(@Valid @RequestBody AccountAuth account) {
        if(accountDAO.validMember(account) != null) {
            Account gen = new Account();
            gen.setUserid(account.userid);
            gen.setPassword(account.password);

            ResponseAuth message = new ResponseAuth(jwtGenerator.generate(gen), accountDAO.validMember(account));
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            ResponseError err = new ResponseError(MidasStatus.LOGIN_FAILED);
            return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "svc/account/search", method = RequestMethod.GET)
    public Iterable<Account> findAll() {
        return accountDAO.allMember();
    }

    @RequestMapping(value = "svc/account/search/{id}", method = RequestMethod.GET)
    public Account findOne(@PathVariable(value = "id") Long id) {
        return accountDAO.selectMember(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam(value = "id") Long id) {
        return accountDAO.deleteMember(id);
    }

    @RequestMapping(value = "svc/account/privilege/{user_id}", method = RequestMethod.PUT)
    public ResponseEntity<?> privilege(@PathVariable(value ="user_id") String userid) {
        return accountDAO.privilegeMember(userid);
    }

    // 받은 문자열이 숫자로만 되어 있는지 판별하는 메소드
    private boolean isNum(String str) {
        if(str.isEmpty())
            return false;

        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) < '0' || str.charAt(i) > '9')
                return false;
        }
        return true;
    }

    // 받은 문자열이 문자로만 되어 있는지 판별하는 메소드
    private boolean isChar(String str) {
        if(str.isEmpty())
            return false;

        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) < 'a' || str.charAt(i) > 'z')
                return false;
        }
        return true;
    }

    // 받은 아이디가 해당 조건에 유효한지 판별하는 메소드
    private boolean isValidID(String id) {
        if(id.isEmpty())
            return false;

        if(id.length() < 6 || id.length() > 21)
            return false;

        return !(isNum(id) || isChar(id));
    }

    // 받은 비밀번호가 해당 조건에 유효한지 판별하는 메소드
    private boolean isValidPW(String pw) {
        if(pw.isEmpty())
            return false;

        if(pw.length() < 8 || pw.length() > 16)
            return false;

        if(isNum(pw) || isChar(pw))
            return false;

        // 연속되는 숫자 or 문자 방지
        String pattern = "(\\p{Alnum})\\1{2,}";
        Matcher matcher = Pattern.compile(pattern).matcher(pw);

        return !matcher.find();
    }
}
