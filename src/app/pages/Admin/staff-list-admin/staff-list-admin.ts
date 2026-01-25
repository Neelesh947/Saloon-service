import { ChangeDetectorRef, Component } from '@angular/core';
import { StaffResponseDTO } from '../../../services/DTOs/staff-response-dto';
import { StaffService } from '../../../services/staff/staff-service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-staff-list-admin',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './staff-list-admin.html',
  styleUrl: './staff-list-admin.scss',
})
export class StaffListAdmin {

  staffs: StaffResponseDTO[] = [];
  pageInfo = {
    totalElements: 0,
    pageNumber: 0,
    pageSize: 10
  };

  // Default filter is 'true' to show active saloons
  statusFilter: string = 'true';

  constructor(private staff_service: StaffService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadServices();
  }

  loadServices(page: number = this.pageInfo.pageNumber): void {
    const isEnabled = this.getIsEnabledFromFilter();
    this.staff_service.getListOfStaffToAdmin(isEnabled, page, this.pageInfo.pageSize).subscribe({
      next: (res: any) => {
        this.staffs = res.content;
        this.cdr.detectChanges();
      }, error: (err) => {
        console.error('Error fetching saloons', err);
      }
    })
  }

  dissableEnableService(service: any) { }

  deleteService(service: any) { }

  private getIsEnabledFromFilter(): boolean | undefined {
    if (this.statusFilter === 'true') return true;
    if (this.statusFilter === 'false') return false;
    return undefined; // for 'all'
  }

  onStatusChange(): void {
    this.pageInfo.pageNumber = 0;
    this.loadServices();
  }

  get totalPages(): number {
    return Math.ceil(this.pageInfo.totalElements / this.pageInfo.pageSize);
  }

  get isFirstPage(): boolean {
    return this.pageInfo.pageNumber === 0;
  }

  get isLastPage(): boolean {
    return this.staffs.length < this.pageInfo.pageSize;
  }

  goToNextPage(): void {
    if (!this.isLastPage) {
      this.pageInfo.pageNumber += 1;
      this.loadServices(this.pageInfo.pageNumber);
    }
  }

  goToPreviousPage(): void {
    if (!this.isFirstPage) {
      this.pageInfo.pageNumber -= 1;
      this.loadServices(this.pageInfo.pageNumber);
    }
  }
}
