package com.seneca.senecaforum.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "topics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic implements Comparable<Topic>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Integer topicId;

    @Column(name = "topic_name")
    private String topicName;

    @Column(name = "views")
    private Integer views;

    @OneToMany(mappedBy = "topic",cascade = CascadeType.ALL)
    private Set<Post> posts;

    @Override
    public int compareTo(Topic o) {
        int compareViews = o.getViews().compareTo(this.getViews());
        return compareViews;
    }
}
