import { Injectable } from '@angular/core';
import { Environments } from '../environments';
import { TokenStorageService } from '../auth-services/token-storage-services';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { UserResponseDTO } from '../DTOs/user-response-dto';
import { Observable } from 'rxjs';
import { PaginatedResponse } from '../DTOs/paginated-response';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private baseUrl = Environments.apiBaseUrl + 'User';

  constructor(private http: HttpClient, private token_Service: TokenStorageService) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.token_Service.getAccessToken() || '';
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getUserList(isEnabled?: boolean, page: number = 0, size: number = 10): Observable<PaginatedResponse<UserResponseDTO>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (isEnabled !== undefined) {
      params = params.set('isEnabled', isEnabled.toString());
    }
    return this.http.get<PaginatedResponse<UserResponseDTO>>(`${this.baseUrl}/list`, { params, headers: this.getAuthHeaders() });
  }
}
