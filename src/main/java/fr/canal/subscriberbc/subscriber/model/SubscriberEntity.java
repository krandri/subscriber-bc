package fr.canal.subscriberbc.subscriber.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "public", name = "SUBSCRIBER")
public class SubscriberEntity {
    @Id
    @Column(name = "SUBSCRIBERID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subscriberId;

    @Column(name = "FNAME")
    private String fname;

    @Column(name = "LNAME")
    private String lname;

    @Column(name = "MAIL")
    private String mail;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "ISACTIV")
    private Boolean isActiv;
}
