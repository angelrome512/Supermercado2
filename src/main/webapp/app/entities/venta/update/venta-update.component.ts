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
<<<<<<< HEAD
=======
import { IProducto, Producto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import dayjs from 'dayjs/esm';

>>>>>>> e411ec2d82182960291bb8483bbed51e75adce6f
@Component({
  selector: 'jhi-venta-update',
  templateUrl: './venta-update.component.html',
})
export class VentaUpdateComponent implements OnInit {
  isSaving = false;
  tipoPagoValues = Object.keys(TipoPago);
  searchProducto = '';
  productos: Producto[] = [];

  clientesSharedCollection: ICliente[] = [];
  empleadosSharedCollection: IEmpleado[] = [];
<<<<<<< HEAD
  cajasSharedCollection: ICaja[] = [];

  editForm = this.fb.group({
    id: [],
    numeroFactura: [null, [Validators.max(99999999)]],
    fecha: [],
=======
  productosSharedCollection: IProducto[] = [];

  editForm = this.fb.group({
    id: [],
    numeroFactura: [null, [Validators.max(8)]],
    fecha: [{ value: '', disabled: true }],
>>>>>>> e411ec2d82182960291bb8483bbed51e75adce6f
    total: [],
    tipoPago: [null, [Validators.required]],
    cliente: [],
    empleado: [],
<<<<<<< HEAD
    caja: [],
=======
    productos: [],
>>>>>>> e411ec2d82182960291bb8483bbed51e75adce6f
  });

  constructor(
    protected ventaService: VentaService,
    protected clienteService: ClienteService,
    protected empleadoService: EmpleadoService,
    protected cajaService: CajaService,
    protected activatedRoute: ActivatedRoute,
    protected productoService: ProductoService,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
<<<<<<< HEAD
      // if(venta.id === undefined){
      //   const today = dayjs().startOf('day');
      //   venta.fecha = today;
      // }
=======
      if (venta.id === undefined) {
        const today = dayjs().startOf('day');
        venta.fecha = today;
      }

>>>>>>> e411ec2d82182960291bb8483bbed51e75adce6f
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
    if (productoOption !== '') {
      this.productos.push(productoOption);
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

  trackCajaById(index: number, item: ICaja): number {
    return item.id!;
  }

  ultimaVenta(): void {
    this.ventaService
      .lastSell()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(
        map((ventas: IVenta[]) => this.ventaService.addVentaToCollectionIfMissing(ventas, this.editForm.get('venta')!.value))
      )
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));
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
<<<<<<< HEAD
      caja: venta.caja,
=======
      productos: venta.productos,
>>>>>>> e411ec2d82182960291bb8483bbed51e75adce6f
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
<<<<<<< HEAD
      caja: this.editForm.get(['caja'])!.value,
=======
      productos: this.editForm.get(['productos'])!.value,
>>>>>>> e411ec2d82182960291bb8483bbed51e75adce6f
    };
  }
}
