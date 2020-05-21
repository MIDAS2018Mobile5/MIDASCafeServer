package com.midas2018mobile5.serverapp.api;

import com.midas2018mobile5.serverapp.dao.cafe.CafeSearchService;
import com.midas2018mobile5.serverapp.dao.cafe.CafeService;
import com.midas2018mobile5.serverapp.domain.user.Role;
import com.midas2018mobile5.serverapp.dto.cafe.CafeDto;
import com.midas2018mobile5.serverapp.model.CustomPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@RequestMapping("/cafes")
@RepositoryRestController
@RequiredArgsConstructor
@ResponseBody
public class CafeController {
    private final CafeSearchService cafeSearchService;
    private final CafeService cafeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public Page<CafeDto.Res> getCafeMenus(@PageableDefault final CustomPageRequest pageRequest) {
        return cafeSearchService.search("", pageRequest.of("name")).map(CafeDto.Res::new);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CafeDto.Res getCafeMenu(@PathVariable final long id) {
        return new CafeDto.Res(cafeService.findById(id));
    }

    @Secured(Role.ROLES.ADMIN)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CafeDto.Res addCafeMenu(@RequestBody @Valid final CafeDto.Req dto,
                                   @RequestParam(value = "imgPath", required = false)
                                   final MultipartFile imgPath) throws IOException {
        return new CafeDto.Res(cafeService.create(dto, imgPath));
    }

    @Secured(Role.ROLES.ADMIN)
    @PatchMapping("/{cafeMenuName}")
    @ResponseStatus(HttpStatus.OK)
    public CafeDto.Res updateCafeMenu(@PathVariable final String cafeMenuName,
                                      @RequestParam(value = "imgPath", required = false) final MultipartFile imgFile,
                                      @RequestBody @Valid final CafeDto.Req dto) throws IOException {
        return new CafeDto.Res(cafeService.updateByMenuName(cafeMenuName, dto, imgFile));
    }

    @Secured(Role.ROLES.ADMIN)
    @DeleteMapping("/{cafeMenuName}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteCafeMenu(@PathVariable final String cafeMenuName) {
        cafeService.deleteByMenuName(cafeMenuName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
