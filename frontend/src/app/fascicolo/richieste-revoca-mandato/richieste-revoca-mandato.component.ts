import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AuthService } from 'src/app/auth/auth.service';

@Component({
  selector: 'app-richieste-revoca-mandato',
  templateUrl: './richieste-revoca-mandato.component.html',
  styleUrls: ['./richieste-revoca-mandato.component.css']
})
export class RichiesteRevocaMandatoComponent implements OnInit {

  public selectedTab: string;
  public counter: number;
  private componentDestroyed$: Subject<boolean> = new Subject();
  public roleAppag: boolean = false;

  constructor(
    protected route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.setupRouting();
  }

  public handleTabChange(event) {
    switch (event.index) {
      case 0:
        this.selectedTab = 'DA_VALUTARE';
        this.router.navigate(['./'], { relativeTo: this.route.parent, queryParams: { tabselected: 'da-valutare' } });
        break;
      case 1:
        this.selectedTab = 'VALUTATE';
        this.router.navigate(['./'], { relativeTo: this.route.parent, queryParams: { tabselected: 'valutate' } });
        break;
    }
  }

  public setupRouting() {
    if (localStorage.getItem('selectedRole') == AuthService.roleAdmin) {
      this.roleAppag = true;
    }
    this.route.queryParams.pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(params => {
      const tmpSelectedTab: string = params['tabselected'];
      switch (tmpSelectedTab) {
        case 'da-valutare':
          this.selectedTab = 'DA_VALUTARE';
          break;
        case 'valutate':
          this.selectedTab = 'VALUTATE';
          break;
      }
    }
    );
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public updateCounter(count: number) {
    this.counter = count;
  }
}
