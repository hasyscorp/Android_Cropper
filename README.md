# Android Cropper - Image Cropping Library for Android

#### This project is forked from xuehuayous/Android-Crop.(Original project is Yalantis/uCrop.)
#### Since the modified too much, so did not create a branch from the original project.

## WeChat crop picture demo
<br/>
<div align="center"><img src="https://github.com/hasyscorp/Android_Cropper/blob/master/wechat_demo.gif?raw=true" /></div>

## Using Crop in your application

If you are building with Gradle, simply add the following line to the `dependencies` section of your `build.gradle` file:

```
	implementation 'com.github.hasyscorp:Android_Cropper:1.0.6'
```

If you've added com.android.support:appcompat-v7:xx.x.x Please use the following configuration

    implementation('com.github.hasyscorp:Android_Cropper:1.0.6') {
        exclude group: 'com.android.support', module: 'appcompat-v7'
    }

## Adding Feature(Version History)
v1.0.5 - Top-height editing now available.

## License

    Copyright 2018, HasysCorp

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
