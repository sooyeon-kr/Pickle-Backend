package com.example.pickle_pb.pb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor()
@AllArgsConstructor
@Builder
public class PbTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int interestId;

    @ManyToOne
    @JoinColumn(name = "pb_id", referencedColumnName = "pb_id")
    private Pb pb;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "tagId")
    private Tag tag;


}
