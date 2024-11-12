package com.demo_emp_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@IdClass(EmployeeSkillId.class)
@Table(name = "skill_details" , schema = "tenni")
public class EmployeeSkillEntity implements Serializable {

    @Id
    @Column(name = "skill_id")
    private String skillId;

    @Column(name = "skill_name")
    private String skillName;

    @Column(name = "skill_level")
    private String skillLevel;

    @Id
    @Column(name = "employee_id")
    private String empId ;
}
