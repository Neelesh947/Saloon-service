export interface KeycloakUserDto {
  username: string;
  emailAddress: string;
  password?: string;       // if backend expects password
  firstName?: string;
  lastName?: string;
  enabled: boolean;
  phoneNumber?: string;
  address?: string;
  createdBy?: string;
}