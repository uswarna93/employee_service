package com.demo_emp_service.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDetailsResponseDTO {
private EmployeeDTO empDetails;
private List<EmployeeSkillDTO> skillSet;
}
