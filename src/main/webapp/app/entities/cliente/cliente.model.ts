import { TipoCliente } from 'app/entities/enumerations/tipo-cliente.model';

export interface ICliente {
  id?: number;
  documento?: string;
  nombre?: string;
  direccion?: string;
  email?: string;
  telefono?: string;
  tipoCliente?: TipoCliente | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public documento?: string,
    public nombre?: string,
    public direccion?: string,
    public email?: string,
    public telefono?: string,
    public tipoCliente?: TipoCliente | null
  ) {}
}

export function getClienteIdentifier(cliente: ICliente): number | undefined {
  return cliente.id;
}
