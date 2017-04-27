Picasso是一个优秀的图片加载框架，体积小，功能强大，但是在加载个别图片的时候, MarkableInputStream.class 这个类会抛异常。目前修复的方法就是不使用这个类，全部的图片都先加载成bytes再用BitmapFactory.decodeByteArray获取bitmap。对比了修改前后的内存占用情况，差别很小。

this is a bugfix version for [square/picasso](https://github.com/square/picasso) , base on picasso 2.4 . to fix the [issue 364](https://github.com/square/picasso/issues/364)

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
