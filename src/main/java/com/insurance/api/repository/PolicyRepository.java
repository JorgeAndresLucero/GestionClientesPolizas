package com.insurance.api.repository;

import com.insurance.api.domain.Policy;
import com.insurance.api.domain.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Long> {

    boolean existsByClientId(Long clientId);
    boolean existsByClientIdAndType(Long clientId, PolicyType type);
    List<Policy> findByClientId(Long clientId);
}