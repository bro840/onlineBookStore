import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { ISale } from 'app/shared/model/sale.model';
import { SaleService } from './sale.service';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-sale-update',
    templateUrl: './sale-update.component.html'
})
export class SaleUpdateComponent implements OnInit {
    sale: ISale;
    isSaving: boolean;

    users: IUser[];
    dateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private saleService: SaleService,
        private userService: UserService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sale }) => {
            this.sale = sale;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.sale.id !== undefined) {
            this.subscribeToSaveResponse(this.saleService.update(this.sale));
        } else {
            this.subscribeToSaveResponse(this.saleService.create(this.sale));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISale>>) {
        result.subscribe((res: HttpResponse<ISale>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
}
