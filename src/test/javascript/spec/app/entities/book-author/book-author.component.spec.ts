/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BookStoreTestModule } from '../../../test.module';
import { BookAuthorComponent } from 'app/entities/book-author/book-author.component';
import { BookAuthorService } from 'app/entities/book-author/book-author.service';
import { BookAuthor } from 'app/shared/model/book-author.model';

describe('Component Tests', () => {
    describe('BookAuthor Management Component', () => {
        let comp: BookAuthorComponent;
        let fixture: ComponentFixture<BookAuthorComponent>;
        let service: BookAuthorService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BookStoreTestModule],
                declarations: [BookAuthorComponent],
                providers: []
            })
                .overrideTemplate(BookAuthorComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BookAuthorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookAuthorService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new BookAuthor(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.bookAuthors[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
