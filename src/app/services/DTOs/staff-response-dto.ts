export interface StaffResponseDTO {
    id: string;
    createdAt: string;
    updatedAt: string;
    name: string;
    email: string;
    phone: string;
    keycloakUserId: string;
    serviceIds: string[];
    enable: boolean;
}
