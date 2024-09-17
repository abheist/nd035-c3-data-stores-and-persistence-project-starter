package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final EmployeeService employeeService;
    private final PetService petService;
    private final UserService userService;

    public ScheduleController(ScheduleService scheduleService, EmployeeService employeeService, PetService petService, UserService userService) {
        this.scheduleService = scheduleService;
        this.employeeService = employeeService;
        this.petService = petService;
        this.userService = userService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Long scheduleDTOId = scheduleDTO.getId();
        Optional<Schedule> scheduleOptional = scheduleService.findById(scheduleDTOId);
        Schedule schedule = scheduleOptional.orElseGet(Schedule::new);
        if (scheduleDTOId != 0) {
            schedule.setId(scheduleDTOId);
        }
        schedule.setActivities(scheduleDTO.getActivities());
        schedule.setDate(scheduleDTO.getDate());

        List<Employee> employees = scheduleDTO.getEmployeeIds().stream().map(employeeId -> {
            Optional<Employee> employeeOptional = employeeService.findById(employeeId);

            return employeeOptional.orElseGet(Employee::new);
        }).collect(Collectors.toList());
        schedule.setEmployees(employees);

        List<Pet> pets = scheduleDTO.getPetIds().stream().map(petId -> {
            Optional<Pet> petOptional = petService.findById(petId);

            return petOptional.orElseGet(Pet::new);
        }).collect(Collectors.toList());
        schedule.setPets(pets);
        scheduleService.save(schedule);
        scheduleDTO.setId(schedule.getId());

        return scheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        Iterable<Schedule> scheduleIterable = scheduleService.getAllSchedules();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        for (Schedule schedule: scheduleIterable) {
            ScheduleDTO scheduleDTO = getScheduleDTO(schedule);

            scheduleDTOS.add(scheduleDTO);
        }

        return scheduleDTOS;
    }

    private ScheduleDTO getScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setActivities(schedule.getActivities());
        scheduleDTO.setDate(schedule.getDate());
        List<Long> employeeIds = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        scheduleDTO.setEmployeeIds(employeeIds);
        List<Long> petIds = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        scheduleDTO.setPetIds(petIds);
        scheduleDTO.setId(schedule.getId());

        return scheduleDTO;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = scheduleService.getScheduleForPet(petId);

        return schedules.stream().map(this::getScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = scheduleService.getScheduleForEmployee(employeeId);

        return schedules.stream().map(this::getScheduleDTO).collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Optional<Customer> customerOptional = userService.getUser(customerId);
        Set<ScheduleDTO> scheduleDTOSet = new HashSet<>();
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            for (Pet pet: customer.getPets()) {
                List<ScheduleDTO> scheduleDTOList = getScheduleForPet(pet.getId());
                scheduleDTOSet.addAll(scheduleDTOList);
            }
        }

        return new ArrayList<>(scheduleDTOSet);
    }
}