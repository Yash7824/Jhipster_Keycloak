import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserk } from '../userk.model';

@Component({
  selector: 'jhi-userk-detail',
  templateUrl: './userk-detail.component.html',
})
export class UserkDetailComponent implements OnInit {
  userk: IUserk | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userk }) => {
      this.userk = userk;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
