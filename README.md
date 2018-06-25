# react-native-android-file-util

This is created for [ZenJournal](https://twitter.com/thezenjournal) and currently with only limited features, it uses Android's [Storage Access Framework](https://developer.android.com/guide/topics/providers/document-provider#create) to save file to any android Document Provider, feel free to use it if it suits your needs. There is no need for iOS counterpart because iOS sharesheet can handle file URIs.

## Getting started

The following won't work, because this is currently unpublished.
`$ npm install react-native-android-file-util --save`

You can download this repo and install it through local:
`$ npm install ./wherever-you-save-it`

You want this published? Let me know, and I can do the leg work.

### Mostly automatic installation

`$ react-native link react-native-android-file-util`

### Manual installation

#### Android

1.  Open up `android/app/src/main/java/[...]/MainActivity.java`

- Add `import com.android_file_util.RNAndroidFileUtilPackage;` to the imports at the top of the file
- Add `new RNAndroidFileUtilPackage()` to the list returned by the `getPackages()` method

2.  Append the following lines to `android/settings.gradle`:
    ```
    include ':react-native-android-file-util'
    project(':react-native-android-file-util').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-android-file-util/android')
    ```
3.  Insert the following lines inside the dependencies block in `android/app/build.gradle`:
    ```
      compile project(':react-native-android-file-util')
    ```

## Usage

```javascript
import RNAndroidFileUtil from 'react-native-android-file-util';

// There is also an optional promise you can watch for when the file is saved.
RNAndroidFileUtil.createFile(mineType, fileName, fileContentAsString).then(successFn, failureFn;
```
