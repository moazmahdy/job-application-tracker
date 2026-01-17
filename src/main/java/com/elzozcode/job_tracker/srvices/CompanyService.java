package com.elzozcode.job_tracker.srvices;

import com.elzozcode.job_tracker.dtos.CompanyDto;
import com.elzozcode.job_tracker.entity.Company;
import com.elzozcode.job_tracker.exception.DuplicateResourceException;
import com.elzozcode.job_tracker.exception.ResourceNotFoundException;
import com.elzozcode.job_tracker.repositories.CompanyRepository;
import com.elzozcode.job_tracker.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyDto createCompany(CompanyDto companyDto) {
        if (companyRepository.existsByNameIgnoreCase(companyDto.getName())) {
            throw new DuplicateResourceException("Company with name '" + companyDto.getName() + "' already exists");
        }

        Company company = Company.builder()
                .name(companyDto.getName())
                .description(companyDto.getDescription())
                .website(companyDto.getWebsite())
                .industry(companyDto.getIndustry())
                .location(companyDto.getLocation())
                .build();

        Company savedCompany = companyRepository.save(company);
        return mapToDto(savedCompany);
    }

    public CompanyDto getCompanyProfile() {
        UserPrincipal userPrincipal = getUserPrincipal();
        Company company = companyRepository.findById(userPrincipal.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + userPrincipal.getCompanyId()));
        return mapToDto(company);
    }

    public CompanyDto updateCompanyProfile(CompanyDto companyDto) {
        UserPrincipal userPrincipal = getUserPrincipal();
        Company company = companyRepository.findById(userPrincipal.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + userPrincipal.getCompanyId()));

        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company.setWebsite(companyDto.getWebsite());
        company.setIndustry(companyDto.getIndustry());
        company.setLocation(companyDto.getLocation());

        Company updatedCompany = companyRepository.save(company);
        return mapToDto(updatedCompany);
    }

    public CompanyDto getCompanyById(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
        return mapToDto(company);
    }

    public CompanyDto getCompanyByName(String name) {
        Company company = companyRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with name: " + name));
        return mapToDto(company);
    }

    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CompanyDto> searchCompanies(String searchTerm) {
        return companyRepository.searchCompanies(searchTerm).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CompanyDto> getCompaniesByIndustry(String industry) {
        return companyRepository.findByIndustry(industry).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<String> getAllIndustries() {
        return companyRepository.findAllIndustries();
    }

    public CompanyDto updateCompany(Long companyId, CompanyDto companyDto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company.setWebsite(companyDto.getWebsite());
        company.setIndustry(companyDto.getIndustry());
        company.setLocation(companyDto.getLocation());

        Company updatedCompany = companyRepository.save(company);
        return mapToDto(updatedCompany);
    }

    public void deleteCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
        companyRepository.delete(company);
    }

    private UserPrincipal getUserPrincipal() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private CompanyDto mapToDto(Company company) {
        return CompanyDto.builder()
                .name(company.getName())
                .description(company.getDescription())
                .website(company.getWebsite())
                .industry(company.getIndustry())
                .location(company.getLocation())
                .build();
    }
}

