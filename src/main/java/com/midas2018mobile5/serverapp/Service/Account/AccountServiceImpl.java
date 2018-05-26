package com.midas2018mobile5.serverapp.Service.Account;

import com.midas2018mobile5.serverapp.Model.External.Account.Account;
import com.midas2018mobile5.serverapp.Model.External.Account.AccountAuth;
import com.midas2018mobile5.serverapp.Model.External.Account.AccountDto;
import com.midas2018mobile5.serverapp.Model.External.Account.AccountRepository;
import com.midas2018mobile5.serverapp.Model.Internal.ResourceNotFoundException;
import com.midas2018mobile5.serverapp.Model.Internal.ResponseMessage;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.ResponseError;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.MidasStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    // Not Recommended
    @Autowired
    private AccountRepository ar;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Account selectMember(Long id) {
        return ar.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
    }

    @Override
    public boolean validMember(AccountAuth account) {
        Account currentAccount = ar.selectMember(account.userid);
        if(currentAccount != null)
            return encoder.matches(account.password, currentAccount.getPassword());
        return false;
    }

    @Override
    public Iterable<Account> allMember() {
        return ar.findAll();
    }

    @Override
    public ResponseEntity<?> privilegeMember(String userid) {
        ResponseError err;
        ResponseEntity<?> response;
        try {
            ar.privilegeMember(userid);
            ResponseMessage msg = new ResponseMessage(true);
            response = new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception ex) {
            err = new ResponseError(MidasStatus.INTERNAL_SERVER_ERROR);
            response = new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    // 사용자를 추가하기 전에,
    // 반드시 중복되는 사용자가 있는지를 검사
    @Override
    public ResponseEntity<?> addMember(AccountDto account) {
        ResponseError err;
        ResponseEntity<?> response;
        try {
            if(ar.existsByMember(account.userid) == 0) {
                Account newAccount = new Account();
                newAccount.setUserid(account.userid);
                newAccount.setPassword(encoder.encode(account.password));
                newAccount.setUsername(account.username);
                ar.save(newAccount);

                ResponseMessage msg = new ResponseMessage(true);
                response = new ResponseEntity<>(msg, HttpStatus.OK);
            } else {
                err = new ResponseError(MidasStatus.USERNAME_EXISTS);
                response = new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            err = new ResponseError(MidasStatus.INTERNAL_SERVER_ERROR);
            response = new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> deleteMember(Long id) {
        Account account = ar.findById(id).orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
        ar.delete(account);
        return ResponseEntity.ok().build();
    }
}
