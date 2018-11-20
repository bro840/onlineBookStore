/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BookStoreTestModule } from '../../../test.module';
import { BookAuthorDeleteDialogComponent } from 'app/entities/book-author/book-author-delete-dialog.component';
import { BookAuthorService } from 'app/entities/book-author/book-author.service';

describe('Component Tests', () => {
    describe('BookAuthor Management Delete Component', () => {
        let comp: BookAuthorDeleteDialogComponent;
        let fixture: ComponentFixture<BookAuthorDeleteDialogComponent>;
        let service: BookAuthorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BookStoreTestModule],
                declarations: [BookAuthorDeleteDialogComponent]
            })
                .overrideTemplate(BookAuthorDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BookAuthorDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookAuthorService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
