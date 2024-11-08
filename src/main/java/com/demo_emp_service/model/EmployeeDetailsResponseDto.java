package com.demo_emp_service.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeDetailsResponseDto {
private EmployeeDto empDetails;
private List<EmployeeSkillDto> skillSet;
}
