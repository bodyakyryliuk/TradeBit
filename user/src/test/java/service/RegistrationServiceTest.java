package service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import com.tradebit.RabbitMQMessageProducer;
import com.tradebit.authentication.AuthenticationResponse;
import com.tradebit.exception.InvalidTokenException;
import com.tradebit.exception.UserAlreadyExistsException;
import com.tradebit.exception.UserNotFoundException;
import com.tradebit.registration.EmailRequest;
import com.tradebit.registration.RegistrationRequest;
import com.tradebit.registration.services.JwtService;
import com.tradebit.registration.services.RegistrationServiceImpl;
import com.tradebit.user.models.Role;
import com.tradebit.user.models.User;
import com.tradebit.user.repositories.UserRepository;
import com.tradebit.verificationToken.VerificationToken;
import com.tradebit.verificationToken.VerificationTokenService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

public class RegistrationServiceTest {
    @InjectMocks
    private RegistrationServiceImpl registrationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private VerificationTokenService verificationTokenService;
    @Mock
    private JwtService jwtService;
    @Mock
    private RabbitMQMessageProducer messageProducer;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testRegisterUserAlreadyExists(){
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@example.com");

        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        registrationService.register(request);
    }

    @Test
    public void testSuccessfulRegistration(){
        RegistrationRequest request = RegistrationRequest.builder()
                .email("test@example.com")
                .firstName("firstname")
                .lastName("lastname")
                .password("password")
                .build();

        User user = registrationService.createUserFromRequest(request);
        VerificationToken verificationToken = new VerificationToken(user, UUID.randomUUID().toString());

        when(verificationTokenService.generateVerificationToken(any(User.class)))
                .thenReturn(verificationToken);
        Map<String, String> response = registrationService.register(request);

        EmailRequest emailRequest = EmailRequest.builder()
                .to(request.getEmail())
                .message(verificationToken.getToken())
                .build();

        // Check that the registration was successful
        assertEquals("success", response.get("status"));
        assertEquals("Registration successful! Please check your email to activate your account.", response.get("message"));

        verify(userRepository).save(any(User.class)); // Ensure a new user was saved
        verify(messageProducer).publish(
                eq(emailRequest),
                eq("internal.exchange"),
                eq("internal.email.routing-key")
        ); // Ensure an email was sent

    }

    @Test
    public void testGenerateEmailRequest(){
        String to = "test@example.com";
        String token = UUID.randomUUID().toString();

        EmailRequest emailRequest = registrationService.generateEmailRequest(to, token);

        assertEquals(to, emailRequest.getTo());
        assertEquals(token, emailRequest.getMessage());
    }

    @Test
    public void testCreateUserFromRequest(){
        RegistrationRequest request = RegistrationRequest.builder()
                .email("test@example.com")
                .firstName("firstname")
                .lastName("lastname")
                .password("password")
                .build();

        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);

        User user = registrationService.createUserFromRequest(request);
        assertEquals(request.getEmail(), user.getEmail());
        assertEquals(request.getFirstName(), user.getFirstName());
        assertEquals(request.getLastName(), user.getLastName());
        assertEquals(encodedPassword, user.getPassword());
        assertEquals(Role.USER, user.getRole());
        assertFalse(user.isEnabled());
    }

    @Test(expected = InvalidTokenException.class)
    public void testNullVerificationToken(){
        String token = "token";
        when(verificationTokenService.getVerificationToken(token)).thenReturn(null);

        try{
            registrationService.confirmRegistration(token);
        }catch (InvalidTokenException e){
            assertEquals("Invalid token", e.getMessage());
            throw e;
        }
    }

    @Test(expected = InvalidTokenException.class)
    public void testExpiredVerificationToken(){
        String token = "token";
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setExpiryDate(new Date(System.currentTimeMillis() - 1000));
        when(verificationTokenService.getVerificationToken(token)).thenReturn(verificationToken);

        try{
            registrationService.confirmRegistration(token);
        }catch (InvalidTokenException e){
            assertEquals("Token has expired", e.getMessage());
            throw e;
        }
    }

    @Test(expected = UserNotFoundException.class)
    public void testUserNotFound(){
        String token = "token";
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(null);
        when(verificationTokenService.getVerificationToken(token)).thenReturn(verificationToken);

        try{
            registrationService.confirmRegistration(token);
        }catch (UserNotFoundException e){
            assertEquals("User with a given token doesn't exist", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testConfirmRegistration(){
        String token = "token";
        String jwt = "jwt";
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(new User());
        User user = verificationToken.getUser();
        when(verificationTokenService.getVerificationToken(token)).thenReturn(verificationToken);
        when(jwtService.generateToken(user)).thenReturn(jwt);

        AuthenticationResponse response = registrationService.confirmRegistration(token);
        assertEquals(jwt, response.getToken());
        assertTrue(user.isEnabled());
        verify(verificationTokenService).deleteVerificationToken(verificationToken);
        verify(userRepository).save(user);

    }

}

















