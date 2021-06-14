package com.seneca.senecaforum.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tags")
public class Tag implements Comparable<Tag>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "tag_name",length = 50,unique = true)
    private String tagName;

    @Override
    public int compareTo(Tag o) {
        return tagName.compareTo(o.tagName);
    }
}
