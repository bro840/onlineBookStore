import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BookStoreSharedModule } from 'app/shared';
import {
    SaleDetailsComponent,
    SaleDetailsDetailComponent,
    SaleDetailsUpdateComponent,
    SaleDetailsDeletePopupComponent,
    SaleDetailsDeleteDialogComponent,
    saleDetailsRoute,
    saleDetailsPopupRoute
} from './';

const ENTITY_STATES = [...saleDetailsRoute, ...saleDetailsPopupRoute];

@NgModule({
    imports: [BookStoreSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SaleDetailsComponent,
        SaleDetailsDetailComponent,
        SaleDetailsUpdateComponent,
        SaleDetailsDeleteDialogComponent,
        SaleDetailsDeletePopupComponent
    ],
    entryComponents: [SaleDetailsComponent, SaleDetailsUpdateComponent, SaleDetailsDeleteDialogComponent, SaleDetailsDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BookStoreSaleDetailsModule {}
