import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBookAuthor } from 'app/shared/model/book-author.model';
import { BookAuthorService } from './book-author.service';

@Component({
    selector: 'jhi-book-author-delete-dialog',
    templateUrl: './book-author-delete-dialog.component.html'
})
export class BookAuthorDeleteDialogComponent {
    bookAuthor: IBookAuthor;

    constructor(private bookAuthorService: BookAuthorService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bookAuthorService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'bookAuthorListModification',
                content: 'Deleted an bookAuthor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-book-author-delete-popup',
    template: ''
})
export class BookAuthorDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bookAuthor }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BookAuthorDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.bookAuthor = bookAuthor;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
