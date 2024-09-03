package com.example.pickle_pb.pb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor()
@AllArgsConstructor
@Builder
public class MainField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mainFieldId;

    private String name;
}
