import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBookAuthor } from 'app/shared/model/book-author.model';

@Component({
    selector: 'jhi-book-author-detail',
    templateUrl: './book-author-detail.component.html'
})
export class BookAuthorDetailComponent implements OnInit {
    bookAuthor: IBookAuthor;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bookAuthor }) => {
            this.bookAuthor = bookAuthor;
        });
    }

    previousState() {
        window.history.back();
    }
}
