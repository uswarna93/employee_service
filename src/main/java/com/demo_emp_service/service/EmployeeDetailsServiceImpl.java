package com.demo_emp_service.service;

import com.demo_emp_service.entity.EmployeeEntity;
import com.demo_emp_service.entity.EmployeeSkillEntity;
import com.demo_emp_service.mapper.DtoToEntityMapper;
import com.demo_emp_service.mapper.EntityToDtoMapper;
import com.demo_emp_service.model.*;
import com.demo_emp_service.repository.EmployeeRepository;
import com.demo_emp_service.repository.EmployeeSkillRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class EmployeeDetailsServiceImpl implements EmployeeDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDetailsServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EntityToDtoMapper entityToDtoMapper;
    private final DtoToEntityMapper dtoToEntityMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    EmployeeDetailsServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeSkillRepository employeeSkillRepository,
                               KafkaTemplate<String, String> kafkaTemplate,
                               EntityToDtoMapper entityToDtoMapper, DtoToEntityMapper dtoToEntityMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeSkillRepository = employeeSkillRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.entityToDtoMapper = entityToDtoMapper;
        this.dtoToEntityMapper = dtoToEntityMapper;
    }

    public String sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("EmployeeInfoTopic", message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
            }
        });
        return message;
    }

    @KafkaListener(topics = "EmployeeInfoTopic", groupId = "EmployeeGroup")
    public EmployeePayLoadDto consumeMessage(String payLoad) {
        logger.info("payLoad {} Received in EmployeeGroup from EmployeeInfoTopic ", payLoad);
        EmployeePayLoadDto employeePayLoad = new EmployeePayLoadDto();
        try {
            if (!StringUtils.isEmpty(payLoad)) {
                employeePayLoad = processMessage(payLoad);
                if (isEmployeePayLoadValid(employeePayLoad)) {
                    logger.info("isEmployeePayLoadValid(employeePayLoad) is true");
                    EmployeeEntity employeeEntity = payLoadToEmployeeEntityMapper(employeePayLoad);
                    employeeRepository.save(employeeEntity);
                    logger.info("EmployeeEntity saved");
                    List<EmployeeSkillDto> employeeSkillDtoList = employeePayLoad.getSkills();
                    logger.info("employeeSkillDtoList {}",employeeSkillDtoList);
                    String empId = employeeEntity.getEmpId();
                     logger.info("emoId {}",empId);
                    for (EmployeeSkillDto employeeSkillDto:employeeSkillDtoList)
                    {
                        EmployeeSkillEntity employeeSkillEntity = dtoToEntityMapper
                                .convertSkillPayLoadDtoToEmployeeSkillEntity(employeeSkillDto,empId);
                        employeeSkillRepository.save(employeeSkillEntity);
                        logger.info("EmployeeSkillEntity saved");
                    }
                }
            } else {
                logger.warn("Empty PayLoad Received");
            }
        } catch (Exception exception) {
            logger.error("InValid EmployeePay Load received ");
        }
        return employeePayLoad;
    }

    private EmployeeEntity payLoadToEmployeeEntityMapper(EmployeePayLoadDto employeePayLoad) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        logger.info("Begin converting PayLoad to Employee");
        employeeEntity.setEmpId(employeePayLoad.getEmpId());
        employeeEntity.setFirstName(employeePayLoad.getFirstName());
        employeeEntity.setLastName(employeePayLoad.getLastName());
        return employeeEntity;
    }
    private boolean isEmployeePayLoadValid(EmployeePayLoadDto employeePayLoad) {
        logger.error("Begin checking isEmployeePayLoadValid ");
        try {
            if (!StringUtils.isEmpty(employeePayLoad.getEmpId())) {

                return true;
            }
        } catch (Exception e) {
            logger.error("employeePayLoad is in valid ");
        }
        return false;
    }
    private EmployeePayLoadDto processMessage(String message) throws JsonProcessingException {
        logger.info("Begin processMessage");
        EmployeePayLoadDto employeePayLoad = objectMapper.readValue(message, EmployeePayLoadDto.class);
        logger.info(employeePayLoad+"empPayLoad");
        return employeePayLoad;
    }

    public void saveEmployees(EmployeeDto employeeDto) {
        logger.info("Begin saveEmp() {} :", employeeDto);
        EmployeeEntity employeeEntity = dtoToEntityMapper
                .convertDtoToEmployeeEntity(employeeDto);
        employeeRepository.save(employeeEntity);
    }

    public void saveSkills(EmployeeSkillDto employeeSkill) {
        logger.info("Begin saving Skills: {}", employeeSkill);
        EmployeeSkillEntity employeeSkillEntity = dtoToEntityMapper
                .convertDtoToEmployeeSkillEntity(employeeSkill);
        employeeSkillRepository.save(employeeSkillEntity);
    }

    public EmployeeDetailsResponseDto getEmployeeDetailsByEmpId(String empId) {
        logger.info("Begin getting Employee with empId: {}", empId);
        EmployeeDetailsResponseDto employeeDetailsResponseDto = new EmployeeDetailsResponseDto();
        List<EmployeeSkillEntity> employeeSkillEntityList;
        List<EmployeeSkillDto> employeeSkillDtoList = new ArrayList<>();
        try {
            Optional<EmployeeEntity> employeeEntityOptional = employeeRepository.findById(empId);
            if (employeeEntityOptional.isPresent()) {
                EmployeeEntity employeeEntity = employeeEntityOptional.get();
                // Convert into DTO
                EmployeeDto employeeDto = entityToDtoMapper.convertEmpEntityToEmpDto(employeeEntity);
                employeeDetailsResponseDto.setEmpDetails(employeeDto);
                employeeSkillEntityList = employeeSkillRepository
                        .getEmployeeSkillEntitiesByEmpId(employeeDto.getEmpId());
                employeeSkillEntityList.forEach(e -> employeeSkillDtoList
                        .add(entityToDtoMapper.convertSkillEntityToSkillDto(e)));
                employeeDetailsResponseDto.setSkillSet(employeeSkillDtoList);
            }
        } catch (Exception e) {
            logger.error("No employee details present with empId : {}", empId);
        }
        return employeeDetailsResponseDto;

    }
}
