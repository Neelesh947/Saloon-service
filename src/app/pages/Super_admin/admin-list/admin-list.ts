import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/super-admin/admin-service';
import { SalonSignupRequestDTO } from '../../../services/DTOs/salon-signup-request-dto';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { AdminApprovalRequestDTO } from '../../../services/DTOs/admin-approval-request-dto';

@Component({
  selector: 'app-admin-list',
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admin-list.html',
  styleUrls: ['./admin-list.scss'],
})
export class AdminList implements OnInit {

  requests: SalonSignupRequestDTO[] = [];
  pageInfo = {
    totalElements: 0,
    pageNumber: 0,
    pageSize: 10
  };

  statusFilter: string = 'PENDING';
  searchTerm: string = '';

  constructor(private cdr: ChangeDetectorRef, private admin_service: AdminService) { }

  ngOnInit(): void {
    this.loadRequests();
  }

  onStatusChange() {
    this.pageInfo.pageNumber = 0;
    this.loadRequests();
  }

  loadRequests(page: number = this.pageInfo.pageNumber): void {
    this.admin_service.getPendingRequests(this.statusFilter)
      .subscribe({
        next: (res: SalonSignupRequestDTO[]) => {
          this.requests = res;
          this.pageInfo.totalElements = this.requests.length;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error fetching requests', err);
          this.requests = [];
        }
      });
  }

  approveRequest(requestId: string): void {
    Swal.fire({
      title: 'Approve Request',
      input: 'textarea',
      inputLabel: 'Reason for approval',
      inputPlaceholder: 'Enter remarks...',
      inputAttributes: { 'aria-label': 'Enter remarks' },
      showCancelButton: true,
      confirmButtonText: 'Approve',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed && result.value?.trim()) {
        const dto: AdminApprovalRequestDTO = {
          approved: true,
          remarks: result.value.trim()
        };

        this.admin_service.reviewAdminRequest(requestId, dto)
          .subscribe({
            next: () => {
              Swal.fire('Approved!', 'The admin request has been approved.', 'success');
              this.loadRequests(); // Refresh with updated status
            },
            error: (err) => {
              console.error('Error approving request', err);
              Swal.fire('Error', 'Failed to approve the request', 'error');
            }
          });
      } else if (result.isConfirmed) {
        Swal.fire('Error', 'Remarks are required to approve a request', 'warning');
      }
    });
  }

  rejectRequest(requestId: string): void {
    Swal.fire({
      title: 'Reject Request',
      input: 'textarea',
      inputLabel: 'Reason for rejection',
      inputPlaceholder: 'Enter remarks...',
      inputAttributes: { 'aria-label': 'Enter remarks' },
      showCancelButton: true,
      confirmButtonText: 'Reject',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed && result.value?.trim()) {
        const dto: AdminApprovalRequestDTO = {
          approved: false,
          remarks: result.value.trim()
        };

        this.admin_service.reviewAdminRequest(requestId, dto)
          .subscribe({
            next: () => {
              Swal.fire('Rejected!', 'The request has been rejected.', 'success');
              this.loadRequests();
            },
            error: (err) => {
              console.error('Error rejecting request', err);
              Swal.fire('Error', 'Failed to reject the request', 'error');
            }
          });
      } else if (result.isConfirmed) {
        Swal.fire('Error', 'Remarks are required to reject a request', 'warning');
      }
    });
  }

  onSearch() {
    this.pageInfo.pageNumber = 0;
    this.loadRequests();
  }

  get isFirstPage(): boolean {
    return this.pageInfo.pageNumber === 0;
  }

  get isLastPage(): boolean {
    return this.requests.length < this.pageInfo.pageSize;
  }

  goToNextPage(): void {
    if (!this.isLastPage) {
      this.pageInfo.pageNumber += 1;
      this.loadRequests(this.pageInfo.pageNumber);
    }
  }

  goToPreviousPage(): void {
    if (!this.isFirstPage) {
      this.pageInfo.pageNumber -= 1;
      this.loadRequests(this.pageInfo.pageNumber);
    }
  }
}