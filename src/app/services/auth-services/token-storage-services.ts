import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TokenStorageService {

  private readonly ACCESS_TOKEN_KEY = 'access_token';
  private readonly REFRESH_TOKEN_KEY = 'refresh_token';
  private readonly LOGIN_DATA_KEY = 'loginData';
  private readonly ROLES_KEY = 'roles';
  private readonly DEFAULT_ROLES = ['offline_access', 'uma_authorization', 'default-roles-saloon'];

  constructor() { }

  storeLoginData(data: { access_token: string, [key: string]: any }): void {
    sessionStorage.setItem(this.LOGIN_DATA_KEY, JSON.stringify(data));

    const accessToken = data.access_token;
    const decodedToken = this.decodeJwt(accessToken);
    let roles: string[] = decodedToken?.realm_access?.roles || [];
    roles = roles.filter(role => !this.DEFAULT_ROLES.includes(role));
    sessionStorage.setItem(this.ROLES_KEY, JSON.stringify(roles));
  }

  private decodeJwt(token: string): any {
    if (!token) return null;

    const payload = token.split('.')[1];
    if (!payload) return null;

    try {
      const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
      const decoded = decodeURIComponent(atob(base64).split('').map(c =>
        '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
      ).join(''));

      return JSON.parse(decoded);
    } catch (e) {
      console.error('Invalid JWT token', e);
      return null;
    }
  }

  getRoles(): string[] {
    const roles = sessionStorage.getItem(this.ROLES_KEY);
    return roles ? JSON.parse(roles) : [];
  }

  getAccessToken(): string | null {
    const loginData = sessionStorage.getItem(this.LOGIN_DATA_KEY);
    if (!loginData) {
      return null;
    }

    try {
      const parsed = JSON.parse(loginData);
      return parsed.access_token || null;
    } catch (e) {
      console.error('Failed to parse login data', e);
      return null;
    }
  }

  clear(): void {
    sessionStorage.clear();
  }
}