import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { BookStoreAuthorModule } from './author/author.module';
import { BookStoreGenreModule } from './genre/genre.module';
import { BookStoreBookModule } from './book/book.module';
import { BookStoreSaleModule } from './sale/sale.module';
import { BookStoreSaleDetailsModule } from './sale-details/sale-details.module';
import { BookStoreBookAuthorModule } from './book-author/book-author.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        BookStoreAuthorModule,
        BookStoreGenreModule,
        BookStoreBookModule,
        BookStoreSaleModule,
        BookStoreSaleDetailsModule,
        BookStoreBookAuthorModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BookStoreEntityModule {}
