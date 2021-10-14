package restTests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import restTests.pojos.createTask.CreateTaskRequest;
import restTests.pojos.createTask.CreateTaskResponse;
import restTests.pojos.deleteTask.DeleteTaskRequest;
import restTests.pojos.doRegister.CreateUserResponse;
import restTests.pojos.doRegister.UserRequest;
import restTests.pojos.deleteUser.DeleteUserRequest;
import restTests.pojos.getUser.GetUserRequest;
import restTests.pojos.getUser.GetUserResponse;

import static io.restassured.RestAssured.given;
import static restTests.Endpoints.*;

public class BaseRestTest {
    private static final String BASE_URL = "http://users.bugred.ru/";
    private static final String BASE_PATH = "tasks/rest/";

    public static RequestSpecification getReqSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setBasePath(BASE_PATH)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification getDefaultResSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();
    }

    public static CreateUserResponse createUser(UserRequest rq) {
        RequestSpecification reqSpec = getReqSpec();
        return given().spec(reqSpec).body(rq).post(DO_REGISTER).as(CreateUserResponse.class);
    }

    public static GetUserResponse getUser(GetUserRequest rq) {
        RequestSpecification reqSpec = getReqSpec();
        return given().spec(reqSpec).body(rq).get(GET_USER).as(GetUserResponse.class);
    }

    public static void deleteUser(DeleteUserRequest rq) {
        RequestSpecification reqSpec = getReqSpec();
        given().spec(reqSpec).body(rq).delete(DELETE_USER);
    }

    public static CreateTaskResponse createTask(UserRequest userRequest, CreateTaskRequest rq) {
        RequestSpecification reqSpec = getReqSpec();
        createUser(userRequest);
        return given().spec(reqSpec).body(rq).post(CREATE_TASK).as(CreateTaskResponse.class);
    }

    public static void deleteTask(DeleteTaskRequest rq) {
        RequestSpecification reqSpec = getReqSpec();
        given().spec(reqSpec).body(rq).delete(DELETE_TASK);
    }
}
