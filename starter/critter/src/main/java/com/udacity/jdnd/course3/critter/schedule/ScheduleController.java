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

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    // defined services
    private final EmployeeService employeeService;
    private final ScheduleService scheduleService;
    private final UserService userService;
    private final PetService petService;

    // constructor
    public ScheduleController(EmployeeService employeeService, ScheduleService scheduleService, UserService userService, PetService petService) {
        this.employeeService = employeeService;
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.petService = petService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        // get scheduleId
        Long scheduleDTOId = scheduleDTO.getId();
        Optional<Schedule> scheduleOptional = scheduleService.findById(scheduleDTOId);
        Schedule schedule = scheduleOptional.orElseGet(Schedule::new);

        // if not 0 then set it
        if (scheduleDTOId != 0) {
            schedule.setId(scheduleDTOId);
        }

        // set other values
        schedule.setActivities(scheduleDTO.getActivities());
        schedule.setDate(scheduleDTO.getDate());

        // get and set employee
        List<Employee> employees = scheduleDTO.getEmployeeIds().stream().map(employeeId -> {
            Optional<Employee> employeeOptional = employeeService.findById(employeeId);

            return employeeOptional.orElseGet(Employee::new);
        }).collect(Collectors.toList());

        schedule.setEmployees(employees);

        // get and set pet
        List<Pet> pets = scheduleDTO.getPetIds().stream().map(petId -> {
            Optional<Pet> petOptional = petService.findById(petId);
            return petOptional.orElseGet(Pet::new);
        }).collect(Collectors.toList());

        schedule.setPets(pets);
        scheduleService.save(schedule);
        scheduleDTO.setId(schedule.getId());

        // return the saved
        return scheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        Iterable<Schedule> scheduleIterable = scheduleService.getAllSchedules();
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

        // create list and send back
        for (Schedule schedule : scheduleIterable) {
            ScheduleDTO scheduleDTO = getScheduleDTO(schedule);

            scheduleDTOS.add(scheduleDTO);
        }

        return scheduleDTOS;
    }

    private ScheduleDTO getScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        // get and set
        scheduleDTO.setActivities(schedule.getActivities());
        scheduleDTO.setDate(schedule.getDate());

        // get and set employee
        List<Long> employeeIds = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        scheduleDTO.setEmployeeIds(employeeIds);

        // get-set pet
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
        Set<ScheduleDTO> scheduleDTO = new HashSet<>();

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            for (Pet pet : customer.getPets()) {
                List<ScheduleDTO> scheduleDTOList = getScheduleForPet(pet.getId());
                scheduleDTO.addAll(scheduleDTOList);
            }
        }

        return new ArrayList<>(scheduleDTO);
    }
}