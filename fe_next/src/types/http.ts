export interface APIErrorResponse<T = any> {
  code: string;
  status: number;
  message: string;
  entity: T;
  fieldErrors: string;
  timestamp: number;
  values: string;
}

export enum EDirection {
  ASC = "asc",
  DESC = "desc",
}

export class IParamsListsQuery {
  page?: number;
  size?: number;
  sort?: string;
  search?: string;
}

export class IParamsListUser extends IParamsListsQuery {
  roleBaseId?: number | null;
}
export interface APIPageResponse<T> {
  content: T[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
  pageable: APIPageable;
}
export interface APIPageable {
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  pageNumber: number;
  pageSize: number;
  offset: number;
  unpaged: boolean;
  paged: boolean;
}
