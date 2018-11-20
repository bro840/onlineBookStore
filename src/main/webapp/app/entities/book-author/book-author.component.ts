import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IBookAuthor } from 'app/shared/model/book-author.model';
import { Principal } from 'app/core';
import { BookAuthorService } from './book-author.service';

@Component({
    selector: 'jhi-book-author',
    templateUrl: './book-author.component.html'
})
export class BookAuthorComponent implements OnInit, OnDestroy {
    bookAuthors: IBookAuthor[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private bookAuthorService: BookAuthorService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.bookAuthorService.query().subscribe(
            (res: HttpResponse<IBookAuthor[]>) => {
                this.bookAuthors = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInBookAuthors();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IBookAuthor) {
        return item.id;
    }

    registerChangeInBookAuthors() {
        this.eventSubscriber = this.eventManager.subscribe('bookAuthorListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
