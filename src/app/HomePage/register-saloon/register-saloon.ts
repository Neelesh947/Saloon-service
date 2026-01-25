import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { AdminService } from '../../services/super-admin/admin-service';
import { SalonSignupRequestDTO } from '../../services/DTOs/salon-signup-request-dto';

@Component({
  selector: 'app-register-saloon',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './register-saloon.html',
  styleUrl: './register-saloon.scss',
})
export class RegisterSaloon {

  registerForm!: FormGroup;

  constructor(private fb: FormBuilder, private admin_service: AdminService) { }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      ownerName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobile: ['', Validators.required],
      salonName: ['', Validators.required]
    });
  }

  get f() {
    return this.registerForm.controls;
  }

  registerSalon() {
    if (this.registerForm.valid) {

      const val = this.registerForm.value;
      const dto: SalonSignupRequestDTO = {
        ownerName: val.ownerName,
        email: val.email,
        mobile: val.mobile,
        salonName: val.salonName
      };

      // Call service and handle response
      this.admin_service.createRequestForAdminApproval(dto)
        .subscribe({
          next: (res) => {
            Swal.fire('Success', 'Salon request submitted for approval!', 'success');
            this.registerForm.reset();
          },
          error: (err) => {
            console.error('Error submitting salon request:', err);
            Swal.fire({
              icon: 'success',
              title: 'Success',
              html: `
    Salon request submitted for approval!<br/><br/>
    Kindly wait for approval and stay tuned.<br/>
    An update will be passed on via email.
  `,
              confirmButtonText: 'Okay'
            });
          }
        });

    } else {
      Swal.fire('Error', 'Please complete the form!', 'error');
    }
  }


  get ownerName() { return this.registerForm.get('ownerName'); }
  get email() { return this.registerForm.get('email'); }
  get mobile() { return this.registerForm.get('mobile'); }
  get salonName() { return this.registerForm.get('salonName'); }

}
