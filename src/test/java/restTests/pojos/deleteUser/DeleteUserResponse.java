package restTests.pojos.deleteUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteUserResponse {
    private String type;
    private String message;
}
