package com.midas2018mobile5.serverapp.api;

import com.midas2018mobile5.serverapp.dao.cafe.CafeService;
import com.midas2018mobile5.serverapp.dao.order.OrderSearchService;
import com.midas2018mobile5.serverapp.dao.order.OrderService;
import com.midas2018mobile5.serverapp.dao.user.UserService;
import com.midas2018mobile5.serverapp.domain.cafe.Cafe;
import com.midas2018mobile5.serverapp.domain.order.Order;
import com.midas2018mobile5.serverapp.domain.user.Role;
import com.midas2018mobile5.serverapp.domain.user.userEntity.User;
import com.midas2018mobile5.serverapp.dto.order.OrderDto;
import com.midas2018mobile5.serverapp.model.CustomPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Neon K.I.D on 4/25/20
 * Blog : https://blog.neonkid.xyz
 * Github : https://github.com/NEONKID
 */
@RequestMapping("/orders")
@RepositoryRestController
@RequiredArgsConstructor
@ResponseBody
public class OrderController {
    private final UserService userService;
    private final CafeService cafeService;
    private final OrderService orderService;
    private final OrderSearchService orderSearchService;

    @Secured(Role.ROLES.ADMIN)
    @GetMapping
    public Page<OrderDto.Res> getOrderList(final CustomPageRequest pageRequest) {
        return orderSearchService.search("", pageRequest.of("createdAt"))
                .map(OrderDto.Res::new);
    }

    @PreAuthorize("isAuthenticated() and (#dto.getUserid() == principal.username)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto.Res newOrder(@RequestBody @Valid final OrderDto.Req dto) {
        User curUser = userService.findByUserId(dto.getUserid());
        Cafe menu = cafeService.findByName(dto.getMenuName());

        Order oldOrder = orderService.findNotPurchased(curUser, menu);

        if (oldOrder == null)
            oldOrder = dto.toEntity(curUser, menu);
        else
            oldOrder.update(dto);

        return new OrderDto.Res(orderService.create(oldOrder));
    }

    @Secured(Role.ROLES.ADMIN)
    @PatchMapping("/finish/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto.Res finishOrder(@PathVariable long id) {
        return new OrderDto.Res(orderService.finishOrderById(id));
    }

    @Secured(Role.ROLES.ADMIN)
    @PatchMapping("/accept/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto.Res acceptOrder(@PathVariable long id) {
        return new OrderDto.Res(orderService.acceptOrderById(id));
    }

    @PostAuthorize("isAuthenticated() and (returnObject.userid == principal.username)")
    @PatchMapping("/purchase/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto.Res purchaseOrder(@PathVariable long id) {
        return new OrderDto.Res(orderService.purchaseOrderById(id));
    }

    @PostAuthorize("isAuthenticated() and ((returnObject.userid == principal.username) or hasRole('ROLE_ADMIN'))")
    @PatchMapping("/cancel/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto.Res cacnelOrder(@PathVariable long id) {
        return new OrderDto.Res(orderService.cancelOrderById(id));
    }
}
