package com.uday.bolgManagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity //It tells jpa that this class represents table in database
@Table(name="users") //Allows us to explicitly name table and customize other configurations of table
@Getter
@Setter
@NoArgsConstructor //generates constructor with no parameters. Spring data jpa/hibernate and Jackson Json
// requires a default no-argument constructor to instantiate objects before filling them with database or api
// data via reflection.
@AllArgsConstructor // generates constructor with one parameters for every field.
@Builder //lombok annotation for implementing builder design pattern // no fixed parameter order.
public class User {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false , unique = true , length = 50)
    private String username;

    @Column(nullable = false , unique = true , length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) //is a JPA / Hibernate annotation used to specify how a Java enum should be
    // stored in a relational database table.
    @Column(nullable = false ,length = 20)
    private Role role;

    @CreationTimestamp //Records the exact moment a record was created (e.g. when a post was published)
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
}
