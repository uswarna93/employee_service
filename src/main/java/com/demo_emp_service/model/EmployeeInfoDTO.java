package com.demo_emp_service.model;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class EmployeeInfoDTO {

    private String empId;
    private String firstName;
    private  String lastName;
    private List<EmployeeSkillInfoDTO> employeeSkills;
}
