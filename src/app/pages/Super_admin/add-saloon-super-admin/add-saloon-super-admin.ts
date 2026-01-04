import { Component, OnInit } from '@angular/core';
import { SaloonRequestDTO } from '../../../services/DTOs/saloon-request-dto';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SaloonServices } from '../../../services/saloon-service/saloon-services';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-saloon-super-admin',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-saloon-super-admin.html',
  styleUrl: './add-saloon-super-admin.scss',
})
export class AddSaloonSuperAdmin implements OnInit {

  saloon: SaloonRequestDTO[] = [];

  saloonForm!: FormGroup;

  constructor(private fb: FormBuilder, private saloon_service: SaloonServices, private router: Router) { }

  ngOnInit(): void {
    this.saloonForm = this.fb.group({
      saloonName: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      address: ['', [Validators.required, Validators.maxLength(255)]],
      phone: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(15)]],
      active: [false, [Validators.required]]
    });
  }

  onSubmit() {
    if (this.saloonForm.valid) {
      const saloonData: SaloonRequestDTO = this.saloonForm.value;
      this.saloon_service.createSaloon(saloonData).subscribe({
        next: (response) => {
          Swal.fire('Success', 'Saloon updated successfully!', 'success').then(() => {
            this.router.navigate(['/super-admin-dashboard/saloon-management-admin']);
          })
        }
      })
    } else {
      this.saloonForm.markAllAsTouched();
    }
  }
}
