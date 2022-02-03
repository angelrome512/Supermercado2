package com.supermercado.myapp.domain;

import com.supermercado.myapp.domain.enumeration.TipoPago;
import java.io.Serializable;
import java.time.LocalDate;
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

    @Max(value = 99999999)
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

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Empleado empleado;

    @ManyToOne
    private Producto producto;

    @Column(name = "anular")
    private Boolean anular;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Boolean getAnular() {
        return anular;
    }

    public void setAnular(Boolean anular) {
        this.anular = anular;
    }

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

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Venta producto(Producto producto) {
        this.setProducto(producto);
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
