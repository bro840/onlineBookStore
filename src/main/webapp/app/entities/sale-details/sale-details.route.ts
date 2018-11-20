import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SaleDetails } from 'app/shared/model/sale-details.model';
import { SaleDetailsService } from './sale-details.service';
import { SaleDetailsComponent } from './sale-details.component';
import { SaleDetailsDetailComponent } from './sale-details-detail.component';
import { SaleDetailsUpdateComponent } from './sale-details-update.component';
import { SaleDetailsDeletePopupComponent } from './sale-details-delete-dialog.component';
import { ISaleDetails } from 'app/shared/model/sale-details.model';

@Injectable({ providedIn: 'root' })
export class SaleDetailsResolve implements Resolve<ISaleDetails> {
    constructor(private service: SaleDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((saleDetails: HttpResponse<SaleDetails>) => saleDetails.body));
        }
        return of(new SaleDetails());
    }
}

export const saleDetailsRoute: Routes = [
    {
        path: 'sale-details',
        component: SaleDetailsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleDetails'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale-details/:id/view',
        component: SaleDetailsDetailComponent,
        resolve: {
            saleDetails: SaleDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleDetails'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale-details/new',
        component: SaleDetailsUpdateComponent,
        resolve: {
            saleDetails: SaleDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleDetails'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sale-details/:id/edit',
        component: SaleDetailsUpdateComponent,
        resolve: {
            saleDetails: SaleDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleDetails'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const saleDetailsPopupRoute: Routes = [
    {
        path: 'sale-details/:id/delete',
        component: SaleDetailsDeletePopupComponent,
        resolve: {
            saleDetails: SaleDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SaleDetails'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
