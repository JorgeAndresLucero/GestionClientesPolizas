package com.insurance.api.repository;

import com.insurance.api.domain.HealthMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthMemberRepository extends JpaRepository<HealthMember, Long> {

    List<HealthMember> findByHealthPolicyId(Long policyId);
}
