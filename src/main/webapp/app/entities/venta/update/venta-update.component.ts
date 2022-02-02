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
import { TipoPago } from 'app/entities/enumerations/tipo-pago.model';
import { IProducto, Producto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-venta-update',
  templateUrl: './venta-update.component.html',
})
export class VentaUpdateComponent implements OnInit {
  isSaving = false;
  tipoPagoValues = Object.keys(TipoPago);
  searchProducto = '';
  productos: Producto[] = [];
  ventas?: IVenta;
  cantidadProducto = 1;
  preciosTotal = 0.0;

  clientesSharedCollection: ICliente[] = [];
  empleadosSharedCollection: IEmpleado[] = [];
  productosSharedCollection: IProducto[] = [];

  editForm = this.fb.group({
    id: [],
    numeroFactura: [null, [Validators.max(8)]],
    fecha: [{ value: '', disabled: true }],
    total: [],
    tipoPago: [null, [Validators.required]],
    cliente: [],
    empleado: [],
    productos: [],
  });

  constructor(
    protected ventaService: VentaService,
    protected clienteService: ClienteService,
    protected empleadoService: EmpleadoService,
    protected activatedRoute: ActivatedRoute,
    protected productoService: ProductoService,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      if (venta.id === undefined) {
        const today = dayjs().startOf('day');
        venta.fecha = today;
      }

      this.updateForm(venta);

      this.loadRelationshipsOptions();
    });
  }

  searchProductos(): void {
    if (this.searchProducto !== '') {
      this.productoService.findByCodigo(this.searchProducto).subscribe({
        next: (res: HttpResponse<IProducto[]>) => {
          this.productosSharedCollection = res.body ?? [];
        },
      });
    }
  }

  trackId(index: number, item: IProducto): number {
    return item.id!;
  }

  addProduct(productoOption: IProducto): void {
    this.productos.push(productoOption);
  }

  precioTotalVenta(): void {
    if (this.cantidadProducto !== 0) {
      this.preciosTotal = this.preciosTotal * this.cantidadProducto;
    }
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
      productos: venta.productos,
    });

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing(this.clientesSharedCollection, venta.cliente);
    this.empleadosSharedCollection = this.empleadoService.addEmpleadoToCollectionIfMissing(this.empleadosSharedCollection, venta.empleado);
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
      productos: this.editForm.get(['productos'])!.value,
    };
  }
}
