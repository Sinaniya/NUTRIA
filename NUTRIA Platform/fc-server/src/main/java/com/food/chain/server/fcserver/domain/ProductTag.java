package com.food.chain.server.fcserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ProductTag")
@Table(name = "product_tag")
@JsonIgnoreProperties(value = {"currentProductTag"}, ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductTag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_tag_id", nullable = false, updatable = false)
    private Long productTagId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime date;

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

    @ManyToMany
    @JoinTable(name = "previous_product_tag",
            joinColumns = @JoinColumn(name = "product_tag_id"),
            inverseJoinColumns = @JoinColumn(name = "previous_product_tag_id")
    )
    private Set<ProductTag> previousProductTags = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "previous_product_tag",
            joinColumns = @JoinColumn(name = "previous_product_tag_id"),
            inverseJoinColumns = @JoinColumn(name = "product_tag_id")
    )
    private Set<ProductTag> nextProductTags = new HashSet<>();
}
