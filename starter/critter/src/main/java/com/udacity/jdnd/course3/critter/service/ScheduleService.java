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

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public Optional<Schedule> findById(Long id) {
        return scheduleRepository.findById(id);
    }

    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public Iterable<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> getScheduleForEmployee(long employeeId) {
        Iterable<Schedule> scheduleIterable = scheduleRepository.findAll();
        List<Schedule> schedules = new ArrayList<>();
        for (Schedule schedule: scheduleIterable) {
            List<Employee> employees = schedule.getEmployees();
            for (Employee employee: employees) {
                if (employee.getId() == employeeId) {
                    schedules.add(schedule);

                    break;
                }
            }
        }

        return schedules;
    }

    public List<Schedule> getScheduleForPet(long petId) {
        Iterable<Schedule> scheduleIterable = scheduleRepository.findAll();
        List<Schedule> schedules = new ArrayList<>();
        for (Schedule schedule: scheduleIterable) {
            List<Pet> pets = schedule.getPets();
            for (Pet pet: pets) {
                if (pet.getId() == petId) {
                    schedules.add(schedule);

                    break;
                }
            }
        }

        return schedules;
    }
}