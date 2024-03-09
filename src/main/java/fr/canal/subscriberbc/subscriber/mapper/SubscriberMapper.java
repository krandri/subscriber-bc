package fr.canal.subscriberbc.subscriber.mapper;

import fr.canal.subscriberbc.subscriber.dto.SubscriberDTO;
import fr.canal.subscriberbc.subscriber.model.SubscriberEntity;
import org.springframework.stereotype.Component;

@Component
public class SubscriberMapper {

    public SubscriberDTO entityToDto(final SubscriberEntity entity) {
        return SubscriberDTO.builder()
                .subscriberId(entity.getSubscriberId())
                .fname(entity.getFname())
                .lname(entity.getLname())
                .phone(entity.getPhone())
                .mail(entity.getMail())
                .isActiv(entity.getIsActiv())
                .build();
    }

    public SubscriberEntity dtoToEntity(final SubscriberDTO dto) {
        return SubscriberEntity.builder()
                .subscriberId(dto.getSubscriberId())
                .fname(dto.getFname())
                .lname(dto.getLname())
                .phone(dto.getPhone())
                .mail(dto.getMail())
                .isActiv(dto.getIsActiv())
                .build();
    }
}
