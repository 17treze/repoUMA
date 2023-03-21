export class ArraySupport {

  public static isNullOrEmpty<T>(value: T[]): boolean {
      return value == null || value.length === 0;
  }

  public static isNotEmpty<T>(value: T[]): boolean {
      return value != null && value.length > 0;
  }
}
