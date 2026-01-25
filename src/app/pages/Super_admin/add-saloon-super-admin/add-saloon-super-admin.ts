import { Component, OnInit } from '@angular/core';
import { SaloonRequestDTO } from '../../../services/DTOs/saloon-request-dto';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SaloonServices } from '../../../services/saloon-service/saloon-services';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { AdminService } from '../../../services/super-admin/admin-service';
import { KeycloakUserDto } from '../../../services/DTOs/keycloak-user-dto';

@Component({
  selector: 'app-add-saloon-super-admin',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-saloon-super-admin.html',
  styleUrl: './add-saloon-super-admin.scss',
})
export class AddSaloonSuperAdmin implements OnInit {

  saloon: SaloonRequestDTO[] = [];

  adminForm!: FormGroup;

  constructor(private fb: FormBuilder, private admin_service: AdminService, private router: Router) { }

  ngOnInit(): void {
    this.adminForm = new FormGroup({
      username: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]),
      emailAddress: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required, Validators.minLength(6)]),
      firstName: new FormControl(''),
      lastName: new FormControl(''),
      isEnabled: new FormControl(true),
      phoneNumber: new FormControl('', [Validators.required, Validators.minLength(8), Validators.maxLength(15)]),
      address: new FormControl('', [Validators.required, Validators.maxLength(255)])
    });
  }

  onSubmit() {
    if (this.adminForm.invalid) {
      this.adminForm.markAllAsTouched();
      return;
    }

    const formValue = this.adminForm.value;

    const adminDto: KeycloakUserDto = {
      username: formValue.username,
      emailAddress: formValue.emailAddress,
      password: formValue.password,
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      enabled: formValue.isEnabled,
      phoneNumber: formValue.phoneNumber,
      address: formValue.address
    };

    this.admin_service.createAdmin(adminDto).subscribe({
      next: (res) => {
        this.adminForm.reset({ isEnabled: true });
        this.router.navigate(['/super-admin-dashboard/saloon-management-admin']);
      },
      error: (err) => {
        console.error('Error creating admin', err);
      }
    });
  }
}
