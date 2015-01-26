package com.projectinspire.utilities;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;

public class Operations {

	/**
	 * 
	 * recreates a bitmap with a new height/width determined by the user. This can be used
     * in situations where an API stipulation of less than 19 is required; since the set functions for a bitmap
     * regarding the overall functionality of this method require this value.
     * This should be called whenever an image is downloaded from the Internet, or a local machine, to reduce memory leaks/overloads
     * caused by images that have very high dimensions.
	 * 
	 * @param bm the bitmap passed to the method
	 * @param newHeight the new height of the recreated bitmap
	 * @param newWidth the new height of the recreated bitmap
	 * @return a bitmap recreated with parameters determined by the user. 
	 */
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    
	    //
	    // Create a matrix for the manipulation
	    //
	    Matrix matrix = new Matrix();
	    
	    //
	    // Resize the bitmap
	    //
	    matrix.postScale(scaleWidth, scaleHeight);
	    
	    //
	    // recreate the bitmap with a new size
	    //
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    
	    return resizedBitmap;
	}
	
	/**
	 * returns the bytesize of the give bitmap
	 */
	public int byteSizeOf(Bitmap bitmap) {
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	        return bitmap.getAllocationByteCount();
	    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
	        return bitmap.getByteCount();
	    } else {
	        return bitmap.getRowBytes() * bitmap.getHeight();
	    }
	}
	
}
