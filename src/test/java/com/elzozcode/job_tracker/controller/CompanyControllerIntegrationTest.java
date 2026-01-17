//package com.elzozcode.job_tracker.controller;
//
//import com.elzozcode.job_tracker.dtos.CompanyDto;
//import com.elzozcode.job_tracker.dtos.RegisterDto;
//import com.elzozcode.job_tracker.entity.enums.UserType;
//import com.elzozcode.job_tracker.entity.User;
//import com.elzozcode.job_tracker.repositories.AuthRepository;
//import com.elzozcode.job_tracker.security.JwtUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//public class CompanyControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private AuthRepository authRepository;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    private String companyToken;
//
//    @BeforeEach
//    void setUp() {
//        authRepository.deleteAll();
//
//        RegisterDto companyRegisterDto = new RegisterDto(
//                "companyUser",
//                "company@example.com",
//                "password",
//                "Company User",
//                UserType.COMPANY,
//                "Test Company"
//        );
//
//        User companyUser = new User();
//        companyUser.setUsername(companyRegisterDto.getUsername());
//        companyUser.setEmail(companyRegisterDto.getEmail());
//        companyUser.setPassword(passwordEncoder.encode(companyRegisterDto.getPassword()));
//        companyUser.setFullName(companyRegisterDto.getFullName());
//        companyUser.setRole(com.elzozcode.job_tracker.entity.enums.Role.ROLE_COMPANY);
//
//        authRepository.save(companyUser);
//
//        companyToken = jwtUtil.generateToken(companyUser);
//    }
//
//    @Test
//    void getCompanyProfile_withValidToken_shouldReturnCompanyProfile() throws Exception {
//        mockMvc.perform(get("/api/companies/profile")
//                        .header("Authorization", "Bearer " + companyToken))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Test Company"));
//    }
//
//    @Test
//    void updateCompanyProfile_withValidToken_shouldUpdateCompanyProfile() throws Exception {
//        CompanyDto updatedCompanyDto = CompanyDto.builder()
//                .name("Updated Company Name")
//                .build();
//
//        mockMvc.perform(put("/api/companies/profile")
//                        .header("Authorization", "Bearer " + companyToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedCompanyDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Company Name"));
//    }
//
//    @Test
//    void getCompanyProfile_withUserToken_shouldReturnForbidden() throws Exception {
//        RegisterDto userRegisterDto = new RegisterDto(
//                "testuser",
//                "testuser@example.com",
//                "password",
//                "Test User",
//                UserType.USER,
//                null
//        );
//
//        User user = new User();
//        user.setUsername(userRegisterDto.getUsername());
//        user.setEmail(userRegisterDto.getEmail());
//        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
//        user.setFullName(userRegisterDto.getFullName());
//        user.setRole(com.elzozcode.job_tracker.entity.enums.Role.ROLE_USER);
//
//        authRepository.save(user);
//        String userToken = jwtUtil.generateToken(user);
//
//
//        mockMvc.perform(get("/api/companies/profile")
//                        .header("Authorization", "Bearer " + userToken))
//                .andExpect(status().isForbidden());
//    }
//}
