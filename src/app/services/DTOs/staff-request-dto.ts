export interface StaffRequestDTO {
    username: string;
    firstName: string;
    lastName: string;
    enabled: boolean;
    emailAddress: string;
    phone: string;
    serviceIds?: string[];
}
