import { BasketService } from './../../../entities/basket/basket.service';
import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-basket-icon',
    templateUrl: './basket-icon.component.html',
    styleUrls: ["./basket-icon.component.css"]
})
export class BasketIconComponent implements OnInit {

    private classes: {};
    private icon: string = "shopping-basket";

    constructor(private basketService: BasketService) { }

    ngOnInit() {

        this.basketService.basketChanged().subscribe(resp => {

            if(resp === "basketItemAdded") {
                this.icon = "cart-plus";
                this.classes = { "text-success" : "true" };
            }
            else if(resp === "basketItemRemoved") {
                this.icon = "cart-arrow-down";
                this.classes = { "text-danger" : "true" };
            }

            setTimeout(() => {
                this.icon = "shopping-basket"
                this.classes = { "text-secondary" : "true" };
            }, 500);
        });
    }



}
