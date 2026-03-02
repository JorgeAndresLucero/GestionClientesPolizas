package com.insurance.api.repository;

import com.insurance.api.domain.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findByLifePolicyId(Long policyId);

}