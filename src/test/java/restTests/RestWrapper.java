package restTests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import restTests.pojos.doRegister.CreateUserResponse;
import restTests.pojos.doLogin.LoginByUserRequest;
import restTests.pojos.doRegister.UserRequest;
import restTests.pojos.deleteUser.DeleteUserRequest;

import static io.restassured.RestAssured.given;
import static restTests.Endpoints.*;

public class RestWrapper {
    private static final String BASE_URL = "http://users.bugred.ru/";
    private static final String BASE_PATH = "tasks/rest/";
    private static RequestSpecification REQ_SPEC;
    private Cookies cookies;

    private RestWrapper(Cookies cookies) {
        this.cookies = cookies;

        REQ_SPEC = new RequestSpecBuilder()
                .addCookies(cookies)
                .setBaseUri(BASE_URL)
                .setBasePath(BASE_PATH)
                .setContentType(ContentType.JSON)
                .build();
    }

    private static RestWrapper loginAs(String login, String password) {
        Cookies cookies = given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .basePath(BASE_PATH)
                .body(new LoginByUserRequest(login, password))
                .post(DO_LOGIN)
                .getDetailedCookies();

        return new RestWrapper(cookies);
    }

    public CreateUserResponse createUser(UserRequest rq) {
        return given().spec(REQ_SPEC).body(rq).post(DO_REGISTER).as(CreateUserResponse.class);
    }

    public void deleteUser(DeleteUserRequest rq) {
        given().spec(REQ_SPEC).body(rq).delete(DELETE_USER);
    }
}
