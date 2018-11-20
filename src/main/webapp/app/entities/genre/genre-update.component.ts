import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IGenre } from 'app/shared/model/genre.model';
import { GenreService } from './genre.service';

@Component({
    selector: 'jhi-genre-update',
    templateUrl: './genre-update.component.html'
})
export class GenreUpdateComponent implements OnInit {
    genre: IGenre;
    isSaving: boolean;

    constructor(private genreService: GenreService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ genre }) => {
            this.genre = genre;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.genre.id !== undefined) {
            this.subscribeToSaveResponse(this.genreService.update(this.genre));
        } else {
            this.subscribeToSaveResponse(this.genreService.create(this.genre));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IGenre>>) {
        result.subscribe((res: HttpResponse<IGenre>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
