import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { HomeService } from '../home/home.service';

@Injectable()
export class ProtocollataGuard implements CanActivate {

    constructor(private router: Router, private homeService: HomeService) { }
    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Promise<boolean> {
        return new Promise((resolve) => {
            console.log("Check Protocollata");
            this.homeService.verificaUtente().subscribe((res) => {
                if(res == true)
                    resolve(true);
                else
                    resolve(false);
            },
                err => {
                    resolve(false);
                });
        })
    }
}