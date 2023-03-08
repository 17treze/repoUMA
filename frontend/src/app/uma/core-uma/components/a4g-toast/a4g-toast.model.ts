export interface A4GToastModel {
    id?: string;             /** Identifier of the message */
    key: string;            /** Key of the message in case message is targeted to a specific toast component. */
    severity: keyof typeof A4GToastSeverityEnum;    /** Severity level of the message */
    summary?: string;        /** Summary text of the message */
    detail: string;         /** Detail text of the message */
    life?: number;           /** Default = 3000 - Number of time in milliseconds to wait before closing the message, default 3000 */
    sticky?: boolean;        /** Default = false - Whether the message should be automatically closed based on life property or kept visible */
    closable?: boolean       /** Default = true - When enabled, displays a close icon to hide a message manually */
    data?: any;              /** Arbitrary object to associate with the message */
}

export enum A4GToastSeverityEnum {
    success = 'success',
    info = 'info',
    warn = 'warn',
    error = 'error'
}