/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BookStoreTestModule } from '../../../test.module';
import { SaleDetailsDeleteDialogComponent } from 'app/entities/sale-details/sale-details-delete-dialog.component';
import { SaleDetailsService } from 'app/entities/sale-details/sale-details.service';

describe('Component Tests', () => {
    describe('SaleDetails Management Delete Component', () => {
        let comp: SaleDetailsDeleteDialogComponent;
        let fixture: ComponentFixture<SaleDetailsDeleteDialogComponent>;
        let service: SaleDetailsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [BookStoreTestModule],
                declarations: [SaleDetailsDeleteDialogComponent]
            })
                .overrideTemplate(SaleDetailsDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SaleDetailsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SaleDetailsService);
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
