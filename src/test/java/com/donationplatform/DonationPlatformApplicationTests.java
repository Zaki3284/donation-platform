package com.donationplatform;

import com.donationplatform.auth.dto.login.LoginRequest;
import com.donationplatform.auth.dto.register.RegisterRequest;
import com.donationplatform.dto.DonationCreateRequest;
import com.donationplatform.entity.Campaign;
import com.donationplatform.entity.CampaignStatus;
import com.donationplatform.entity.Role;
import com.donationplatform.entity.User;
import com.donationplatform.repository.CampaignRepository;
import com.donationplatform.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DonationPlatformApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setup() {
		userRepository.findByEmail("admin@example.com").ifPresent(userRepository::delete);
		if (userRepository.findByEmail("test@example.com").isEmpty()) {
			User user = new User();
			user.setNom("Test User");
			user.setEmail("test@example.com");
			user.setPassword(passwordEncoder.encode("test1234"));
			user.setRole(Role.DONATEUR);
			user.setEnabled(true);
			user.setAccountNonLocked(true);
			userRepository.save(user);
		}
		User admin = new User();
		admin.setNom("Admin");
		admin.setEmail("admin@example.com");
		admin.setPassword(passwordEncoder.encode("admin1234"));
		admin.setRole(Role.ADMIN);
		admin.setEnabled(true);
		admin.setAccountNonLocked(true);
		userRepository.save(admin);
	}

	@Test
	void publicCampaignsEndpointIsAccessible() throws Exception {
		mockMvc.perform(get("/api/campaigns"))
				.andExpect(status().isOk());
	}

	@Test
	void loginReturnsTokenAndUser() throws Exception {
		LoginRequest request = new LoginRequest("test@example.com", "test1234");

		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").isNotEmpty())
				.andExpect(jsonPath("$.user.email").value("test@example.com"));
	}

	@Test
	void protectedEndpointRejectsAnonymous() throws Exception {
		mockMvc.perform(get("/api/users"))
				.andExpect(status().isForbidden());
	}

	@Test
	void donorCanCreateDonation() throws Exception {
		Campaign campaign = new Campaign();
		campaign.setTitre("Test Campaign");
		campaign.setDescription("Test Description");
		campaign.setObjectifMontant(1000.0);
		campaign.setMontantCollecte(0.0);
		campaign.setDateDebut(LocalDate.now());
		campaign.setDateFin(LocalDate.now().plusDays(30));
		campaign.setStatut(CampaignStatus.ACTIVE);
		campaign = campaignRepository.save(campaign);

		String token = loginAndGetToken("test@example.com", "test1234");

		DonationCreateRequest donationRequest = new DonationCreateRequest();
		donationRequest.setCampaignId(campaign.getId().toString());
		donationRequest.setAmount(50.0);

		mockMvc.perform(post("/api/donations")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(donationRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.amount").value(50.0))
				.andExpect(jsonPath("$.campaignId").value(campaign.getId().toString()))
				.andExpect(jsonPath("$.donorEmail").doesNotExist());
	}

	@Test
	void adminCanCreateCampaign() throws Exception {
		String token = loginAndGetToken("admin@example.com", "admin1234");

		String payload = """
				{
				  "title": "New Campaign",
				  "description": "Helping people",
				  "goalAmount": 5000.0,
				  "startDate": "2026-02-01",
				  "endDate": "2026-03-01",
				  "status": "ACTIVE"
				}
				""";

		mockMvc.perform(post("/api/campaigns")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value("New Campaign"));
	}

	@Test
	void publicCampaignsListIsAccessible() throws Exception {
		mockMvc.perform(get("/api/campaigns"))
				.andExpect(status().isOk());
	}

	@Test
	void registerCreatesUserAndReturnsToken() throws Exception {
		RegisterRequest request = new RegisterRequest("New User", "newuser@example.com", "newpass123");

		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").isNotEmpty())
				.andExpect(jsonPath("$.user.email").value("newuser@example.com"));
	}

	@Test
	void adminCannotCreateDonation() throws Exception {
		Campaign campaign = new Campaign();
		campaign.setTitre("Admin Campaign");
		campaign.setDescription("Admin Description");
		campaign.setObjectifMontant(1000.0);
		campaign.setMontantCollecte(0.0);
		campaign.setDateDebut(LocalDate.now());
		campaign.setDateFin(LocalDate.now().plusDays(7));
		campaign.setStatut(CampaignStatus.ACTIVE);
		campaign = campaignRepository.save(campaign);

		String token = loginAndGetToken("admin@example.com", "admin1234");

		DonationCreateRequest donationRequest = new DonationCreateRequest();
		donationRequest.setCampaignId(campaign.getId().toString());
		donationRequest.setAmount(25.0);

		mockMvc.perform(post("/api/donations")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(donationRequest)))
				.andExpect(status().isInternalServerError());
	}

	@Test
	void adminTokenAllowsAccessToProtectedUsersEndpoint() throws Exception {
		String token = loginAndGetToken("admin@example.com", "admin1234");

		mockMvc.perform(get("/api/users")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}

	@Test
	void invalidTokenIsRejected() throws Exception {
		mockMvc.perform(get("/api/users")
						.header("Authorization", "Bearer invalid.token.value"))
				.andExpect(status().isForbidden());
	}

	@Test
	void donorCannotAccessAdminStatistics() throws Exception {
		String token = loginAndGetToken("test@example.com", "test1234");

		mockMvc.perform(get("/api/statistics")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isInternalServerError());
	}

	@Test
	void donorCanFetchOwnDonations() throws Exception {
		Campaign campaign = new Campaign();
		campaign.setTitre("Donor Campaign");
		campaign.setDescription("Donor Description");
		campaign.setObjectifMontant(300.0);
		campaign.setMontantCollecte(0.0);
		campaign.setDateDebut(LocalDate.now());
		campaign.setDateFin(LocalDate.now().plusDays(5));
		campaign.setStatut(CampaignStatus.ACTIVE);
		campaign = campaignRepository.save(campaign);

		String token = loginAndGetToken("test@example.com", "test1234");

		DonationCreateRequest donationRequest = new DonationCreateRequest();
		donationRequest.setCampaignId(campaign.getId().toString());
		donationRequest.setAmount(30.0);

		mockMvc.perform(post("/api/donations")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(donationRequest)))
				.andExpect(status().isOk());

		mockMvc.perform(get("/api/donations/my")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}

	private String loginAndGetToken(String email, String password) throws Exception {
		LoginRequest request = new LoginRequest(email, password);

		String response = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		JsonNode node = objectMapper.readTree(response);
		return node.get("token").asText();
	}
}
