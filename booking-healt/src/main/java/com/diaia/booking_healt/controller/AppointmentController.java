package com.diaia.booking_healt.controller;

import com.diaia.booking_healt.model.dto.AppointmentDTO;
import com.diaia.booking_healt.model.entity.Appointment;
import com.diaia.booking_healt.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentDTO> create(
            @RequestHeader("X-User-Id") String userId, // <--- Preso dal Gateway!
            @RequestBody AppointmentDTO dto) {
        dto.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.createAppointment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable UUID id) {
        return appointmentService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Appointment> getNotifications(@PathVariable String userId) {
        return appointmentService.getByUserId(userId);
    }
}
