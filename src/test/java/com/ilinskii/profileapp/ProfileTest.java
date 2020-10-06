package com.ilinskii.profileapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilinskii.profileapp.api.profile.ProfileCreateRequest;
import com.ilinskii.profileapp.api.profile.ProfileFilterRequest;
import com.ilinskii.profileapp.api.profile.ProfileIdResponse;
import com.ilinskii.profileapp.api.profile.ProfileResponse;
import com.ilinskii.profileapp.model.Profile;
import com.ilinskii.profileapp.repository.ProfileRepository;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.ilinskii.profileapp.config.SecurityConfig.SEC_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileTest extends AbstractTest {

    @Value("${api-key}")
    private String apiKey;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProfileRepository profileRepository;

    @BeforeEach
    public void beforeEach() {
        profileRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    public void add_and_get_profile_success() {
        ProfileCreateRequest profileCreateRequest = new ProfileCreateRequest();

        profileCreateRequest.setAge(18);
        profileCreateRequest.setName("Alice");
        profileCreateRequest.setEmail("alice@mail.com");

        var profileIdResponse = mockMvc.perform(
                post("/profiles/set")
                        .header(SEC_HEADER, apiKey)
                        .content(objectMapper.writeValueAsString(profileCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        var profileId = objectMapper.readValue(profileIdResponse.getContentAsString(), ProfileIdResponse.class);

        assertThat(profileId.getIdUser()).isNotNull();

        var profileResponse = mockMvc.perform(
                get("/profiles/" + profileId.getIdUser()).header(SEC_HEADER, apiKey))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var profile = objectMapper.readValue(profileResponse.getContentAsString(), ProfileResponse.class);

        assertThat(profile.getId()).isEqualTo(profileId.getIdUser());
        assertThat(profile.getAge()).isEqualTo(profileCreateRequest.getAge());
        assertThat(profile.getEmail()).isEqualTo(profileCreateRequest.getEmail());
        assertThat(profile.getName()).isEqualTo(profileCreateRequest.getName());
        assertThat(profile.getCreated()).isNotNull();
    }

    @Test
    @SneakyThrows
    public void find_by_id_not_found() {
        mockMvc.perform(
                get("/profiles/1").header(SEC_HEADER, apiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    public void add_profile_name_validation_fails() {
        ProfileCreateRequest profileCreateRequest = new ProfileCreateRequest();

        profileCreateRequest.setAge(18);
        profileCreateRequest.setName(null);
        profileCreateRequest.setEmail("alice@mail.com");
        expectAddProfileBadRequest(profileCreateRequest);

        profileCreateRequest.setName("");
        expectAddProfileBadRequest(profileCreateRequest);

        profileCreateRequest.setName(StringUtils.repeat("a", Profile.SIZE_NAME + 1));
        expectAddProfileBadRequest(profileCreateRequest);
    }

    @Test
    public void add_profile_email_validation_fails() {
        ProfileCreateRequest profileCreateRequest = new ProfileCreateRequest();

        profileCreateRequest.setAge(18);
        profileCreateRequest.setName("alice");
        profileCreateRequest.setEmail(null);
        expectAddProfileBadRequest(profileCreateRequest);

        profileCreateRequest.setEmail("incorrect-email");
        expectAddProfileBadRequest(profileCreateRequest);

        profileCreateRequest.setEmail(StringUtils.repeat("a", Profile.SIZE_EMAIL) + "alice@mail.com");
        expectAddProfileBadRequest(profileCreateRequest);
    }

    @Test
    @SneakyThrows
    public void add_profile_email_check_duplication_fails() {
        ProfileCreateRequest profileCreateRequest = new ProfileCreateRequest();

        profileCreateRequest.setAge(18);
        profileCreateRequest.setName("alice1");
        profileCreateRequest.setEmail("alice-dup@mail.com");

        expectAddProfileIsOK(profileCreateRequest);

        profileCreateRequest.setAge(21);
        profileCreateRequest.setName("alice2");

        mockMvc.perform(
                post("/profiles/set")
                        .header(SEC_HEADER, apiKey)
                        .content(objectMapper.writeValueAsString(profileCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void add_profile_age_validation_fails() {
        ProfileCreateRequest profileCreateRequest = new ProfileCreateRequest();

        profileCreateRequest.setAge(null);
        profileCreateRequest.setName("alice");
        profileCreateRequest.setEmail("alice@mail.com");
        expectAddProfileBadRequest(profileCreateRequest);

        profileCreateRequest.setAge(Profile.MIN_AGE - 1);
        expectAddProfileBadRequest(profileCreateRequest);

        profileCreateRequest.setAge(Profile.MAX_AGE + 1);
        expectAddProfileBadRequest(profileCreateRequest);
    }

    @Test
    @SneakyThrows
    public void add_profile_and_get_last_success() {
        ProfileCreateRequest profileCreateRequest = new ProfileCreateRequest();

        profileCreateRequest.setAge(18);
        profileCreateRequest.setName("Alice1");
        profileCreateRequest.setEmail("alice1@mail.com");
        expectAddProfileIsOK(profileCreateRequest);

        profileCreateRequest.setAge(18);
        profileCreateRequest.setName("Alice2");
        profileCreateRequest.setEmail("alice2@mail.com");
        expectAddProfileIsOK(profileCreateRequest);

        var profileResponse = mockMvc.perform(
                get("/profiles/last").header(SEC_HEADER, apiKey))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        var profile = objectMapper.readValue(profileResponse.getContentAsString(), ProfileResponse.class);

        assertThat(profile.getAge()).isEqualTo(profileCreateRequest.getAge());
        assertThat(profile.getEmail()).isEqualTo(profileCreateRequest.getEmail());
        assertThat(profile.getName()).isEqualTo(profileCreateRequest.getName());
        assertThat(profile.getCreated()).isNotNull();
    }

    @Test
    @SneakyThrows
    public void find_last_not_found() {
        mockMvc.perform(
                get("/profiles/last").header(SEC_HEADER, apiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void add_profiles_and_get_all_success() {
        ProfileCreateRequest profileCreateRequest = new ProfileCreateRequest();

        profileCreateRequest.setAge(18);
        profileCreateRequest.setName("Alice1");
        profileCreateRequest.setEmail("alice1@mail.com");
        expectAddProfileIsOK(profileCreateRequest);

        profileCreateRequest.setAge(18);
        profileCreateRequest.setName("Alice2");
        profileCreateRequest.setEmail("alice2@mail.com");
        expectAddProfileIsOK(profileCreateRequest);

        profileCreateRequest.setAge(18);
        profileCreateRequest.setName("Alice3");
        profileCreateRequest.setEmail("alice3@mail.com");
        expectAddProfileIsOK(profileCreateRequest);

        mockMvc.perform(
                get("/profiles")
                        .header(SEC_HEADER, apiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @SneakyThrows
    public void add_and_get_profile_by_email_success() {
        ProfileCreateRequest profileCreateRequest = new ProfileCreateRequest();

        profileCreateRequest.setAge(18);
        profileCreateRequest.setName("Alice");
        profileCreateRequest.setEmail("alice@mail.com");

        expectAddProfileIsOK(profileCreateRequest);

        ProfileFilterRequest profileFilterRequest = new ProfileFilterRequest();
        profileFilterRequest.setEmail(profileCreateRequest.getEmail());

        var profileResponse = mockMvc.perform(
                post("/profiles/get")
                        .content(objectMapper.writeValueAsString(profileFilterRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(SEC_HEADER, apiKey))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        var profile = objectMapper.readValue(profileResponse.getContentAsString(), ProfileResponse.class);

        assertThat(profile.getAge()).isEqualTo(profileCreateRequest.getAge());
        assertThat(profile.getEmail()).isEqualTo(profileCreateRequest.getEmail());
        assertThat(profile.getName()).isEqualTo(profileCreateRequest.getName());
    }


    @SneakyThrows
    private void expectAddProfileBadRequest(ProfileCreateRequest profileCreateRequest) {
        mockMvc.perform(
                post("/profiles/set")
                        .header(SEC_HEADER, apiKey)
                        .content(objectMapper.writeValueAsString(profileCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    private void expectAddProfileIsOK(ProfileCreateRequest profileCreateRequest) {
        mockMvc.perform(
                post("/profiles/set")
                        .header(SEC_HEADER, apiKey)
                        .content(objectMapper.writeValueAsString(profileCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
