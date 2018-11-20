/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BookStoreTestModule } from '../../../test.module';
import { BookAuthorDetailComponent } from 'app/entities/book-author/book-author-detail.component';
import { BookAuthor } from 'app/shared/model/book-author.model';

describe('Component Tests', () => {
    describe('BookAuthor Management Detail Component', () => {
        let comp: BookAuthorDetailComponent;
        let fixture: ComponentFixture<BookAuthorDetailComponent>;
        const route = ({ data: of({ bookAuthor: new BookAuthor(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BookStoreTestModule],
                declarations: [BookAuthorDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BookAuthorDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BookAuthorDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.bookAuthor).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
