import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEmpleado, Empleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';
import { Cargo } from 'app/entities/enumerations/cargo.model';

@Component({
  selector: 'jhi-empleado-update',
  templateUrl: './empleado-update.component.html',
})
export class EmpleadoUpdateComponent implements OnInit {
  isSaving = false;
  cargoValues = Object.keys(Cargo);

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    documento: [null, [Validators.required, Validators.maxLength(10)]],
    direccion: [null, [Validators.required]],
    email: [null, [Validators.required]],
    telefono: [null, [Validators.required]],
    cargo: [null, [Validators.required]],
    codigoSU: [],
    activo: [],
  });

  constructor(protected empleadoService: EmpleadoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleado }) => {
      this.updateForm(empleado);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleado = this.createFromForm();
    if (empleado.id !== undefined) {
      this.subscribeToSaveResponse(this.empleadoService.update(empleado));
    } else {
      this.subscribeToSaveResponse(this.empleadoService.create(empleado));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleado>>): void {
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

  protected updateForm(empleado: IEmpleado): void {
    this.editForm.patchValue({
      id: empleado.id,
      nombre: empleado.nombre,
      documento: empleado.documento,
      direccion: empleado.direccion,
      email: empleado.email,
      telefono: empleado.telefono,
      cargo: empleado.cargo,
      codigoSU: empleado.codigoSU,
      activo: empleado.activo,
    });
  }

  protected createFromForm(): IEmpleado {
    return {
      ...new Empleado(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      documento: this.editForm.get(['documento'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
      email: this.editForm.get(['email'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      cargo: this.editForm.get(['cargo'])!.value,
      codigoSU: this.editForm.get(['codigoSU'])!.value,
      activo: this.editForm.get(['activo'])!.value,
    };
  }
}
