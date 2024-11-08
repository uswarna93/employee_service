package com.demo_emp_service.mapper;
import com.demo_emp_service.entity.EmployeeEntity;
import com.demo_emp_service.entity.EmployeeSkillEntity;
import com.demo_emp_service.model.EmployeeDto;
import com.demo_emp_service.model.EmployeeSkillDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DtoToEntityMapper {
    private static final Logger logger = LoggerFactory.getLogger(DtoToEntityMapper.class);

    public EmployeeSkillEntity convertDtoToEmployeeSkillEntity(EmployeeSkillDto employeeSkillDto) {
        logger.info("Begin Converting EmployeeSkillDto to EmployeeSkillEntity");
        EmployeeSkillEntity employeeSkillEntity = new EmployeeSkillEntity();
        employeeSkillEntity.setSkillId(employeeSkillDto.getSkillId());
        employeeSkillEntity.setSkillLevel(employeeSkillDto.getSkillLevel());
        employeeSkillEntity.setSkillName(employeeSkillDto.getSkillName());
        return employeeSkillEntity;
    }

    public EmployeeEntity convertDtoToEmployeeEntity(EmployeeDto employeeDto) {
        logger.info("Begin Converting EmployeeDto to EmployeeEntity");
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setEmpId(employeeDto.getEmpId());
        employeeEntity.setFirstName(employeeDto.getFirstName());
        employeeEntity.setLastName(employeeDto.getLastName());
        return employeeEntity;
    }

    public EmployeeSkillEntity convertSkillPayLoadDtoToEmployeeSkillEntity(EmployeeSkillDto employeeSkillDto,String empId) {
        logger.info("Begin Converting EmployeeSkillDto to EmployeeSkillEntity");
        EmployeeSkillEntity employeeSkillEntity = new EmployeeSkillEntity();
        employeeSkillEntity.setSkillId(employeeSkillDto.getSkillId());
        employeeSkillEntity.setSkillLevel(employeeSkillDto.getSkillLevel());
        employeeSkillEntity.setSkillName(employeeSkillDto.getSkillName());
       employeeSkillEntity.setEmpId(empId);
        return employeeSkillEntity;
    }
}
