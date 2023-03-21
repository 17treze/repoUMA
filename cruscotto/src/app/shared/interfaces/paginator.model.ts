export interface PaginatorA4G<T> {
    count: number;
    risultati: T;
}

export interface PaginatorEvent {
    filters?: any;
    first: number;
    globalFilter?: any;
    multiSortMeta?: any;
    rows: number;
    sortField: string;
    sortOrder: number;
}