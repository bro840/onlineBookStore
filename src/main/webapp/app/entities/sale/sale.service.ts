import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISale } from 'app/shared/model/sale.model';

type EntityResponseType = HttpResponse<ISale>;
type EntityArrayResponseType = HttpResponse<ISale[]>;

@Injectable({ providedIn: 'root' })
export class SaleService {
    public resourceUrl = SERVER_API_URL + 'api/sales';

    constructor(private http: HttpClient) {}

    create(sale: ISale): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sale);
        return this.http
            .post<ISale>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(sale: ISale): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sale);
        return this.http
            .put<ISale>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISale>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISale[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(sale: ISale): ISale {
        const copy: ISale = Object.assign({}, sale, {
            date: sale.date != null && sale.date.isValid() ? sale.date.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.date = res.body.date != null ? moment(res.body.date) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((sale: ISale) => {
            sale.date = sale.date != null ? moment(sale.date) : null;
        });
        return res;
    }
}
