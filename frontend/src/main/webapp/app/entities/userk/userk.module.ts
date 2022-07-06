import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UserkComponent } from './list/userk.component';
import { UserkDetailComponent } from './detail/userk-detail.component';
import { UserkUpdateComponent } from './update/userk-update.component';
import { UserkDeleteDialogComponent } from './delete/userk-delete-dialog.component';
import { UserkRoutingModule } from './route/userk-routing.module';

@NgModule({
  imports: [SharedModule, UserkRoutingModule],
  declarations: [UserkComponent, UserkDetailComponent, UserkUpdateComponent, UserkDeleteDialogComponent],
  entryComponents: [UserkDeleteDialogComponent],
})
export class UserkModule {}
