/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BookStoreTestModule } from '../../../test.module';
import { SaleDetailsComponent } from 'app/entities/sale-details/sale-details.component';
import { SaleDetailsService } from 'app/entities/sale-details/sale-details.service';
import { SaleDetails } from 'app/shared/model/sale-details.model';

describe('Component Tests', () => {
    describe('SaleDetails Management Component', () => {
        let comp: SaleDetailsComponent;
        let fixture: ComponentFixture<SaleDetailsComponent>;
        let service: SaleDetailsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BookStoreTestModule],
                declarations: [SaleDetailsComponent],
                providers: []
            })
                .overrideTemplate(SaleDetailsComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SaleDetailsComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleDetailsService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new SaleDetails(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.saleDetails[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
