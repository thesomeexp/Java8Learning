package com.example.accessingdatajpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author liangzhirong
 * @date 2021/6/20
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    Customer findById(long id);
}
