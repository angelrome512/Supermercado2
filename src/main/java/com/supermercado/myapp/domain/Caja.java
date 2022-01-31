package com.supermercado.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Caja.
 */
@Entity
@Table(name = "caja")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Caja implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @NotNull
    @Column(name = "saldo_inicial", nullable = false)
    private Double saldoInicial;

    @Column(name = "total_efectivo")
    private Double totalEfectivo;

    @Column(name = "total_tarjeta")
    private Double totalTarjeta;

    @Column(name = "saldo_total")
    private Double saldoTotal;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cliente", "empleado", "producto" }, allowSetters = true)
    private Venta venta;

    @ManyToOne
    private Empleado empleado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Caja id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public Caja fecha(LocalDate fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getSaldoInicial() {
        return this.saldoInicial;
    }

    public Caja saldoInicial(Double saldoInicial) {
        this.setSaldoInicial(saldoInicial);
        return this;
    }

    public void setSaldoInicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Double getTotalEfectivo() {
        return this.totalEfectivo;
    }

    public Caja totalEfectivo(Double totalEfectivo) {
        this.setTotalEfectivo(totalEfectivo);
        return this;
    }

    public void setTotalEfectivo(Double totalEfectivo) {
        this.totalEfectivo = totalEfectivo;
    }

    public Double getTotalTarjeta() {
        return this.totalTarjeta;
    }

    public Caja totalTarjeta(Double totalTarjeta) {
        this.setTotalTarjeta(totalTarjeta);
        return this;
    }

    public void setTotalTarjeta(Double totalTarjeta) {
        this.totalTarjeta = totalTarjeta;
    }

    public Double getSaldoTotal() {
        return this.saldoTotal;
    }

    public Caja saldoTotal(Double saldoTotal) {
        this.setSaldoTotal(saldoTotal);
        return this;
    }

    public void setSaldoTotal(Double saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Caja venta(Venta venta) {
        this.setVenta(venta);
        return this;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Caja empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Caja)) {
            return false;
        }
        return id != null && id.equals(((Caja) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Caja{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", saldoInicial=" + getSaldoInicial() +
            ", totalEfectivo=" + getTotalEfectivo() +
            ", totalTarjeta=" + getTotalTarjeta() +
            ", saldoTotal=" + getSaldoTotal() +
            "}";
    }
}
