export class Base64Support {

    public static decode(base64Content: string): Uint8Array {
        const fileContent: string = atob(base64Content);
        var uint8 = new Uint8Array(fileContent.length);
        for (var i = 0; i <  uint8.length; i++){
            uint8[i] = fileContent.charCodeAt(i);
        }
        return uint8;
    }

    public static encode(content: string): string {
        return btoa(content);
    }

    public static decodeToBlob(base64Content: string, mimeType: string) {
        const uint8Content = Base64Support.decode(base64Content);
        return new Blob([uint8Content], {type: mimeType});
    }

}