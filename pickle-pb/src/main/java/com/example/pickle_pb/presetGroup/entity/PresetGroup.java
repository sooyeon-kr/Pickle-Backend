package com.example.pickle_pb.presetGroup.entity;

import com.example.pickle_pb.pb.entity.Pb;
import com.example.real_common.global.BaseTimeEntity;
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
public class PresetGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preset_group_id")
    private int id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pb_id")
    private Pb pb;

    public void setName(String name) {
        this.name = name;
    }
}
