package fr.canal.subscriberbc.subscriber.model.specification;

import fr.canal.subscriberbc.subscriber.model.SubscriberEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class SubscriberSpecification {

    public static Specification<SubscriberEntity> byCriteria(final List<SearchCriteria> criteriaList) {
        Specification<SubscriberEntity> specification = Specification.where(null);
        for (SearchCriteria c : criteriaList) {
            specification = specification.and((root, query, criteriaBuilder) -> {
                if (isStringAttribute(c.getKey())) {
                    return criteriaBuilder.like(root.get(c.getKey()), "%" + c.getValue() + "%");
                } else if (isBooleanAttribute(c.getKey())) {
                    return criteriaBuilder.equal(root.get(c.getKey()), Boolean.valueOf(c.getValue()));
                } else {
                    return criteriaBuilder.equal(root.get(c.getKey()), c.getValue());
                }
            });
        }
        return specification;
    }

    private static boolean isStringAttribute(final String criteria) {
        List<String> attributes = List.of("fname", "lname", "mail", "phone");
        return attributes.contains(criteria);
    }

    private static boolean isBooleanAttribute(final String criteria) {
        List<String> attributes = List.of("isActiv");
        return attributes.contains(criteria);
    }
}
