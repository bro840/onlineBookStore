import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { BookAuthor } from 'app/shared/model/book-author.model';
import { BookAuthorService } from './book-author.service';
import { BookAuthorComponent } from './book-author.component';
import { BookAuthorDetailComponent } from './book-author-detail.component';
import { BookAuthorUpdateComponent } from './book-author-update.component';
import { BookAuthorDeletePopupComponent } from './book-author-delete-dialog.component';
import { IBookAuthor } from 'app/shared/model/book-author.model';

@Injectable({ providedIn: 'root' })
export class BookAuthorResolve implements Resolve<IBookAuthor> {
    constructor(private service: BookAuthorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((bookAuthor: HttpResponse<BookAuthor>) => bookAuthor.body));
        }
        return of(new BookAuthor());
    }
}

export const bookAuthorRoute: Routes = [
    {
        path: 'book-author',
        component: BookAuthorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookAuthors'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'book-author/:id/view',
        component: BookAuthorDetailComponent,
        resolve: {
            bookAuthor: BookAuthorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookAuthors'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'book-author/new',
        component: BookAuthorUpdateComponent,
        resolve: {
            bookAuthor: BookAuthorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookAuthors'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'book-author/:id/edit',
        component: BookAuthorUpdateComponent,
        resolve: {
            bookAuthor: BookAuthorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookAuthors'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bookAuthorPopupRoute: Routes = [
    {
        path: 'book-author/:id/delete',
        component: BookAuthorDeletePopupComponent,
        resolve: {
            bookAuthor: BookAuthorResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookAuthors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
