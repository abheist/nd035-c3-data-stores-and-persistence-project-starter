package com.udacity.jdnd.course3.critter.user;

import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<EmployeeSkill> skills;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> daysAvailable;

    public Employee(String name, List<EmployeeSkill> skills, List<DayOfWeek> daysAvailable) {
        this.name = name;
        this.skills = skills;
        this.daysAvailable = daysAvailable;
    }

}