package com.midas2018mobile5.serverapp.Controller;

import com.midas2018mobile5.serverapp.Model.External.Cafe.Cafe;
import com.midas2018mobile5.serverapp.Model.External.Cafe.CafeDto;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.MidasStatus;
import com.midas2018mobile5.serverapp.Model.Internal.errCode.ResponseError;
import com.midas2018mobile5.serverapp.Service.Cafe.CafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/cafe")
public class CafeController {
    @Autowired
    private CafeService cafeDAO;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity<?> add(@Valid @RequestBody Cafe cafe) {
        if(!isValidMenu(cafe.name)) {
            ResponseError msg = new ResponseError(MidasStatus.BAD_MENUNAME);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        return cafeDAO.addMenu(cafe);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@Valid @RequestBody CafeDto cafe) {
        return cafeDAO.deleteMenu(cafe.name);
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public Iterable<Cafe> searchAll() {
        return cafeDAO.searchAll();
    }

    private boolean isValidMenu(String menu) {
        if(menu.isEmpty())
            return false;

        return !containsNum(menu);
    }

    // 받은 문자열 중 숫자가 있는지 판별하는 메소드
    private boolean containsNum(String str) {
        if(str.isEmpty())
            return true;
        return false;
    }
}
