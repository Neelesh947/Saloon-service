import { SaloonServiceDTO } from "./saloon-service-dto";

export interface UserResponseDTO {
  keycloakUserId: string;
  firstName: string;
  lastName: string;
  email: string;
  enable: boolean;
  attributes?: {
    [key: string]: string[];
  };
  bookedServiceIds: SaloonServiceDTO[];
}