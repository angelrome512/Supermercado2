package com.mycompany.myapp.repository.specification;

import com.mycompany.myapp.domain.Producto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class ProductoSpecification {

    public static Specification<Producto> searchingParam(String filter) {
        return new Specification<Producto>() {
            private static final long serialVersionUID = 1L;

            public Predicate toPredicate(Root<Producto> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                query.distinct(true);

                List<Predicate> ors = new ArrayList<Predicate>();

                Expression<String> codigo = root.get("codigo").as(String.class);
                Expression<String> nombre = root.get("nombre").as(String.class);
                Expression<String> cantidad = root.get("cantidad").as(String.class);
                Expression<String> precioBase = root.get("precioBase").as(String.class);
                Expression<String> precioTotal = root.get("precioTotal").as(String.class);
                Expression<String> tipoProducto = root.get("tipoProducto").as(String.class);

                String[] searchParam = filter.split(" ");

                for (int i = 0; i < searchParam.length; i++) {
                    List<Predicate> predicates = new ArrayList<Predicate>();
                    predicates.add(builder.like(codigo, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(nombre, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(cantidad, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(precioBase, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(precioTotal, "%" + searchParam[i] + "%"));
                    predicates.add(builder.like(tipoProducto, "%" + searchParam[i] + "%"));

                    ors.add(builder.or(predicates.toArray(new Predicate[] {})));
                }
                Predicate result = builder.and(ors.toArray(new Predicate[] {}));

                return result;
            }
        };
    }
}
