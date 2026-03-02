package com.insurance.api.service;

import com.insurance.api.domain.Beneficiary;
import com.insurance.api.domain.Client;
import com.insurance.api.domain.LifePolicy;
import com.insurance.api.dto.BeneficiaryRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.BeneficiaryRepository;
import com.insurance.api.repository.LifePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de operaciones de negocio relacionadas con beneficiarios.
 * <p>
 * Proporciona funcionalidad para añadir beneficiarios a pólizas de vida y consultarlos.
 * Cada póliza de vida puede tener un máximo de 2 beneficiarios.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see Beneficiary
 * @see BeneficiaryRepository
 * @see LifePolicyRepository
 */
@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final LifePolicyRepository lifePolicyRepository;
    
    /**
     * Añade un nuevo beneficiario a una póliza de vida.
     * <p>
     * Valida que la póliza no haya alcanzado el límite máximo de 2 beneficiarios.
     * </p>
     *
     * @param policyId identificador único de la póliza de vida
     * @param request objeto {@link BeneficiaryRequest} con los datos del beneficiario
     * @return el beneficiario añadido
     * @throws BusinessException si la póliza ya tiene 2 beneficiarios o no existe
     */
    public Beneficiary addBeneficiary(Long policyId, BeneficiaryRequest request) {

        LifePolicy policy = lifePolicyRepository.findById(policyId)
                .orElseThrow(() -> new BusinessException("Life policy not found"));

        // Regla: máximo 2 beneficiarios
        if (policy.getBeneficiaries().size() >= 2) {
            throw new BusinessException("Life policy can only have 2 beneficiaries");
        }

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setName(request.getName());
        beneficiary.setRelationship(request.getRelationship());
        beneficiary.setLifePolicy(policy);

        return beneficiaryRepository.save(beneficiary);
    }

    /**
     * Obtiene la lista de beneficiarios de una póliza de vida específica.
     *
     * @param policyId identificador único de la póliza de vida
     * @return lista de beneficiarios de la póliza
     * @throws BusinessException si no hay beneficiarios para la póliza
     */
    public List<Beneficiary> getByPolicy(Long policyId) {

        List<Beneficiary> beneficiaries =
                beneficiaryRepository.findByLifePolicyId(policyId);

        if (beneficiaries.isEmpty()) {
            throw new BusinessException("No beneficiaries found for this policy");
        }

        return beneficiaries;
    }

    /**
     * Obtiene la lista completa de todos los beneficiarios del sistema.
     *
     * @return lista de todos los beneficiarios registrados
     */
    public List<Beneficiary> findAll() {
        return beneficiaryRepository.findAll();
    }

}