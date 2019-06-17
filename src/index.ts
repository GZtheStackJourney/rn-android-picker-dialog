
import { NativeModules } from 'react-native';

interface IOptions {
    dialogTitle: string,
    sideText: string
}

export const openDialog = async (inputs: string[][], selectedIndices: number[], options: IOptions) => {
    const picker = NativeModules.PickerAndroid;
    const finalArray = [];
    const finalOptions = {
        dialogTitle: options.dialogTitle || "",
        sideText: options.sideText || ""
    };
    if(Array.isArray(inputs) && Array.isArray(selectedIndices)) {
        for(let i = 0; i < inputs.length; i++) {
            const filterArr = inputs[i].map(c => `${c}`);
            finalArray.push(filterArr);
        }
    }
    return picker.showDialog(finalArray, selectedIndices, finalOptions);
}
