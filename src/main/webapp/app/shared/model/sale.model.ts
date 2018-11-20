import { Moment } from 'moment';
import { ISaleDetails } from 'app/shared/model//sale-details.model';
import { IUser } from 'app/core/user/user.model';

export interface ISale {
    id?: number;
    date?: Moment;
    saleDetails?: ISaleDetails[];
    user?: IUser;
}

export class Sale implements ISale {
    constructor(public id?: number, public date?: Moment, public saleDetails?: ISaleDetails[], public user?: IUser) {}
}
