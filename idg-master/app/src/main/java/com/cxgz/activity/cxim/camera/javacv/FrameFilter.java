/*
 * Copyright (C) 2015 Samuel Audet
 *
 * Licensed either under the Apache License, Version 2.0, or (at your option)
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation (subject to the "Classpath" exception),
 * either version 2, or any later version (collectively, the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     http://www.gnu.org/licenses/
 *     http://www.gnu.org/software/classpath/license.html
 *
 * or as provided in the LICENSE.txt file that accompanied this code.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cxgz.activity.cxim.camera.javacv;

/**
 * A frame processor that may filter video and audio frames, or both.
 * After calling {@link #start()}, we can add frames to the graph with
 * {@link #push(Frame)} and get the filtered ones with {@link #pull()}.
 *
 * @author Samuel Audet
 */
public abstract class FrameFilter
{
    public static FrameFilter createDefault(String filtersDescr, int imageWidth, int imageHeight) throws Exception
    {
        return new FFmpegFrameFilter(filtersDescr, imageWidth, imageHeight);
    }

    protected String filters;
    protected int imageWidth;
    protected int imageHeight;
    protected int pixelFormat;
    protected double frameRate;
    protected double aspectRatio;

    public String getFilters()
    {
        return filters;
    }

    public void setFilters(String filters)
    {
        this.filters = filters;
    }

    public int getImageWidth()
    {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth)
    {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight()
    {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight)
    {
        this.imageHeight = imageHeight;
    }

    public int getPixelFormat()
    {
        return pixelFormat;
    }

    public void setPixelFormat(int pixelFormat)
    {
        this.pixelFormat = pixelFormat;
    }

    public double getFrameRate()
    {
        return frameRate;
    }

    public void setFrameRate(double frameRate)
    {
        this.frameRate = frameRate;
    }

    public double getAspectRatio()
    {
        return aspectRatio;
    }

    public void setAspectRatio(double aspectRatio)
    {
        this.aspectRatio = aspectRatio;
    }

    public static class Exception extends java.lang.Exception
    {
        public Exception(String message)
        {
            super(message);
        }

        public Exception(String message, Throwable cause)
        {
            super(message, cause);
        }
    }

    public abstract void start() throws Exception;

    public abstract void stop() throws Exception;

    public abstract void push(Frame frame) throws Exception;

    public abstract Frame pull() throws Exception;

    public abstract void release() throws Exception;

    public void restart() throws Exception
    {
        stop();
        start();
    }

    public void flush() throws Exception
    {
        while (pull() != null) ;
    }
}
