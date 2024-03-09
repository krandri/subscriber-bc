package fr.canal.subscriberbc.subscriber.repository;

import fr.canal.subscriberbc.subscriber.model.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberJpaRepository extends JpaRepository<SubscriberEntity, Integer>, JpaSpecificationExecutor<SubscriberEntity> {
    Optional<SubscriberEntity> findByMailAndPhone(final String mail, final String phone);
}
