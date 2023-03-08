// Interface
export interface IChildInteraction {
   index: number;
   selfRef: any;
   compInteraction: any;
   isValid: boolean;
   title: string;
   codeResponsabilita: string;

   setDisabled(input: boolean);
   removeMe(input: boolean);
}
