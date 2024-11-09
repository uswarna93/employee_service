package com.demo_emp_service.service;

import com.demo_emp_service.entity.EmployeeEntity;
import com.demo_emp_service.entity.EmployeeSkillEntity;
import com.demo_emp_service.exceptions.EmployeeAlreadyExistsException;
import com.demo_emp_service.exceptions.EmployeeNotFoundException;
import com.demo_emp_service.exceptions.EmployeeSkillAlreadyExistsException;
import com.demo_emp_service.exceptions.SkillAlreadyExistsException;
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
    public EmployeePayLoadDTO consumeMessage(String payLoad) {
        logger.info("payLoad {} Received in EmployeeGroup from EmployeeInfoTopic ", payLoad);
        EmployeePayLoadDTO employeePayLoad = new EmployeePayLoadDTO();
        try {
            if (!StringUtils.isEmpty(payLoad)) {
                employeePayLoad = processMessage(payLoad);
                if (isEmployeePayLoadValid(employeePayLoad)) {
                    logger.info("isEmployeePayLoadValid(employeePayLoad) is true");
                    EmployeeEntity employeeEntity = payLoadToEmployeeEntityMapper(employeePayLoad);
                    employeeRepository.save(employeeEntity);
                    logger.info("EmployeeEntity saved");
                    List<EmployeeSkillDTO> employeeSkillDtoList = employeePayLoad.getSkills();
                    logger.info("employeeSkillDtoList {}",employeeSkillDtoList);
                    String empId = employeeEntity.getEmpId();
                     logger.info("emoId {}",empId);
                    for (EmployeeSkillDTO employeeSkillDto:employeeSkillDtoList)
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

    private EmployeeEntity payLoadToEmployeeEntityMapper(EmployeePayLoadDTO employeePayLoad) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        logger.info("Begin converting PayLoad to Employee");
        employeeEntity.setEmpId(employeePayLoad.getEmpId());
        employeeEntity.setFirstName(employeePayLoad.getFirstName());
        employeeEntity.setLastName(employeePayLoad.getLastName());
        return employeeEntity;
    }
    private boolean isEmployeePayLoadValid(EmployeePayLoadDTO employeePayLoad) {
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
    private EmployeePayLoadDTO processMessage(String message) throws JsonProcessingException {
        logger.info("Begin processMessage");
        EmployeePayLoadDTO employeePayLoad = objectMapper.readValue(message, EmployeePayLoadDTO.class);
        logger.info(employeePayLoad+"empPayLoad");
        return employeePayLoad;
    }

    public void saveEmployees(EmployeeDTO employeeDto) throws RuntimeException {
        logger.info("Begin saveEmp() {} :", employeeDto);
        try {
            EmployeeEntity employeeEntity = dtoToEntityMapper
                    .convertDtoToEmployeeEntity(employeeDto);
            if (!isEmployeeExists(employeeEntity)){
                employeeRepository.save(employeeEntity);
            }else {
                throw new EmployeeAlreadyExistsException
                        ("Employee Already exists with empId "+employeeDto.getEmpId());
            }
        }catch (EmployeeAlreadyExistsException e){
            throw new EmployeeAlreadyExistsException(e.getMessage());
        }
    }

    private boolean isEmployeeExists(EmployeeEntity employeeEntity) {
        boolean result = employeeRepository.
                existsByEmpIdAndFirstNameIgnoreCaseAndLastNameIgnoreCase
                        (employeeEntity.getEmpId(), employeeEntity.getFirstName(), employeeEntity.getLastName());

        return result;
    }

    public void saveSkills(EmployeeSkillInfoDTO employeeSkillInfo) throws RuntimeException {
        logger.info("Begin saving Skills: {}", employeeSkillInfo);
        try {
            EmployeeSkillEntity employeeSkillEntity = dtoToEntityMapper
                    .convertDtoToEmployeeSkillEntity(employeeSkillInfo);
            if(employeeRepository.existsById(employeeSkillEntity.getEmpId())) {
                if (!isSkillExists(employeeSkillEntity)) {
                    if(!isSkillNameExists(employeeSkillEntity)){
                        employeeSkillRepository.save(employeeSkillEntity);
                    }else {throw new SkillAlreadyExistsException(
                            "skillName already exists for empId: "+employeeSkillEntity.getEmpId());}
                }else{throw new EmployeeSkillAlreadyExistsException("Employee Skill already exists");}
            }else {throw new EmployeeNotFoundException
                        ("No Employee Found with empId "+employeeSkillEntity.getEmpId());}
            }catch (EmployeeSkillAlreadyExistsException e){
            throw new EmployeeSkillAlreadyExistsException(e.getMessage());
        }catch (EmployeeNotFoundException e){
            throw new EmployeeNotFoundException(e.getMessage());
        }catch (SkillAlreadyExistsException e){
            throw new SkillAlreadyExistsException(e.getMessage());
        }
    }

    private boolean isSkillNameExists(EmployeeSkillEntity employeeSkillEntity) {
        return employeeSkillRepository
                .existsByEmpIdAndSkillNameIgnoreCase(
                        employeeSkillEntity.getEmpId(),
                        employeeSkillEntity.getSkillName());
    }

    private boolean isSkillExists(EmployeeSkillEntity employeeSkillEntity) {
        boolean result= employeeSkillRepository.
                existsByEmpIdAndSkillIdAndSkillNameIgnoreCaseAndSkillLevelIgnoreCase
                        (employeeSkillEntity.getEmpId(), employeeSkillEntity.getSkillId(),
                                employeeSkillEntity.getSkillName(), employeeSkillEntity.getSkillLevel());
        return result;
    }

    public EmployeeDetailsResponseDTO getEmployeeDetailsByEmpId(String empId) {
        logger.info("Begin getting Employee with empId: {}", empId);
        EmployeeDetailsResponseDTO employeeDetailsResponseDto = new EmployeeDetailsResponseDTO();
        List<EmployeeSkillEntity> employeeSkillEntityList;
        List<EmployeeSkillDTO> employeeSkillDtoList = new ArrayList<>();
        try {
            Optional<EmployeeEntity> employeeEntityOptional = employeeRepository.findById(empId);
            if (employeeEntityOptional.isPresent()) {
                EmployeeEntity employeeEntity = employeeEntityOptional.get();
                // Convert into DTO
                EmployeeDTO employeeDto = entityToDtoMapper.convertEmpEntityToEmpDto(employeeEntity);
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
