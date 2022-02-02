package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TipoPago;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Venta.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Max(value = 8)
    @Column(name = "numero_factura")
    private Integer numeroFactura;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "total")
    private Double total;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private TipoPago tipoPago;

    @OneToMany(mappedBy = "venta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "venta" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Empleado empleado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "empleado", "ventas" }, allowSetters = true)
    private Caja caja;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroFactura() {
        return this.numeroFactura;
    }

    public Venta numeroFactura(Integer numeroFactura) {
        this.setNumeroFactura(numeroFactura);
        return this;
    }

    public void setNumeroFactura(Integer numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Venta fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return this.total;
    }

    public Venta total(Double total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public TipoPago getTipoPago() {
        return this.tipoPago;
    }

    public Venta tipoPago(TipoPago tipoPago) {
        this.setTipoPago(tipoPago);
        return this;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(Set<Producto> productos) {
        if (this.productos != null) {
            this.productos.forEach(i -> i.setVenta(null));
        }
        if (productos != null) {
            productos.forEach(i -> i.setVenta(this));
        }
        this.productos = productos;
    }

    public Venta productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Venta addProducto(Producto producto) {
        this.productos.add(producto);
        producto.setVenta(this);
        return this;
    }

    public Venta removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.setVenta(null);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Venta cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Venta empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    public Caja getCaja() {
        return this.caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public Venta caja(Caja caja) {
        this.setCaja(caja);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return id != null && id.equals(((Venta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", numeroFactura=" + getNumeroFactura() +
            ", fecha='" + getFecha() + "'" +
            ", total=" + getTotal() +
            ", tipoPago='" + getTipoPago() + "'" +
            "}";
    }
}
