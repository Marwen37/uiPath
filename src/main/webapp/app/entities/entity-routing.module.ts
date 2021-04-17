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
        path: 'robot',
        data: { pageTitle: 'uiPathApp.robot.home.title' },
        loadChildren: () => import('./robot/robot.module').then(m => m.RobotModule),
      },
      {
        path: 'environment',
        data: { pageTitle: 'uiPathApp.environment.home.title' },
        loadChildren: () => import('./environment/environment.module').then(m => m.EnvironmentModule),
      },
      {
        path: 'process',
        data: { pageTitle: 'uiPathApp.process.home.title' },
        loadChildren: () => import('./process/process.module').then(m => m.ProcessModule),
      },
      {
        path: 'job',
        data: { pageTitle: 'uiPathApp.job.home.title' },
        loadChildren: () => import('./job/job.module').then(m => m.JobModule),
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
