package com.tradebit.configuration;

import com.tradebit.config.SecurityConfig;
import com.tradebit.controller.AuthController;
import com.tradebit.controller.UserController;
import com.tradebit.service.AuthorizationService;
import com.tradebit.service.KeycloakService;
import com.tradebit.service.RegistrationService;
import com.tradebit.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class, AuthController.class})
@Import(SecurityConfig.class)
@ExtendWith(SpringExtension.class)
public class SecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private KeycloakService keycloakService;
    @MockBean
    private RegistrationService registrationService;
    @MockBean
    private AuthorizationService authorizationService;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void whenNonAdmin_thenAccessDeniedForAdminEndpoint() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenUnauthenticated_thenAccessDeniedForAdminEndpoint() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void whenAdmin_thenAccessGrantedForAdminEndpoint() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUnauthenticated_thenAccessGrantedForPublicEndpoint() throws Exception {
        mockMvc.perform(get("/auth"))
                .andExpect(status().isOk());
    }


}
