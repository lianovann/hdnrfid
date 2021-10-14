package restTests.pojos.doRegister;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateUserResponse extends UserRequest {
    private String avatar;
    private Integer birthday;
    private String gender;
    @JsonProperty("date_start")
    private Integer dateStart;
    private String hobby;
}
