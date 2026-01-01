import { Injectable } from '@angular/core';
import { Environments } from '../environments';
import { HttpClient, HttpParams } from '@angular/common/http';
import { SalonResponseDTO } from '../DTOs/salon-response-dto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SaloonServices {
  private baseUrl = Environments.apiBaseUrl + 'Saloons';

  constructor(private http: HttpClient) { }

  getSaloonsList(isEnabled?: boolean, page: number = 0, size: number = 10): Observable<SalonResponseDTO> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (isEnabled !== undefined) {
      params = params.set('isEnabled', isEnabled.toString());
    }

    return this.http.get<SalonResponseDTO>(`${this.baseUrl}/list`, { params });
  }
}
