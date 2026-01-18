import { Injectable } from '@angular/core';
import { Environments } from '../environments';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenStorageService } from '../auth-services/token-storage-services';
import { Observable } from 'rxjs';
import { SaloonServiceDTO } from '../DTOs/saloon-service-dto';
import { ServiceRequestDTO } from '../DTOs/service-request-dto';

@Injectable({
  providedIn: 'root',
})
export class ProvidedServicesBySaloon {

  private baseUrl = Environments.apiBaseUrl + 'Saloons/SaloonService';

  constructor(private http: HttpClient, private token_Service: TokenStorageService) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.token_Service.getAccessToken() || '';
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getServicesBySalon(): Observable<SaloonServiceDTO[]> {
    return this.http.get<SaloonServiceDTO[]>(`${this.baseUrl}/salon`, { headers: this.getAuthHeaders() });
  }

  getServiceById(serviceId: string): Observable<SaloonServiceDTO> {
    return this.http.get<SaloonServiceDTO>(`${this.baseUrl}/${serviceId}`, { headers: this.getAuthHeaders() });
  }

  createService(serviceDto: ServiceRequestDTO): Observable<SaloonServiceDTO> {
    const url = `${this.baseUrl}`;
    return this.http.post<SaloonServiceDTO>(url, serviceDto, { headers: this.getAuthHeaders() });
  }
}
