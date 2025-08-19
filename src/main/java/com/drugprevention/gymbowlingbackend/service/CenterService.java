package com.drugprevention.gymbowlingbackend.service;

import com.drugprevention.gymbowlingbackend.dto.CenterDTO;
import com.drugprevention.gymbowlingbackend.dto.CreateCenterDTO;
import com.drugprevention.gymbowlingbackend.entity.Center;
import com.drugprevention.gymbowlingbackend.repository.CenterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CenterService {

    private final CenterRepository centerRepository;

    public CenterService(CenterRepository centerRepository) {
        this.centerRepository = centerRepository;
    }

    public List<CenterDTO> getAllActiveCenters() {
        return centerRepository.findByIsActiveTrueOrderByNameAsc()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<CenterDTO> getAllCenters() {
        return centerRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public Optional<CenterDTO> getCenterById(Long id) {
        return centerRepository.findById(id)
            .map(this::convertToDTO);
    }

    public CenterDTO createCenter(CreateCenterDTO createCenterDTO) {
        // Check if center name already exists
        if (centerRepository.existsByName(createCenterDTO.getName())) {
            throw new RuntimeException("Center with name '" + createCenterDTO.getName() + "' already exists");
        }

        Center center = new Center(
            createCenterDTO.getName(),
            createCenterDTO.getAddress(),
            createCenterDTO.getPhone(),
            createCenterDTO.getEmail(),
            createCenterDTO.getDescription()
        );

        Center savedCenter = centerRepository.save(center);
        return convertToDTO(savedCenter);
    }

    public CenterDTO updateCenter(Long id, CreateCenterDTO updateCenterDTO) {
        return centerRepository.findById(id)
            .map(existingCenter -> {
                // Check if new name conflicts with existing centers (excluding current one)
                if (!existingCenter.getName().equals(updateCenterDTO.getName()) &&
                    centerRepository.existsByName(updateCenterDTO.getName())) {
                    throw new RuntimeException("Center with name '" + updateCenterDTO.getName() + "' already exists");
                }

                existingCenter.setName(updateCenterDTO.getName());
                existingCenter.setAddress(updateCenterDTO.getAddress());
                existingCenter.setPhone(updateCenterDTO.getPhone());
                existingCenter.setEmail(updateCenterDTO.getEmail());
                existingCenter.setDescription(updateCenterDTO.getDescription());

                Center savedCenter = centerRepository.save(existingCenter);
                return convertToDTO(savedCenter);
            })
            .orElseThrow(() -> new RuntimeException("Center not found with id: " + id));
    }

    public void deleteCenter(Long id) {
        if (centerRepository.existsById(id)) {
            centerRepository.deleteById(id);
        } else {
            throw new RuntimeException("Center not found with id: " + id);
        }
    }

    public CenterDTO toggleCenterStatus(Long id) {
        return centerRepository.findById(id)
            .map(center -> {
                center.setIsActive(!center.getIsActive());
                Center savedCenter = centerRepository.save(center);
                return convertToDTO(savedCenter);
            })
            .orElseThrow(() -> new RuntimeException("Center not found with id: " + id));
    }

    private CenterDTO convertToDTO(Center center) {
        return new CenterDTO(
            center.getId(),
            center.getName(),
            center.getAddress(),
            center.getPhone(),
            center.getEmail(),
            center.getDescription(),
            center.getIsActive(),
            center.getCreatedAt()
        );
    }
}
