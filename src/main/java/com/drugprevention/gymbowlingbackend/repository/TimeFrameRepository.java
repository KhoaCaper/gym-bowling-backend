package com.drugprevention.gymbowlingbackend.repository;

import com.drugprevention.gymbowlingbackend.entity.TimeFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeFrameRepository extends JpaRepository<TimeFrame, Long> {
    List<TimeFrame> findByCenterIdAndIsAvailableTrue(Long centerId);
    List<TimeFrame> findByCenterIdAndDayOfWeekAndIsAvailableTrue(Long centerId, com.drugprevention.gymbowlingbackend.entity.TimeFrame.DayOfWeek dayOfWeek);
    List<TimeFrame> findByCenterIdAndIsAvailableTrueOrderByStartTimeAsc(Long centerId);
    List<TimeFrame> findByIsAvailableTrue();
}
