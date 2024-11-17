package ma.yc.Citronix.farm.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ma.yc.Citronix.farm.domain.model.aggregate.Farm;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FarmCustomRepositoryImpl implements FarmCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Farm> search ( String name, String localization, Double surface, LocalDateTime creationDate ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Farm> query = cb.createQuery(Farm.class);
        Root<Farm> root = query.from(Farm.class);

        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (localization != null && !localization.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("localization")), "%" + localization.toLowerCase() + "%"));
        }
        if (surface != null) {
            predicates.add(cb.equal(root.get("surface"), surface));
        }
        if (creationDate != null) {
            predicates.add(cb.equal(root.get("creationDate"), creationDate));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }
}
