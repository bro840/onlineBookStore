import { AccountService } from './../../../core/auth/account.service';
import { UserService, Account } from 'app/core';
import { BookService } from 'app/entities/book';
import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { IBook } from 'app/shared/model/book.model';

@Component({
    selector: 'jhi-book-search',
    templateUrl: './book-search.component.html',
    styles: []
})
export class BookSearchComponent implements OnInit {

    private books;

    constructor(private bookService: BookService) { }

    ngOnInit() {
        this.loadBooks();

    }

    loadBooks() {
        this.bookService.query().subscribe(
            (res: HttpResponse<IBook[]>) => {
                this.books = res.body;
            }
        );
    }

    searchBookByTitle(title:string) : void {

        this.bookService.query({title: title}).subscribe(
            (res: HttpResponse<IBook[]>) => {
                this.books = res.body;
            }
        );
    }



}
