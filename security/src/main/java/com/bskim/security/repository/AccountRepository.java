package com.bskim.security.repository;

import java.util.Optional;

import com.bskim.security.domain.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    public Optional<Account> findByUsername(String username);

}