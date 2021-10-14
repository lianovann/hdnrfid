package restTests.fullUpdateUser;

import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import restTests.BaseRestTest;
import restTests.pojos.MessageResponse;
import restTests.pojos.deleteUser.DeleteUserRequest;
import restTests.pojos.doRegister.UserRequest;
import restTests.pojos.fullUpdateUser.UpdateUserRequest;
import restTests.pojos.fullUpdateUser.UpdateUserResponse;

import java.lang.reflect.Field;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static restTests.Endpoints.FULL_UPDATE_USER;
import static utils.Generator.generateUser;
import static utils.Generator.getRandomString;

public class UpdateUserTest extends BaseRestTest {
    private static RequestSpecification rq;
    private static ResponseSpecification rs;
    private static UserRequest userRequest;
    private static UpdateUserRequest updateUserRequest;
    private MessageResponse messageResponse;
    private static final String birthday = "12.01.1900";
    private static final String dateStart = "12.09.2021";
    private static final String iNN = "012345678901";
    private static final String gender = "m";

    @BeforeAll
    public static void setup() {
        rq = getReqSpec();
        rs = getDefaultResSpec();
        userRequest = generateUser();
    }

    @BeforeEach
    public void prepareData() {
        updateUserRequest = new UpdateUserRequest();
        prepareBaseData(getRandomString(15));
    }

    @Test
    @DisplayName("You can update user information")
    public void updateUserWithValidData() {
        createUser(userRequest);

        UpdateUserResponse updateUserResponse = given()
                .spec(rq)
                .when()
                .body(updateUserRequest)
                .post(FULL_UPDATE_USER)
                .then()
                .spec(rs)
                .extract().as(UpdateUserResponse.class);

        assertEquals(updateUserRequest, updateUserResponse);

        deleteUser(DeleteUserRequest.builder().email(userRequest.getEmail()).build());
    }

    @Test
    @DisplayName("You can't update information about user who existent")
    public void updateUserWhichNotExistent() {
        messageResponse = given()
                .spec(rq)
                .when()
                .body(updateUserRequest)
                .post(FULL_UPDATE_USER)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getMessage)
                .isEqualTo("Пользователь с таким email не найден!");
    }

    @ParameterizedTest(name = "You {1} enter {2} digits for 'INN'")
    @CsvSource({
            "01234567890, can't, 11",
            "0123456789012, can't, 13"
    })
    @DisplayName("Yoy can enter only 12 digits fot 'INN'")
    public void updateUserWithWrongINN(String newINN, String rule, String count) {
        updateUserRequest.setINN(newINN);

        messageResponse = given()
                .spec(rq)
                .when()
                .body(updateUserRequest)
                .post(FULL_UPDATE_USER)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getMessage)
                .isEqualTo(" Значение " + updateUserRequest.getINN() + " ИНН ФЛ должен содержать 12 цифр");
    }

    @Test
    @DisplayName("You can't enter nothing expect 'f' or 'm' for 'gender'")
    public void updateUserWithWrongGender() {
        updateUserRequest.setGender("x");

        messageResponse = given()
                .spec(rq)
                .when()
                .body(updateUserRequest)
                .post(FULL_UPDATE_USER)
                .then()
                .spec(rs)
                .extract().as(MessageResponse.class);

        assertThat(messageResponse)
                .extracting(MessageResponse::getMessage)
                .isEqualTo(" Значение " + updateUserRequest.getGender() + " некорректное,должна быть либо m либо f");
    }

    private static void prepareBaseData(String data) {
        updateUserRequest = UpdateUserRequest
                .builder()
                .name(data)
                .iNN(iNN)
                .gender(gender)
                .email(userRequest.getEmail())
                .birthday(birthday)
                .dateStart(dateStart)
                .hobby(data)
                .nameOne(data)
                .surnameOne(data)
                .fatherNameOne(data)
                .cat(data)
                .dog(data)
                .parrot(data)
                .cavy(data)
                .hamster(data)
                .squirrel(data)
                .phone(data)
                .address(data)
                .build();
    }

    private void assertEquals(Object firstObject, Object secondObject) {
        for (Field firstObjectField : firstObject.getClass().getFields()) {
            Field secondObjectField;
            try {
                secondObjectField = secondObject.getClass().getField(firstObjectField.getName());
                assertThat(firstObjectField.get(firstObject)).isEqualTo(secondObjectField.get(secondObject));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
}
