package restTests.deleteUser;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import restTests.BaseRestTest;
import restTests.pojos.MessageResponse;
import restTests.pojos.deleteUser.DeleteUserRequest;
import restTests.pojos.doRegister.CreateUserResponse;
import restTests.pojos.doRegister.UserRequest;
import restTests.pojos.getUser.GetUserRequest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static restTests.Endpoints.DELETE_USER;
import static restTests.Endpoints.GET_USER;
import static utils.Generator.*;

public class DeleteUserTest extends BaseRestTest {
    private static RequestSpecification rq;
    private static ResponseSpecification rs;
    private static UserRequest userRequest;
    private static final DeleteUserRequest deleteUserRequest = new DeleteUserRequest();
    private MessageResponse messageResponse;

    @BeforeAll
    public static void setup() {
        rq = getReqSpec();
        rs = getDefaultResSpec();
        userRequest = generateUser();
    }

    @Test
    @DisplayName("You can delete existent user")
    public void deleteUser() {
        CreateUserResponse createUserResponse = createUser(userRequest);
        deleteUserRequest.setEmail(userRequest.getEmail());
        GetUserRequest getUserRequest = GetUserRequest
                .builder()
                .email(createUserResponse.getEmail())
                .build();

        given()
                .spec(rq)
                .when()
                .body(deleteUserRequest)
                .delete(DELETE_USER)
                .then()
                .spec(rs);

        messageResponse = given()
                .spec(rq)
                .when()
                .body(getUserRequest)
                .get(GET_USER)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse.getMessage())
                .isEqualTo(" Пользователь не найден " + createUserResponse.getEmail());
    }

    @Test
    @DisplayName("You can't delete not existent user")
    public void deleteUserWhoNotExistent() {
        deleteUserRequest.setEmail(getRandomString(30) + "@" + getRandomString(30) + ".com");

        messageResponse = given()
                .spec(rq)
                .when()
                .body(deleteUserRequest)
                .delete(DELETE_USER)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getMessage)
                .isEqualTo("Пользователь с таким email не найден!");
    }

    @Test
    @DisplayName("You can't delete user with wrong 'email' in request body")
    public void deleteUserWithWrongEmail() {
        deleteUserRequest.setEmail(getRandomString(30) + "@" + getRandomString(30) + "." + getRandomNumber());

        messageResponse = given()
                .spec(rq)
                .when()
                .body(deleteUserRequest)
                .delete(DELETE_USER)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getMessage)
                .isEqualTo("email неправильный!");
    }
}
