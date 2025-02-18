package coursework.backend.dto.feedback;

import coursework.backend.entity.enums.Decision;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDecisionDTO {

    @NotNull
    private Decision decision;


}
