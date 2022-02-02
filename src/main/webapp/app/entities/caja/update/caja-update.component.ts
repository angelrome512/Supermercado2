import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICaja, Caja } from '../caja.model';
import { CajaService } from '../service/caja.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

@Component({
  selector: 'jhi-caja-update',
  templateUrl: './caja-update.component.html',
})
export class CajaUpdateComponent implements OnInit {
  isSaving = false;

  ventasSharedCollection: IVenta[] = [];
  empleadosSharedCollection: IEmpleado[] = [];

  editForm = this.fb.group({
    id: [],
    fecha: [],
    saldoInicial: [null, [Validators.required]],
    totalEfectivo: [],
    totalTarjeta: [],
    saldoTotal: [],
    venta: [],
    empleado: [],
  });

  constructor(
    protected cajaService: CajaService,
    protected ventaService: VentaService,
    protected empleadoService: EmpleadoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caja }) => {
      this.updateForm(caja);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const caja = this.createFromForm();
    if (caja.id !== undefined) {
      this.subscribeToSaveResponse(this.cajaService.update(caja));
    } else {
      this.subscribeToSaveResponse(this.cajaService.create(caja));
    }
  }

  trackVentaById(index: number, item: IVenta): number {
    return item.id!;
  }

  trackEmpleadoById(index: number, item: IEmpleado): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICaja>>): void {
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

  protected updateForm(caja: ICaja): void {
    this.editForm.patchValue({
      id: caja.id,
      fecha: caja.fecha,
      saldoInicial: caja.saldoInicial,
      totalEfectivo: caja.totalEfectivo,
      totalTarjeta: caja.totalTarjeta,
      saldoTotal: caja.saldoTotal,
      venta: caja.venta,
      empleado: caja.empleado,
    });

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing(this.ventasSharedCollection, caja.venta);
    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing(this.empleadosSharedCollection, caja.empleado);
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing(ventas, this.editForm.get('venta')!.value)))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));

    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) =>
          this.empleadoService.addEmpleadoToCollectionIfMissing(empleados, this.editForm.get('empleado')!.value)
        )
      )
      .subscribe((empleados: IEmpleado[]) => (this.empleadosSharedCollection = empleados));
  }

  protected createFromForm(): ICaja {
    return {
      ...new Caja(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      saldoInicial: this.editForm.get(['saldoInicial'])!.value,
      totalEfectivo: this.editForm.get(['totalEfectivo'])!.value,
      totalTarjeta: this.editForm.get(['totalTarjeta'])!.value,
      saldoTotal: this.editForm.get(['saldoTotal'])!.value,
      venta: this.editForm.get(['venta'])!.value,
      empleado: this.editForm.get(['empleado'])!.value,
    };
  }
}
