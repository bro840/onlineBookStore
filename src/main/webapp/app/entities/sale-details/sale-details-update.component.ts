import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISaleDetails } from 'app/shared/model/sale-details.model';
import { SaleDetailsService } from './sale-details.service';
import { ISale } from 'app/shared/model/sale.model';
import { SaleService } from 'app/entities/sale';
import { IBook } from 'app/shared/model/book.model';
import { BookService } from 'app/entities/book';

@Component({
    selector: 'jhi-sale-details-update',
    templateUrl: './sale-details-update.component.html'
})
export class SaleDetailsUpdateComponent implements OnInit {
    saleDetails: ISaleDetails;
    isSaving: boolean;

    sales: ISale[];

    books: IBook[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private saleDetailsService: SaleDetailsService,
        private saleService: SaleService,
        private bookService: BookService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ saleDetails }) => {
            this.saleDetails = saleDetails;
        });
        this.saleService.query().subscribe(
            (res: HttpResponse<ISale[]>) => {
                this.sales = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.bookService.query().subscribe(
            (res: HttpResponse<IBook[]>) => {
                this.books = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.saleDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.saleDetailsService.update(this.saleDetails));
        } else {
            this.subscribeToSaveResponse(this.saleDetailsService.create(this.saleDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISaleDetails>>) {
        result.subscribe((res: HttpResponse<ISaleDetails>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackSaleById(index: number, item: ISale) {
        return item.id;
    }

    trackBookById(index: number, item: IBook) {
        return item.id;
    }
}
