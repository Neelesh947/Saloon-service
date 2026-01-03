import { Injectable } from '@angular/core';
import { Environments } from '../environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AnalyticsServices {
  private baseUrl = Environments.apiBaseUrl + 'Saloons';

  constructor(private http: HttpClient) { }

  getMetrics(): Observable<any> {
    return this.http.get(`${this.baseUrl}/metrics`);
  }

  getUserGrowth(start: string, end: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/users?start=${start}&end=${end}`);
  }

  getRevenue(start: string, end: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/revenue?start=${start}&end=${end}`);
  }

  getTopSaloons(n: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/top-saloons?n=${n}`);
  }
}
