import { ISale } from 'app/shared/model//sale.model';
import { IBook } from 'app/shared/model//book.model';

export interface ISaleDetails {
    id?: number;
    quantity?: number;
    sale?: ISale;
    book?: IBook;
}

export class SaleDetails implements ISaleDetails {
    constructor(public id?: number, public quantity?: number, public sale?: ISale, public book?: IBook) {}
}
