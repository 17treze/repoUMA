import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-amministrazione',
  templateUrl: './amministrazione.component.html',
  styleUrls: ['./amministrazione.component.css']
})
export class AmministrazioneComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router, private messageService: MessageService) { }

  ngOnInit() {
    this.messageService.clear();
  }

}
