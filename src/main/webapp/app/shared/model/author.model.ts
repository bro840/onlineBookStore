export const enum Country {
    Portugual = 'Portugual',
    Spain = 'Spain',
    Italy = 'Italy',
    England = 'England',
    France = 'France',
    Germany = 'Germany'
}

export interface IAuthor {
    id?: number;
    name?: string;
    country?: Country;
}

export class Author implements IAuthor {
    constructor(public id?: number, public name?: string, public country?: Country) {}
}
