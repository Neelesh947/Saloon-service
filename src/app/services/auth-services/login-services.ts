import { Injectable } from '@angular/core';
import { Environments } from '../environments';
import { LoginRequestDto } from '../DTOs/login-request-dto';
import { Observable } from 'rxjs';
import { LoginResponseDto } from '../DTOs/login-response-dto';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenStorageService } from './token-storage-services';

@Injectable({
  providedIn: 'root',
})
export class LoginServices {

  private baseUrl = Environments.apiBaseUrl + 'keycloak';

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) { }

  login(data: LoginRequestDto): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(`${this.baseUrl}/login`, data, {
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true
    });
  }

  logout(realm: string): Observable<void> {
    const refreshToken = this.tokenStorage.getRefreshToken();
    if (!refreshToken) {
      throw new Error('No refresh token found');
    }
    const headers = new HttpHeaders({
      'refresh-token': refreshToken
    });

    return this.http.post<void>(`${this.baseUrl}/logout`, null, { headers });
  }

}
