package com.tuananhdo.repository.impl;

import com.tuananhdo.entity.Location;
import com.tuananhdo.repository.FilterableLocationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.tuananhdo.entity.Location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class FilterableLocationRepositoryImpl implements FilterableLocationRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<Location> listWithFilter(Pageable pageable, Map<String, Object> filterFields) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> query = builder.createQuery(Location.class);

        Root<Location> root = query.from(Location.class);
        List<Predicate> predicates = getValueOfPredicate(filterFields, builder, root);
        query.where(predicates.toArray(new Predicate[0]));

        List<Order> listOrder = new ArrayList<>();
        pageable.getSort().stream().forEach(order -> {
            System.out.println("Order field: " + order.getProperty());
            listOrder.add(builder.asc(root.get(order.getProperty())));
        });
        query.orderBy(listOrder);

        TypedQuery<Location> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Location> listResult = typedQuery.getResultList();
        long totalRows = getTotalRows(filterFields,builder);
        return new PageImpl<>(listResult, pageable, totalRows);
    }

    private static List<Predicate> getValueOfPredicate(Map<String, Object> filterFields,
                                                       CriteriaBuilder builder,
                                                       Root<Location> root) {
        List<Predicate> predicates = new ArrayList<>();
        if (!filterFields.isEmpty()) {
            predicates = filterFields.entrySet()
                    .stream()
                    .map(entry -> builder.equal(root.get(entry.getKey()), entry.getValue()))
                    .toList();
        }
        List<Predicate> newPredicates = new ArrayList<>(predicates);
        newPredicates.add(builder.equal(root.get("trashed"),false));
        return newPredicates;
    }

    private long getTotalRows(Map<String, Object> filterFields,
                              CriteriaBuilder builder) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Location> countRoot = countQuery.from(Location.class);

        countQuery.select(builder.count(countRoot));

        List<Predicate> predicates = getValueOfPredicate(filterFields, builder, countRoot);
        countQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
