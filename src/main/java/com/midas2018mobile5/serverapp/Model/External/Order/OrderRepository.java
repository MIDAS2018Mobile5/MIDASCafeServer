package com.midas2018mobile5.serverapp.Model.External.Order;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OrderRepository extends CrudRepository<Order, Long> {
    @Query(value = "select o from Order o where o.name = :name")
    Iterable<Order> selectOrder(@Param(value = "name") String name);

    @Modifying
    @Query(value = "update Order o set o.isordered = 1 where o.bid = :id")
    void updateOrder(@Param(value = "id") Long id);

    @Query(value = "select o from Order o where o.bid = id")
    Iterable<Order> findBybid(@Param("id") Long id);

    @Query(value = "select o from Order o where o.bid = :id")
    Order findOne(@Param(value = "id") Long id);
}
