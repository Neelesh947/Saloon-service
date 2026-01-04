import { PageInfo } from "./page-info";

export interface PaginatedResponse<T> {
    content: T[];
    pageInfo: PageInfo;
}
