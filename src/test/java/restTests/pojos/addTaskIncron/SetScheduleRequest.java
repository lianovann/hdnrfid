package restTests.pojos.addTaskIncron;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class SetScheduleRequest {
    @JsonProperty("email_owner")
    private String emailOwner;
    @JsonProperty("task_id")
    private String taskId; //BUG! ID must be integer, but it strings now
    private Integer hours;
    private Integer minutes;
    private Integer month;
    private Integer days;
    @JsonProperty("day_weeks")
    private Integer dayWeeks;
}
