package com.diaia.booking_healt.repository;

import com.diaia.booking_healt.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppointmentRepository  extends JpaRepository<Appointment, UUID> {
}
