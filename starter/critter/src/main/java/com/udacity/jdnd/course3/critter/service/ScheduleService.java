package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    // scheduleService constructor
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // find schedule by id
    public Optional<Schedule> findById(Long id) {
        return scheduleRepository.findById(id);
    }

    // save schedule
    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    // get all schedules
    public Iterable<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // get employee's schedule
    public List<Schedule> getScheduleForEmployee(long employeeId) {
        // get all schedules
        Iterable<Schedule> scheduleIterable = scheduleRepository.findAll();
        List<Schedule> schedules = new ArrayList<>();

        // get employees based on schedule
        for (Schedule schedule : scheduleIterable) {
            List<Employee> employees = schedule.getEmployees();

            // loop over employees
            for (Employee employee : employees) {
                // filter employee
                if (employee.getId() == employeeId) {
                    schedules.add(schedule);
                    break;
                }
            }
        }

        return schedules;
    }

    // Get Pets schedule
    public List<Schedule> getScheduleForPet(long petId) {
        Iterable<Schedule> scheduleIterable = scheduleRepository.findAll();
        List<Schedule> schedules = new ArrayList<>();

        // get pets based on schedule
        for (Schedule schedule : scheduleIterable) {
            // list of pets
            List<Pet> pets = schedule.getPets();

            // loop over pets
            for (Pet pet : pets) {
                // filter pet
                if (pet.getId() == petId) {
                    schedules.add(schedule);

                    break;
                }
            }
        }

        return schedules;
    }
}