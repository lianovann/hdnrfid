package restTests.doLogin;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.*;
import restTests.BaseRestTest;
import restTests.pojos.deleteUser.DeleteUserRequest;
import restTests.pojos.doLogin.LoginByUserRequest;
import restTests.pojos.doLogin.LoginByUserResponse;
import restTests.pojos.doRegister.UserRequest;

import static io.restassured.RestAssured.given;
import static restTests.Endpoints.DO_LOGIN;
import static utils.Generator.generateUser;

public class LoginByUserTest extends BaseRestTest {
    private static RequestSpecification rq;
    private static ResponseSpecification rs;
    private UserRequest userRequest;
    private LoginByUserRequest loginByUserRequest;
    private LoginByUserResponse loginByUserResponse;

    @BeforeAll
    public static void setup() {
        rq = getReqSpec();
        rs = getDefaultResSpec();

    }

    @BeforeEach
    public void prepareData() {
        userRequest = generateUser();
        loginByUserRequest = LoginByUserRequest
                .builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }

    @Test
    @DisplayName("You can login as existent user")
    public void loginAsExistentUser() {
        createUser(userRequest);

        loginByUserResponse = given()
                .spec(rq)
                .when()
                .body(loginByUserRequest)
                .post(DO_LOGIN)
                .then()
                .spec(rs)
                .extract().as(LoginByUserResponse.class);

        Assertions.assertTrue(loginByUserResponse.isResult());

        deleteUser(DeleteUserRequest.builder().email(userRequest.getEmail()).build());
    }

    @Test
    @DisplayName("You can't login as user who not existent")
    public void loginAsNotExistentUser() {
        loginByUserResponse = given()
                .spec(rq)
                .when()
                .body(loginByUserRequest)
                .post(DO_LOGIN)
                .then()
                .spec(rs)
                .extract().as(LoginByUserResponse.class);

        Assertions.assertFalse(loginByUserResponse.isResult());
    }
}
