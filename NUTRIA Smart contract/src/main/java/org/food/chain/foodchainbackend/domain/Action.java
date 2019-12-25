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
@Entity(name = "Action")
@Table(name = "action")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "actionId",
//        scope = Action.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Action implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id", nullable = false, updatable = false)
    private Long actionId;
    @Column(name = "action_name", nullable = false, unique = true)
    private String actionName;
}
