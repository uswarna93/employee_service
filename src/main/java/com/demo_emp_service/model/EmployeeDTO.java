package com.demo_emp_service.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class EmployeeDTO {
    @NotNull(message = "employeeId cannot be null")
    private String empId;

    @NotNull(message = "firstName cannot be null")
    private String firstName;

//    @NotNull(message = "lastName cannot be null")
    private  String lastName;

    @NotNull(message = "department cannot be null")
    private String department;
}
