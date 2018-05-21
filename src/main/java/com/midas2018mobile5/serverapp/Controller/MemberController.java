package com.midas2018mobile5.serverapp.Controller;

import com.midas2018mobile5.serverapp.Model.External.Member;
import com.midas2018mobile5.serverapp.Model.Internal.MidasStatus;
import com.midas2018mobile5.serverapp.Model.Internal.ResponseMessage;
import com.midas2018mobile5.serverapp.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/members")
public class MemberController {
    @Autowired
    private MemberService memberDAO;

    @RequestMapping(value = "reg", method = RequestMethod.POST)
    public ResponseEntity<?> register(@Valid @RequestBody Member member) {
        return memberDAO.addMember(member);
    }

    // 현재 Message 영역에 Login Success 를 붙였지만,
    // 차후 여유가 생기면 Session key를 붙이게 될 수 있음.
    @RequestMapping(value = "auth", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> validiate(@Valid @RequestBody Member member) {
        ResponseMessage message;
        if(memberDAO.validMember(member))
            message = new ResponseMessage(MidasStatus.SUCCESS, "Login success");
        else
            message = new ResponseMessage(MidasStatus.FAIL, "Incorrect ID or Password");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Member findOne(@PathVariable(value = "id") Long id) {
        return memberDAO.selectMember(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam(value = "id") Long id) {
        return memberDAO.deleteMember(id);
    }
}
