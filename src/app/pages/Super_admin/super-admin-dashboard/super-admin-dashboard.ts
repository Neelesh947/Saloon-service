import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { SalonResponseDTO } from '../../../services/DTOs/salon-response-dto';
import { SaloonServices } from '../../../services/saloon-service/saloon-services';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-super-admin-dashboard',
  imports: [CommonModule, FormsModule],
  templateUrl: './super-admin-dashboard.html',
  styleUrls: ['./super-admin-dashboard.scss'], // <-- fixed typo
})
export class SuperAdminDashboard implements OnInit {
  saloons: SalonResponseDTO[] = [];
  pageInfo = {
    totalElements: 0,
    pageNumber: 0,
    pageSize: 10
  };

  // Default filter is 'true' to show active saloons
  statusFilter: string = 'true';

  constructor(private saloonService: SaloonServices) { }

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
}