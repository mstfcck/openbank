package com.openbank.accountservice.repository;

import com.openbank.accountservice.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    List<Account> findByUserId(Long userId);
    
    Page<Account> findByUserId(Long userId, Pageable pageable);
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    boolean existsByAccountNumber(String accountNumber);
    
    @Query("SELECT a FROM Account a WHERE a.userId = :userId AND a.status = :status")
    List<Account> findByUserIdAndStatus(@Param("userId") Long userId, 
                                       @Param("status") Account.AccountStatus status);
    
    @Query("SELECT a FROM Account a WHERE a.status = :status")
    Page<Account> findByStatus(@Param("status") Account.AccountStatus status, Pageable pageable);
    
    @Query("SELECT a FROM Account a WHERE a.accountType = :accountType")
    Page<Account> findByAccountType(@Param("accountType") Account.AccountType accountType, Pageable pageable);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.userId = :userId")
    long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.status = :status")
    long countByStatus(@Param("status") Account.AccountStatus status);
}
