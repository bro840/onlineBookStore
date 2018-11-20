import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BookStoreSharedModule } from 'app/shared';
import {
    BookAuthorComponent,
    BookAuthorDetailComponent,
    BookAuthorUpdateComponent,
    BookAuthorDeletePopupComponent,
    BookAuthorDeleteDialogComponent,
    bookAuthorRoute,
    bookAuthorPopupRoute
} from './';

const ENTITY_STATES = [...bookAuthorRoute, ...bookAuthorPopupRoute];

@NgModule({
    imports: [BookStoreSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BookAuthorComponent,
        BookAuthorDetailComponent,
        BookAuthorUpdateComponent,
        BookAuthorDeleteDialogComponent,
        BookAuthorDeletePopupComponent
    ],
    entryComponents: [BookAuthorComponent, BookAuthorUpdateComponent, BookAuthorDeleteDialogComponent, BookAuthorDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BookStoreBookAuthorModule {}
