import { NgModule } from '@angular/core';

import { BookStoreSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [BookStoreSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [BookStoreSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class BookStoreSharedCommonModule {}
