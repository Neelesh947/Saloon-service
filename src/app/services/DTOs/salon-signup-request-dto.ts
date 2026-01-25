export interface SalonSignupRequestDTO {
    id?: string;           // UUID string
    ownerName: string;
    email: string;
    mobile: string;
    salonName: string;
    status?: string;       // e.g., "PENDING", "APPROVED", "REJECTED"
    realm?: string;
}
