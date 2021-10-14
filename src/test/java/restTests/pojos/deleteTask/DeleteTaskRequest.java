package restTests.pojos.deleteTask;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class DeleteTaskRequest {
    @JsonProperty("email_owner")
    private String emailOwner;
    @JsonProperty("task_id")
    private Object taskId; //BUG! ID must be integer, but it strings now
}
