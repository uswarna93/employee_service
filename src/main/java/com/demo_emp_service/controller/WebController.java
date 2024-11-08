package com.demo_emp_service.controller;

import com.demo_emp_service.model.EmployeeDetailsResponseDto;
import com.demo_emp_service.model.EmployeeDto;
import com.demo_emp_service.model.EmployeeSkillDto;
import com.demo_emp_service.service.EmployeeDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
           result = employeeDetailsService.sendMessage(msg);
       }catch (Exception exception){
           logger.error("Exception {} while Producing a message to topic",exception.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message was not sent to topic");
       }
       return ResponseEntity.status(HttpStatus.OK).body("Message Saved to data base \n"+result);
    }
    @Operation(summary = "Save Employees", description = "saves Employees into Data Base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved to database"),
            @ApiResponse(responseCode = "500", description = "Internal server Exception - Unable to save Employee")
    })
    @PostMapping("/saveEmployees")
    public ResponseEntity<?> addEmployees(@Valid @RequestBody EmployeeDto employeeDto){
        logger.info("Begin saving employee: {} ",employeeDto);
        try {
            employeeDetailsService.saveEmployees(employeeDto);
        } catch (Exception e){
            logger.error("Exception {} while saving employee:",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("employee was not saved:");
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("employee saved");
    }
    @Operation(summary = "save Employee Skills ", description = "It saves Employee skills into Data Base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved to database"),
            @ApiResponse(responseCode = "500", description = "Internal server Exception - Unable to save Employee Skills")
    })
    @PostMapping("/saveEmployeeSkills")
    public ResponseEntity<?> addEmployeeSkills(@Valid @RequestBody EmployeeSkillDto employeeSkillDto){
        logger.info("Begin saving EmployeeSkills: {}",employeeSkillDto);
        try {
            employeeDetailsService.saveSkills(employeeSkillDto);
        } catch (Exception e){
            logger.error("Exception {} while saving EmployeeSkills:",e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EmployeeSkills not saved:");
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("EmployeeSkills saved");
   }
    @Operation(summary = "Retrieves EmployeeDetails with EmpId", description = "It retrieves employee details from Data Base")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved from database"),
            @ApiResponse(responseCode = "404", description = "Not found - Employee Details not found with empId")
    })
    @GetMapping("/getEmployeeDetailsByEmpId/{empId}")
    public ResponseEntity<?> getEmployeeDetailsByEmpId(@Valid @PathVariable String empId){
        logger.info("Getting EmployeeInfo with empId {}",empId);
        EmployeeDetailsResponseDto employeeDetailsResponseDto;
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
