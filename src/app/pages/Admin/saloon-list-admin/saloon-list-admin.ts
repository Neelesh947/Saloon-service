import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SalonResponseDTO } from '../../../services/DTOs/salon-response-dto';
import { CommonModule, NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SaloonServices } from '../../../services/saloon-service/saloon-services';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-saloon-list-admin',
  imports: [FormsModule, CommonModule, RouterModule, NgIf],
  templateUrl: './saloon-list-admin.html',
  styleUrl: './saloon-list-admin.scss',
})
export class SaloonListAdmin implements OnInit {

  saloons: SalonResponseDTO[] = [];
  pageInfo = {
    totalElements: 0,
    pageNumber: 0,
    pageSize: 10
  };

  statusFilter: string = 'true';

  constructor(private saloon_service: SaloonServices, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadSaloons();
  }

  onStatusChange() {
    this.pageInfo.pageNumber = 0;
    this.loadSaloons();
  }

  loadSaloons(page: number = this.pageInfo.pageNumber): void {
    const isEnabled = this.getIsEnabledFromFilter();
    this.saloon_service.getListOfLinkedSaloonWithTheUser(isEnabled, page, this.pageInfo.pageSize).subscribe({
      next: (res: any) => {
        this.saloons = res.content;
        this.pageInfo = res.pageInfo;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching saloons', err);
      }
    })
  }

  private getIsEnabledFromFilter(): boolean | undefined {
    if (this.statusFilter === 'true') return true;
    if (this.statusFilter === 'false') return false;
    return undefined; // for 'all'
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
        this.saloon_service.ActiveInactiveUsers(saloon.id, newStatus).subscribe({
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
      this.saloon_service.deleteSaloon(saloon.id).subscribe({
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
