import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'machine',
        data: { pageTitle: 'uiPathApp.machine.home.title' },
        loadChildren: () => import('./machine/machine.module').then(m => m.MachineModule),
      },
      {
        path: 'urobot',
        data: { pageTitle: 'uiPathApp.urobot.home.title' },
        loadChildren: () => import('./urobot/urobot.module').then(m => m.UrobotModule),
      },
      {
        path: 'uenvironment',
        data: { pageTitle: 'uiPathApp.uenvironment.home.title' },
        loadChildren: () => import('./uenvironment/uenvironment.module').then(m => m.UenvironmentModule),
      },
      {
        path: 'uprocess',
        data: { pageTitle: 'uiPathApp.uprocess.home.title' },
        loadChildren: () => import('./uprocess/uprocess.module').then(m => m.UprocessModule),
      },
      {
        path: 'ujob',
        data: { pageTitle: 'uiPathApp.ujob.home.title' },
        loadChildren: () => import('./ujob/ujob.module').then(m => m.UjobModule),
      },
      {
        path: 'upackage',
        data: { pageTitle: 'uiPathApp.upackage.home.title' },
        loadChildren: () => import('./upackage/upackage.module').then(m => m.UpackageModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
