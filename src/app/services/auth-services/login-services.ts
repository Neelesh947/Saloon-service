import { Injectable } from '@angular/core';
import { Environments } from '../environments';
import { LoginRequestDto } from '../DTOs/login-request-dto';
import { Observable } from 'rxjs';
import { LoginResponseDto } from '../DTOs/login-response-dto';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class LoginServices {

  private baseUrl = Environments.apiBaseUrl + 'keycloak';

  constructor(private http: HttpClient) { }

  login(data: LoginRequestDto): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(`${this.baseUrl}/login`, data, {
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true
    });
  }

}
