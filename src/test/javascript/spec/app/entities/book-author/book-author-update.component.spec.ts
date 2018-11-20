/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { BookStoreTestModule } from '../../../test.module';
import { BookAuthorUpdateComponent } from 'app/entities/book-author/book-author-update.component';
import { BookAuthorService } from 'app/entities/book-author/book-author.service';
import { BookAuthor } from 'app/shared/model/book-author.model';

describe('Component Tests', () => {
    describe('BookAuthor Management Update Component', () => {
        let comp: BookAuthorUpdateComponent;
        let fixture: ComponentFixture<BookAuthorUpdateComponent>;
        let service: BookAuthorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BookStoreTestModule],
                declarations: [BookAuthorUpdateComponent]
            })
                .overrideTemplate(BookAuthorUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BookAuthorUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookAuthorService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new BookAuthor(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.bookAuthor = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new BookAuthor();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.bookAuthor = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
