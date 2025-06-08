//package com.example.keycloak;
//
//import com.example.keycloak.CustomUserDataMapper.UserDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.keycloak.models.UserModel;
//import org.keycloak.models.UserSessionModel;
//import org.keycloak.representations.IDToken;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class CustomUserDataMapperTest {
//
//    private CustomUserDataMapper mapper;
//
//    @Mock
//    private UserSessionModel userSession;
//
//    @Mock
//    private UserModel userModel;
//
//    @Mock
//    private IDToken idToken;
//
//    private Map<String, Object> otherClaims;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mapper = new CustomUserDataMapper();
//        otherClaims = new HashMap<>();
//
//        // Common mock setups
//        when(userSession.getUser()).thenReturn(userModel);
//        when(idToken.getOtherClaims()).thenReturn(otherClaims); // Return our map for claims
//    }
//
//    @Test
//    void testTransformIdTokenAddsUserProfileClaim() {
//        String testUserId = "test-user-id-123";
//        String expectedUsername = "user_" + testUserId;
//        String expectedEmail = testUserId + "@example.com";
//
//        when(userModel.getId()).thenReturn(testUserId);
//
//        // Call the method under test
//        IDToken resultToken = mapper.transformIdToken(idToken, userSession);
//
//        // Verify that otherClaims map was updated in the original idToken mock
//        // (or the resultToken if transformIdToken returns a new instance - current impl modifies input)
//        assertSame(idToken, resultToken, "transformIdToken should return the same token instance.");
//
//        Map<String, Object> claims = resultToken.getOtherClaims();
//        assertNotNull(claims, "Claims map should not be null.");
//        assertTrue(claims.containsKey("user_profile"), "Claims should contain 'user_profile'.");
//
//        Object userProfileClaim = claims.get("user_profile");
//        assertNotNull(userProfileClaim, "'user_profile' claim should not be null.");
//        assertTrue(userProfileClaim instanceof UserDTO, "'user_profile' should be an instance of UserDTO.");
//
//        UserDTO userProfile = (UserDTO) userProfileClaim;
//        assertEquals(testUserId, userProfile.getId(), "UserDTO ID should match.");
//        assertEquals(expectedUsername, userProfile.getUsername(), "UserDTO username should match.");
//        assertEquals(expectedEmail, userProfile.getEmail(), "UserDTO email should match.");
//
//        // Verify that setOtherClaims was called on the idToken
//        verify(idToken).setOtherClaims(claims);
//    }
//
//    @Test
//    void testTransformUserInfoTokenAddsUserProfileClaim() {
//        String testUserId = "test-user-id-456";
//        String expectedUsername = "user_" + testUserId;
//        String expectedEmail = testUserId + "@example.com";
//
//        when(userModel.getId()).thenReturn(testUserId);
//
//        // Ensure otherClaims is fresh for this test if not using a new IDToken mock
//        // In this setup, idToken.getOtherClaims() returns the 'otherClaims' map initialized in setUp.
//        // If it was modified by a previous test and idToken mock is reused, clear it.
//        otherClaims.clear();
//
//
//        // Call the method under test
//        IDToken resultToken = mapper.transformUserInfoToken(idToken, userSession);
//
//        // Verify
//        assertSame(idToken, resultToken, "transformUserInfoToken should return the same token instance.");
//
//        Map<String, Object> claims = resultToken.getOtherClaims();
//        assertNotNull(claims, "Claims map should not be null.");
//        assertTrue(claims.containsKey("user_profile"), "Claims should contain 'user_profile'.");
//
//        Object userProfileClaim = claims.get("user_profile");
//        assertNotNull(userProfileClaim, "'user_profile' claim should not be null.");
//        assertTrue(userProfileClaim instanceof UserDTO, "'user_profile' should be an instance of UserDTO.");
//
//        UserDTO userProfile = (UserDTO) userProfileClaim;
//        assertEquals(testUserId, userProfile.getId(), "UserDTO ID should match.");
//        assertEquals(expectedUsername, userProfile.getUsername(), "UserDTO username should match.");
//        assertEquals(expectedEmail, userProfile.getEmail(), "UserDTO email should match.");
//
//        // Verify that setOtherClaims was called on the idToken
//        verify(idToken).setOtherClaims(claims);
//    }
//
//    @Test
//    void testTransformIdTokenHandlesNullInitialClaims() {
//        String testUserId = "test-user-id-789";
//        when(userModel.getId()).thenReturn(testUserId);
//        when(idToken.getOtherClaims()).thenReturn(null); // Simulate initial null claims
//
//        IDToken resultToken = mapper.transformIdToken(idToken, userSession);
//
//        Map<String, Object> claims = resultToken.getOtherClaims();
//        assertNotNull(claims, "Claims map should not be null even if initially null.");
//        assertTrue(claims.containsKey("user_profile"), "Claims should contain 'user_profile'.");
//        assertTrue(claims.get("user_profile") instanceof UserDTO, "'user_profile' should be an instance of UserDTO.");
//
//        // Verify setOtherClaims was called with a new map
//        verify(idToken).setOtherClaims(argThat(map -> map != null && map.containsKey("user_profile")));
//    }
//
//    @Test
//    void testTransformUserInfoTokenHandlesNullInitialClaims() {
//        String testUserId = "test-user-id-abc";
//        when(userModel.getId()).thenReturn(testUserId);
//        when(idToken.getOtherClaims()).thenReturn(null); // Simulate initial null claims
//
//        IDToken resultToken = mapper.transformUserInfoToken(idToken, userSession);
//
//        Map<String, Object> claims = resultToken.getOtherClaims();
//        assertNotNull(claims, "Claims map should not be null even if initially null.");
//        assertTrue(claims.containsKey("user_profile"), "Claims should contain 'user_profile'.");
//        assertTrue(claims.get("user_profile") instanceof UserDTO, "'user_profile' should be an instance of UserDTO.");
//
//        // Verify setOtherClaims was called with a new map
//        verify(idToken).setOtherClaims(argThat(map -> map != null && map.containsKey("user_profile")));
//    }
//}
