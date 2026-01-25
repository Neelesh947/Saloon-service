export interface AdminApprovalRequestDTO {
    approved: boolean;    // true = approve, false = reject
    remarks?: string;     // required if rejected
}
