package restTests.doRegister;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.*;
import restTests.BaseRestTest;
import restTests.pojos.MessageResponse;
import restTests.pojos.deleteUser.DeleteUserRequest;
import restTests.pojos.doRegister.CreateUserResponse;
import restTests.pojos.doRegister.UserRequest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static restTests.Endpoints.DO_REGISTER;
import static utils.Constants.MANAGER_EMAIL;
import static utils.Generator.generateUser;
import static utils.Generator.getRandomString;

public class CreateUserTest extends BaseRestTest {
    private static RequestSpecification rq;
    private static ResponseSpecification rs;
    private UserRequest userRequest;

    @BeforeAll
    public static void setup() {
        rq = getReqSpec();
        rs = getDefaultResSpec();
    }

    @BeforeEach
    public void prepareData() {
        userRequest = generateUser();
    }

    @Test
    @DisplayName("You can create new user")
    public void createUser() {
        CreateUserResponse createUserResponse = given()
                .spec(rq)
                .when()
                .body(userRequest)
                .post(DO_REGISTER)
                .then()
                .spec(rs)
                .extract().as(CreateUserResponse.class);

        assertThat(createUserResponse)
                .extracting(CreateUserResponse::getEmail)
                .isEqualTo(userRequest.getEmail());

        deleteUser(DeleteUserRequest.builder().email(userRequest.getEmail()).build());
    }

    @Test
    @DisplayName("You can't create user with existent email")
    public void createUserWithExistentEmail() {
        userRequest.setEmail(MANAGER_EMAIL);

        MessageResponse messageResponse = given()
                .spec(rq)
                .when()
                .body(userRequest)
                .post(DO_REGISTER)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getMessage)
                .isEqualTo(" email " + userRequest.getEmail() + " уже есть в базе");
    }

    @Test
    @Disabled
    @DisplayName("BUG! You can (?) create user with wrong email")
    public void deleteUserWithWrongEmail() {
        userRequest.setEmail(getRandomString(30) + "@" + getRandomString(30) + "." + getRandomString(30));

        MessageResponse messageResponse = given()
                .spec(rq)
                .when()
                .body(userRequest)
                .post(DO_REGISTER)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getMessage)
                .isEqualTo(" Некоректный  email " + userRequest.getEmail());
    }
}
