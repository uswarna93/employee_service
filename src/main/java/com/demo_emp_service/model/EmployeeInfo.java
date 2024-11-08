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
public class EmployeeInfo {

    private String empId;
    private String firstName;
    private  String lastName;
    private List<EmployeeSkillInfo> employeeSkills;
}
