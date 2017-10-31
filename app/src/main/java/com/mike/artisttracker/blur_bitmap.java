/*******************************************************
 *  This class is a support class for blur images purpose
 *  Using RenderScript to blur the images via bitmap
 ********************************************************/

package com.mike.artisttracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.*;

public class blur_bitmap {

    public static Bitmap blur_image(Context context, Bitmap input_bitmap)
    {

        try{
            RenderScript rs = RenderScript.create(context);

            Bitmap blurred_bitmap = input_bitmap.copy(Bitmap.Config.ARGB_8888, true);

            // allocate memory
            Allocation input = Allocation.createFromBitmap(rs, blurred_bitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());

            // blur image
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(25.f);

            script.setInput(input);
            script.forEach(output);

            output.copyTo(blurred_bitmap);

            input_bitmap.recycle();

            return blurred_bitmap;
        }
       catch(Exception e)
        {
            return null;
        }
    }
}
