package restTests.pojos.fullUpdateUser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class UpdateUserRequest {
    private String name;
    private String email;
    private String birthday;
    private String gender;
    @JsonProperty("date_start")
    private String dateStart;
    private String hobby;
    @JsonProperty("name1")
    private String nameOne;
    @JsonProperty("surname1")
    private String surnameOne;
    @JsonProperty("fathername1")
    private String fatherNameOne;
    private String cat;
    private String dog;
    private String parrot;
    private String cavy;
    private String hamster;
    private String squirrel;
    private String phone;
    @JsonProperty("adres")
    private String address;
    @JsonProperty("inn")
    private String iNN;
}
