package com.demo_emp_service.service;

import com.demo_emp_service.model.EmployeeDetailsResponseDTO;
import com.demo_emp_service.model.EmployeeDTO;
import com.demo_emp_service.model.EmployeeSkillInfoDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeDetailsService {

    String sendMessage(String msg);
    void saveEmployees(EmployeeDTO employeeDto) throws Exception;
    void saveSkills(@Valid EmployeeSkillInfoDTO employeeSkillInfo) throws Exception;
    EmployeeDetailsResponseDTO getEmployeeDetailsByEmpId(String empId);
}
