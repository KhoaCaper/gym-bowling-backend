package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.dto.TimeFrameDTO;
import com.drugprevention.gymbowlingbackend.entity.TimeFrame;
import com.drugprevention.gymbowlingbackend.entity.Center;
import com.drugprevention.gymbowlingbackend.repository.TimeFrameRepository;
import com.drugprevention.gymbowlingbackend.repository.CenterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeFrameService {

    private final TimeFrameRepository timeFrameRepository;
    private final CenterRepository centerRepository;

    public TimeFrameService(TimeFrameRepository timeFrameRepository, CenterRepository centerRepository) {
        this.timeFrameRepository = timeFrameRepository;
        this.centerRepository = centerRepository;
    }

    public List<TimeFrameDTO> getAllAvailableTimeFrames() {
        return timeFrameRepository.findByIsAvailableTrue()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TimeFrameDTO> getTimeFramesByCenter(Long centerId) {
        return timeFrameRepository.findByCenterIdAndIsAvailableTrueOrderByStartTimeAsc(centerId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TimeFrameDTO> getTimeFramesByCenterAndDay(Long centerId, String dayOfWeek) {
        try {
            com.drugprevention.gymbowlingbackend.entity.TimeFrame.DayOfWeek day = 
                com.drugprevention.gymbowlingbackend.entity.TimeFrame.DayOfWeek.valueOf(dayOfWeek.toUpperCase());
            return timeFrameRepository.findByCenterIdAndDayOfWeekAndIsAvailableTrue(centerId, day)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid day of week: " + dayOfWeek);
        }
    }

    public Optional<TimeFrameDTO> getTimeFrameById(Long id) {
        return timeFrameRepository.findById(id)
            .map(this::convertToDTO);
    }

    public TimeFrameDTO createTimeFrame(Long centerId, String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        // Validate center exists
        Center center = centerRepository.findById(centerId)
            .orElseThrow(() -> new RuntimeException("Center not found with id: " + centerId));

        // Validate day of week
        com.drugprevention.gymbowlingbackend.entity.TimeFrame.DayOfWeek day;
        try {
            day = com.drugprevention.gymbowlingbackend.entity.TimeFrame.DayOfWeek.valueOf(dayOfWeek.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid day of week: " + dayOfWeek);
        }

        // Validate time range
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("Start time cannot be after end time");
        }

        TimeFrame timeFrame = new TimeFrame(center, startTime, endTime, day);
        TimeFrame savedTimeFrame = timeFrameRepository.save(timeFrame);
        return convertToDTO(savedTimeFrame);
    }

    public TimeFrameDTO updateTimeFrame(Long id, String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        return timeFrameRepository.findById(id)
            .map(existingTimeFrame -> {
                // Validate day of week
                com.drugprevention.gymbowlingbackend.entity.TimeFrame.DayOfWeek day;
                try {
                    day = com.drugprevention.gymbowlingbackend.entity.TimeFrame.DayOfWeek.valueOf(dayOfWeek.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid day of week: " + dayOfWeek);
                }

                // Validate time range
                if (startTime.isAfter(endTime)) {
                    throw new RuntimeException("Start time cannot be after end time");
                }

                existingTimeFrame.setDayOfWeek(day);
                existingTimeFrame.setStartTime(startTime);
                existingTimeFrame.setEndTime(endTime);

                TimeFrame savedTimeFrame = timeFrameRepository.save(existingTimeFrame);
                return convertToDTO(savedTimeFrame);
            })
            .orElseThrow(() -> new RuntimeException("Time frame not found with id: " + id));
    }

    public void deleteTimeFrame(Long id) {
        if (timeFrameRepository.existsById(id)) {
            timeFrameRepository.deleteById(id);
        } else {
            throw new RuntimeException("Time frame not found with id: " + id);
        }
    }

    public TimeFrameDTO toggleTimeFrameAvailability(Long id) {
        return timeFrameRepository.findById(id)
            .map(timeFrame -> {
                timeFrame.setIsAvailable(!timeFrame.getIsAvailable());
                TimeFrame savedTimeFrame = timeFrameRepository.save(timeFrame);
                return convertToDTO(savedTimeFrame);
            })
            .orElseThrow(() -> new RuntimeException("Time frame not found with id: " + id));
    }

    private TimeFrameDTO convertToDTO(TimeFrame timeFrame) {
        return new TimeFrameDTO(
            timeFrame.getId(),
            timeFrame.getCenter().getId(),
            timeFrame.getCenter().getName(),
            timeFrame.getStartTime(),
            timeFrame.getEndTime(),
            timeFrame.getDayOfWeek().toString(),
            timeFrame.getIsAvailable(),
            timeFrame.getCreatedAt()
        );
    }
}
