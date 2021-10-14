package restTests.updateTask;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.*;
import restTests.BaseRestTest;
import restTests.pojos.MessageResponse;
import restTests.pojos.createTask.CreateTaskRequest;
import restTests.pojos.createTask.CreateTaskResponse;
import restTests.pojos.deleteTask.DeleteTaskRequest;
import restTests.pojos.deleteUser.DeleteUserRequest;
import restTests.pojos.doRegister.UserRequest;
import restTests.pojos.updateTask.UpdateTaskRequest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static restTests.Endpoints.UPDATE_TASK;
import static utils.Constants.MANAGER_EMAIL;
import static utils.Generator.generateUser;
import static utils.Generator.getRandomNumber;

public class UpdateTaskTest extends BaseRestTest {
    private static RequestSpecification rq;
    private static ResponseSpecification rs;
    private static UserRequest userRequest;
    private static UpdateTaskRequest updateTaskRequest;
    private static MessageResponse messageResponse;
    private CreateTaskResponse createTaskResponse;
    private static final String wrongTaskId = "-1";
    private static final String emailOwner = MANAGER_EMAIL;

    @BeforeAll
    public static void setup() {
        rq = getReqSpec();
        rs = getDefaultResSpec();
    }

    @BeforeEach
    public void prepareData() {
        String randomNumber = getRandomNumber();
        userRequest = generateUser();

        createTaskResponse = createTask(userRequest, CreateTaskRequest
                .builder()
                .taskTitle(userRequest.getName())
                .taskDescription(userRequest.getName())
                .emailOwner(emailOwner)
                .emailAssign(userRequest.getEmail()).
                build());

        updateTaskRequest = UpdateTaskRequest
                .builder()
                .taskTitle(randomNumber)
                .taskDescription(randomNumber)
                .emailOwner(emailOwner)
                .emailAssign(userRequest.getEmail())
                .idTask(createTaskResponse.getIdTask())
                .build();
    }

    @AfterEach
    public void shutDown() {
        deleteUser(DeleteUserRequest.builder().email(userRequest.getEmail()).build());
        deleteTask(DeleteTaskRequest.builder().emailOwner(emailOwner).taskId(createTaskResponse.getIdTask()).build());
    }

    @Test
    @DisplayName("You can update task")
    public void updateTask() {
        messageResponse = given()
                .spec(rq)
                .when()
                .body(updateTaskRequest)
                .put(UPDATE_TASK)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getType)
                .isEqualTo("success");
    }

    @Test
    @DisplayName("You can't update task by id if it not existent")
    public void updateTaskByWrongId() {
        updateTaskRequest.setIdTask(wrongTaskId);

        messageResponse = given()
                .spec(rq)
                .when()
                .body(updateTaskRequest)
                .put(UPDATE_TASK)
                .then().log().all()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getMessage)
                .isEqualTo(" Задача с id_task " + updateTaskRequest.getIdTask() + " не найдена");
    }
}
