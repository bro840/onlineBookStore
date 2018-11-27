import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Genre } from 'app/shared/model/genre.model';
import { GenreService } from './genre.service';
import { GenreComponent } from './genre.component';
import { GenreDetailComponent } from './genre-detail.component';
import { GenreUpdateComponent } from './genre-update.component';
import { GenreDeletePopupComponent } from './genre-delete-dialog.component';
import { IGenre } from 'app/shared/model/genre.model';

@Injectable({ providedIn: 'root' })
export class GenreResolve implements Resolve<IGenre> {
    constructor(private service: GenreService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((genre: HttpResponse<Genre>) => genre.body));
        }
        return of(new Genre());
    }
}

export const genreRoute: Routes = [
    {
        path: 'genre',
        component: GenreComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Genres'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'genre/:id/view',
        component: GenreDetailComponent,
        resolve: {
            genre: GenreResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Genres'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'genre/new',
        component: GenreUpdateComponent,
        resolve: {
            genre: GenreResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Genres'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'genre/:id/edit',
        component: GenreUpdateComponent,
        resolve: {
            genre: GenreResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Genres'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const genrePopupRoute: Routes = [
    {
        path: 'genre/:id/delete',
        component: GenreDeletePopupComponent,
        resolve: {
            genre: GenreResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'Genres'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
