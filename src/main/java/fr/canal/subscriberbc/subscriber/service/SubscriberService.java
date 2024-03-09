package fr.canal.subscriberbc.subscriber.service;

import fr.canal.subscriberbc.subscriber.dto.SubscriberDTO;
import fr.canal.subscriberbc.subscriber.exception.ExistingSubscriberException;
import fr.canal.subscriberbc.subscriber.exception.SubscriberNotFoundException;
import fr.canal.subscriberbc.subscriber.mapper.SubscriberMapper;
import fr.canal.subscriberbc.subscriber.model.SubscriberEntity;
import fr.canal.subscriberbc.subscriber.model.specification.SearchCriteria;
import fr.canal.subscriberbc.subscriber.model.specification.SubscriberSpecification;
import fr.canal.subscriberbc.subscriber.repository.SubscriberJpaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SubscriberService {

    private SubscriberJpaRepository repository;

    private SubscriberMapper mapper;

    public SubscriberService(SubscriberJpaRepository repository, SubscriberMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public SubscriberDTO create(SubscriberDTO subscriberDTO) throws ExistingSubscriberException {
        Optional<SubscriberEntity> entity = repository.findByMailAndPhone(subscriberDTO.getMail(), subscriberDTO.getPhone());
        if (entity.isPresent()) {
            throw new ExistingSubscriberException("Subscriber already exists");
        } else {
            subscriberDTO.setIsActiv(true);
            return mapper.entityToDto(repository.save(mapper.dtoToEntity(subscriberDTO)));
        }
    }

    public SubscriberDTO update(String id, SubscriberDTO subscriberDTO) throws SubscriberNotFoundException {
        SubscriberEntity entityToUpdate = repository.findById(Integer.valueOf(id)).orElseThrow(() -> new SubscriberNotFoundException("Subscriber not found"));
        SubscriberDTO entityDTOToUpdate = mapper.entityToDto(entityToUpdate);
        BeanUtils.copyProperties(subscriberDTO, entityDTOToUpdate, getNullPropertyNames(subscriberDTO));
        return mapper.entityToDto(repository.save(mapper.dtoToEntity(entityDTOToUpdate)));
    }

    public SubscriberDTO cancel(String id) throws SubscriberNotFoundException {
        SubscriberDTO toUpdate = mapper.entityToDto(repository.findById(Integer.valueOf(id)).orElseThrow(() -> new SubscriberNotFoundException("Subscriber not found")));
        toUpdate.setIsActiv(false);
        return mapper.entityToDto(repository.save(mapper.dtoToEntity(toUpdate)));
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        Arrays.stream(pds).forEach(pd -> {
            Object value = beanWrapper.getPropertyValue(pd.getName());
            if (value == null) {
                emptyNames.add(pd.getName());
            }
        });
        return emptyNames.toArray(new String[0]);
    }

    public List<SubscriberDTO> searchSubscribers(String search) {
        List<SearchCriteria> criterias = parseCriteriasFromString(search);
        return repository.findAll(SubscriberSpecification.byCriteria(criterias)).stream().map(mapper::entityToDto).toList();
    }

    private List<SearchCriteria> parseCriteriasFromString(String search) {
        Pattern pattern = Pattern.compile("[><:]");
        List<SearchCriteria> criterias = new ArrayList<>();

        String[] criteriasArray = search.split(",");
        for (String criteria : criteriasArray) {
            System.out.println(criteria);
            String operator = "";
            Matcher matcher = pattern.matcher(criteria);
            if (matcher.find()) {
                operator = matcher.group();
            }
            String[] parts = criteria.split(operator);
            criterias.add(new SearchCriteria(parts[0], operator, parts[1]));
        }
        return criterias;
    }
}
