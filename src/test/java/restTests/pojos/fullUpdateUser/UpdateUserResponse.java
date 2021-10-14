package restTests.pojos.fullUpdateUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserResponse extends UpdateUserRequest{
    private String avatar;
    private String type;
    private String message;
    private String password;
}
