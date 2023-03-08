export interface ErrorDTO {
    error: ErrorMessageException;
}

export interface ErrorMessageException {
    error: string;
    message: string;
    path: string;
    status: number;
    timestamp: number;
}