package com.midas2018mobile5.serverapp.dao.cafe;

import com.midas2018mobile5.serverapp.domain.cafe.Cafe;
import com.midas2018mobile5.serverapp.dto.cafe.CafeDto;
import com.midas2018mobile5.serverapp.error.exception.cafe.CafeMenuDuplicationException;
import com.midas2018mobile5.serverapp.error.exception.cafe.CafeMenuNotFoundException;
import com.midas2018mobile5.serverapp.repository.cafe.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CafeService {
    private final CafeRepository cafeRepository;

    @Transactional(readOnly = true)
    public Cafe findById(long id) {
        final Optional<Cafe> cafe = cafeRepository.findById(id);
        cafe.orElseThrow(() -> new CafeMenuNotFoundException(id));
        return cafe.get();
    }

    @Transactional(readOnly = true)
    public Cafe findByName(String menuName) {
        final Cafe cafe = cafeRepository.findByName(menuName);
        if (cafe == null) throw new CafeMenuNotFoundException(menuName);
        return cafe;
    }

    @Transactional(readOnly = true)
    public boolean isExistedCafeMenu(String menuName) {
        return cafeRepository.findByName(menuName) != null;
    }

    public Cafe create(CafeDto.Req dto, MultipartFile file) throws IOException {
        if (isExistedCafeMenu(dto.getName()))
            throw new CafeMenuDuplicationException(dto.getName());
        return cafeRepository.save(dto.toEntity(file));
    }

    public Cafe updateByMenuName(String menuName, CafeDto.Req dto,
                                 @Nullable MultipartFile file) throws IOException {
        final Cafe cafe = findByName(menuName);
        cafe.update(dto, file);
        return cafe;
    }

    public void deleteByMenuName(String menuName) {
        final Cafe cafe = findByName(menuName);
        cafeRepository.delete(cafe);
    }
}
