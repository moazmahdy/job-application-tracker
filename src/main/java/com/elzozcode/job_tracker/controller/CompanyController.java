package com.elzozcode.job_tracker.controller;

import com.elzozcode.job_tracker.dtos.CompanyDto;
import com.elzozcode.job_tracker.srvices.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Manage companies and job listings")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/profile")
    @Operation(summary = "Get company profile", description = "Retrieve company profile for the logged in company")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyDto> getCompanyProfile() {
        CompanyDto company = companyService.getCompanyProfile();
        return ResponseEntity.ok(company);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update company profile", description = "Update company profile for the logged in company")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyDto> updateCompanyProfile(@RequestBody CompanyDto companyDto) {
        CompanyDto updatedCompany = companyService.updateCompanyProfile(companyDto);
        return ResponseEntity.ok(updatedCompany);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company by ID", description = "Retrieve company details by ID")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id) {
        CompanyDto company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get company by name", description = "Retrieve company details by name")
    public ResponseEntity<CompanyDto> getCompanyByName(@PathVariable String name) {
        CompanyDto company = companyService.getCompanyByName(name);
        return ResponseEntity.ok(company);
    }

    @GetMapping
    @Operation(summary = "Get all companies", description = "Retrieve all companies in the system")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<CompanyDto> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/search")
    @Operation(summary = "Search companies", description = "Search companies by name, industry, or location")
    public ResponseEntity<List<CompanyDto>> searchCompanies(@RequestParam String searchTerm) {
        List<CompanyDto> companies = companyService.searchCompanies(searchTerm);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/industry/{industry}")
    @Operation(summary = "Get companies by industry", description = "Retrieve all companies in a specific industry")
    public ResponseEntity<List<CompanyDto>> getCompaniesByIndustry(@PathVariable String industry) {
        List<CompanyDto> companies = companyService.getCompaniesByIndustry(industry);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/industries")
    @Operation(summary = "Get all industries", description = "Retrieve all available industries")
    public ResponseEntity<List<String>> getAllIndustries() {
        List<String> industries = companyService.getAllIndustries();
        return ResponseEntity.ok(industries);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update company", description = "Update company information")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable Long id, @RequestBody CompanyDto companyDto) {
        CompanyDto updatedCompany = companyService.updateCompany(id, companyDto);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete company", description = "Delete a company from the system")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}

