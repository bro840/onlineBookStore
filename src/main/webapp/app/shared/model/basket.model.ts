export interface IBasket {
    id?: number;
    user_id?: number;
    book_id?: number;
}

export class Basket implements IBasket {
    constructor(public id?: number, public user_id?: number, public book_id?: number) {}
}
