package com.demo_emp_service.mapper;
import com.demo_emp_service.entity.EmployeeEntity;
import com.demo_emp_service.entity.EmployeeSkillEntity;
import com.demo_emp_service.model.EmployeeDTO;
import com.demo_emp_service.model.EmployeeSkillDTO;
import com.demo_emp_service.model.EmployeeSkillInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DtoToEntityMapper {
    private static final Logger logger = LoggerFactory.getLogger(DtoToEntityMapper.class);

    public EmployeeSkillEntity convertDtoToEmployeeSkillEntity(EmployeeSkillInfoDTO employeeSkillInfo) {
        logger.info("Begin Converting EmployeeSkillDto to EmployeeSkillEntity");
        EmployeeSkillEntity employeeSkillEntity = new EmployeeSkillEntity();
        employeeSkillEntity.setSkillId(employeeSkillInfo.getSkillId());
        employeeSkillEntity.setSkillLevel(employeeSkillInfo.getSkillLevel());
        employeeSkillEntity.setSkillName(employeeSkillInfo.getSkillName());
        employeeSkillEntity.setEmpId(employeeSkillInfo.getEmpId());
        return employeeSkillEntity;
    }

    public EmployeeEntity convertDtoToEmployeeEntity(EmployeeDTO employeeDto) {
        logger.info("Begin Converting EmployeeDto to EmployeeEntity");
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setEmpId(employeeDto.getEmpId());
        employeeEntity.setFirstName(employeeDto.getFirstName());
        employeeEntity.setLastName(employeeDto.getLastName());
        employeeEntity.setDepartment(employeeDto.getDepartment());
        return employeeEntity;
    }

    public EmployeeSkillEntity convertSkillPayLoadDtoToEmployeeSkillEntity(EmployeeSkillDTO employeeSkillDto, String empId) {
        logger.info("Begin Converting EmployeeSkillDto to EmployeeSkillEntity");
        EmployeeSkillEntity employeeSkillEntity = new EmployeeSkillEntity();
        employeeSkillEntity.setSkillId(employeeSkillDto.getSkillId());
        employeeSkillEntity.setSkillLevel(employeeSkillDto.getSkillLevel());
        employeeSkillEntity.setSkillName(employeeSkillDto.getSkillName());
       employeeSkillEntity.setEmpId(empId);
        return employeeSkillEntity;
    }
}
