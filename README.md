Picasso是一个优秀的图片加载框架，体积小，功能强大，但是在加载个别图片的时候, MarkableInputStream.class 这个类会抛异常。目前修复的方法就是不使用这个类，全部的图片都先加载成bytes再用BitmapFactory.decodeByteArray获取bitmap。对比了修改前后的内存占用情况，差别很小。

this is a bugfix version of [square/picasso](https://github.com/square/picasso) , base on picasso 2.4 . to fix the [issue 364](https://github.com/square/picasso/issues/364)

see [NetworkRequestHandler.java](https://github.com/luffyjet/picassomod/blob/master/picasso/src/main/java/com/squareup/picasso/NetworkRequestHandler.java):
 
```java
 
 class NetworkRequestHandler extends RequestHandler {
    
    ...
    
    private Bitmap decodeStream(InputStream stream, Request data) throws IOException {
//    MarkableInputStream markStream = new MarkableInputStream(stream);
//    stream = markStream;
//
//    long mark = markStream.savePosition(MARKER);

        final BitmapFactory.Options options = createBitmapOptions(data);
        final boolean calculateSize = requiresInSampleSize(options);

//    boolean isWebPFile = Utils.isWebPFile(stream);
//    markStream.reset(mark);
        // When decode WebP network、 stream, BitmapFactory throw JNI Exception and make app crash.
        // Decode byte array instead
//    if (isWebPFile) {
        byte[] bytes = Utils.toByteArray(stream);
        if (calculateSize) {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            calculateInSampleSize(data.targetWidth, data.targetHeight, options, data);
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        if (bitmap == null) {
            throw new IOException("Failed to decode stream.");
        }
        return bitmap;
//    } else {
//      if (calculateSize) {
//        BitmapFactory.decodeStream(stream, null, options);
//        calculateInSampleSize(data.targetWidth, data.targetHeight, options, data);
//
//        markStream.reset(mark);
//      }
//      Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
//      if (bitmap == null) {
//         Treat null as an IO exception, we will eventually retry.
//        throw new IOException("Failed to decode stream.");
//      }
//      return bitmap;
//    }
    }
}

```

Download
--------

grab via Gradle:
```groovy
repositories {
    jcenter()
}

compile 'com.luffyjet:picasso:1.0'
```
or Maven:
```xml
<dependency>
  <groupId>com.luffyjet</groupId>
  <artifactId>picasso</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>
```



ProGuard
--------

If you are using ProGuard you might need to add the following option:
```
-dontwarn com.squareup.okhttp.**
```



License
--------

    Copyright 2013 Square, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


 [1]: http://square.github.io/picasso/
 [2]: https://search.maven.org/remote_content?g=com.squareup.picasso&a=picasso&v=LATEST
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
