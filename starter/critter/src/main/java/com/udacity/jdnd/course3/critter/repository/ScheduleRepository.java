package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {
}