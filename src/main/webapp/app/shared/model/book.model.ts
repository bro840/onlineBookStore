import { ISaleDetails } from 'app/shared/model//sale-details.model';
import { IAuthor } from 'app/shared/model//author.model';
import { IGenre } from 'app/shared/model//genre.model';

export interface IBook {
    id?: number;
    title?: string;
    isbn?: string;
    quantity?: number;
    price?: number;
    saleDetails?: ISaleDetails[];
    authors?: IAuthor[];
    genres?: IGenre[];
}

export class Book implements IBook {
    constructor(
        public id?: number,
        public title?: string,
        public isbn?: string,
        public quantity?: number,
        public price?: number,
        public saleDetails?: ISaleDetails[],
        public authors?: IAuthor[],
        public genres?: IGenre[]
    ) {}
}
