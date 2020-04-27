package com.midas2018mobile5.serverapp.api;

import com.midas2018mobile5.serverapp.config.security.api.token.ApiTokenFactory;
import com.midas2018mobile5.serverapp.dao.user.UserSearchService;
import com.midas2018mobile5.serverapp.dao.user.UserService;
import com.midas2018mobile5.serverapp.domain.user.Role;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.dto.user.UserDto;
import com.midas2018mobile5.serverapp.dto.user.UserSearchType;
import com.midas2018mobile5.serverapp.model.CustomPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by Neon K.I.D on 4/24/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@RequestMapping("/users")
@RepositoryRestController
@RequiredArgsConstructor
@ResponseBody
public class UserController {
    private final UserService userService;
    private final UserSearchService userSearchService;
    private final ApiTokenFactory apiTokenFactory;

    @Secured(Role.ROLES.ADMIN)
    @GetMapping
    public Page<UserDto.Res> getUsers(@RequestParam(name = "type") final UserSearchType type,
                                      @RequestParam(name = "value", required = false) final String value,
                                      final CustomPageRequest pageRequest) {
        return userSearchService.search(type, value, pageRequest.of("createdAt")).map(UserDto.Res::new);
    }

    @Secured(Role.ROLES.ADMIN)
    @GetMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserDto.Res getUser(@PathVariable final long id) {
        return new UserDto.Res(userService.findById(id));
    }

    @PostMapping(value = "/signUp")
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto.Res registerUser(@RequestBody @Valid final UserDto.SignUpReq dto) {
        return new UserDto.Res(userService.create(dto));
    }

    @PostMapping(value = "/signIn")
    @ResponseStatus(value = HttpStatus.OK)
    public UserDto.SignInRes validateUser(@RequestBody @Valid final UserDto.SignInReq dto) {
        User user = userService.validate(dto);
        return UserDto.SignInRes.builder()
                .user(user)
                .token(apiTokenFactory.createToken(user.getUserid(), user.getRoles()))
                .build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/signOut")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> signOutUser(HttpServletRequest req, HttpServletResponse res) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null)
            new SecurityContextLogoutHandler().logout(req, res, auth);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated() and ((#userid == principal.username) or hasRole('ROLE_ADMIN'))")
    @PatchMapping(value = "/{userid}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserDto.Res updateUser(@PathVariable final String userid, @RequestBody UserDto.MyUserRequest dto) {
        return new UserDto.Res(userService.updateByUserId(userid, dto));
    }

    @PreAuthorize("isAuthenticated() and ((#userid == principal.username) or hasRole('ROLE_ADMIN'))")
    @DeleteMapping(value = "/{userid}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> deleteUser(@PathVariable final String userid) {
        userService.deleteByUserId(userid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
