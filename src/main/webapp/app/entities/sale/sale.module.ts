import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BookStoreSharedModule } from 'app/shared';
import { BookStoreAdminModule } from 'app/admin/admin.module';
import {
    SaleComponent,
    SaleDetailComponent,
    SaleUpdateComponent,
    SaleDeletePopupComponent,
    SaleDeleteDialogComponent,
    saleRoute,
    salePopupRoute
} from './';

const ENTITY_STATES = [...saleRoute, ...salePopupRoute];

@NgModule({
    imports: [BookStoreSharedModule, BookStoreAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SaleComponent, SaleDetailComponent, SaleUpdateComponent, SaleDeleteDialogComponent, SaleDeletePopupComponent],
    entryComponents: [SaleComponent, SaleUpdateComponent, SaleDeleteDialogComponent, SaleDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BookStoreSaleModule {}
