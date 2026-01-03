import { Component } from '@angular/core';
import { ChartData } from 'chart.js';
import { AnalyticsServices } from '../../../services/super-admin/analytics-services';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-analytics-dashboard-super-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, BaseChartDirective],
  templateUrl: './analytics-dashboard-super-admin.html',
  styleUrl: './analytics-dashboard-super-admin.scss',
})
export class AnalyticsDashboardSuperAdmin {

  metrics: any = {};
  userChartData: ChartData<'line'> = { labels: [], datasets: [] };
  revenueChartData: ChartData<'bar'> = { labels: [], datasets: [] };
  topSaloons: any[] = [];
  topN: number = 5;

  constructor(private analyicsDashboard: AnalyticsServices) { }

  loadMetrics() {
    this.analyicsDashboard.getMetrics().subscribe({
      next: (data) => this.metrics = data,
      error: () => this.metrics = {}
    });
  }

  loadUserChart(start?: string, end?: string) {
    this.analyicsDashboard.getUserGrowth(start || '', end || '').subscribe({
      next: (data) => {
        this.userChartData = {
          labels: data?.dates || [],
          datasets: [{
            label: 'Users',
            data: data?.values || [],
            borderColor: '#0d6efd',
            backgroundColor: 'rgba(13,110,253,0.2)',
            fill: true,
            tension: 0.3,
            pointRadius: 4,
            pointBackgroundColor: '#0d6efd'
          }]
        };
      },
      error: () => this.userChartData = { labels: [], datasets: [] }
    });
  }

  loadRevenueChart(start?: string, end?: string) {
    this.analyicsDashboard.getRevenue(start || '', end || '').subscribe({
      next: (data) => {
        this.revenueChartData = {
          labels: data?.dates || [],
          datasets: [{
            label: 'Revenue',
            data: data?.values || [],
            backgroundColor: '#198754'
          }]
        };
      },
      error: () => this.revenueChartData = { labels: [], datasets: [] }
    });
  }

  loadTopSaloons() {
    this.analyicsDashboard.getTopSaloons(this.topN).subscribe({
      next: (data) => this.topSaloons = data || [],
      error: () => this.topSaloons = []
    });
  }

  updateUserChart(startInput: HTMLInputElement, endInput: HTMLInputElement) {
    const start = startInput.value;
    const end = endInput.value;
    if (!start || !end) return;
    this.loadUserChart(start, end);
  }

  updateRevenueChart(startInput: HTMLInputElement, endInput: HTMLInputElement) {
    const start = startInput.value;
    const end = endInput.value;
    if (!start || !end) return;
    this.loadRevenueChart(start, end);
  }

  updateTopSaloons(nInput: HTMLInputElement) {
    const n = parseInt(nInput.value);
    if (isNaN(n) || n < 1) return;
    this.topN = n;
    this.loadTopSaloons();
  }

}
