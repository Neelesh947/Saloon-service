import { SaloonServiceDTO } from "./saloon-service-dto";

export interface SalonResponseDTO {
    id: string;
    saloonName: string;
    address: string;
    phone: string;
    active: boolean;
    services: SaloonServiceDTO[];
    createdBy: string;
    createdAt: string;
    updatedAt: string;
}
