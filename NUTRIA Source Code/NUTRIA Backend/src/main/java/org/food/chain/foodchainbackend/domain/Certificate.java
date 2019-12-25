package org.food.chain.foodchainbackend.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "Certificate")
@Table(name = "certificate")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "certificateId",
//        scope = Certificate.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Certificate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id", nullable = false, updatable = false)
    private Long certificateId;

    @Column(name = "certificate_name", nullable = false, unique = true)
    private String certificateName;
}
