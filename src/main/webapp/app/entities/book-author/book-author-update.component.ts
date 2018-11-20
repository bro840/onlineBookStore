import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IBookAuthor } from 'app/shared/model/book-author.model';
import { BookAuthorService } from './book-author.service';
import { IBook } from 'app/shared/model/book.model';
import { BookService } from 'app/entities/book';
import { IAuthor } from 'app/shared/model/author.model';
import { AuthorService } from 'app/entities/author';

@Component({
    selector: 'jhi-book-author-update',
    templateUrl: './book-author-update.component.html'
})
export class BookAuthorUpdateComponent implements OnInit {
    bookAuthor: IBookAuthor;
    isSaving: boolean;

    books: IBook[];

    authors: IAuthor[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private bookAuthorService: BookAuthorService,
        private bookService: BookService,
        private authorService: AuthorService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ bookAuthor }) => {
            this.bookAuthor = bookAuthor;
        });
        this.bookService.query().subscribe(
            (res: HttpResponse<IBook[]>) => {
                this.books = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.authorService.query().subscribe(
            (res: HttpResponse<IAuthor[]>) => {
                this.authors = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.bookAuthor.id !== undefined) {
            this.subscribeToSaveResponse(this.bookAuthorService.update(this.bookAuthor));
        } else {
            this.subscribeToSaveResponse(this.bookAuthorService.create(this.bookAuthor));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBookAuthor>>) {
        result.subscribe((res: HttpResponse<IBookAuthor>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackBookById(index: number, item: IBook) {
        return item.id;
    }

    trackAuthorById(index: number, item: IAuthor) {
        return item.id;
    }
}
