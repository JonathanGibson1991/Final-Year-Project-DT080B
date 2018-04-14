package com.example.jonat.samra.utils;

import android.nfc.Tag;

public final class TagUtils {
    public static String getTagId(Tag tag) {
        if(tag == null){
            return null;
        }else{
            StringBuilder tagId = new StringBuilder();
            for (byte aTagId : tag.getId()) {
                tagId.append(Integer.toString(aTagId & 0xFF));
            }
            return tagId.toString();
        }
    }
}
