package com.demo_emp_service.service;

import com.demo_emp_service.model.EmployeeDetailsResponseDto;
import com.demo_emp_service.model.EmployeeDto;
import com.demo_emp_service.model.EmployeeSkillDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeDetailsService {

    String sendMessage(String msg);
    void saveEmployees(EmployeeDto employeeDto) throws Exception;
    void saveSkills(EmployeeSkillDto employeeSkillDto) throws Exception;
    EmployeeDetailsResponseDto getEmployeeDetailsByEmpId(String empId);
}
