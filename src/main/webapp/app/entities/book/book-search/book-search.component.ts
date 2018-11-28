import { AccountService } from './../../../core/auth/account.service';
import { Basket } from './../../../shared/model/basket.model';
import { BasketService } from './../../basket/basket.service';
import { BookService } from 'app/entities/book';
import { Component, OnInit, Output } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { IBook, Book } from 'app/shared/model/book.model';

@Component({
    selector: 'jhi-book-search',
    templateUrl: './book-search.component.html',
    styles: []
})
export class BookSearchComponent implements OnInit {

    private userId : number;
    private books;
    private basket: Array<Basket> = new Array<Basket>();

    constructor(private bookService: BookService,
                private basketService: BasketService,
                private accounService: AccountService
               ) {
    }

    ngOnInit() {

        this.loadBooks();

        this.accounService.get().subscribe(res => {
            this.userId = +res.body.id;
            this.loadBasket(this.userId);
        });
    }

    loadBasket(userId: number) {
        this.basketService.getByUser(userId).subscribe(res => {
            this.basket = (res.body as Array<Basket>);
        });
    }

    loadBooks() {
        this.bookService.query().subscribe(
            (res: HttpResponse<IBook[]>) => {
                this.books = res.body;
            }
        );
    }

    searchBookByTitle(title:string): void {

        this.bookService.query({title: title}).subscribe(
            (res: HttpResponse<IBook[]>) => {
                this.books = res.body;
            }
        );
    }

    isBookInBasket(bookId: number) : boolean {
        return this.basket.find(x => x.book_id === bookId) == null ? false : true;

    }

    // on the server
    addBookToBasket(bookId: number): void{

        let basket: Basket = new Basket(null, this.userId, bookId);
        this.basketService.create(basket).subscribe(resp => {
            basket.id = resp.body.id;
            this.basket.push(basket);

            this.basketService.basketItemAdded();
        });
    }
    removeBookFromBasket(bookId: number): void {

        let basket: Basket = this.basket.find(x => x.book_id === bookId);

        this.basketService.remove(basket.id).subscribe(resp => {
            let index = this.basket.indexOf(basket);
            this.basket.splice(index,1);

            this.basketService.basketItemRemoved();
        });
    }
}
