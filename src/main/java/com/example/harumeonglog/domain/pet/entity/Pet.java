package com.example.harumeonglog.domain.pet.entity;

import com.example.harumeonglog.domain.pet.entity.enums.Gender;
import com.example.harumeonglog.domain.pet.entity.enums.PetSize;
import com.example.harumeonglog.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "pet")
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "size", nullable = false)
    @Enumerated(EnumType.STRING)
    private PetSize size;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "main_image")
    private String mainImage;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    public void update(String name, PetSize size, String type, Gender gender, LocalDate birth, String mainImage){
        this.name = name;
        this.size = size;
        this.type = type;
        this.gender = gender;
        this.birth = birth;
        this.mainImage = mainImage;
    }

    public void softDelete(){
        this.deletedAt = LocalDateTime.now();
    }
}
