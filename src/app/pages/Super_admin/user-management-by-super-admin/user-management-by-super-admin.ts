import { CommonModule, NgIf } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { UserResponseDTO } from '../../../services/DTOs/user-response-dto';
import { UserService } from '../../../services/user/user-service';

@Component({
  selector: 'app-user-management-by-super-admin',
  imports: [[FormsModule, CommonModule, RouterModule, NgIf]],
  templateUrl: './user-management-by-super-admin.html',
  styleUrl: './user-management-by-super-admin.scss',
})
export class UserManagementBySuperAdmin implements OnInit {
  users: UserResponseDTO[] = [];
  // Default filter is 'true' to show active saloons
  statusFilter: string = 'true';
  pageInfo = {
    totalElements: 0,
    pageNumber: 0,
    pageSize: 10
  };

  constructor(private user_service: UserService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(page: number = this.pageInfo.pageNumber): void {
    const isEnabled = this.getIsEnabledFromFilter();
    this.user_service.getUserList(isEnabled, page, this.pageInfo.pageSize).subscribe({
      next: (res: any) => {
        this.users = res.content;
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

  onStatusChange() { }

  dissableEnableUser(user: any) { }

  deleteUser(user: any) { }

  get totalPages(): number {
    return Math.ceil(this.pageInfo.totalElements / this.pageInfo.pageSize);
  }

  get isFirstPage(): boolean {
    return this.pageInfo.pageNumber === 0;
  }

  get isLastPage(): boolean {
    return this.users.length < this.pageInfo.pageSize;
  }

  goToNextPage(): void {
    if (!this.isLastPage) {
      this.pageInfo.pageNumber += 1;
      this.loadUsers(this.pageInfo.pageNumber);
    }
  }

  goToPreviousPage(): void {
    if (!this.isFirstPage) {
      this.pageInfo.pageNumber -= 1;
      this.loadUsers(this.pageInfo.pageNumber);
    }
  }
}
