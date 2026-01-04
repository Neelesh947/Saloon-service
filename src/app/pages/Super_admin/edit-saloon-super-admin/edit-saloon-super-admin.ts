import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SalonResponseDTO } from '../../../services/DTOs/salon-response-dto';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SaloonServices } from '../../../services/saloon-service/saloon-services';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-edit-saloon-super-admin',
  imports: [FormsModule, CommonModule],
  templateUrl: './edit-saloon-super-admin.html',
  styleUrl: './edit-saloon-super-admin.scss',
})
export class EditSaloonSuperAdmin implements OnInit {

  saloon: SalonResponseDTO = {
    id: '',
    saloonName: '',
    address: '',
    phone: '',
    active: true,
    services: [],
    createdBy: '',
    createdAt: '',
    updatedAt: ''
  };


  constructor(private route: ActivatedRoute, private router: Router, private saloon_service: SaloonServices, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    const saloonId = this.route.snapshot.paramMap.get('id');
    if (saloonId) {
      this.loadSaloon(saloonId);
    }
  }

  loadSaloon(id: string) {
    this.saloon_service.getSaloonById(id).subscribe({
      next: (res: any) => {
        this.saloon = res;
        this.cdr.detectChanges();
      }, error: (err) => {
        Swal.fire('Error', 'Something went wrong! ?', 'error');
      }
    })
  }

  saveSaloon() {
    if (!this.saloon.id) {
      Swal.fire('Error', 'Saloon ID is missing!', 'error');
      return;
    }
    this.saloon_service.updateSaloon(this.saloon.id, this.saloon).subscribe({
      next: (res: SalonResponseDTO) => {
        Swal.fire('Success', 'Saloon updated successfully!', 'success').then(() => {
          this.router.navigate(['/super-admin-dashboard/saloon-management-admin']);
        })
      },
      error: (err) => {
        console.error(err);
        Swal.fire('Error', 'Failed to update saloon. Please try again.', 'error');
      }
    })
  }

  cancel() {
    this.router.navigate(['/super-admin-dashboard/saloon-management-admin']);
  }
}
