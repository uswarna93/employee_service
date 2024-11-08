package com.demo_emp_service.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSkillInfo {

    @NotNull(message = "skillId cannot be null")
    private String skillId;

    @NotNull(message = "skillName cannot be null")
    private String skillName;

    @NotNull(message = "skillLevel cannot be null")
    private String skillLevel;
}
