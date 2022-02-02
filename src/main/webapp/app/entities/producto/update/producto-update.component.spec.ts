import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductoService } from '../service/producto.service';
import { IProducto, Producto } from '../producto.model';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

import { ProductoUpdateComponent } from './producto-update.component';

describe('Producto Management Update Component', () => {
  let comp: ProductoUpdateComponent;
  let fixture: ComponentFixture<ProductoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productoService: ProductoService;
  let ventaService: VentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductoUpdateComponent],
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
      .overrideTemplate(ProductoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productoService = TestBed.inject(ProductoService);
    ventaService = TestBed.inject(VentaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Venta query and add missing value', () => {
      const producto: IProducto = { id: 456 };
      const venta: IVenta = { id: 42017 };
      producto.venta = venta;

      const ventaCollection: IVenta[] = [{ id: 85168 }];
      jest.spyOn(ventaService, 'query').mockReturnValue(of(new HttpResponse({ body: ventaCollection })));
      const additionalVentas = [venta];
      const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
      jest.spyOn(ventaService, 'addVentaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(ventaService.query).toHaveBeenCalled();
      expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(ventaCollection, ...additionalVentas);
      expect(comp.ventasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const producto: IProducto = { id: 456 };
      const venta: IVenta = { id: 61229 };
      producto.venta = venta;

      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(producto));
      expect(comp.ventasSharedCollection).toContain(venta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Producto>>();
      const producto = { id: 123 };
      jest.spyOn(productoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: producto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productoService.update).toHaveBeenCalledWith(producto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Producto>>();
      const producto = new Producto();
      jest.spyOn(productoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: producto }));
      saveSubject.complete();

      // THEN
      expect(productoService.create).toHaveBeenCalledWith(producto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Producto>>();
      const producto = { id: 123 };
      jest.spyOn(productoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ producto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productoService.update).toHaveBeenCalledWith(producto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackVentaById', () => {
      it('Should return tracked Venta primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVentaById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
