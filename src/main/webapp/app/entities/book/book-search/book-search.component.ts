import { AccountService } from './../../../core/auth/account.service';
import { Basket } from './../../../shared/model/basket.model';
import { BasketService } from './../../basket/basket.service';
import { BookService } from 'app/entities/book';
import { Component, OnInit, Output } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { IBook, Book } from 'app/shared/model/book.model';
import { Author, IAuthor } from 'app/shared/model/author.model';
import { AuthorService } from 'app/entities/author';

@Component({
    selector: 'jhi-book-search',
    templateUrl: './book-search.component.html',
    styleUrls: ["./book-search.component.css"]
})
export class BookSearchComponent implements OnInit {

    private listStyle: boolean = false;
    private userId: number;
    private books: Array<Book> = new Array<Book>();
    private authors: Array<Author> = new Array<Author>();
    private basket: Array<Basket> = new Array<Basket>();

    constructor(private bookService: BookService,
        private basketService: BasketService,
        private authorService: AuthorService,
        private accounService: AccountService
    ) {
    }


    // loads component methods
    ngOnInit() {

        this.loadBooks();
        this.loasAuthors();
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
    loasAuthors() {
        this.authorService.query().subscribe(
            (res: HttpResponse<IAuthor[]>) => {
                this.authors = res.body;
            }
        );
    }

    bookSearch(title: string, author: string) {

        this.bookService.query(
            {
                title: title,
                author: author
            }).subscribe(
            (res: HttpResponse<IBook[]>) => {
                this.books = res.body;
            }
        );
    }



    // array methods
    isBookInBasket(bookId: number): boolean {
        return this.basket.find(x => x.book_id === bookId) == null ? false : true;
    }



    // updates basket database table
    addBookToBasket(bookId: number): void {

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
            this.basket.splice(index, 1);

            this.basketService.basketItemRemoved();
        });
    }


    // img url method
    generateUrl(bookImg?: string): string {

        if(bookImg == null) {
            return "../../../../content/images/book.png";
        }

        return "../../../../content/images/" + bookImg;
    }



    // list style methods
    toggleListStyleBlock() {
        this.listStyle = false;
    }
    toggleListStyleList() {
        this.listStyle = true;
    }
}
