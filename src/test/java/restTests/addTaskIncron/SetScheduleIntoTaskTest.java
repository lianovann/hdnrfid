package restTests.addTaskIncron;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.*;
import restTests.BaseRestTest;
import restTests.pojos.MessageResponse;
import restTests.pojos.addTaskIncron.SetScheduleRequest;
import restTests.pojos.createTask.CreateTaskRequest;
import restTests.pojos.createTask.CreateTaskResponse;
import restTests.pojos.deleteTask.DeleteTaskRequest;
import restTests.pojos.deleteUser.DeleteUserRequest;
import restTests.pojos.doRegister.UserRequest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static restTests.Endpoints.ADD_TASK_INCRON;
import static utils.Constants.MANAGER_EMAIL;
import static utils.Generator.generateUser;

public class SetScheduleIntoTaskTest extends BaseRestTest {
    private static RequestSpecification rq;
    private static ResponseSpecification rs;
    private static final String emailOwner = MANAGER_EMAIL;
    private UserRequest userRequest;
    private CreateTaskResponse createTaskResponse;
    private SetScheduleRequest setScheduleRequest;

    @BeforeAll
    public static void setup() {
        rq = getReqSpec();
        rs = getDefaultResSpec();
    }

    @BeforeEach
    public void prepareData() {
        userRequest = generateUser();

        createTaskResponse = createTask(userRequest, CreateTaskRequest
                .builder()
                .taskTitle(userRequest.getName())
                .taskDescription(userRequest.getName())
                .emailOwner(emailOwner)
                .emailAssign(userRequest.getEmail()).
                build());

        setScheduleRequest = SetScheduleRequest
                .builder()
                .emailOwner(emailOwner)
                .taskId(createTaskResponse.getIdTask())
                .hours(23)
                .minutes(59)
                .month(12)
                .days(31)
                .dayWeeks(7)
                .build();
    }

    @AfterEach
    public void shutDown() {
        deleteUser(DeleteUserRequest.builder().email(userRequest.getEmail()).build());
        deleteTask(DeleteTaskRequest.builder().emailOwner(emailOwner).taskId(createTaskResponse.getIdTask()).build());
    }

    @Test
    @DisplayName("You can set schedule into task")
    public void setSchedule() {
        MessageResponse messageResponse = given()
                .spec(rq)
                .when()
                .body(setScheduleRequest)
                .post(ADD_TASK_INCRON)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getType)
                .isEqualTo("success");
    }

    @Test
    @DisplayName("You can't set schedule into task with wrong hours")
    public void setScheduleWithWrongHours() {
        setScheduleRequest = SetScheduleRequest
                .builder()
                .hours(35)
                .build();

        MessageResponse messageResponse = given()
                .spec(rq)
                .when()
                .body(setScheduleRequest)
                .post(ADD_TASK_INCRON)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getType)
                .isEqualTo("error");
    }

    @Test
    @DisplayName("You can't set schedule into task with wrong minutes")
    public void setScheduleWithWrongMinutes() {}

    @Test
    @DisplayName("You can't set schedule into task with wrong month")
    public void setScheduleWithWrongMonth() {}

    @Test
    @DisplayName("You can't set schedule into task with wrong days")
    public void setScheduleWithWrongDays() {}

    @Test
    @DisplayName("You can't set schedule into task with wrong day of week")
    public void setScheduleWithWrongDayOfWeek() {}
}
