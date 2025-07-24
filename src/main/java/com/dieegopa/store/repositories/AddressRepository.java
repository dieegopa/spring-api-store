package com.dieegopa.store.repositories;

import com.dieegopa.store.users.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}