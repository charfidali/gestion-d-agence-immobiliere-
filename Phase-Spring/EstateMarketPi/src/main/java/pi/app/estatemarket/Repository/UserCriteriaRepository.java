package pi.app.estatemarket.Repository;



import org.springframework.stereotype.Repository;
import org.springframework.data.domain.*;
import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Entities.UserPage;
import pi.app.estatemarket.Entities.UserSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Repository
public class UserCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public UserCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<UserApp> findAllWithFilters(UserPage userPage,
                                            UserSearchCriteria userSearchCriteria){
        CriteriaQuery<UserApp> criteriaQuery = criteriaBuilder.createQuery(UserApp.class);
        Root<UserApp> userAppRoot = criteriaQuery.from(UserApp.class);
        Predicate predicate = getPredicate(userSearchCriteria, userAppRoot);
        criteriaQuery.where(predicate);
        setOrder(userPage, criteriaQuery, userAppRoot);

        TypedQuery<UserApp> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(userPage.getPageNumber() * userPage.getPageSize());
        typedQuery.setMaxResults(userPage.getPageSize());

        Pageable pageable = getPageable(userPage);

        long usersCount = getUsersCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, usersCount);
    }

    private Predicate getPredicate(UserSearchCriteria userSearchCriteria,
                                   Root<UserApp> userAppRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(userSearchCriteria.getFirstName())){
            predicates.add(
                    criteriaBuilder.like(userAppRoot.get("firstName"),
                            "%" + userSearchCriteria.getFirstName() + "%")
            );
        }
        if(Objects.nonNull(userSearchCriteria.getLastName())){
            predicates.add(
                    criteriaBuilder.like(userAppRoot.get("lastName"),
                            "%" + userSearchCriteria.getLastName() + "%")
            );
        }
        if(Objects.nonNull(userSearchCriteria.getEmailAddress())){
            predicates.add(
                    criteriaBuilder.like(userAppRoot.get("emailAddress"),
                            "%" + userSearchCriteria.getEmailAddress() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(UserPage userPage,
                          CriteriaQuery<UserApp> criteriaQuery,
                          Root<UserApp> userRoot) {
        if(userPage.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(userRoot.get(userPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(userRoot.get(userPage.getSortBy())));
        }
    }

    private Pageable getPageable(UserPage userRoot) {
        Sort sort = Sort.by(userRoot.getSortDirection(), userRoot.getSortBy());
        return PageRequest.of(userRoot.getPageNumber(),userRoot.getPageSize(), sort);
    }

    private long getUsersCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<UserApp> countRoot = countQuery.from(UserApp.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
