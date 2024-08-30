package com.example.pickle_pb.presetGroup.entity;

import com.example.pickle_pb.pb.entity.Pb;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PresetGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "pb_id")
    private Pb pb;

    public void setName(String name) {
        this.name = name;
    }
}
