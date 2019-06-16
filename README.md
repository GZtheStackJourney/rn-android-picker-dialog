# react-native-test-lib

## Getting started

`$ npm install rn-android-picker-dialog --save`

### Mostly automatic installation

`$ react-native link rn-android-picker-dialog`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.rnandroidpicker.PickerPackage;` to the imports at the top of the file
  - Add `new PickerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':rn-android-picker-dialog'
  	project(':rn-android-picker-dialog').projectDir = new File(rootProject.projectDir, 	'../node_modules/rn-android-picker-dialog/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':rn-android-picker-dialog')
  	```

## Usage
```javascript
import PickerDialog from 'rn-android-picker-dialog';

const inputs = [["00", "01", "02", "03"], ["00", "10", "20", "30", "40", "50"], ["am", "pm"]];
const selectedValues = [0, 0, 0];
const dialogTitle = "Select a time";

try {
    const result = await PickerDialog.showDialog(inputs, selectedValues, dialogTitle);
    console.warn(result);
} catch (error) {
    console.warn(error.message);        
}
```

### Arguments for PickerDialog.showDialog

| Argument                          | Type     | Min | Max | Description      |
|-----------------------------------|-------------|------|-----|--------------|
|`Picker values`                      |`Array of array of strings` | `1` | `3`         |values for picker                                                               
|`Selected values`                       |`Array of integers`       |`1` |`3`          |current selected index in array                                                                
|`Dialog Title`                       |`String`        |`0` |`20`         |title of dialog

### Return values from PickerDialog.showDialog

| Action | Value |
|--------|-------------|
|`onOk` | `Array` |
|`onDismiss` | `null` |

