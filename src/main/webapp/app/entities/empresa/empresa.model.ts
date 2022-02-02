export interface IEmpresa {
  id?: number;
  nombre?: string | null;
  nif?: string | null;
  direccion?: string | null;
  telefono?: string | null;
}

export class Empresa implements IEmpresa {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public nif?: string | null,
    public direccion?: string | null,
    public telefono?: string | null
  ) {}
}

export function getEmpresaIdentifier(empresa: IEmpresa): number | undefined {
  return empresa.id;
}
