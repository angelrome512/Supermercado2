import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CajaService } from '../service/caja.service';
import { ICaja, Caja } from '../caja.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

import { CajaUpdateComponent } from './caja-update.component';

describe('Caja Management Update Component', () => {
  let comp: CajaUpdateComponent;
  let fixture: ComponentFixture<CajaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cajaService: CajaService;
  let empleadoService: EmpleadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CajaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CajaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CajaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cajaService = TestBed.inject(CajaService);
    empleadoService = TestBed.inject(EmpleadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empleado query and add missing value', () => {
      const caja: ICaja = { id: 456 };
      const empleado: IEmpleado = { id: 84115 };
      caja.empleado = empleado;

      const empleadoCollection: IEmpleado[] = [{ id: 47814 }];
      jest.spyOn(empleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadoCollection })));
      const additionalEmpleados = [empleado];
      const expectedCollection: IEmpleado[] = [...additionalEmpleados, ...empleadoCollection];
      jest.spyOn(empleadoService, 'addEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ caja });
      comp.ngOnInit();

      expect(empleadoService.query).toHaveBeenCalled();
      expect(empleadoService.addEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(empleadoCollection, ...additionalEmpleados);
      expect(comp.empleadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const caja: ICaja = { id: 456 };
      const empleado: IEmpleado = { id: 99490 };
      caja.empleado = empleado;

      activatedRoute.data = of({ caja });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(caja));
      expect(comp.empleadosSharedCollection).toContain(empleado);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Caja>>();
      const caja = { id: 123 };
      jest.spyOn(cajaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caja });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: caja }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cajaService.update).toHaveBeenCalledWith(caja);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Caja>>();
      const caja = new Caja();
      jest.spyOn(cajaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caja });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: caja }));
      saveSubject.complete();

      // THEN
      expect(cajaService.create).toHaveBeenCalledWith(caja);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Caja>>();
      const caja = { id: 123 };
      jest.spyOn(cajaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caja });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cajaService.update).toHaveBeenCalledWith(caja);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEmpleadoById', () => {
      it('Should return tracked Empleado primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmpleadoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
