import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ISaleDetails } from 'app/shared/model/sale-details.model';
import { Principal } from 'app/core';
import { SaleDetailsService } from './sale-details.service';

@Component({
    selector: 'jhi-sale-details',
    templateUrl: './sale-details.component.html'
})
export class SaleDetailsComponent implements OnInit, OnDestroy {
    saleDetails: ISaleDetails[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private saleDetailsService: SaleDetailsService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.saleDetailsService.query().subscribe(
            (res: HttpResponse<ISaleDetails[]>) => {
                this.saleDetails = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSaleDetails();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISaleDetails) {
        return item.id;
    }

    registerChangeInSaleDetails() {
        this.eventSubscriber = this.eventManager.subscribe('saleDetailsListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
