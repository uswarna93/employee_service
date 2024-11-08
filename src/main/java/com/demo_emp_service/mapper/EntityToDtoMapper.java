package com.demo_emp_service.mapper;

import com.demo_emp_service.entity.EmployeeEntity;
import com.demo_emp_service.entity.EmployeeSkillEntity;
import com.demo_emp_service.model.EmployeeDto;
import com.demo_emp_service.model.EmployeeSkillDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EntityToDtoMapper {
    private static final Logger logger = LoggerFactory.getLogger(EntityToDtoMapper.class);
    public EmployeeDto convertEmpEntityToEmpDto(EmployeeEntity employeeEntity) {
        logger.info("Begin Converting EmployeeEntity to EmployeeDto");
        EmployeeDto employeeDto=new EmployeeDto();
        employeeDto.setEmpId(employeeEntity.getEmpId());
        employeeDto.setFirstName(employeeEntity.getFirstName());
        employeeDto.setLastName(employeeEntity.getLastName());
        return employeeDto;
    }
    public EmployeeSkillDto convertSkillEntityToSkillDto(EmployeeSkillEntity employeeSkillEntity) {
        logger.info("Begin Converting EmployeeSkillEntity to EmployeeSkillDto");
        EmployeeSkillDto employeeSkillDto=new EmployeeSkillDto();
        employeeSkillDto.setSkillId(employeeSkillEntity.getSkillId());
        employeeSkillDto.setSkillLevel(employeeSkillEntity.getSkillLevel());
        employeeSkillDto.setSkillName(employeeSkillEntity.getSkillName());
        return employeeSkillDto;
    }


}
