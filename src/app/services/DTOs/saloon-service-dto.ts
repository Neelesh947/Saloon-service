import { ServiceCategory } from "../enums/models/service-category";

export interface SaloonServiceDTO {
  id: string;                 // UUID
  name: string;
  description: string;
  category: ServiceCategory;  // enum
  durationInMinutes: number;
  price: number;              // BigDecimal → number
  active: boolean;

  salonId: string;            // UUID
  salonName: string;
}
