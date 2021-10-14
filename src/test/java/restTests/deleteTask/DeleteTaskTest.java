package restTests.deleteTask;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.*;
import restTests.BaseRestTest;
import restTests.pojos.MessageResponse;
import restTests.pojos.createTask.CreateTaskRequest;
import restTests.pojos.createTask.CreateTaskResponse;
import restTests.pojos.deleteTask.DeleteTaskRequest;
import restTests.pojos.doRegister.UserRequest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static restTests.Endpoints.DELETE_TASK;
import static utils.Constants.MANAGER_EMAIL;
import static utils.Generator.generateUser;

public class DeleteTaskTest extends BaseRestTest {
    private static RequestSpecification rq;
    private static ResponseSpecification rs;
    private static UserRequest userRequest;
    private static CreateTaskResponse createTaskResponse;
    private MessageResponse messageResponse;
    private static final String emailOwner = MANAGER_EMAIL;
    private static final String incorrectTaskId = "bla";
    private static final Integer wrongTaskId = -1;

    @BeforeAll
    public static void setup() {
        rq = getReqSpec();
        rs = getDefaultResSpec();
        userRequest = generateUser();
        createTaskResponse = createTask(userRequest, CreateTaskRequest
                .builder()
                .taskTitle(userRequest.getName())
                .taskDescription(userRequest.getName())
                .emailOwner(emailOwner)
                .emailAssign(userRequest.getEmail()).
                build());
    }

    @Test
    @DisplayName("You can delete existent task")
    public void deleteTask() {
        DeleteTaskRequest deleteTaskRequest = DeleteTaskRequest
                .builder()
                .emailOwner(emailOwner)
                .taskId(createTaskResponse.getIdTask())
                .build();

        messageResponse = given()
                .spec(rq)
                .when()
                .body(deleteTaskRequest)
                .delete(DELETE_TASK)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getType)
                .isEqualTo("success");
    }

    @Test
    @DisplayName("You can't delete not existent task")
    public void deleteNotExistentTask() {
        DeleteTaskRequest deleteTaskRequest = DeleteTaskRequest
                .builder()
                .emailOwner(emailOwner)
                .taskId(wrongTaskId)
                .build();

        messageResponse = given()
                .spec(rq)
                .when()
                .body(deleteTaskRequest)
                .delete(DELETE_TASK)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getType)
                .isEqualTo(" Задача с ID " + wrongTaskId + " не найдена");
    }

    @Test
    @DisplayName("You can't delete task with incorrect id")
    public void deleteTaskWithIncorrectId() {
        DeleteTaskRequest deleteTaskRequest = DeleteTaskRequest
                .builder()
                .emailOwner(emailOwner)
                .taskId(incorrectTaskId)
                .build();

        messageResponse = given()
                .spec(rq)
                .when()
                .body(deleteTaskRequest)
                .delete(DELETE_TASK)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getType)
                .isEqualTo(" task_id это обязательный параметр  некорректный");
    }

    @Test
    @DisplayName("You can't delete task if email of 'task_owner' is wrong")
    public void deleteTaskWithWrongEmailOfTaskOwner() {
        DeleteTaskRequest deleteTaskRequest = DeleteTaskRequest
                .builder()
                .emailOwner(emailOwner)
                .taskId(createTaskResponse.getIdTask())
                .build();

        messageResponse = given()
                .spec(rq)
                .when()
                .body(deleteTaskRequest)
                .delete(DELETE_TASK)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getType)
                .isEqualTo("Пользователь не найден c email_owner " + userRequest.getEmail());
    }
}


