import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SaloonServiceDTO } from '../../../services/DTOs/saloon-service-dto';
import { StaffService } from '../../../services/staff/staff-service';
import { ProvidedServicesBySaloon } from '../../../services/saloon-service/provided-services-by-saloon';
import { StaffRequestDTO } from '../../../services/DTOs/staff-request-dto';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-staff-by-admin',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './add-staff-by-admin.html',
  styleUrl: './add-staff-by-admin.scss',
})
export class AddStaffByAdmin implements OnInit {

  staffForm!: FormGroup;
  services: SaloonServiceDTO[] = [];
  loading = false;
  realm = 'my-realm';

  constructor(private fb: FormBuilder, private staff_Service: StaffService, private salon_Service: ProvidedServicesBySaloon, private router: Router) { }

  ngOnInit(): void {
    this.initForm();
    this.loadServices();
  }

  initForm(): void {
    this.staffForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      enabled: ['', Validators.required],
      emailAddress: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      serviceIds: [[]]
    });
  }

  loadServices(): void {
    this.salon_Service.getServicesBySalon().subscribe({
      next: (data) => {
        console.log('Services from API:', data);  // ✅ log here
        this.services = data;
      },
      error: (err) => {
        console.error('Failed to load services:', err);
      }
    });
  }


  onSubmit(): void {
    if (this.staffForm.invalid) {
      return;
    }

    this.loading = true;

    const payload: StaffRequestDTO = {
      ...this.staffForm.value
    };
    if (!payload.serviceIds || payload.serviceIds.length === 0) {
      delete payload.serviceIds;
    }
    this.staff_Service.addStaff(payload).subscribe({
      next: (response) => {
        this.loading = false;
        Swal.fire({
          icon: 'success',
          title: 'Staff Created',
          text: 'The staff member has been successfully added.',
          confirmButtonText: 'OK'
        }).then(() => {
          this.router.navigate(['/admin-dashboard/staf-list-by-admin']);
        });
      },
      error: (err) => {
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Failed to create staff. Please try again.',
          confirmButtonText: 'OK'
        });
        console.error('Failed to create staff:', err);
      }
    });
  }

  toggleService(serviceId: string, checked: boolean) {
    const current = this.staffForm.value.serviceIds as string[];
    if (checked) {
      this.staffForm.patchValue({ serviceIds: [...current, serviceId] });
    } else {
      this.staffForm.patchValue({ serviceIds: current.filter(id => id !== serviceId) });
    }
  }

  get selectedServicesNames(): string {
    const selectedIds = this.staffForm.value.serviceIds as string[];
    if (!selectedIds || selectedIds.length === 0) {
      return 'Select services';
    }

    // map ids to names
    return selectedIds
      .map(id => this.services.find(s => s.id === id)?.name)
      .filter(name => !!name)
      .join(', ');
  }

}
