package restTests.createTask;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import restTests.BaseRestTest;
import restTests.pojos.MessageResponse;
import restTests.pojos.createTask.CreateTaskRequest;
import restTests.pojos.createTask.CreateTaskResponse;
import restTests.pojos.deleteTask.DeleteTaskRequest;
import restTests.pojos.doRegister.UserRequest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static restTests.Endpoints.CREATE_TASK;
import static utils.Constants.MANAGER_EMAIL;
import static utils.Generator.*;

public class CreateTaskTest extends BaseRestTest {
    private static RequestSpecification rq;
    private static ResponseSpecification rs;
    private static UserRequest userRequest;
    private CreateTaskRequest createTaskRequest;
    private MessageResponse messageResponse;
    private static final String emailOwner = MANAGER_EMAIL;

    @BeforeAll
    public static void setup() {
        rq = getReqSpec();
        rs = getDefaultResSpec();
        userRequest = generateUser();
    }

    @BeforeEach
    public void prepareData() {
        createTaskRequest = CreateTaskRequest
                .builder()
                .taskTitle(userRequest.getName())
                .taskDescription(userRequest.getName())
                .emailOwner(emailOwner)
                .emailAssign(userRequest.getEmail())
                .build();
    }

    @Test
    @DisplayName("You can create task")
    public void createTask() {
        createUser(userRequest);

        CreateTaskResponse createTaskResponse = given()
                .spec(rq)
                .when()
                .body(createTaskRequest)
                .post(CREATE_TASK)
                .then()
                .spec(rs)
                .extract().as(CreateTaskResponse.class);

        assertThat(createTaskResponse)
                .extracting(CreateTaskResponse::getType)
                .isEqualTo("success");

        deleteTask(DeleteTaskRequest.builder().emailOwner(emailOwner).taskId(createTaskResponse.getIdTask()).build());
    }

    @Test
    @DisplayName("You can't create task as user who not existent")
    public void createTaskWithNotExistentUser() {
        messageResponse = given()
                .spec(rq)
                .when()
                .body(createTaskRequest)
                .post(CREATE_TASK)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getMessage)
                .isEqualTo("Пользователь не найден c email_assign " + createTaskRequest.getEmailAssign());
    }

    @Test
    @DisplayName("You can't create task if email of 'task_owner' is wrong")
    public void createTaskWithWrongEmailOfTaskOwner() {
        createTaskRequest.setEmailOwner(getRandomString(15) + "@" + getRandomString(15) + ".ru");

        messageResponse = given()
                .spec(rq)
                .when()
                .body(createTaskRequest)
                .post(CREATE_TASK)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getMessage)
                .isEqualTo("Пользователь не найден c email_owner " + createTaskRequest.getEmailOwner());
    }
}
