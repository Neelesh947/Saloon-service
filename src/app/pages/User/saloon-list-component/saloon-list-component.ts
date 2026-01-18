import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { SalonResponseDTO } from '../../../services/DTOs/salon-response-dto';
import { SaloonServices } from '../../../services/saloon-service/saloon-services';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-saloon-list-component',
  imports: [CommonModule],
  templateUrl: './saloon-list-component.html',
  styleUrl: './saloon-list-component.scss',
})
export class SaloonListComponent implements OnInit {

  saloons: SalonResponseDTO[] = [];
  pageInfo = {
    totalElements: 0,
    pageNumber: 0,
    pageSize: 12
  };

  constructor(private saloonService: SaloonServices, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadSaloons();
  }

  loadSaloons(page: number = this.pageInfo.pageNumber): void {
    this.saloonService.getSaloonsList(true, page, this.pageInfo.pageSize).subscribe({
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
