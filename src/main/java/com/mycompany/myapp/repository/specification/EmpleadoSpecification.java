package com.mycompany.myapp.repository.specification;

import com.mycompany.myapp.domain.Empleado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class EmpleadoSpecification {

    public static Specification<Empleado> searchingParam(String filter) {
        return new Specification<Empleado>() {
            private static final long serialVersionUID = 1L;

            public Predicate toPredicate(Root<Empleado> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                query.distinct(true);

                List<Predicate> ors = new ArrayList<Predicate>();

                Expression<String> nombre = root.get("nombre").as(String.class);
                Expression<String> direccion = root.get("direccion").as(String.class);
                Expression<String> email = root.get("email").as(String.class);
                Expression<String> telefono = root.get("telefono").as(String.class);
                Expression<String> cargo = root.get("cargo").as(String.class);
                Expression<String> codigoSU = root.get("codigoSU").as(String.class);

                String[] searchParam = filter.split(" ");

                for (int i = 0; i < searchParam.length; i++) {
                    List<Predicate> predicates = new ArrayList<Predicate>();
                    predicates.add(builder.like(nombre, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(direccion, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(email, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(telefono, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(cargo, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(codigoSU, "%" + searchParam[i] + "%"));

                    ors.add(builder.or(predicates.toArray(new Predicate[] {})));
                }
                Predicate result = builder.and(ors.toArray(new Predicate[] {}));

                return result;
            }
        };
    }
}
