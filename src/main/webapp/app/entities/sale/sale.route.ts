import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Sale } from 'app/shared/model/sale.model';
import { SaleService } from './sale.service';
import { SaleComponent } from './sale.component';
import { SaleDetailComponent } from './sale-detail.component';
import { SaleUpdateComponent } from './sale-update.component';
import { SaleDeletePopupComponent } from './sale-delete-dialog.component';
import { ISale } from 'app/shared/model/sale.model';

@Injectable({ providedIn: 'root' })
export class SaleResolve implements Resolve<ISale> {
    constructor(private service: SaleService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((sale: HttpResponse<Sale>) => sale.body));
        }
        return of(new Sale());
    }
}

export const saleRoute: Routes = [
    {
        path: 'sale',
        component: SaleComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sales'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale/:id/view',
        component: SaleDetailComponent,
        resolve: {
            sale: SaleResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sales'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale/new',
        component: SaleUpdateComponent,
        resolve: {
            sale: SaleResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sales'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale/:id/edit',
        component: SaleUpdateComponent,
        resolve: {
            sale: SaleResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sales'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const salePopupRoute: Routes = [
    {
        path: 'sale/:id/delete',
        component: SaleDeletePopupComponent,
        resolve: {
            sale: SaleResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sales'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
