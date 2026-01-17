//package com.elzozcode.job_tracker.controller;
//
//import com.elzozcode.job_tracker.dtos.CreateJobApplicationDto;
//import com.elzozcode.job_tracker.dtos.RegisterDto;
//import com.elzozcode.job_tracker.entity.enums.UserType;
//import com.elzozcode.job_tracker.entity.Company;
//import com.elzozcode.job_tracker.entity.Job;
//import com.elzozcode.job_tracker.entity.JobApplication;
//import com.elzozcode.job_tracker.entity.User;
//import com.elzozcode.job_tracker.repositories.AuthRepository;
//import com.elzozcode.job_tracker.repositories.CompanyRepository;
//import com.elzozcode.job_tracker.repositories.JobApplicationRepository;
//import com.elzozcode.job_tracker.repositories.JobRepository;
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
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//public class JobApplicationControllerIntegrationTest {
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
//    private CompanyRepository companyRepository;
//
//    @Autowired
//    private JobRepository jobRepository;
//
//    @Autowired
//    private JobApplicationRepository jobApplicationRepository;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    private String companyToken;
//    private String userToken;
//    private Long jobId;
//    private JobApplication jobApplication;
//
//    @BeforeEach
//    void setUp() {
//        authRepository.deleteAll();
//        companyRepository.deleteAll();
//        jobRepository.deleteAll();
//        jobApplicationRepository.deleteAll();
//
//        // Create company and user
//        Company company = new Company();
//        company.setName("Test Company");
//        company = companyRepository.save(company);
//
//        RegisterDto companyRegisterDto = new RegisterDto("companyUser", "company@example.com", "password", "Company User", UserType.COMPANY, "Test Company");
//        User companyUser = new User();
//        companyUser.setUsername(companyRegisterDto.getUsername());
//        companyUser.setEmail(companyRegisterDto.getEmail());
//        companyUser.setPassword(passwordEncoder.encode(companyRegisterDto.getPassword()));
//        companyUser.setFullName(companyRegisterDto.getFullName());
//        companyUser.setRole(com.elzozcode.job_tracker.entity.enums.Role.ROLE_COMPANY);
//        companyUser.setCompany(company);
//        companyUser = authRepository.save(companyUser);
//        companyToken = jwtUtil.generateToken(companyUser);
//
//        // Create regular user
//        RegisterDto userRegisterDto = new RegisterDto("testuser", "testuser@example.com", "password", "Test User", UserType.USER, null);
//        User user = new User();
//        user.setUsername(userRegisterDto.getUsername());
//        user.setEmail(userRegisterDto.getEmail());
//        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
//        user.setFullName(userRegisterDto.getFullName());
//        user.setRole(com.elzozcode.job_tracker.entity.enums.Role.ROLE_USER);
//        user = authRepository.save(user);
//        userToken = jwtUtil.generateToken(user);
//
//        // Create job
//        Job job = new Job();
//        job.setJobTitle("Test Job");
//        job.setCompany(company);
//        job.setIsActive(true);
//        job = jobRepository.save(job);
//        jobId = job.getId();
//
//        // Create job application
//        jobApplication = new JobApplication();
//        jobApplication.setUser(user);
//        jobApplication.setJob(job);
//        jobApplicationRepository.save(jobApplication);
//    }
//
//    @Test
//    void applyForJob_withUserToken_shouldCreateApplication() throws Exception {
//        CreateJobApplicationDto dto = new CreateJobApplicationDto();
//        dto.setJobId(jobId);
//        mockMvc.perform(post("/api/applications/apply")
//                        .header("Authorization", "Bearer " + userToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void applyForJob_withCompanyToken_shouldReturnForbidden() throws Exception {
//        CreateJobApplicationDto dto = new CreateJobApplicationDto();
//        dto.setJobId(jobId);
//        mockMvc.perform(post("/api/applications/apply")
//                        .header("Authorization", "Bearer " + companyToken)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    void getJobApplicationsByJobId_withCompanyToken_shouldReturnApplications() throws Exception {
//        mockMvc.perform(get("/api/applications/job/" + jobId)
//                        .header("Authorization", "Bearer " + companyToken))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void getJobApplicationsByJobId_withUserToken_shouldReturnForbidden() throws Exception {
//        mockMvc.perform(get("/api/applications/job/" + jobId)
//                        .header("Authorization", "Bearer " + userToken))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    void getApplicationById_withOwnedApplication_shouldReturnApplication() throws Exception {
//        mockMvc.perform(get("/api/applications/" + jobApplication.getId())
//                        .header("Authorization", "Bearer " + userToken))
//                .andExpect(status().isOk());
//    }
//}