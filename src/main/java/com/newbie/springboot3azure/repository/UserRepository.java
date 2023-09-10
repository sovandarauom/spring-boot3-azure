package com.newbie.springboot3azure.repository;


import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.newbie.springboot3azure.entity.User;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCosmosRepository<User, String> {

    Flux<User> findByCountry(String country);

}
