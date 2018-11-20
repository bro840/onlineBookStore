import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISaleDetails } from 'app/shared/model/sale-details.model';

type EntityResponseType = HttpResponse<ISaleDetails>;
type EntityArrayResponseType = HttpResponse<ISaleDetails[]>;

@Injectable({ providedIn: 'root' })
export class SaleDetailsService {
    public resourceUrl = SERVER_API_URL + 'api/sale-details';

    constructor(private http: HttpClient) {}

    create(saleDetails: ISaleDetails): Observable<EntityResponseType> {
        return this.http.post<ISaleDetails>(this.resourceUrl, saleDetails, { observe: 'response' });
    }

    update(saleDetails: ISaleDetails): Observable<EntityResponseType> {
        return this.http.put<ISaleDetails>(this.resourceUrl, saleDetails, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISaleDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISaleDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
