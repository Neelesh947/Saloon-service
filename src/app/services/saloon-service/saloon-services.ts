import { Injectable } from '@angular/core';
import { Environments } from '../environments';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { SalonResponseDTO } from '../DTOs/salon-response-dto';
import { Observable } from 'rxjs';
import { TokenStorageService } from '../auth-services/token-storage-services';

@Injectable({
  providedIn: 'root',
})
export class SaloonServices {
  private baseUrl = Environments.apiBaseUrl + 'Saloons';

  constructor(private http: HttpClient, private token_Service: TokenStorageService) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.token_Service.getAccessToken() || '';
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getSaloonsList(isEnabled?: boolean, page: number = 0, size: number = 10): Observable<SalonResponseDTO> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (isEnabled !== undefined) {
      params = params.set('isEnabled', isEnabled.toString());
    }

    return this.http.get<SalonResponseDTO>(`${this.baseUrl}/list`, { params });
  }

  ActiveInactiveUsers(saloonId: string, isEnabled: boolean): Observable<any> {
    const url = `${this.baseUrl}/update-status/${saloonId}?isEnabled=${isEnabled}`;
    return this.http.patch(url, {}, { headers: this.getAuthHeaders() });
  }
}
