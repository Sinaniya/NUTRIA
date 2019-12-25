package org.food.chain.foodchainbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "ProductTag")
@Table(name = "product_tag")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "productTagId",
//        scope = ProductTag.class)
@JsonIgnoreProperties(value = {"currentProductTag"}, ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductTag implements Serializable {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_tag_id", nullable = false, updatable = false)
    private Long productTagId;
    @Column(nullable = false, updatable = false)
    private Date date;
    @Column(nullable = false, updatable = false)
    private Double longitude;
    @Column(nullable = false, updatable = false)
    private Double latitude;
    @Column(nullable = false, updatable = false, unique = true)
    private String productTagHash;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH})
    @JoinTable(name = "product_tag_action",
            joinColumns = @JoinColumn(name = "product_tag_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id"))
    private Set<Action> productTagActions = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "producer_id")
    private Producer productTagProducer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_product_tag_id")
    private ProductTag previousProductTag;

    @JoinColumn(name = "previousProductTagHash")
    private String previousProductTagHash;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productTagId")
    private ProductTag currentProductTag;
}
