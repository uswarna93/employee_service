package com.demo_emp_service.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class EmployeePayLoadDTO {
    private String empId;
    private String firstName;
    private  String lastName;
    private List<EmployeeSkillDTO> skills;
}
