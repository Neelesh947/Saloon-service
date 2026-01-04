import { CommonModule, NgIf } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SalonResponseDTO } from '../../../services/DTOs/salon-response-dto';
import { SaloonServices } from '../../../services/saloon-service/saloon-services';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-saloon-managements',
  imports: [FormsModule, CommonModule, RouterModule, NgIf],
  templateUrl: './saloon-managements.html',
  styleUrl: './saloon-managements.scss',
})
export class SaloonManagements {
  saloons: SalonResponseDTO[] = [];
  pageInfo = {
    totalElements: 0,
    pageNumber: 0,
    pageSize: 10
  };

  // Default filter is 'true' to show active saloons
  statusFilter: string = 'true';

  constructor(private saloonService: SaloonServices, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadSaloons();
  }

  // Load saloons based on the current statusFilter
  loadSaloons(): void {
    const isEnabled = this.getIsEnabledFromFilter();
    this.saloonService.getSaloonsList(isEnabled, 0, this.pageInfo.pageSize).subscribe({
      next: (res: any) => {
        this.saloons = res.content;
        this.pageInfo = res.pageInfo;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching saloons', err);
      }
    });
  }

  // Convert statusFilter string to boolean | undefined
  private getIsEnabledFromFilter(): boolean | undefined {
    if (this.statusFilter === 'true') return true;
    if (this.statusFilter === 'false') return false;
    return undefined; // for 'all'
  }

  // Called when user changes the select dropdown
  onStatusChange(): void {
    this.loadSaloons();
  }

  dissableEnableUser(saloon: any) {

    const newStatus = !saloon.active;
    Swal.fire({
      title: 'Are you sure?',
      text: newStatus
        ? `Do you want to activate ${saloon.saloonName}?`
        : `Do you want to inactivate ${saloon.saloonName}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        this.saloonService.ActiveInactiveUsers(saloon.id, newStatus).subscribe({
          next: () => {
            saloon.active = newStatus;
            Swal.fire(
              newStatus ? 'Activated!' : 'Inactivated!',
              `${saloon.saloonName} has been ${newStatus ? 'activated' : 'inactivated'
              }.`,
              'success'
            );
            window.location.reload();
          },
          error: (err) => {
            console.error('Error updating saloon status:', err);
            Swal.fire('Error', 'Failed to mark saloon inactive.', 'error');
          }
        });
      }
    });
  }
}
