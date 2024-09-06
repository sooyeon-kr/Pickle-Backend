package com.example.pickle_pb.presetGroup.entity;

import com.example.pickle_pb.pb.entity.Pb;
import com.example.pickle_pb.preset.entity.Preset;
import com.example.real_common.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "presetGroup", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Preset> presets;

    public void setName(String name) {
        this.name = name;
    }
}
