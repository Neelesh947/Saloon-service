export interface StaffRequestDTO {
    username: string;
    firstName: string;
    lastName: string;
    enable: boolean;
    email: string;
    phone: string;
    serviceIds?: string[];
}
