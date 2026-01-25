import { Injectable } from '@angular/core';
import { Environments } from '../environments';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenStorageService } from '../auth-services/token-storage-services';
import { Observable } from 'rxjs';
import { KeycloakUserDto } from '../DTOs/keycloak-user-dto';
import { SalonSignupFormDTO } from '../DTOs/salon-signup-form-dto';
import { SalonSignupRequestDTO } from '../DTOs/salon-signup-request-dto';
import { AdminApprovalRequestDTO } from '../DTOs/admin-approval-request-dto';

@Injectable({
  providedIn: 'root',
})
export class AdminService {

  private baseUrl = Environments.apiBaseUrl + 'Admin';

  constructor(private http: HttpClient, private token_Service: TokenStorageService) { }

  private getAuthHeaders(): HttpHeaders {
    const token = this.token_Service.getAccessToken() || '';
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  /**
   * Create a new request for admin approval (Salon/Admin signup)
   * @param dto SalonSignupFormDTO containing ownerName, email, mobile, salonName
   */
  createRequestForAdmin(dto: SalonSignupFormDTO): Observable<any> {
    const url = `${this.baseUrl}/create-request-for-admin`;
    return this.http.post<any>(url, dto);
  }

  /**
  * Get a list of pending requests for a given realm and status
  * @param status e.g., "PENDING", "APPROVED", "REJECTED"
  * @param realm Keycloak realm (e.g., "master")
  */
  getPendingRequests(status: string): Observable<SalonSignupRequestDTO[]> {
    const url = `${this.baseUrl}/pending/${status}`;
    return this.http.get<SalonSignupRequestDTO[]>(url, { headers: this.getAuthHeaders() });
  }

  /**
   * Approve or reject a pending admin/salon request
   * @param requestId UUID of the signup request
   * @param realm Keycloak realm (e.g., "master")
   * @param dto AdminApprovalRequestDTO containing approved flag and optional remarks
   */
  reviewAdminRequest(requestId: string, dto: AdminApprovalRequestDTO): Observable<any> {
    const url = `${this.baseUrl}/${requestId}/review`;
    return this.http.post<any>(url, dto, { headers: this.getAuthHeaders() });
  }

  /**
   * Create a new admin user for a specific realm
   * @param adminRequestDto DTO matching backend KeycloakuserDto
   * @param realm Realm name (path variable)
   */
  createAdmin(adminRequestDto: KeycloakUserDto): Observable<{ [key: string]: string }> {
    const url = `${this.baseUrl}/createAdmin`;
    return this.http.post<{ [key: string]: string }>(url, adminRequestDto, { headers: this.getAuthHeaders() });
  }

 /**
 * Create a new salon signup request for admin approval
 * @param realm The keycloak realm (e.g., "master" or your configured realm)
 * @param dto SalonSignupRequestDTO containing the signup data
 */
  createRequestForAdminApproval(dto: SalonSignupRequestDTO): Observable<any> {
    const url = `${this.baseUrl}/create-request-for-admin`;
    return this.http.post<any>(url, dto);
  }
}