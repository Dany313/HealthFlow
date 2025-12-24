package com.diaia.notification_service.service;

import com.diaia.notification_service.model.dto.AppointmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "booking-service") // Deve coincidere col nome registrato su Eureka
public interface BookingClient {

    @GetMapping("/api/v1/appointments/{id}")
    AppointmentDTO getAppointmentById(@PathVariable("id") UUID id);
}
