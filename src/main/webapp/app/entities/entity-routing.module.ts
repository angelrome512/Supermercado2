import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cliente',
        data: { pageTitle: 'supermercado2App.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'producto',
        data: { pageTitle: 'supermercado2App.producto.home.title' },
        loadChildren: () => import('./producto/producto.module').then(m => m.ProductoModule),
      },
      {
        path: 'empleado',
        data: { pageTitle: 'supermercado2App.empleado.home.title' },
        loadChildren: () => import('./empleado/empleado.module').then(m => m.EmpleadoModule),
      },
      {
        path: 'venta',
        data: { pageTitle: 'supermercado2App.venta.home.title' },
        loadChildren: () => import('./venta/venta.module').then(m => m.VentaModule),
      },
      {
        path: 'caja',
        data: { pageTitle: 'supermercado2App.caja.home.title' },
        loadChildren: () => import('./caja/caja.module').then(m => m.CajaModule),
      },
      {
        path: 'empresa',
        data: { pageTitle: 'supermercado2App.empresa.home.title' },
        loadChildren: () => import('./empresa/empresa.module').then(m => m.EmpresaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
