import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SalonResponseDTO } from '../../../services/DTOs/salon-response-dto';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SaloonServices } from '../../../services/saloon-service/saloon-services';
import Swal from 'sweetalert2';
import { SaloonRequestDTO } from '../../../services/DTOs/saloon-request-dto';

@Component({
  selector: 'app-saloon-list-admin',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
  templateUrl: './saloon-list-admin.html',
  styleUrls: ['./saloon-list-admin.scss'],
})
export class SaloonListAdmin implements OnInit {

  saloons: SalonResponseDTO[] = [];
  profileForms: { [key: string]: FormGroup } = {};
  passwordForms: { [key: string]: FormGroup } = {};

  constructor(private saloon_service: SaloonServices,
    private cdr: ChangeDetectorRef,
    private fb: FormBuilder) { }

  ngOnInit(): void {
    this.loadSaloons();
  }

  loadSaloons(): void {
    this.saloon_service.getListOfLinkedSaloonWithTheUser().subscribe({
      next: (res: any) => {
        this.saloons = res.content;

        // Initialize form for each salon with only editable fields
        this.saloons.forEach(salon => {
          this.profileForms[salon.id] = this.fb.group({
            saloonName: [salon.saloonName],
            address: [salon.address],
            phone: [salon.phone]
          });
          this.passwordForms[salon.id] = this.fb.group({
            newPassword: [''],
            confirmPassword: ['']
          });
        });
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching saloons', err);
      }
    });
  }

  saveProfile(salonId: string) {
    const form = this.profileForms[salonId];
    if (!form) {
      Swal.fire('Error', 'Form not found for this salon.', 'error');
      return;
    }
    if (form.valid) {
      const updatedProfile = form.getRawValue();
      const request: SaloonRequestDTO = {
        saloonName: updatedProfile.saloonName,
        address: updatedProfile.address,
        phone: updatedProfile.phone,
        active: true
      };
      console.log(request,"update")
      this.saloon_service.updateSaloon(salonId, request).subscribe({
        next: (updatedSalon) => {
          this.profileForms[salonId].patchValue({
            saloonName: updatedSalon.saloonName,
            address: updatedSalon.address,
            phone: updatedSalon.phone
          });
          Swal.fire('Success', 'Profile updated successfully!', 'success');
        },
        error: (err) => {
          console.error('Error updating salon profile:', err);
          Swal.fire('Error', 'Failed to update profile. Please try again.', 'error');
        }
      })
    } else {
      Swal.fire('Error', 'Please fill all required fields.', 'error');
    }
  }

  updatePassword(id: string) { }
}