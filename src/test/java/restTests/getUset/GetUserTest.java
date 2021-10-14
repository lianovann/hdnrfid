package restTests.getUset;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import restTests.BaseRestTest;
import restTests.pojos.MessageResponse;
import restTests.pojos.deleteUser.DeleteUserRequest;
import restTests.pojos.doRegister.UserRequest;
import restTests.pojos.getUser.GetUserRequest;
import restTests.pojos.getUser.GetUserResponse;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static restTests.Endpoints.GET_USER;
import static utils.Generator.generateUser;

public class GetUserTest extends BaseRestTest {
    private static RequestSpecification rq;
    private static ResponseSpecification rs;
    private static UserRequest userRequest;
    private static final GetUserRequest getUserRequest = new GetUserRequest();

    @BeforeAll
    public static void setup() {
        rq = getReqSpec();
        rs = getDefaultResSpec();
        userRequest = generateUser();
    }

    @Test
    @DisplayName("You can get information about user who existent")
    public void getUser() {
        createUser(userRequest);
        getUserRequest.setEmail(userRequest.getEmail());

        GetUserResponse getUserResponse = given()
                .spec(rq)
                .when()
                .body(getUserRequest)
                .get(GET_USER)
                .then()
                .spec(rs)
                .extract().as(GetUserResponse.class);

        assertThat(getUserResponse)
                .extracting(GetUserResponse::getEmail)
                .isEqualTo(getUserRequest.getEmail());

        deleteUser(DeleteUserRequest.builder().email(userRequest.getEmail()).build());
    }

    @Test
    @DisplayName("You can't get information about user who not existent")
    public void getUserWhichNotExistent() {
        getUserRequest.setEmail(userRequest.getEmail());

        MessageResponse messageResponse = given()
                .spec(rq)
                .when()
                .body(getUserRequest)
                .get(GET_USER)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getMessage)
                .isEqualTo(" Пользователь не найден " + userRequest.getEmail());
    }
}
