export class EnumBase {
    static innerDecode(input, lista): string {
        if (input && lista) {
            const mappingStato = lista.filter(x => x.value === input);
            if (mappingStato && mappingStato[0]) {
                return mappingStato[0].name;
            }
        }
        return input;
    }
}