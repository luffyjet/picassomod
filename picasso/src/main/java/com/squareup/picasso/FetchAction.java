/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.picasso;

import android.graphics.Bitmap;

/**
 * TODO 已修改 添加了回调
 */
class FetchAction extends Action<Object> {

    private final Object target;
    private Callback callback;

    FetchAction(Picasso picasso, Request data, boolean skipCache, String key, Object tag) {
        super(picasso, null, data, skipCache, false, 0, null, key, tag);
        this.target = new Object();
    }

    FetchAction(Picasso picasso, Request data, boolean skipCache, String key, Object tag, Callback callback) {
        super(picasso, null, data, skipCache, false, 0, null, key, tag);
        this.target = new Object();
        this.callback = callback;
    }

    @Override
    void complete(Bitmap result, Picasso.LoadedFrom from) {
        if (null != callback) {
            callback.onSuccess();
        }
    }

    @Override
    public void error() {
        if (null != callback) {
            callback.onError();
        }
    }

    @Override
    Object getTarget() {
        return target;
    }

    @Override
    void cancel() {
        super.cancel();
        if (callback != null) {
            callback = null;
        }
    }
}
