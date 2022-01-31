import dayjs from 'dayjs/esm';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { IProducto } from 'app/entities/producto/producto.model';
import { TipoPago } from 'app/entities/enumerations/tipo-pago.model';

export interface IVenta {
  id?: number;
  numeroFactura?: number | null;
  fecha?: dayjs.Dayjs | null;
  total?: number | null;
  tipoPago?: TipoPago;
  cliente?: ICliente | null;
  empleado?: IEmpleado | null;
  producto?: IProducto | null;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public numeroFactura?: number | null,
    public fecha?: dayjs.Dayjs | null,
    public total?: number | null,
    public tipoPago?: TipoPago,
    public cliente?: ICliente | null,
    public empleado?: IEmpleado | null,
    public producto?: IProducto | null
  ) {}
}

export function getVentaIdentifier(venta: IVenta): number | undefined {
  return venta.id;
}
