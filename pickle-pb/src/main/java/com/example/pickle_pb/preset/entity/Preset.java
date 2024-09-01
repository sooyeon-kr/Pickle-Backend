package com.example.pickle_pb.preset.entity;

import com.example.pickle_pb.presetGroup.entity.PresetGroup;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Preset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preset_id")
    private int id;
    @Column(length = 255, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preset_group_id")
    private PresetGroup presetGroup;

}
