package restTests.pojos.getUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserResponse {
    private String avatar;
    private Integer birthday;
    private String gender;
    @JsonProperty("date_start")
    private Integer dateStart;
    private String hobby;
    private String type;
    private String message;
    private String name;
    private String password;
    private String email;
}
