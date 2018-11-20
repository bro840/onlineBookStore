import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IGenre } from 'app/shared/model/genre.model';
import { Principal } from 'app/core';
import { GenreService } from './genre.service';

@Component({
    selector: 'jhi-genre',
    templateUrl: './genre.component.html'
})
export class GenreComponent implements OnInit, OnDestroy {
    genres: IGenre[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private genreService: GenreService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.genreService.query().subscribe(
            (res: HttpResponse<IGenre[]>) => {
                this.genres = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInGenres();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IGenre) {
        return item.id;
    }

    registerChangeInGenres() {
        this.eventSubscriber = this.eventManager.subscribe('genreListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
