/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BookStoreTestModule } from '../../../test.module';
import { SaleDetailsDetailComponent } from 'app/entities/sale-details/sale-details-detail.component';
import { SaleDetails } from 'app/shared/model/sale-details.model';

describe('Component Tests', () => {
    describe('SaleDetails Management Detail Component', () => {
        let comp: SaleDetailsDetailComponent;
        let fixture: ComponentFixture<SaleDetailsDetailComponent>;
        const route = ({ data: of({ saleDetails: new SaleDetails(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BookStoreTestModule],
                declarations: [SaleDetailsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SaleDetailsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SaleDetailsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.saleDetails).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
