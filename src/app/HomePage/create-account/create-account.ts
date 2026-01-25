import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-create-account',
  imports: [FormsModule],
  templateUrl: './create-account.html',
  styleUrl: './create-account.scss',
})
export class CreateAccount {

  onSubmit(form: any) {}

  goBack() {
    window.history.back();
  }
}
