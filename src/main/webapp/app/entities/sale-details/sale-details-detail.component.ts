import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISaleDetails } from 'app/shared/model/sale-details.model';

@Component({
    selector: 'jhi-sale-details-detail',
    templateUrl: './sale-details-detail.component.html'
})
export class SaleDetailsDetailComponent implements OnInit {
    saleDetails: ISaleDetails;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ saleDetails }) => {
            this.saleDetails = saleDetails;
        });
    }

    previousState() {
        window.history.back();
    }
}
