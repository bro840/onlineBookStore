import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { IBasket } from 'app/shared/model/basket.model';
import { Observable, BehaviorSubject } from 'rxjs';

type EntityResponseType = HttpResponse<IBasket>;

@Injectable({
    providedIn: 'root'
})
export class BasketService {

    private basket = new BehaviorSubject<string>("");

    public resourceUrl = SERVER_API_URL + 'api/baskets';

    constructor(private http: HttpClient) { }

    create(basket: IBasket): Observable<EntityResponseType> {
        return this.http.post<IBasket>(this.resourceUrl, basket, { observe: 'response' });
    }

    remove(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    getByUser(id: number): Observable<EntityResponseType> {
        return this.http.get<IBasket>(`${this.resourceUrl}/user/${id}`, { observe: 'response' });
    }


    basketChanged(): BehaviorSubject<string> {
        return this.basket;
    }
    basketItemAdded() {
        this.basket.next("basketItemAdded");
    }
    basketItemRemoved() {
        this.basket.next("basketItemRemoved");
    }
}
