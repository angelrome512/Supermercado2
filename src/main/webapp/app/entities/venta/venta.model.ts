import dayjs from 'dayjs/esm';
import { IProducto } from 'app/entities/producto/producto.model';
import { ICliente } from 'app/entities/cliente/cliente.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { TipoPago } from 'app/entities/enumerations/tipo-pago.model';

export interface IVenta {
  id?: number;
  numeroFactura?: number | null;
  fecha?: dayjs.Dayjs | null;
  total?: number | null;
  tipoPago?: TipoPago;
  productos?: IProducto[] | null;
  cliente?: ICliente | null;
  empleado?: IEmpleado | null;
}

export class Venta implements IVenta {
  constructor(
    public id?: number,
    public numeroFactura?: number | null,
    public fecha?: dayjs.Dayjs | null,
    public total?: number | null,
    public tipoPago?: TipoPago,
    public productos?: IProducto[] | null,
    public cliente?: ICliente | null,
    public empleado?: IEmpleado | null
  ) {}
}

export function getVentaIdentifier(venta: IVenta): number | undefined {
  return venta.id;
}
