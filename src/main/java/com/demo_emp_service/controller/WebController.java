package com.demo_emp_service.controller;

import com.demo_emp_service.exceptions.*;
import com.demo_emp_service.model.EmployeeDetailsResponseDTO;
import com.demo_emp_service.model.EmployeeDTO;
import com.demo_emp_service.model.EmployeePayLoadDTO;
import com.demo_emp_service.model.EmployeeSkillInfoDTO;
import com.demo_emp_service.service.EmployeeDetailsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class WebController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    private final EmployeeDetailsService employeeDetailsService;

   private WebController(EmployeeDetailsService employeeDetailsService){
        this.employeeDetailsService =employeeDetailsService;
    }

    @Operation(summary = "Produce Employee Details with Skill set", description = "It saves the message into Data Base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved to database"),
            @ApiResponse(responseCode = "500", description = "Internal server Exception - Unable to produce message")
    })
    @PostMapping("/produceMessage")
    public ResponseEntity<?> produceMessage(@Valid @RequestBody String msg){
        logger.info("Begin Producing a message to topic: {} ",msg);
        String result;
       try {
          if(isValidMessage(msg)) {
              result = employeeDetailsService.sendMessage(msg);
              return ResponseEntity.status(HttpStatus.OK)
                      .body("Message Saved to data base \n"+result);
          }else {
              return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                      .body("please enter valid details");
          }
       } catch (EmployeeNotFoundException | JsonProcessingException | NoSuchFieldException exception) {
           logger.error("Exception {}",exception.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
       }catch (Exception e){
           logger.error("Exception {} while Producing a message to topic",e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message was not sent to topic");
       }

    }

    private boolean isValidMessage(String msg) throws JsonProcessingException, NoSuchFieldException {
        ObjectMapper objectMapper=new ObjectMapper();
        EmployeePayLoadDTO employeePayLoadDTO = objectMapper
                .readValue(msg, EmployeePayLoadDTO.class);
                if (!StringUtils.isEmpty(employeePayLoadDTO.getEmpId())) {
                    if (StringUtils.isNotBlank(employeePayLoadDTO.getFirstName())) {
                        StringUtils.isNotBlank(employeePayLoadDTO.getDepartment());
                    }else {
                        throw new NoSuchFieldException("please Enter FirstName/Department");
                    }
                }else {
                    throw new EmployeeNotFoundException("Employee not found ,please Enter valid EmpId");
                }
                return true;
    }

    @Operation(summary = "Save Employees", description = "saves Employees into Data Base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved to database"),
            @ApiResponse(responseCode = "500", description = "Internal server Exception - Unable to save Employee")})
    @PostMapping("/saveEmployees")
    public ResponseEntity<?> addEmployees(@Valid @RequestBody EmployeeDTO employeeDto){
        logger.info("Begin saving employee: {} ",employeeDto);
        try {
             employeeDetailsService.saveEmployees(employeeDto);
        }catch (EmployeeAlreadyExistsException employeeAlreadyExistsException){
            logger.error("Employee already exists{} ",employeeAlreadyExistsException.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(employeeAlreadyExistsException.getMessage());
        }
        catch (Exception e){
            logger.error("Exception {} while saving employee:",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("employee was not saved:");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created "+employeeDto);
    }
    @Operation(summary = "save Employee Skills ", description = "It saves Employee skills into Data Base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved to database"),
            @ApiResponse(responseCode = "500", description = "Internal server Exception - Unable to save Employee Skills")})
    @PostMapping("/saveEmployeeSkills")
    public ResponseEntity<?> addEmployeeSkills(@Valid @RequestBody EmployeeSkillInfoDTO employeeSkillInfo){
        logger.info("Begin saving EmployeeSkills: {}",employeeSkillInfo);
        try {
            employeeDetailsService.saveSkills(employeeSkillInfo);
        }catch (EmployeeSkillAlreadyExistsException skillAlreadyExistsException) {
            logger.error("EmployeeSkills already exists: {}", skillAlreadyExistsException.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(skillAlreadyExistsException.getMessage());
        }catch (EmployeeNotFoundException notFoundException){
                logger.error("Employee Not found with EmpId "+notFoundException.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
        }catch (SkillAlreadyExistsException e){
            logger.error("Exception: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            logger.error("Exception: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EmployeeSkills not saved:"+e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("EmployeeSkills saved");
   }
    @Operation(summary = "Retrieves EmployeeDetails with EmpId", description = "It retrieves employee details from Data Base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved from database"),
            @ApiResponse(responseCode = "404", description = "Not found - Employee Details not found with empId")})
    @GetMapping("/getEmployeeDetailsByEmpId/{empId}")
    public ResponseEntity<?> getEmployeeDetailsByEmpId(@Valid @PathVariable String empId){
        logger.info("Getting EmployeeInfo with empId {}",empId);
        EmployeeDetailsResponseDTO employeeDetailsResponseDto;
        try {
            employeeDetailsResponseDto = employeeDetailsService
                    .getEmployeeDetailsByEmpId(empId);
            return ResponseEntity.ok(employeeDetailsResponseDto);
        } catch (Exception e){
            logger.error("Exception {} while saving EmployeeSkills:",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Employee info not found with empId: "+empId);
        }
    }
}
