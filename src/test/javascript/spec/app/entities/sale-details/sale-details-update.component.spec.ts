/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { BookStoreTestModule } from '../../../test.module';
import { SaleDetailsUpdateComponent } from 'app/entities/sale-details/sale-details-update.component';
import { SaleDetailsService } from 'app/entities/sale-details/sale-details.service';
import { SaleDetails } from 'app/shared/model/sale-details.model';

describe('Component Tests', () => {
    describe('SaleDetails Management Update Component', () => {
        let comp: SaleDetailsUpdateComponent;
        let fixture: ComponentFixture<SaleDetailsUpdateComponent>;
        let service: SaleDetailsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BookStoreTestModule],
                declarations: [SaleDetailsUpdateComponent]
            })
                .overrideTemplate(SaleDetailsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SaleDetailsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleDetailsService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new SaleDetails(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.saleDetails = entity;
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
                    const entity = new SaleDetails();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.saleDetails = entity;
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
