import { ServiceCategory } from "../enums/models/service-category";

export interface ServiceRequestDTO {
    name: string;
    description: string;
    category: ServiceCategory; 
    durationInMinutes: number;
    price: number;
    active: boolean;
}
