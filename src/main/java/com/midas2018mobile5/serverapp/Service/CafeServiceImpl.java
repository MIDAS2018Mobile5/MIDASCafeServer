package com.midas2018mobile5.serverapp.Service;

import com.midas2018mobile5.serverapp.Model.External.Cafe.Cafe;
import com.midas2018mobile5.serverapp.Model.External.Cafe.CafeRepository;
import com.midas2018mobile5.serverapp.Model.Internal.ResponseMessage;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.MidasStatus;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CafeServiceImpl implements CafeService {
    @Autowired
    private CafeRepository cr;

    @Override
    public Cafe selectMenu(String name) {
        return cr.selectMenu(name);
    }

    @Override
    public ResponseEntity<?> addMenu(Cafe cafe) {
        ResponseError err;
        ResponseEntity<?> response;
        try {
            if(cr.existsByMenu(cafe.name) == 0) {
                cr.save(cafe);
                ResponseMessage msg = new ResponseMessage(true);
                response = new ResponseEntity<>(msg, HttpStatus.OK);
            } else {
                err = new ResponseError(MidasStatus.MENU_EXISTS);
                response = new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            err = new ResponseError(MidasStatus.INTERNAL_SERVER_ERROR);
            response = new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> deleteMenu(String name) {
        Cafe cafe = cr.selectMenu(name);
        cr.delete(cafe);
        return ResponseEntity.ok().build();
    }

    @Override
    public Iterable<Cafe> searchAll() {
        return cr.findAll();
    }
}
