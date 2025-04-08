package com.example.harumeonglog.domain.event.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 목욕 일정 엔티티
@Entity
@Getter
@AllArgsConstructor
@Table(name = "bath_event")
public class BathEventEntity extends EventEntity {

}