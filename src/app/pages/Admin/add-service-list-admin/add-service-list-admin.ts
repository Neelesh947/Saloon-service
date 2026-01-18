import { CommonModule, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ServiceRequestDTO } from '../../../services/DTOs/service-request-dto';
import { ProvidedServicesBySaloon } from '../../../services/saloon-service/provided-services-by-saloon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-service-list-admin',
  imports: [NgIf, ReactiveFormsModule, CommonModule],
  templateUrl: './add-service-list-admin.html',
  styleUrl: './add-service-list-admin.scss',
})
export class AddServiceListAdmin implements OnInit {

  services: ServiceRequestDTO[] = [];

  serviceForm!: FormGroup;

  constructor(private provide_service: ProvidedServicesBySaloon, private fb: FormBuilder, private router: Router) { }

  ngOnInit(): void {
    this.serviceForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]),
      description: new FormControl('', [Validators.required, Validators.maxLength(255)]),
      category: new FormControl('', Validators.required),
      durationInMinutes: new FormControl('', [Validators.required, Validators.min(1)]),
      price: new FormControl('', [Validators.required, Validators.min(0)]),
      active: new FormControl(false)
    });
  }

  onSubmit() {
    if (this.serviceForm.invalid) {
      this.serviceForm.markAllAsTouched();
      return;
    }
    const newService: ServiceRequestDTO = this.serviceForm.value;
    this.provide_service.createService(newService).subscribe({
      next: (res) => {
        console.log('Service created successfully', res);
        this.serviceForm.reset();
      },
      error: (err) => console.error('Error creating service', err)
    });
  }
}
