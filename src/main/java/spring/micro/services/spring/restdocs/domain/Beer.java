package spring.micro.services.spring.restdocs.domain;

/*
 * Created by Nibir Hossain on 12.08.20
 */

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import spring.micro.services.spring.restdocs.web.model.BeerStyleEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Beer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;
    @Version
    private Long version;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;
    @UpdateTimestamp
    private Timestamp lastModifiedDate;
    private String name;
    @NotNull
    private BeerStyleEnum beerStyle;
    @Column(unique = true)
    private Long upc;
    private BigDecimal price;
    private Integer minOnHand;
    private Integer quantityToBrew;
}
