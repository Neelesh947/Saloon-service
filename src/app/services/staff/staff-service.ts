import { Injectable } from '@angular/core';
import { Environments } from '../environments';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { TokenStorageService } from '../auth-services/token-storage-services';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../DTOs/paginated-response';
import { StaffResponseDTO } from '../DTOs/staff-response-dto';
import { StaffRequestDTO } from '../DTOs/staff-request-dto';

@Injectable({
  providedIn: 'root',
})
export class StaffService {
  private baseUrl = Environments.apiBaseUrl + 'Staff';

  constructor(private http: HttpClient, private token_Service: TokenStorageService) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.token_Service.getAccessToken() || '';
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getListOfStaffToAdmin(isEnabled?: boolean, page: number = 0, size: number = 10): Observable<PaginatedResponse<StaffResponseDTO>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (isEnabled !== undefined) {
      params = params.set('isEnabled', isEnabled.toString());
    }
    const url = `${this.baseUrl}/list`;
    return this.http.get<PaginatedResponse<StaffResponseDTO>>(url, { params, headers: this.getAuthHeaders() });
  }

  addStaff(staff: StaffRequestDTO): Observable<{ [key: string]: string }> {
    const url = `${this.baseUrl}`;
    return this.http.post<{ [key: string]: string }>(url, staff, { headers: this.getAuthHeaders() });
  }
}
