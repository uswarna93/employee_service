package com.demo_emp_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "employees_details", schema = "tenni")
public class EmployeeEntity implements Serializable {

    @Id
    @Column(name = "employee_id")
    private String empId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

}
