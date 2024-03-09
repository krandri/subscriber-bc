package fr.canal.subscriberbc.subscriber.service;

import fr.canal.subscriberbc.subscriber.dto.SubscriberDTO;
import fr.canal.subscriberbc.subscriber.exception.ExistingSubscriberException;
import fr.canal.subscriberbc.subscriber.exception.SubscriberNotFoundException;
import fr.canal.subscriberbc.subscriber.mapper.SubscriberMapper;
import fr.canal.subscriberbc.subscriber.model.SubscriberEntity;
import fr.canal.subscriberbc.subscriber.repository.SubscriberJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriberServiceTest {

    @Mock
    private SubscriberJpaRepository repository;

    @Mock
    private SubscriberMapper mapper;

    @InjectMocks
    private SubscriberService service;

    @Test
    void testCreateWhenOk() throws ExistingSubscriberException {
        SubscriberEntity entity = SubscriberEntity.builder()
                .subscriberId(1)
                .fname("K")
                .lname("R")
                .phone("O6O6O6O6O6")
                .mail("mail@mail.com")
                .isActiv(true).build();
        SubscriberDTO dto = SubscriberDTO.builder()
                .subscriberId(1)
                .fname("K")
                .lname("R")
                .phone("O6O6O6O6O6")
                .mail("mail@mail.com")
                .isActiv(true).build();


        when(repository.findByMailAndPhone(any(), any())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(entity);

        when(mapper.entityToDto(any())).thenCallRealMethod();
        when(mapper.dtoToEntity(any())).thenCallRealMethod();

        SubscriberDTO result = service.create(dto);
        verify(repository).save(any());

        assertNotNull(result);
        assertEquals("K", dto.getFname());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testCreateWhenAlreadyExistingSubscriber() throws ExistingSubscriberException {
        SubscriberEntity entity = SubscriberEntity.builder()
                .subscriberId(1)
                .fname("K")
                .lname("R")
                .phone("O6O6O6O6O6")
                .mail("mail@mail.com")
                .isActiv(true).build();
        SubscriberDTO dto = SubscriberDTO.builder()
                .subscriberId(1)
                .fname("K")
                .lname("R")
                .phone("O6O6O6O6O6")
                .mail("mail@mail.com")
                .isActiv(true).build();
        when(repository.findByMailAndPhone(any(), any())).thenReturn(Optional.of(entity));

        assertThrows(ExistingSubscriberException.class, () -> service.create(dto));
    }

    @Test
    public void testUpdateSubscriber() throws SubscriberNotFoundException {
        String subscriberId = "1";
        SubscriberEntity entity = SubscriberEntity.builder()
                .subscriberId(1)
                .fname("K")
                .lname("R")
                .phone("O6O6O6O6O6")
                .mail("mail@mail.com")
                .isActiv(true).build();
        SubscriberDTO dto = SubscriberDTO.builder()
                .subscriberId(1)
                .fname("K")
                .lname("R")
                .phone("O6O6O6O6O6")
                .mail("mail@mail.com")
                .isActiv(true).build();

        Mockito.when(repository.findById(Integer.valueOf(subscriberId))).thenReturn(Optional.of(entity));
        Mockito.when(repository.save(any())).thenReturn(entity);

        when(mapper.entityToDto(any())).thenCallRealMethod();
        when(mapper.dtoToEntity(any())).thenCallRealMethod();

        SubscriberDTO result = service.update(subscriberId, dto);

        verify(repository).save(any());
        verify(repository).findById(any());

        assertNotNull(result);
        assertEquals("K", dto.getFname());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testUpdateSubscriberNotFoundException() {
        String subscriberId = "1";
        SubscriberDTO subscriberDTO = new SubscriberDTO();

        Mockito.when(repository.findById(Integer.valueOf(subscriberId))).thenReturn(Optional.empty());

        assertThrows(SubscriberNotFoundException.class, () -> {
            service.update(subscriberId, subscriberDTO);
        });
    }

    @Test
    public void testCancelSubscriber() throws SubscriberNotFoundException {
        String subscriberId = "1";
        SubscriberEntity entity = SubscriberEntity.builder()
                .subscriberId(1)
                .fname("K")
                .lname("R")
                .phone("O6O6O6O6O6")
                .mail("mail@mail.com")
                .isActiv(true).build();
        SubscriberEntity saved = SubscriberEntity.builder()
                .subscriberId(1)
                .fname("K")
                .lname("R")
                .phone("O6O6O6O6O6")
                .mail("mail@mail.com")
                .isActiv(false).build();


        Mockito.when(repository.findById(Integer.valueOf(subscriberId))).thenReturn(Optional.of(entity));
        Mockito.when(repository.save(any())).thenReturn(saved);


        when(mapper.entityToDto(any())).thenCallRealMethod();
        when(mapper.dtoToEntity(any())).thenCallRealMethod();

        SubscriberDTO result = service.cancel(subscriberId);


        verify(repository).save(any());
        verify(repository).findById(any());

        assertNotNull(result);
        assertFalse(result.getIsActiv());
        verifyNoMoreInteractions(repository);

    }

    @Test
    public void testCancelSubscriberNotFoundException() {
        String subscriberId = "1";

        Mockito.when(repository.findById(Integer.valueOf(subscriberId))).thenReturn(java.util.Optional.empty());

        assertThrows(SubscriberNotFoundException.class, () -> {
            service.cancel(subscriberId);
        });
    }

    @Test
    public void testSearchSubscribers() {
        SubscriberEntity e1 = SubscriberEntity.builder()
                .subscriberId(1)
                .fname("K")
                .lname("R")
                .phone("O6O6O6O6O6")
                .mail("mail@mail.com")
                .isActiv(false).build();

        SubscriberEntity e2 = SubscriberEntity.builder()
                .subscriberId(1)
                .fname("K")
                .lname("R")
                .phone("O6O6O6O6O6")
                .mail("mail@mail.com")
                .isActiv(false).build();

        String searchQuery = "isActiv:false";
        List<SubscriberEntity> subscriberEntities = List.of(e1, e2);

        when(repository.findAll(any(Specification.class))).thenReturn(subscriberEntities);

        when(mapper.entityToDto(any())).thenCallRealMethod();

        List<SubscriberDTO> result = service.searchSubscribers(searchQuery);


        verify(repository).findAll(any(Specification.class));

        assertNotNull(result);
        assertEquals(2, result.size());
        verifyNoMoreInteractions(repository);
    }
}