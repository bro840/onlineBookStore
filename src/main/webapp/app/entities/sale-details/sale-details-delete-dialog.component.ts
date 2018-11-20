import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISaleDetails } from 'app/shared/model/sale-details.model';
import { SaleDetailsService } from './sale-details.service';

@Component({
    selector: 'jhi-sale-details-delete-dialog',
    templateUrl: './sale-details-delete-dialog.component.html'
})
export class SaleDetailsDeleteDialogComponent {
    saleDetails: ISaleDetails;

    constructor(
        private saleDetailsService: SaleDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.saleDetailsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'saleDetailsListModification',
                content: 'Deleted an saleDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sale-details-delete-popup',
    template: ''
})
export class SaleDetailsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ saleDetails }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SaleDetailsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.saleDetails = saleDetails;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
