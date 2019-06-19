interface IOptions {
    dialogTitle: string;
    sideText: string;
}
export declare const openDialog: (inputs: string[][], selectedIndices: number[], options: IOptions) => Promise<any>;
export {};
