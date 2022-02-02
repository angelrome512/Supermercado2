import dayjs from 'dayjs/esm';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { IVenta } from 'app/entities/venta/venta.model';

export interface ICaja {
  id?: number;
  fecha?: dayjs.Dayjs | null;
  saldoInicial?: number;
  totalEfectivo?: number | null;
  totalTarjeta?: number | null;
  saldoTotal?: number | null;
  empleado?: IEmpleado | null;
  ventas?: IVenta[] | null;
}

export class Caja implements ICaja {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs | null,
    public saldoInicial?: number,
    public totalEfectivo?: number | null,
    public totalTarjeta?: number | null,
    public saldoTotal?: number | null,
    public empleado?: IEmpleado | null,
    public ventas?: IVenta[] | null
  ) {}
}

export function getCajaIdentifier(caja: ICaja): number | undefined {
  return caja.id;
}
