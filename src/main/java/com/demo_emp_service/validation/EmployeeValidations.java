package com.demo_emp_service.validation;

import com.demo_emp_service.entity.EmployeeEntity;
import com.demo_emp_service.entity.EmployeeSkillEntity;
import com.demo_emp_service.repository.EmployeeRepository;
import com.demo_emp_service.repository.EmployeeSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidations {
    private final EmployeeSkillRepository employeeSkillRepository;
     private final EmployeeRepository employeeRepository;

    public EmployeeValidations(EmployeeSkillRepository employeeSkillRepository, EmployeeRepository employeeRepository) {
        this.employeeSkillRepository = employeeSkillRepository;
        this.employeeRepository = employeeRepository;
    }


    public boolean isSkillExists(EmployeeSkillEntity employeeSkillEntity) {
        boolean result= employeeSkillRepository.
                existsByEmpIdAndSkillIdAndSkillNameIgnoreCaseAndSkillLevelIgnoreCase
                        (employeeSkillEntity.getEmpId(), employeeSkillEntity.getSkillId(),
                                employeeSkillEntity.getSkillName(), employeeSkillEntity.getSkillLevel());
        return result;
    }
    public boolean isSkillNameExists(EmployeeSkillEntity employeeSkillEntity) {
        return employeeSkillRepository
                .existsByEmpIdAndSkillNameIgnoreCase(
                        employeeSkillEntity.getEmpId(),
                        employeeSkillEntity.getSkillName());
    }
    public boolean isEmployeeExists(EmployeeEntity employeeEntity) {
        boolean result = employeeRepository.
                existsByEmpIdAndFirstNameIgnoreCaseAndLastNameIgnoreCase
                        (employeeEntity.getEmpId(), employeeEntity.getFirstName(), employeeEntity.getLastName());

        return result;
    }

}

