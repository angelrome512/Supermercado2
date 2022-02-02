package com.mycompany.myapp.repository.specification;

import com.mycompany.myapp.domain.Cliente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ClienteSpecification {

    public static Specification<Cliente> searchingCliente(String filter) {
        return new Specification<Cliente>() {
            private static final long serialVersionUID = 1L;

            public Predicate toPredicate(Root<Cliente> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                query.distinct(true);

                List<Predicate> ors = new ArrayList<Predicate>();

                Expression<String> documento = root.get("documento").as(String.class);
                Expression<String> nombre = root.get("nombre").as(String.class);
                Expression<String> direccion = root.get("direccion").as(String.class);
                Expression<String> email = root.get("email").as(String.class);
                Expression<String> telefono = root.get("telefono").as(String.class);

                String[] searchParam = filter.split(" ");

                for (int i = 0; i < searchParam.length; i++) {
                    List<Predicate> predicates = new ArrayList<Predicate>();
                    predicates.add(builder.like(documento, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(nombre, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(direccion, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(email, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(telefono, "%" + searchParam[i] + "%"));

                    ors.add(builder.or(predicates.toArray(new Predicate[] {})));
                }
                Predicate result = builder.and(ors.toArray(new Predicate[] {}));

                return result;
            }
        };
    }
}
