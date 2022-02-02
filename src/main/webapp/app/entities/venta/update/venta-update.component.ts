import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVenta, Venta } from '../venta.model';
import { VentaService } from '../service/venta.service';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { ClienteService } from 'app/entities/cliente/service/cliente.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { ICaja } from 'app/entities/caja/caja.model';
import { CajaService } from 'app/entities/caja/service/caja.service';
import { TipoPago } from 'app/entities/enumerations/tipo-pago.model';

@Component({
  selector: 'jhi-venta-update',
  templateUrl: './venta-update.component.html',
})
export class VentaUpdateComponent implements OnInit {
  isSaving = false;
  tipoPagoValues = Object.keys(TipoPago);

  clientesSharedCollection: ICliente[] = [];
  empleadosSharedCollection: IEmpleado[] = [];
  cajasSharedCollection: ICaja[] = [];

  editForm = this.fb.group({
    id: [],
    numeroFactura: [null, [Validators.max(8)]],
    fecha: [],
    total: [],
    tipoPago: [null, [Validators.required]],
    cliente: [],
    empleado: [],
    caja: [],
  });

  constructor(
    protected ventaService: VentaService,
    protected clienteService: ClienteService,
    protected empleadoService: EmpleadoService,
    protected cajaService: CajaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.updateForm(venta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const venta = this.createFromForm();
    if (venta.id !== undefined) {
      this.subscribeToSaveResponse(this.ventaService.update(venta));
    } else {
      this.subscribeToSaveResponse(this.ventaService.create(venta));
    }
  }

  trackClienteById(index: number, item: ICliente): number {
    return item.id!;
  }

  trackEmpleadoById(index: number, item: IEmpleado): number {
    return item.id!;
  }

  trackCajaById(index: number, item: ICaja): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(venta: IVenta): void {
    this.editForm.patchValue({
      id: venta.id,
      numeroFactura: venta.numeroFactura,
      fecha: venta.fecha,
      total: venta.total,
      tipoPago: venta.tipoPago,
      cliente: venta.cliente,
      empleado: venta.empleado,
      caja: venta.caja,
    });

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, venta.cliente);
    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing(this.empleadosSharedCollection, venta.empleado);
    this.cajasSharedCollection = this.cajaService.addCajaToCollectionIfMissing(this.cajasSharedCollection, venta.caja);
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing(clientes, this.editForm.get('cliente')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing(empleados, this.editForm.get('empleado')!.value)
        )
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));

    this.cajaService
      .query()
      .pipe(map((res: HttpResponse<ICaja[]>) => res.body ?? []))
      .pipe(map((cajas: ICaja[]) => this.cajaService.addCajaToCollectionIfMissing(cajas, this.editForm.get('caja')!.value)))
      .subscribe((cajas: ICaja[]) => (this.cajasSharedCollection = cajas));
  }

  protected createFromForm(): IVenta {
    return {
      ...new Venta(),
      id: this.editForm.get(['id'])!.value,
      numeroFactura: this.editForm.get(['numeroFactura'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      total: this.editForm.get(['total'])!.value,
      tipoPago: this.editForm.get(['tipoPago'])!.value,
      cliente: this.editForm.get(['cliente'])!.value,
      empleado: this.editForm.get(['empleado'])!.value,
      caja: this.editForm.get(['caja'])!.value,
    };
  }
}
