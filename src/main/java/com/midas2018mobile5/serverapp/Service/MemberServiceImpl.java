package com.midas2018mobile5.serverapp.Service;

import com.midas2018mobile5.serverapp.Model.External.Member;
import com.midas2018mobile5.serverapp.Model.External.MemberRepository;
import com.midas2018mobile5.serverapp.Model.Internal.MidasStatus;
import com.midas2018mobile5.serverapp.Model.Internal.ResourceNotFoundException;
import com.midas2018mobile5.serverapp.Model.Internal.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository mr;

    @Override
    public Member selectMember(Long id) {
        return mr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));
    }

    @Override
    public boolean validMember(Member member) {
        return mr.existsByMember(member.username, member.password) != 0;
    }

    @Override
    public Iterable<Member> allMember() {
        return mr.findAll();
    }

    @Override
    public ResponseEntity<?> addMember(Member member) {
        ResponseMessage message;
        ResponseEntity<ResponseMessage> response;
        try {
            if(mr.existsByMember(member.username) == 0) {
                mr.save(member);
                message = new ResponseMessage(MidasStatus.SUCCESS, "Register OK");
                response = new ResponseEntity<>(message, HttpStatus.OK);
            } else {
                message = new ResponseMessage(MidasStatus.FAIL, "Bad request");
                response = new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            message = new ResponseMessage(MidasStatus.ERROR, "Server Error");
            response = new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> deleteMember(Long id) {
        Member member = mr.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));
        mr.delete(member);
        return ResponseEntity.ok().build();
    }
}
