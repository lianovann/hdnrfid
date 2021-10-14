package restTests.pojos.updateTask;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class UpdateTaskRequest {
    @JsonProperty("id_task")
    private String idTask;
    @JsonProperty("task_title")
    public String taskTitle;
    @JsonProperty("task_description")
    public String taskDescription;
    @JsonProperty("email_owner")
    public String emailOwner;
    @JsonProperty("email_assign")
    public String emailAssign;
}
