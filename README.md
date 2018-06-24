
# react-native-android-file-util

## Getting started

`$ npm install react-native-android-file-util --save`

### Mostly automatic installation

`$ react-native link react-native-android-file-util`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.android_file_util.RNAndroidFileUtilPackage;` to the imports at the top of the file
  - Add `new RNAndroidFileUtilPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-android-file-util'
  	project(':react-native-android-file-util').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-android-file-util/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-android-file-util')
  	```


## Usage
```javascript
import RNAndroidFileUtil from 'react-native-android-file-util';

// TODO: What to do with the module?
RNAndroidFileUtil;
```
  