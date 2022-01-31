package com.supermercado.myapp.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.supermercado.myapp.domain.Caja} entity.
 */
public class CajaDTO implements Serializable {

    private Long id;

    private LocalDate fecha;

    @NotNull
    private Double saldoInicial;

    private Double totalEfectivo;

    private Double totalTarjeta;

    private Double saldoTotal;

    private VentaDTO venta;

    private EmpleadoDTO empleado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Double getTotalEfectivo() {
        return totalEfectivo;
    }

    public void setTotalEfectivo(Double totalEfectivo) {
        this.totalEfectivo = totalEfectivo;
    }

    public Double getTotalTarjeta() {
        return totalTarjeta;
    }

    public void setTotalTarjeta(Double totalTarjeta) {
        this.totalTarjeta = totalTarjeta;
    }

    public Double getSaldoTotal() {
        return saldoTotal;
    }

    public void setSaldoTotal(Double saldoTotal) {
        this.saldoTotal = saldoTotal;
    }

    public VentaDTO getVenta() {
        return venta;
    }

    public void setVenta(VentaDTO venta) {
        this.venta = venta;
    }

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CajaDTO)) {
            return false;
        }

        CajaDTO cajaDTO = (CajaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cajaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CajaDTO{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", saldoInicial=" + getSaldoInicial() +
            ", totalEfectivo=" + getTotalEfectivo() +
            ", totalTarjeta=" + getTotalTarjeta() +
            ", saldoTotal=" + getSaldoTotal() +
            ", venta=" + getVenta() +
            ", empleado=" + getEmpleado() +
            "}";
    }
}
