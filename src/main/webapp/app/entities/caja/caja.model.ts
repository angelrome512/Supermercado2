import dayjs from 'dayjs/esm';
import { IVenta } from 'app/entities/venta/venta.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';

export interface ICaja {
  id?: number;
  fecha?: dayjs.Dayjs | null;
  saldoInicial?: number;
  totalEfectivo?: number | null;
  totalTarjeta?: number | null;
  saldoTotal?: number | null;
  venta?: IVenta | null;
  empleado?: IEmpleado | null;
}

export class Caja implements ICaja {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs | null,
    public saldoInicial?: number,
    public totalEfectivo?: number | null,
    public totalTarjeta?: number | null,
    public saldoTotal?: number | null,
    public venta?: IVenta | null,
    public empleado?: IEmpleado | null
  ) {}
}

export function getCajaIdentifier(caja: ICaja): number | undefined {
  return caja.id;
}
