export class StringSupport {

    public static isNullOrEmpty(value: String): boolean {
        return value == null || value.trim().length === 0;
    }

    public static isNotEmpty(value: String): boolean {
        return value != null && value.trim().length > 0;
    }
}
