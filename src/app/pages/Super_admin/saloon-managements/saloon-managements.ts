import { CommonModule, NgIf } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SalonResponseDTO } from '../../../services/DTOs/salon-response-dto';
import { SaloonServices } from '../../../services/saloon-service/saloon-services';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-saloon-managements',
  imports: [FormsModule, CommonModule, RouterModule],
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
  loadSaloons(page: number = this.pageInfo.pageNumber): void {
    const isEnabled = this.getIsEnabledFromFilter();
    this.saloonService.getSaloonsList(isEnabled, page, this.pageInfo.pageSize).subscribe({
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
    this.pageInfo.pageNumber = 0;
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

  deleteUser(saloon: any) {
    Swal.fire({
      title: 'Are you sure?',
      text: 'Once the user is deleted it never be recovered',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes',
      cancelButtonText: 'No',
      reverseButtons: true
    }).then((result) => {
      this.saloonService.deleteSaloon(saloon.id).subscribe({
        next: (response) => {
          Swal.fire('success', 'Saloon Deleted Successfully', 'success').then(() => {
            window.location.reload();
          })
        },
        error: (err) => {
          console.error('Error Deleting saloon:', err);
          Swal.fire('Error', 'Failed to delete Saloon', 'error');
        }
      })
    })
  }

  get totalPages(): number {
    return Math.ceil(this.pageInfo.totalElements / this.pageInfo.pageSize);
  }

  get isFirstPage(): boolean {
    return this.pageInfo.pageNumber === 0;
  }

  get isLastPage(): boolean {
    return this.saloons.length < this.pageInfo.pageSize;
  }

  goToNextPage(): void {
    if (!this.isLastPage) {
      this.pageInfo.pageNumber += 1;
      this.loadSaloons(this.pageInfo.pageNumber);
    }
  }

  goToPreviousPage(): void {
    if (!this.isFirstPage) {
      this.pageInfo.pageNumber -= 1;
      this.loadSaloons(this.pageInfo.pageNumber);
    }
  }
}
