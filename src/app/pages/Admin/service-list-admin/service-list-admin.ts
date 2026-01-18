import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SaloonServiceDTO } from '../../../services/DTOs/saloon-service-dto';
import { ProvidedServicesBySaloon } from '../../../services/saloon-service/provided-services-by-saloon';
import { FormsModule } from '@angular/forms';
import { CommonModule, NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-service-list-admin',
  imports: [FormsModule, CommonModule, RouterModule],
  templateUrl: './service-list-admin.html',
  styleUrl: './service-list-admin.scss',
})
export class ServiceListAdmin implements OnInit {

  services: SaloonServiceDTO[] = [];
  pageInfo = {
    totalElements: 0,
    pageNumber: 0,
    pageSize: 10
  };

  // Default filter is 'true' to show active saloons
  statusFilter: string = 'true';

  constructor(private saloon_Service: ProvidedServicesBySaloon, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadServices();
  }

  loadServices(page: number = this.pageInfo.pageNumber): void {

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
    return this.services.length < this.pageInfo.pageSize;
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
