import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProducto, Producto } from '../producto.model';
import { ProductoService } from '../service/producto.service';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';
import { TipoIva } from 'app/entities/enumerations/tipo-iva.model';
import { TipoProducto } from 'app/entities/enumerations/tipo-producto.model';

@Component({
  selector: 'jhi-producto-update',
  templateUrl: './producto-update.component.html',
})
export class ProductoUpdateComponent implements OnInit {
  isSaving = false;
  tipoIvaValues = Object.keys(TipoIva);
  tipoProductoValues = Object.keys(TipoProducto);

  ventasSharedCollection: IVenta[] = [];

  editForm = this.fb.group({
    id: [],
    codigo: [null, [Validators.required]],
    nombre: [null, [Validators.required]],
    cantidad: [null, [Validators.required]],
    precioBase: [null, [Validators.required]],
    precioTotal: [],
    tipoIva: [],
    tipoProducto: [],
    venta: [],
  });

  constructor(
    protected productoService: ProductoService,
    protected ventaService: VentaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ producto }) => {
      this.updateForm(producto);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const producto = this.createFromForm();
    if (producto.id !== undefined) {
      this.subscribeToSaveResponse(this.productoService.update(producto));
    } else {
      this.subscribeToSaveResponse(this.productoService.create(producto));
    }
  }

  trackVentaById(index: number, item: IVenta): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProducto>>): void {
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

  protected updateForm(producto: IProducto): void {
    this.editForm.patchValue({
      id: producto.id,
      codigo: producto.codigo,
      nombre: producto.nombre,
      cantidad: producto.cantidad,
      precioBase: producto.precioBase,
      precioTotal: producto.precioTotal,
      tipoIva: producto.tipoIva,
      tipoProducto: producto.tipoProducto,
      venta: producto.venta,
    });

    this.ventasSharedCollection = this.ventaService.addVentaToCollectionIfMissing(this.ventasSharedCollection, producto.venta);
  }

  protected loadRelationshipsOptions(): void {
    this.ventaService
      .query()
      .pipe(map((res: HttpResponse<IVenta[]>) => res.body ?? []))
      .pipe(map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing(ventas, this.editForm.get('venta')!.value)))
      .subscribe((ventas: IVenta[]) => (this.ventasSharedCollection = ventas));
  }

  protected createFromForm(): IProducto {
    return {
      ...new Producto(),
      id: this.editForm.get(['id'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      precioBase: this.editForm.get(['precioBase'])!.value,
      precioTotal: this.editForm.get(['precioTotal'])!.value,
      tipoIva: this.editForm.get(['tipoIva'])!.value,
      tipoProducto: this.editForm.get(['tipoProducto'])!.value,
      venta: this.editForm.get(['venta'])!.value,
    };
  }
}
