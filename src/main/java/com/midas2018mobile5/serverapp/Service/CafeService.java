package com.midas2018mobile5.serverapp.Service;

import com.midas2018mobile5.serverapp.Model.External.Cafe.Cafe;
import org.springframework.http.ResponseEntity;

public interface CafeService {
    Cafe selectMenu(String name);
    ResponseEntity<?> addMenu(Cafe cafe);
    ResponseEntity<?> deleteMenu(String name);
    Iterable<Cafe> searchAll();
}
