package com.demo_emp_service.mapper;

import com.demo_emp_service.entity.EmployeeEntity;
import com.demo_emp_service.entity.EmployeeSkillEntity;
import com.demo_emp_service.model.EmployeeDTO;
import com.demo_emp_service.model.EmployeeSkillDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EntityToDtoMapper {
    private static final Logger logger = LoggerFactory.getLogger(EntityToDtoMapper.class);
    public EmployeeDTO convertEmpEntityToEmpDto(EmployeeEntity employeeEntity) {
        logger.info("Begin Converting EmployeeEntity to EmployeeDto");
        EmployeeDTO employeeDto=new EmployeeDTO();
        employeeDto.setEmpId(employeeEntity.getEmpId());
        employeeDto.setFirstName(employeeEntity.getFirstName());
        employeeDto.setLastName(employeeEntity.getLastName());
        return employeeDto;
    }
    public EmployeeSkillDTO convertSkillEntityToSkillDto(EmployeeSkillEntity employeeSkillEntity) {
        logger.info("Begin Converting EmployeeSkillEntity to EmployeeSkillDto");
        EmployeeSkillDTO employeeSkillDto=new EmployeeSkillDTO();
        employeeSkillDto.setSkillId(employeeSkillEntity.getSkillId());
        employeeSkillDto.setSkillLevel(employeeSkillEntity.getSkillLevel());
        employeeSkillDto.setSkillName(employeeSkillEntity.getSkillName());
        return employeeSkillDto;
    }


}
