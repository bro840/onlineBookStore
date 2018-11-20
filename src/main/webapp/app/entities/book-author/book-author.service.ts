import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBookAuthor } from 'app/shared/model/book-author.model';

type EntityResponseType = HttpResponse<IBookAuthor>;
type EntityArrayResponseType = HttpResponse<IBookAuthor[]>;

@Injectable({ providedIn: 'root' })
export class BookAuthorService {
    public resourceUrl = SERVER_API_URL + 'api/book-authors';

    constructor(private http: HttpClient) {}

    create(bookAuthor: IBookAuthor): Observable<EntityResponseType> {
        return this.http.post<IBookAuthor>(this.resourceUrl, bookAuthor, { observe: 'response' });
    }

    update(bookAuthor: IBookAuthor): Observable<EntityResponseType> {
        return this.http.put<IBookAuthor>(this.resourceUrl, bookAuthor, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IBookAuthor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IBookAuthor[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
