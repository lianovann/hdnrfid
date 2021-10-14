package restTests.pojos.createTask;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateTaskResponse {
    private String type;
    @JsonProperty("id_task")
    private String idTask;
    private String message;
}
