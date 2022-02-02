import { IVenta } from 'app/entities/venta/venta.model';
import { TipoIva } from 'app/entities/enumerations/tipo-iva.model';
import { TipoProducto } from 'app/entities/enumerations/tipo-producto.model';

export interface IProducto {
  id?: number;
  codigo?: string;
  nombre?: string;
  cantidad?: number;
  precioBase?: number;
  precioTotal?: number | null;
  tipoIva?: TipoIva | null;
  tipoProducto?: TipoProducto | null;
  venta?: IVenta | null;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public codigo?: string,
    public nombre?: string,
    public cantidad?: number,
    public precioBase?: number,
    public precioTotal?: number | null,
    public tipoIva?: TipoIva | null,
    public tipoProducto?: TipoProducto | null,
    public venta?: IVenta | null
  ) {}
}

export function getProductoIdentifier(producto: IProducto): number | undefined {
  return producto.id;
}
