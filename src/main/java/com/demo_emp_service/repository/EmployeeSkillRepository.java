package com.demo_emp_service.repository;

import com.demo_emp_service.entity.EmployeeId;
import com.demo_emp_service.entity.EmployeeSkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkillEntity, EmployeeId> {
    List<EmployeeSkillEntity> getEmployeeSkillEntitiesByEmpId(String empId);

    boolean existsByEmpIdAndSkillIdAndSkillNameIgnoreCaseAndSkillLevelIgnoreCase
            (String empId, String skillId, String skillName, String skillLevel);
}
