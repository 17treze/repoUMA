import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AuthService } from 'src/app/auth/auth.service';

@Component({
  selector: 'app-configurazione',
  templateUrl: './configurazione.component.html',
  styleUrls: ['./configurazione.component.scss']
})
export class ConfigurazioneUmaComponent implements OnInit {

  public displayCardConfigurazioneUma: boolean;

  constructor(private router: Router, private messageService: MessageService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.messageService.clear();
    this.displayCardConfigurazioneUma = localStorage.getItem('selectedRole') === AuthService.roleCaa;
    console.log('this.displayCardConfigurazioneUma: ' + this.displayCardConfigurazioneUma);
  }

}