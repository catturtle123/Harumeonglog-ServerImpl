package com.example.harumeonglog.domain.event.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

// 목욕 일정 엔티티
@Entity
@Getter
@AllArgsConstructor
@SuperBuilder
@Table(name = "bath_event")
public class BathEvent extends Event {

}