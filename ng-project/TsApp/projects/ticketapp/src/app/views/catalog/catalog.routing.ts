import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CatalogComponent } from './catalog.component';
import { CatalogModule } from './catalog.module';

const routes: Routes = [{ path: '', component: CatalogComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes), CatalogModule],
  exports: [RouterModule]
})
export class CatalogRouting { }
