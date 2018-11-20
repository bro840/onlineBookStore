import { IBook } from 'app/shared/model//book.model';
import { IAuthor } from 'app/shared/model//author.model';

export interface IBookAuthor {
    id?: number;
    bookId?: IBook;
    authorId?: IAuthor;
}

export class BookAuthor implements IBookAuthor {
    constructor(public id?: number, public bookId?: IBook, public authorId?: IAuthor) {}
}
